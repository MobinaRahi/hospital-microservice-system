package hospital.billingservice.service;

import hospital.billingservice.dto.payment.PaymentCreateDto;
import hospital.billingservice.dto.payment.PaymentResponseDto;
import hospital.billingservice.exception.invoice.InvoiceNotFoundException;
import hospital.billingservice.exception.payment.DuplicatePaymentReferenceException;
import hospital.billingservice.exception.payment.PaymentExceedsBalanceException;
import hospital.billingservice.exception.payment.PaymentNotFoundException;
import hospital.billingservice.mapper.PaymentMapper;
import hospital.billingservice.model.Invoice;
import hospital.billingservice.model.Payment;
import hospital.billingservice.model.enums.InvoiceStatus;
import hospital.billingservice.model.enums.PaymentMethod;
import hospital.billingservice.repository.InvoiceRepository;
import hospital.billingservice.repository.PaymentRepository;
import hospital.billingservice.service.impl.PaymentServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PaymentServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private InvoiceRepository invoiceRepository;
    @Mock private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Invoice testInvoice;
    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testInvoice = Invoice.builder()
                .id(1L).invoiceNumber("INV-001").patientId(100L)
                .totalAmount(BigDecimal.valueOf(545000))
                .status(InvoiceStatus.PENDING)
                .build();

        testPayment = Payment.builder()
                .id(1L)
                .invoice(testInvoice)
                .amount(BigDecimal.valueOf(300000))
                .method(PaymentMethod.CARD)
                .paymentDate(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Create Payment")
    class CreatePaymentTests {

        @Test
        @DisplayName("should create payment successfully")
        void shouldCreatePayment() {
            PaymentCreateDto dto = PaymentCreateDto.builder()
                    .invoiceId(1L).amount(BigDecimal.valueOf(300000))
                    .method(PaymentMethod.CARD).paymentDate(LocalDateTime.now()).build();

            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));
            when(paymentRepository.findByInvoiceId(1L)).thenReturn(List.of());
            when(paymentMapper.toEntity(any(PaymentCreateDto.class))).thenReturn(testPayment);
            when(paymentMapper.toResponseDto(any(Payment.class)))
                    .thenReturn(PaymentResponseDto.builder().id(1L).build());
            when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
            when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

            PaymentResponseDto result = paymentService.createPayment(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(paymentRepository).save(any(Payment.class));
        }

        @Test
        @DisplayName("should throw when invoice not found")
        void shouldThrowWhenInvoiceNotFound() {
            PaymentCreateDto dto = PaymentCreateDto.builder().invoiceId(999L).build();

            when(invoiceRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> paymentService.createPayment(dto))
                    .isInstanceOf(InvoiceNotFoundException.class);
        }

        @Test
        @DisplayName("should throw when payment exceeds balance")
        void shouldThrowWhenExceedsBalance() {
            PaymentCreateDto dto = PaymentCreateDto.builder()
                    .invoiceId(1L).amount(BigDecimal.valueOf(999999999)).build();

            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));
            when(paymentRepository.findByInvoiceId(1L)).thenReturn(List.of());

            assertThatThrownBy(() -> paymentService.createPayment(dto))
                    .isInstanceOf(PaymentExceedsBalanceException.class);
        }

        @Test
        @DisplayName("should throw when reference number exists")
        void shouldThrowWhenReferenceExists() {
            PaymentCreateDto dto = PaymentCreateDto.builder()
                    .invoiceId(1L).amount(BigDecimal.valueOf(300000))
                    .referenceNumber("REF-001").build();

            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));
            when(paymentRepository.findByInvoiceId(1L)).thenReturn(List.of());
            when(paymentRepository.existsByReferenceNumber("REF-001")).thenReturn(true);

            assertThatThrownBy(() -> paymentService.createPayment(dto))
                    .isInstanceOf(DuplicatePaymentReferenceException.class);
        }
    }

    @Nested
    @DisplayName("Read Payment")
    class ReadPaymentTests {

        @Test
        @DisplayName("should get payment by id")
        void shouldGetById() {
            PaymentResponseDto expected = PaymentResponseDto.builder().id(1L).build();

            when(paymentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testPayment));
            when(paymentMapper.toResponseDto(testPayment)).thenReturn(expected);

            PaymentResponseDto result = paymentService.getPaymentById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(paymentRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> paymentService.getPaymentById(999L))
                    .isInstanceOf(PaymentNotFoundException.class);
        }

        @Test
        @DisplayName("should get payments by invoice")
        void shouldGetByInvoice() {
            when(paymentRepository.findByInvoiceId(1L)).thenReturn(List.of(testPayment));
            when(paymentMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    PaymentResponseDto.builder().id(1L).build()));

            assertThat(paymentService.getPaymentsByInvoice(1L)).hasSize(1);
        }

        @Test
        @DisplayName("should get total paid for invoice")
        void shouldGetTotalPaid() {
            when(paymentRepository.findByInvoiceId(1L)).thenReturn(List.of(testPayment));

            BigDecimal total = paymentService.getTotalPaidForInvoice(1L);

            assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(300000));
        }

        @Test
        @DisplayName("should get remaining balance")
        void shouldGetRemainingBalance() {
            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));
            when(paymentRepository.findByInvoiceId(1L)).thenReturn(List.of());

            BigDecimal balance = paymentService.getRemainingBalance(1L);

            assertThat(balance).isEqualByComparingTo(BigDecimal.valueOf(545000));
        }
    }

    @Nested
    @DisplayName("Delete Payment")
    class DeletePaymentTests {

        @Test
        @DisplayName("should soft delete payment")
        void shouldSoftDelete() {
            when(paymentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testPayment));

            paymentService.deletePayment(1L);

            verify(paymentRepository).save(argThat(Payment::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check reference number existence")
        void shouldCheckReferenceExistence() {
            when(paymentRepository.existsByReferenceNumber("REF-001")).thenReturn(true);

            assertThat(paymentService.referenceNumberExists("REF-001")).isTrue();
        }

        @Test
        @DisplayName("should check if payment would exceed balance")
        void shouldCheckWouldExceedBalance() {
            when(invoiceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInvoice));
            when(paymentRepository.findByInvoiceId(1L)).thenReturn(List.of());

            boolean result = paymentService.wouldExceedBalance(1L, BigDecimal.valueOf(999999));

            assertThat(result).isTrue();
        }
    }
}
