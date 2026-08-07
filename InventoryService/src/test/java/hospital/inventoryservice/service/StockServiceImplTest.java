package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.stock.StockCreateDto;
import hospital.inventoryservice.dto.stock.StockResponseDto;
import hospital.inventoryservice.dto.stock.StockUpdateDto;
import hospital.inventoryservice.exception.drug.DrugNotFoundException;
import hospital.inventoryservice.exception.stock.InsufficientStockException;
import hospital.inventoryservice.exception.stock.StockNotFoundException;
import hospital.inventoryservice.mapper.StockMapper;
import hospital.inventoryservice.model.Drug;
import hospital.inventoryservice.model.Stock;
import hospital.inventoryservice.repository.DrugRepository;
import hospital.inventoryservice.repository.StockRepository;
import hospital.inventoryservice.service.impl.StockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StockServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock private StockRepository stockRepository;
    @Mock private DrugRepository drugRepository;
    @Mock private StockMapper stockMapper;

    @InjectMocks
    private StockServiceImpl stockService;

    private Drug testDrug;
    private Stock testStock;

    @BeforeEach
    void setUp() {
        testDrug = Drug.builder().id(10L).genericName("Amoxicillin").build();

        testStock = Stock.builder()
                .id(1L)
                .drug(testDrug)
                .batchNumber("BATCH-001")
                .quantity(100)
                .minStockLevel(20)
                .maxStockLevel(500)
                .location("Warehouse A")
                .expiryDate(LocalDate.now().plusDays(365))
                .build();
    }

    @Nested
    @DisplayName("Create Stock")
    class CreateStockTests {

        @Test
        @DisplayName("should create stock successfully")
        void shouldCreateStock() {
            StockCreateDto dto = StockCreateDto.builder()
                    .drugId(10L)
                    .batchNumber("BATCH-001")
                    .quantity(100)
                    .minStockLevel(20)
                    .build();

            when(drugRepository.findNotDeletedById(10L)).thenReturn(Optional.of(testDrug));
            when(stockMapper.toEntity(any(StockCreateDto.class))).thenReturn(testStock);
            when(stockMapper.toResponseDto(any(Stock.class))).thenReturn(
                    StockResponseDto.builder().id(1L).batchNumber("BATCH-001").quantity(100).build());
            when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

            StockResponseDto result = stockService.createStock(dto);

            assertThat(result).isNotNull();
            verify(stockRepository).save(argThat(s -> s.getLastRestockedAt() != null));
        }

        @Test
        @DisplayName("should throw when drug doesn't exist")
        void shouldThrowWhenDrugNotFound() {
            StockCreateDto dto = StockCreateDto.builder().drugId(999L).batchNumber("B").quantity(10).build();

            when(drugRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> stockService.createStock(dto))
                    .isInstanceOf(DrugNotFoundException.class);
        }

        @Test
        @DisplayName("should throw when quantity is negative")
        void shouldThrowWhenQuantityNegative() {
            StockCreateDto dto = StockCreateDto.builder().drugId(10L).batchNumber("B").quantity(-5).build();

            when(drugRepository.findNotDeletedById(10L)).thenReturn(Optional.of(testDrug));

            assertThatThrownBy(() -> stockService.createStock(dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Stock Operations")
    class StockOperationTests {

        @Test
        @DisplayName("should add stock quantity")
        void shouldAddStock() {
            Stock updatedStock = Stock.builder().id(1L).drug(testDrug).quantity(120).build();

            when(stockRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testStock));
            when(stockRepository.save(any(Stock.class))).thenReturn(updatedStock);
            when(stockMapper.toResponseDto(updatedStock)).thenReturn(
                    StockResponseDto.builder().quantity(120).build());

            StockResponseDto result = stockService.addStock(1L, 20);

            assertThat(result.getQuantity()).isEqualTo(120);
        }

        @Test
        @DisplayName("should remove stock quantity")
        void shouldRemoveStock() {
            Stock updatedStock = Stock.builder().id(1L).drug(testDrug).quantity(80).build();

            when(stockRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testStock));
            when(stockRepository.save(any(Stock.class))).thenReturn(updatedStock);
            when(stockMapper.toResponseDto(updatedStock)).thenReturn(
                    StockResponseDto.builder().quantity(80).build());

            StockResponseDto result = stockService.removeStock(1L, 20);

            assertThat(result.getQuantity()).isEqualTo(80);
        }

        @Test
        @DisplayName("should throw when removing more than available")
        void shouldThrowWhenRemovingMoreThanAvailable() {
            when(stockRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testStock));

            assertThatThrownBy(() -> stockService.removeStock(1L, 200))
                    .isInstanceOf(InsufficientStockException.class);
        }
    }

    @Nested
    @DisplayName("Stock Alerts")
    class StockAlertTests {

        @Test
        @DisplayName("should get low stock alerts correctly")
        void shouldGetLowStockAlerts() {
            Stock lowStock = Stock.builder().id(2L).drug(testDrug).quantity(10).minStockLevel(20).build();
            Stock normalStock = Stock.builder().id(3L).drug(testDrug).quantity(50).minStockLevel(20).build();

            when(stockRepository.findAllNotDeleted()).thenReturn(List.of(testStock, lowStock, normalStock));
            when(stockMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    StockResponseDto.builder().quantity(10).build()));

            List<StockResponseDto> result = stockService.getLowStockAlerts();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should get expired stocks")
        void shouldGetExpiredStocks() {
            when(stockRepository.findByExpiryDateBefore(LocalDate.now()))
                    .thenReturn(List.of(testStock));
            when(stockMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    StockResponseDto.builder().build()));

            List<StockResponseDto> result = stockService.getExpiredStocks();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should get total quantity by drug")
        void shouldGetTotalQuantity() {
            when(stockRepository.findTotalQuantityByDrugId(10L)).thenReturn(100);

            int total = stockService.getTotalQuantityByDrug(10L);

            assertThat(total).isEqualTo(100);
        }
    }
}
