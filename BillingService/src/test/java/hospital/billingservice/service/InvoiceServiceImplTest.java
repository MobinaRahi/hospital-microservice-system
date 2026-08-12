package hospital.billingservice.service;

import hospital.billingservice.dto.invoice.InvoiceCreateDto;
import hospital.billingservice.dto.invoice.InvoiceResponseDto;
import hospital.billingservice.exception.invoice.DuplicateInvoiceNumberException;
import hospital.billingservice.exception.invoice.IllegalInvoiceStatusException;
import hospital.billingservice.exception.invoice.InvoiceNotFoundException;
import hospital.billingservice.mapper.InvoiceItemMapper;
import hospital.billingservice.mapper.InvoiceMapper;
import hospital.billingservice.model.Invoice;
import hospital.billingservice.model.enums.InvoiceStatus;
import hospital.billingservice.repository.InvoiceItemRepository;
import hospital.billingservice.repository.InvoiceRepository;
import hospital.billingservice.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link InvoiceServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock private InvoiceRepository invoiceRepository;
    @Mock private InvoiceItemRepository invoiceItemRepository;
    @Mock private InvoiceMapper invoiceMapper;
    @Mock private InvoiceItemMapper invoiceItemMapper;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Invoice testInvoice;

    @BeforeEach
    void setUp() {
        testInvoice = Invoice.builder()
                .id(1L)
                .invoiceNumber("INV-2026-001")
                .patientId(100L)
                .issueDate(LocalDateTime.now())
                .subtotal(BigDecimal.valueOf(500000))
                .discount(BigDecimal.ZERO)
                .tax(BigDecimal.valueOf(45000))
                .totalAmount(BigDecimal.valueOf(545000))
                .status(InvoiceStatus.PENDING)
                .build();
    }

    @Nested
    @DisplayName("Create Invoice")
    class CreateInvoiceTests {

        @Test
        @DisplayName("should create invoice successfully")
        void shouldCreateInvoice() {
            InvoiceCreateDto dto = InvoiceCreateDto.builder()
                    .invoiceNumber("INV-2026-001").patientId(100L)
                    .subtotal(BigDecimal.valueOf(500000)).items(Collections.emptyList()).build();

            when(invoiceRepository.existsByInvoiceNumber("INV-2026-001")).thenReturn(false);
            when(invoiceMapper.toEntity(any(InvoiceCreateDto.class))).thenReturn(testInvoice);
            when(invoiceMapper.toResponseDto(any(Invoice.class)))
                    .thenReturn(InvoiceResponseDto.builder().id(1L).build());
            when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

            InvoiceResponseDto result = invoiceService.createInvoice(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(invoiceRepository, times(2)).save(any(Invoice.class));
        }

        @Test
        @DisplayName("should throw when invoice number exists")
        void shouldThrowWhenNumberExists() {
            InvoiceCreateDto dto = InvoiceCreateDto.builder().invoiceNumber("INV-2026-001").build();

            when(invoiceRepository.existsByInvoiceNumber("INV-2026-001")).thenReturn(true);

            assertThatThrownBy(() -> invoiceService.createInvoice(dto))
                    .isInstanceOf(DuplicateInvoiceNumberException.class);
        }
    }

    @Nested
    @DisplayName("Read Invoice")
    class ReadInvoiceTests {

        @Test
        @DisplayName("should get invoice by id")
        void shouldGetById() {
            InvoiceResponseDto expected = InvoiceResponseDto.builder().id(1L).build();

            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));
            when(invoiceMapper.toResponseDto(testInvoice)).thenReturn(expected);

            InvoiceResponseDto result = invoiceService.getInvoiceById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(invoiceRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> invoiceService.getInvoiceById(999L))
                    .isInstanceOf(InvoiceNotFoundException.class);
        }

        @Test
        @DisplayName("should get overdue invoices")
        void shouldGetOverdueInvoices() {
            when(invoiceRepository.findOverdueInvoices(any(LocalDate.class))).thenReturn(List.of(testInvoice));
            when(invoiceMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    InvoiceResponseDto.builder().id(1L).build()));

            assertThat(invoiceService.getOverdueInvoices()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitionTests {

        @Test
        @DisplayName("should cancel invoice")
        void shouldCancelInvoice() {
            testInvoice.setStatus(InvoiceStatus.PENDING);
            InvoiceResponseDto expected = InvoiceResponseDto.builder().build();

            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));
            when(invoiceMapper.toResponseDto(any(Invoice.class))).thenReturn(expected);
            when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

            invoiceService.cancelInvoice(1L);

            verify(invoiceRepository).save(argThat(i -> i.getStatus() == InvoiceStatus.CANCELLED));
        }

        @Test
        @DisplayName("should throw when cancelling paid invoice")
        void shouldThrowWhenCancellingPaid() {
            testInvoice.setStatus(InvoiceStatus.PAID);

            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));

            assertThatThrownBy(() -> invoiceService.cancelInvoice(1L))
                    .isInstanceOf(IllegalInvoiceStatusException.class);
        }

        @Test
        @DisplayName("should mark invoice as paid")
        void shouldMarkAsPaid() {
            testInvoice.setStatus(InvoiceStatus.PARTIAL);
            InvoiceResponseDto expected = InvoiceResponseDto.builder().build();

            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));
            when(invoiceMapper.toResponseDto(any(Invoice.class))).thenReturn(expected);
            when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

            invoiceService.markAsPaid(1L);

            verify(invoiceRepository).save(argThat(i -> i.getStatus() == InvoiceStatus.PAID));
        }
    }

    @Nested
    @DisplayName("Delete Invoice")
    class DeleteInvoiceTests {

        @Test
        @DisplayName("should soft delete invoice")
        void shouldSoftDelete() {
            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));

            invoiceService.deleteInvoice(1L);

            verify(invoiceRepository).save(argThat(Invoice::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check invoice number existence")
        void shouldCheckInvoiceNumberExistence() {
            when(invoiceRepository.existsByInvoiceNumber("INV-2026-001")).thenReturn(true);

            assertThat(invoiceService.invoiceNumberExists("INV-2026-001")).isTrue();
        }
    }
}
