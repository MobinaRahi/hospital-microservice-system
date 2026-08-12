package hospital.billingservice.service;

import hospital.billingservice.dto.invoiceitem.InvoiceItemCreateDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemResponseDto;
import hospital.billingservice.exception.invoice.InvoiceNotFoundException;
import hospital.billingservice.exception.invoiceitem.CannotAddItemToInvoiceException;
import hospital.billingservice.exception.invoiceitem.InvoiceItemNotFoundException;
import hospital.billingservice.mapper.InvoiceItemMapper;
import hospital.billingservice.model.Invoice;
import hospital.billingservice.model.InvoiceItem;
import hospital.billingservice.model.enums.InvoiceStatus;
import hospital.billingservice.repository.InvoiceItemRepository;
import hospital.billingservice.repository.InvoiceRepository;
import hospital.billingservice.service.impl.InvoiceItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link InvoiceItemServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class InvoiceItemServiceImplTest {

    @Mock private InvoiceItemRepository invoiceItemRepository;
    @Mock private InvoiceRepository invoiceRepository;
    @Mock private InvoiceItemMapper invoiceItemMapper;

    @InjectMocks
    private InvoiceItemServiceImpl invoiceItemService;

    private Invoice testInvoice;
    private InvoiceItem testItem;

    @BeforeEach
    void setUp() {
        testInvoice = Invoice.builder()
                .id(1L).invoiceNumber("INV-001").patientId(100L)
                .status(InvoiceStatus.PENDING)
                .subtotal(BigDecimal.valueOf(500000))
                .totalAmount(BigDecimal.valueOf(545000))
                .build();

        testItem = InvoiceItem.builder()
                .id(1L)
                .serviceCode("DOC-001")
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(500000))
                .totalPrice(BigDecimal.valueOf(500000))
                .invoice(testInvoice)
                .build();
    }

    @Nested
    @DisplayName("Create Invoice Item")
    class CreateInvoiceItemTests {

        @Test
        @DisplayName("should add item to PENDING invoice")
        void shouldAddItemToInvoice() {
            InvoiceItemCreateDto dto = InvoiceItemCreateDto.builder()
                    .serviceCode("DOC-001").quantity(1)
                    .unitPrice(BigDecimal.valueOf(500000)).build();

            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));
            when(invoiceItemMapper.toEntity(any(InvoiceItemCreateDto.class))).thenReturn(testItem);
            when(invoiceItemMapper.toResponseDto(any(InvoiceItem.class)))
                    .thenReturn(InvoiceItemResponseDto.builder().id(1L).build());
            when(invoiceItemRepository.save(any(InvoiceItem.class))).thenReturn(testItem);
            when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

            InvoiceItemResponseDto result = invoiceItemService.addItemToInvoice(1L, dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(invoiceItemRepository).save(any(InvoiceItem.class));
        }

        @Test
        @DisplayName("should throw when adding item to non-PENDING invoice")
        void shouldThrowWhenAddingToNonPending() {
            testInvoice.setStatus(InvoiceStatus.PAID);
            InvoiceItemCreateDto dto = InvoiceItemCreateDto.builder().build();

            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));

            assertThatThrownBy(() -> invoiceItemService.addItemToInvoice(1L, dto))
                    .isInstanceOf(CannotAddItemToInvoiceException.class);
        }

        @Test
        @DisplayName("should throw when invoice not found")
        void shouldThrowWhenInvoiceNotFound() {
            InvoiceItemCreateDto dto = InvoiceItemCreateDto.builder().build();

            when(invoiceRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> invoiceItemService.addItemToInvoice(999L, dto))
                    .isInstanceOf(InvoiceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Read Invoice Item")
    class ReadInvoiceItemTests {

        @Test
        @DisplayName("should get item by id")
        void shouldGetById() {
            InvoiceItemResponseDto expected = InvoiceItemResponseDto.builder().id(1L).build();

            when(invoiceItemRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testItem));
            when(invoiceItemMapper.toResponseDto(testItem)).thenReturn(expected);

            InvoiceItemResponseDto result = invoiceItemService.getItemById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(invoiceItemRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> invoiceItemService.getItemById(999L))
                    .isInstanceOf(InvoiceItemNotFoundException.class);
        }

        @Test
        @DisplayName("should get items by invoice")
        void shouldGetByInvoice() {
            when(invoiceItemRepository.findByInvoiceId(1L)).thenReturn(List.of(testItem));
            when(invoiceItemMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    InvoiceItemResponseDto.builder().id(1L).build()));

            assertThat(invoiceItemService.getItemsByInvoice(1L)).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Delete Invoice Item")
    class DeleteInvoiceItemTests {

        @Test
        @DisplayName("should soft delete item")
        void shouldSoftDelete() {
            when(invoiceItemRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testItem));

            invoiceItemService.deleteItem(1L);

            verify(invoiceItemRepository).save(argThat(InvoiceItem::isDeleted));
        }
    }
}
