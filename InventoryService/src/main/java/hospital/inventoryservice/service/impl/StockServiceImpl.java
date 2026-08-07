package hospital.inventoryservice.service.impl;

import hospital.inventoryservice.dto.stock.StockCreateDto;
import hospital.inventoryservice.dto.stock.StockResponseDto;
import hospital.inventoryservice.dto.stock.StockUpdateDto;

import hospital.inventoryservice.mapper.StockMapper;
import hospital.inventoryservice.exception.stock.StockNotFoundException;
import hospital.inventoryservice.exception.stock.InsufficientStockException;
import hospital.inventoryservice.exception.drug.DrugNotFoundException;
import hospital.inventoryservice.model.Drug;
import hospital.inventoryservice.model.Stock;
import hospital.inventoryservice.repository.DrugRepository;
import hospital.inventoryservice.repository.StockRepository;
import hospital.inventoryservice.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link StockService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each drug can have multiple stock batches</li>
 *   <li>Quantity cannot be negative</li>
 *   <li>Low stock alerts are triggered when quantity falls below minStockLevel</li>
 *   <li>Expired stock should not be dispensed</li>
 *   <li>Stock is updated when purchase orders are received</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final DrugRepository drugRepository;
    private final StockMapper stockMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    @Override
    public StockResponseDto createStock(StockCreateDto dto) {
        log.info("Creating stock for drug: {}", dto.getDrugId());

        // Validate drug exists
        Drug drug = drugRepository.findNotDeletedById(dto.getDrugId())
                .orElseThrow(() -> DrugNotFoundException.byId(dto.getDrugId()));

        // Validate quantity is not negative
        if (dto.getQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        // Map DTO to entity
        Stock stock = stockMapper.toEntity(dto);
        stock.setDrug(drug);
        stock.setLastRestockedAt(LocalDateTime.now());

        // Save and return
        Stock saved = stockRepository.save(stock);
        log.info("Stock created with id: {}", saved.getId());

        return stockMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public StockResponseDto getStockById(Long id) {
        log.debug("Fetching stock by id: {}", id);

        Stock stock = stockRepository.findNotDeletedById(id)
                .orElseThrow(() -> StockNotFoundException.byId(id));

        return stockMapper.toResponseDto(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponseDto> getStocksByDrug(Long drugId) {
        log.debug("Fetching stocks for drug: {}", drugId);

        List<Stock> stocks = stockRepository.findByDrugId(drugId);
        return stockMapper.toResponseDtoList(stocks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponseDto> getAllStocks() {
        log.debug("Fetching all stocks");

        List<Stock> stocks = stockRepository.findAllNotDeleted();
        return stockMapper.toResponseDtoList(stocks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponseDto> getExpiredStocks() {
        log.debug("Fetching expired stocks");

        List<Stock> stocks = stockRepository.findByExpiryDateBefore(LocalDate.now());
        return stockMapper.toResponseDtoList(stocks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponseDto> getExpiringStocks(int daysThreshold) {
        log.debug("Fetching stocks expiring within {} days", daysThreshold);

        LocalDate expiryDate = LocalDate.now().plusDays(daysThreshold);
        List<Stock> stocks = stockRepository.findByExpiryDateBetween(LocalDate.now(), expiryDate);
        return stockMapper.toResponseDtoList(stocks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponseDto> getLowStockAlerts() {
        log.debug("Fetching low stock alerts");

        // Get all stocks and filter those where quantity <= minStockLevel
        List<Stock> allStocks = stockRepository.findAllNotDeleted();
        List<Stock> lowStockItems = allStocks.stream()
                .filter(stock -> stock.getMinStockLevel() != null)
                .filter(stock -> stock.getQuantity() <= stock.getMinStockLevel())
                .toList();

        return stockMapper.toResponseDtoList(lowStockItems);
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalQuantityByDrug(Long drugId) {
        log.debug("Fetching total quantity for drug: {}", drugId);

        Integer total = stockRepository.findTotalQuantityByDrugId(drugId);
        return total != null ? total : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponseDto> getStocksByLocation(String location) {
        log.debug("Fetching stocks by location: {}", location);

        List<Stock> stocks = stockRepository.findByLocationContainingIgnoreCase(location);
        return stockMapper.toResponseDtoList(stocks);
    }

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    @Override
    public StockResponseDto updateStock(Long id, StockUpdateDto dto) {
        log.info("Updating stock id: {}", id);

        Stock stock = stockRepository.findNotDeletedById(id)
                .orElseThrow(() -> StockNotFoundException.byId(id));

        // Validate quantity is not negative
        if (dto.getQuantity() != null && dto.getQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        // Map update DTO to entity
        stockMapper.updateEntity(dto, stock);

        Stock saved = stockRepository.save(stock);
        log.info("Stock updated id: {}", saved.getId());

        return stockMapper.toResponseDto(saved);
    }

    @Override
    public StockResponseDto addStock(Long id, int quantity) {
        log.info("Adding {} units to stock id: {}", quantity, id);

        Stock stock = stockRepository.findNotDeletedById(id)
                .orElseThrow(() -> StockNotFoundException.byId(id));

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity to add cannot be negative");
        }

        stock.setQuantity(stock.getQuantity() + quantity);
        stock.setLastRestockedAt(LocalDateTime.now());

        Stock saved = stockRepository.save(stock);
        return stockMapper.toResponseDto(saved);
    }

    @Override
    public StockResponseDto removeStock(Long id, int quantity) {
        log.info("Removing {} units from stock id: {}", quantity, id);

        Stock stock = stockRepository.findNotDeletedById(id)
                .orElseThrow(() -> StockNotFoundException.byId(id));

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity to remove cannot be negative");
        }

        if (quantity > stock.getQuantity()) {
            throw new InsufficientStockException(id, quantity, stock.getQuantity());
        }

        stock.setQuantity(stock.getQuantity() - quantity);

        Stock saved = stockRepository.save(stock);
        return stockMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    @Override
    public void deleteStock(Long id) {
        log.info("Soft-deleting stock id: {}", id);

        Stock stock = stockRepository.findNotDeletedById(id)
                .orElseThrow(() -> StockNotFoundException.byId(id));

        stock.softDelete(null);
        stockRepository.save(stock);
    }
}
