package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderCreateDto;
import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderResponseDto;
import hospital.inventoryservice.exception.purchaseorder.IllegalStatusTransitionException;
import hospital.inventoryservice.exception.purchaseorder.PurchaseOrderNotFoundException;
import hospital.inventoryservice.exception.supplier.SupplierNotFoundException;
import hospital.inventoryservice.mapper.PurchaseOrderMapper;
import hospital.inventoryservice.model.PurchaseOrder;
import hospital.inventoryservice.model.Supplier;
import hospital.inventoryservice.model.enums.PurchaseOrderStatus;
import hospital.inventoryservice.repository.PurchaseOrderRepository;
import hospital.inventoryservice.repository.SupplierRepository;
import hospital.inventoryservice.service.impl.PurchaseOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PurchaseOrderServiceImpl}.
 * Tests the full status workflow: PENDING → APPROVED → SENT → PARTIAL → COMPLETED
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceImplTest {

    @Mock private PurchaseOrderRepository orderRepository;
    @Mock private SupplierRepository supplierRepository;
    @Mock private PurchaseOrderMapper orderMapper;

    @InjectMocks
    private PurchaseOrderServiceImpl orderService;

    private Supplier testSupplier;
    private PurchaseOrder pendingOrder;

    @BeforeEach
    void setUp() {
        testSupplier = Supplier.builder().id(100L).name("PharmaCorp").build();

        pendingOrder = PurchaseOrder.builder()
                .id(1L)
                .supplier(testSupplier)
                .orderDate(LocalDate.now())
                .status(PurchaseOrderStatus.PENDING)
                .build();
    }

    @Nested
    @DisplayName("Create Purchase Order")
    class CreateOrderTests {

        @Test
        @DisplayName("should create order in PENDING status")
        void shouldCreateOrder() {
            PurchaseOrderCreateDto dto = PurchaseOrderCreateDto.builder()
                    .supplierId(100L)
                    .orderDate(LocalDate.now())
                    .build();

            PurchaseOrderResponseDto expected = PurchaseOrderResponseDto.builder()
                    .id(1L).status(PurchaseOrderStatus.PENDING).build();

            when(supplierRepository.findNotDeletedById(100L)).thenReturn(Optional.of(testSupplier));
            when(orderMapper.toEntity(any(PurchaseOrderCreateDto.class))).thenReturn(pendingOrder);
            when(orderMapper.toResponseDto(any(PurchaseOrder.class))).thenReturn(expected);
            when(orderRepository.save(any(PurchaseOrder.class))).thenReturn(pendingOrder);

            PurchaseOrderResponseDto result = orderService.createPurchaseOrder(dto);

            assertThat(result.getStatus()).isEqualTo(PurchaseOrderStatus.PENDING);
        }

        @Test
        @DisplayName("should throw when supplier not found")
        void shouldThrowWhenSupplierNotFound() {
            PurchaseOrderCreateDto dto = PurchaseOrderCreateDto.builder()
                    .supplierId(999L)
                    .orderDate(LocalDate.now())
                    .build();

            when(supplierRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.createPurchaseOrder(dto))
                    .isInstanceOf(SupplierNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Status Workflow")
    class StatusWorkflowTests {

        @Test
        @DisplayName("should approve PENDING order")
        void shouldApproveOrder() {
            PurchaseOrderResponseDto expected = PurchaseOrderResponseDto.builder()
                    .status(PurchaseOrderStatus.APPROVED).build();

            when(orderRepository.findNotDeletedById(1L)).thenReturn(Optional.of(pendingOrder));
            when(orderMapper.toResponseDto(any(PurchaseOrder.class))).thenReturn(expected);
            when(orderRepository.save(any(PurchaseOrder.class))).thenReturn(pendingOrder);

            PurchaseOrderResponseDto result = orderService.approveOrder(1L);

            assertThat(result.getStatus()).isEqualTo(PurchaseOrderStatus.APPROVED);
            verify(orderRepository).save(argThat(o -> o.getStatus() == PurchaseOrderStatus.APPROVED));
        }

        @Test
        @DisplayName("should throw when approving non-PENDING order")
        void shouldThrowWhenApprovingNonPending() {
            pendingOrder.setStatus(PurchaseOrderStatus.APPROVED);

            when(orderRepository.findNotDeletedById(1L)).thenReturn(Optional.of(pendingOrder));

            assertThatThrownBy(() -> orderService.approveOrder(1L))
                    .isInstanceOf(IllegalStatusTransitionException.class);
        }

        @Test
        @DisplayName("should send APPROVED order")
        void shouldSendOrder() {
            pendingOrder.setStatus(PurchaseOrderStatus.APPROVED);

            PurchaseOrderResponseDto expected = PurchaseOrderResponseDto.builder()
                    .status(PurchaseOrderStatus.SENT).build();

            when(orderRepository.findNotDeletedById(1L)).thenReturn(Optional.of(pendingOrder));
            when(orderMapper.toResponseDto(any(PurchaseOrder.class))).thenReturn(expected);
            when(orderRepository.save(any(PurchaseOrder.class))).thenReturn(pendingOrder);

            PurchaseOrderResponseDto result = orderService.sendOrder(1L);

            assertThat(result.getStatus()).isEqualTo(PurchaseOrderStatus.SENT);
        }

        @Test
        @DisplayName("should complete SENT order")
        void shouldCompleteOrder() {
            pendingOrder.setStatus(PurchaseOrderStatus.SENT);

            PurchaseOrderResponseDto expected = PurchaseOrderResponseDto.builder()
                    .status(PurchaseOrderStatus.COMPLETED).build();

            when(orderRepository.findNotDeletedById(1L)).thenReturn(Optional.of(pendingOrder));
            when(orderMapper.toResponseDto(any(PurchaseOrder.class))).thenReturn(expected);
            when(orderRepository.save(any(PurchaseOrder.class))).thenReturn(pendingOrder);

            PurchaseOrderResponseDto result = orderService.completeOrder(1L);

            assertThat(result.getStatus()).isEqualTo(PurchaseOrderStatus.COMPLETED);
        }

        @Test
        @DisplayName("should cancel order at any status except COMPLETED")
        void shouldCancelOrder() {
            pendingOrder.setStatus(PurchaseOrderStatus.APPROVED);

            PurchaseOrderResponseDto expected = PurchaseOrderResponseDto.builder()
                    .status(PurchaseOrderStatus.CANCELLED).build();

            when(orderRepository.findNotDeletedById(1L)).thenReturn(Optional.of(pendingOrder));
            when(orderMapper.toResponseDto(any(PurchaseOrder.class))).thenReturn(expected);
            when(orderRepository.save(any(PurchaseOrder.class))).thenReturn(pendingOrder);

            orderService.cancelOrder(1L);

            verify(orderRepository).save(argThat(o -> o.getStatus() == PurchaseOrderStatus.CANCELLED));
        }

        @Test
        @DisplayName("should throw when cancelling COMPLETED order")
        void shouldThrowWhenCancellingCompleted() {
            pendingOrder.setStatus(PurchaseOrderStatus.COMPLETED);

            when(orderRepository.findNotDeletedById(1L)).thenReturn(Optional.of(pendingOrder));

            assertThatThrownBy(() -> orderService.cancelOrder(1L))
                    .isInstanceOf(IllegalStatusTransitionException.class);
        }
    }

    @Nested
    @DisplayName("Read Orders")
    class ReadOrderTests {

        @Test
        @DisplayName("should get order by id")
        void shouldGetById() {
            PurchaseOrderResponseDto expected = PurchaseOrderResponseDto.builder()
                    .id(1L).status(PurchaseOrderStatus.PENDING).build();

            when(orderRepository.findNotDeletedById(1L)).thenReturn(Optional.of(pendingOrder));
            when(orderMapper.toResponseDto(pendingOrder)).thenReturn(expected);

            PurchaseOrderResponseDto result = orderService.getPurchaseOrderById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should get overdue orders")
        void shouldGetOverdueOrders() {
            when(orderRepository.findByExpectedDeliveryDateBeforeAndStatusNot(
                    any(LocalDate.class), eq(PurchaseOrderStatus.COMPLETED)))
                    .thenReturn(List.of(pendingOrder));
            when(orderMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    PurchaseOrderResponseDto.builder().build()));

            List<PurchaseOrderResponseDto> result = orderService.getOverduePurchaseOrders();

            assertThat(result).hasSize(1);
        }
    }
}
