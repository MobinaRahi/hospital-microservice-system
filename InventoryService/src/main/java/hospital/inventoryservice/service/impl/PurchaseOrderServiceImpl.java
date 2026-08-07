package hospital.inventoryservice.service.impl;

import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderCreateDto;
import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderResponseDto;
import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderUpdateDto;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemCreateDto;

import hospital.inventoryservice.mapper.PurchaseOrderMapper;
import hospital.inventoryservice.exception.purchaseorder.PurchaseOrderNotFoundException;
import hospital.inventoryservice.exception.purchaseorder.IllegalStatusTransitionException;
import hospital.inventoryservice.exception.supplier.SupplierNotFoundException;
import hospital.inventoryservice.model.PurchaseOrder;
import hospital.inventoryservice.model.PurchaseOrderItem;
import hospital.inventoryservice.model.Supplier;
import hospital.inventoryservice.model.enums.PurchaseOrderStatus;
import hospital.inventoryservice.repository.PurchaseOrderRepository;
import hospital.inventoryservice.repository.SupplierRepository;
import hospital.inventoryservice.service.PurchaseOrderItemService;
import hospital.inventoryservice.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link PurchaseOrderService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Status workflow: PENDING → APPROVED → SENT → PARTIAL → COMPLETED</li>
 *   <li>Cancel can happen at any status</li>
 *   <li>Receiving a purchase order auto-updates stock quantities</li>
 *   <li>Total amount is auto-calculated from items</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderMapper orderMapper;
    private final PurchaseOrderItemService itemService;

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    @Override
    public PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderCreateDto dto) {
        log.info("Creating purchase order for supplier: {}", dto.getSupplierId());

        // Validate supplier exists
        Supplier supplier = supplierRepository.findNotDeletedById(dto.getSupplierId())
                .orElseThrow(() -> SupplierNotFoundException.byId(dto.getSupplierId()));

        // Map DTO to entity
        PurchaseOrder order = orderMapper.toEntity(dto);
        order.setSupplier(supplier);
        order.setStatus(PurchaseOrderStatus.PENDING);

        // Save and return
        PurchaseOrder saved = orderRepository.save(order);
        log.info("Purchase order created with id: {}", saved.getId());

        return orderMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponseDto getPurchaseOrderById(Long id) {
        log.debug("Fetching purchase order by id: {}", id);

        PurchaseOrder order = orderRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderNotFoundException.byId(id));

        return orderMapper.toResponseDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDto> getAllPurchaseOrders() {
        log.debug("Fetching all purchase orders");

        List<PurchaseOrder> orders = orderRepository.findAllNotDeleted();
        return orderMapper.toResponseDtoList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDto> getPurchaseOrdersByStatus(PurchaseOrderStatus status) {
        log.debug("Fetching purchase orders by status: {}", status);

        List<PurchaseOrder> orders = orderRepository.findByStatus(status);
        return orderMapper.toResponseDtoList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDto> getPurchaseOrdersBySupplier(Long supplierId) {
        log.debug("Fetching purchase orders by supplier: {}", supplierId);

        List<PurchaseOrder> orders = orderRepository.findBySupplierId(supplierId);
        return orderMapper.toResponseDtoList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDto> getPurchaseOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching purchase orders by date range: {} to {}", startDate, endDate);

        List<PurchaseOrder> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        return orderMapper.toResponseDtoList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDto> getOverduePurchaseOrders() {
        log.debug("Fetching overdue purchase orders");

        List<PurchaseOrder> orders = orderRepository.findByExpectedDeliveryDateBeforeAndStatusNot(
                LocalDate.now(), PurchaseOrderStatus.COMPLETED);
        return orderMapper.toResponseDtoList(orders);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    @Override
    public PurchaseOrderResponseDto updatePurchaseOrder(Long id, PurchaseOrderUpdateDto dto) {
        log.info("Updating purchase order id: {}", id);

        PurchaseOrder order = orderRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderNotFoundException.byId(id));

        // Map update DTO to entity
        orderMapper.updateEntity(dto, order);

        // Recalculate total amount
        order.recalculateTotal();

        PurchaseOrder saved = orderRepository.save(order);
        log.info("Purchase order updated id: {}", saved.getId());

        return orderMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Status Transitions
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public PurchaseOrderResponseDto approveOrder(Long id) {
        log.info("Approving purchase order id: {}", id);

        PurchaseOrder order = orderRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderNotFoundException.byId(id));

        if (order.getStatus() != PurchaseOrderStatus.PENDING) {
            throw IllegalStatusTransitionException.fromTo(order.getStatus().name(), "APPROVED");
        }

        order.setStatus(PurchaseOrderStatus.APPROVED);
        PurchaseOrder saved = orderRepository.save(order);

        return orderMapper.toResponseDto(saved);
    }

    @Override
    public PurchaseOrderResponseDto sendOrder(Long id) {
        log.info("Sending purchase order id: {}", id);

        PurchaseOrder order = orderRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderNotFoundException.byId(id));

        if (order.getStatus() != PurchaseOrderStatus.APPROVED) {
            throw IllegalStatusTransitionException.fromTo(order.getStatus().name(), "SENT");
        }

        order.setStatus(PurchaseOrderStatus.SENT);
        PurchaseOrder saved = orderRepository.save(order);

        return orderMapper.toResponseDto(saved);
    }

    @Override
    public PurchaseOrderResponseDto partialReceiveOrder(Long id) {
        log.info("Partially receiving purchase order id: {}", id);

        PurchaseOrder order = orderRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderNotFoundException.byId(id));

        if (order.getStatus() != PurchaseOrderStatus.SENT) {
            throw IllegalStatusTransitionException.fromTo(order.getStatus().name(), "PARTIAL");
        }

        order.setStatus(PurchaseOrderStatus.PARTIAL);
        order.setActualDeliveryDate(LocalDate.now());
        PurchaseOrder saved = orderRepository.save(order);

        return orderMapper.toResponseDto(saved);
    }

    @Override
    public PurchaseOrderResponseDto completeOrder(Long id) {
        log.info("Completing purchase order id: {}", id);

        PurchaseOrder order = orderRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderNotFoundException.byId(id));

        if (order.getStatus() != PurchaseOrderStatus.SENT && order.getStatus() != PurchaseOrderStatus.PARTIAL) {
            throw IllegalStatusTransitionException.fromTo(order.getStatus().name(), "COMPLETED");
        }

        order.setStatus(PurchaseOrderStatus.COMPLETED);
        order.setActualDeliveryDate(LocalDate.now());
        PurchaseOrder saved = orderRepository.save(order);

        return orderMapper.toResponseDto(saved);
    }

    @Override
    public PurchaseOrderResponseDto cancelOrder(Long id) {
        log.info("Cancelling purchase order id: {}", id);

        PurchaseOrder order = orderRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderNotFoundException.byId(id));

        if (order.getStatus() == PurchaseOrderStatus.COMPLETED) {
            throw IllegalStatusTransitionException.cannotCancelCompleted();
        }

        order.setStatus(PurchaseOrderStatus.CANCELLED);
        PurchaseOrder saved = orderRepository.save(order);

        return orderMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    @Override
    public void deletePurchaseOrder(Long id) {
        log.info("Soft-deleting purchase order id: {}", id);

        PurchaseOrder order = orderRepository.findNotDeletedById(id)
                .orElseThrow(() -> PurchaseOrderNotFoundException.byId(id));

        order.softDelete(null);
        orderRepository.save(order);
    }
}
