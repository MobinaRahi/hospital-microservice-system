package hospital.inventoryservice.service.impl;

import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemCreateDto;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemResponseDto;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemUpdateDto;

import hospital.inventoryservice.mapper.PurchaseOrderItemMapper;
import hospital.inventoryservice.exception.purchaseorderitem.PurchaseOrderItemNotFoundException;
import hospital.inventoryservice.exception.purchaseorder.PurchaseOrderNotFoundException;
import hospital.inventoryservice.exception.purchaseorder.IllegalStatusTransitionException;
import hospital.inventoryservice.exception.drug.DrugNotFoundException;
import hospital.inventoryservice.model.Drug;
import hospital.inventoryservice.model.PurchaseOrder;
import hospital.inventoryservice.model.PurchaseOrderItem;
import hospital.inventoryservice.model.enums.PurchaseOrderStatus;
import hospital.inventoryservice.repository.DrugRepository;
import hospital.inventoryservice.repository.PurchaseOrderItemRepository;
import hospital.inventoryservice.repository.PurchaseOrderRepository;
import hospital.inventoryservice.service.PurchaseOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link PurchaseOrderItemService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Items belong to a purchase order</li>
 *   <li>receivedQuantity cannot exceed ordered quantity</li>
 *   <li>Receiving items auto-updates stock</li>
 *   <li>Subtotal is auto-calculated (unitPrice × receivedQuantity)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {

    private final PurchaseOrderItemRepository itemRepository;
    private final PurchaseOrderRepository orderRepository;
    private final DrugRepository drugRepository;
    private final PurchaseOrderItemMapper itemMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    @Override
    public PurchaseOrderItemResponseDto addItemToOrder(Long purchaseOrderId, PurchaseOrderItemCreateDto dto) {
        log.info("Adding item to purchase order: {}", purchaseOrderId);

        // Validate purchase order exists and is in PENDING status
        PurchaseOrder order = orderRepository.findNotDeletedById(purchaseOrderId)
                .orElseThrow(() -> PurchaseOrderNotFoundException.byId(purchaseOrderId));

        if (order.getStatus() != PurchaseOrderStatus.PENDING) {
            throw IllegalStatusTransitionException.canOnlyAddToPending();
        }

        // Validate drug exists
        Drug drug = drugRepository.findNotDeletedById(dto.getDrugId())
                .orElseThrow(() -> DrugNotFoundException.byId(dto.getDrugId()));

        // Map DTO to entity
        PurchaseOrderItem item = itemMapper.toEntity(dto);
        item.setPurchaseOrder(order);
        item.setDrug(drug);
        item.setReceivedQuantity(0);

        // Save and return
        PurchaseOrderItem saved = itemRepository.save(item);

        // Recalculate order total
        order.recalculateTotal();
        orderRepository.save(order);

        log.info("Item added with id: {}", saved.getId());
        return itemMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderItemResponseDto getItemById(Long id) {
        log.debug("Fetching purchase order item by id: {}", id);

        PurchaseOrderItem item = itemRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderItemNotFoundException.byId(id));

        return itemMapper.toResponseDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderItemResponseDto> getItemsByOrder(Long purchaseOrderId) {
        log.debug("Fetching items for purchase order: {}", purchaseOrderId);

        List<PurchaseOrderItem> items = itemRepository.findByPurchaseOrderId(purchaseOrderId);
        return itemMapper.toResponseDtoList(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderItemResponseDto> getPartiallyReceivedItems() {
        log.debug("Fetching partially received items");

        List<PurchaseOrderItem> items = itemRepository.findByReceivedQuantityLessThanQuantity();
        return itemMapper.toResponseDtoList(items);
    }

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    @Override
    public PurchaseOrderItemResponseDto updateItem(Long id, PurchaseOrderItemUpdateDto dto) {
        log.info("Updating purchase order item id: {}", id);

        PurchaseOrderItem item = itemRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderItemNotFoundException.byId(id));

        // Map update DTO to entity
        itemMapper.updateEntity(dto, item);

        PurchaseOrderItem saved = itemRepository.save(item);

        // Recalculate order total
        PurchaseOrder order = saved.getPurchaseOrder();
        order.recalculateTotal();
        orderRepository.save(order);

        log.info("Purchase order item updated id: {}", saved.getId());
        return itemMapper.toResponseDto(saved);
    }

    @Override
    public PurchaseOrderItemResponseDto recordReceived(Long id, int receivedQuantity) {
        log.info("Recording received quantity for item id: {}", id);

        PurchaseOrderItem item = itemRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderItemNotFoundException.byId(id));

        // Validate received quantity
        if (receivedQuantity < 0) {
            throw new IllegalArgumentException("Received quantity cannot be negative");
        }

        if (receivedQuantity > item.getQuantity()) {
            throw new IllegalArgumentException("Received quantity cannot exceed ordered quantity");
        }

        item.setReceivedQuantity(receivedQuantity);
        PurchaseOrderItem saved = itemRepository.save(item);

        // Recalculate order total
        PurchaseOrder order = saved.getPurchaseOrder();
        order.recalculateTotal();
        orderRepository.save(order);

        return itemMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    @Override
    public void deleteItem(Long id) {
        log.info("Soft-deleting purchase order item id: {}", id);

        PurchaseOrderItem item = itemRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderItemNotFoundException.byId(id));

        // Check if order is still in PENDING status
        PurchaseOrder order = item.getPurchaseOrder();
        if (order.getStatus() != PurchaseOrderStatus.PENDING) {
            throw IllegalStatusTransitionException.canOnlyAddToPending();
        }

        item.softDelete(null);
        itemRepository.save(item);

        // Recalculate order total
        order.recalculateTotal();
        orderRepository.save(order);
    }
}
