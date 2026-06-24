package hospital.coreservice.service;

import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.request.CompleteRegistrationRequest;
import hospital.coreservice.mapper.PatientMapper;
import hospital.coreservice.model.Patient;
import hospital.coreservice.model.User;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.repository.PatientRepository;
import hospital.coreservice.repository.RoomRepository;
import hospital.coreservice.repository.UserRepository;
import hospital.coreservice.service.imp.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("تست completeRegistration — آپدیت اطلاعات بیمار و یوزر")
class CompleteRegistrationTest {

    @Mock PatientRepository patientRepository;
    @Mock UserRepository userRepository;
    @Mock RoomRepository roomRepository;
    @Mock PatientMapper patientMapper;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks
    PatientServiceImpl patientService;

    // داده‌های تست
    User mockUser;
    Patient mockPatient;
    CompleteRegistrationRequest request;

    @BeforeEach
    void setUp() {
        // یوزر اولیه — همونی که موقع نوبت‌گیری ساخته شده
        mockUser = new User();
        mockUser.setId(10L);
        mockUser.setUsername("0123456789");      // کد ملی قبلی
        mockUser.setEmail("0123456789@temp.hospital.com");
        mockUser.setPasswordHash("tempPasswordHash");
        mockUser.setFirstName("علی");
        mockUser.setLastName("احمدی");

        // بیمار مرتبط با یوزر
        mockPatient = new Patient();
        mockPatient.setId(3L);
        mockPatient.setUser(mockUser);
        mockPatient.setNationalId("0123456789");
        mockPatient.setFirstName("علی");
        mockPatient.setLastName("احمدی");
        mockPatient.setPhoneNumber("09123456789");
        mockPatient.setGender(Gender.UNKNOWN);
        mockPatient.setBloodType(BloodType.UNKNOWN);
        mockPatient.setAddress("ثبت نشده");
        mockPatient.setBirthDate(LocalDate.now().minusYears(30));

        // درخواست تکمیل پروفایل
        request = new CompleteRegistrationRequest();
        request.setPatientId(3L);
        request.setUsername("ali_ahmadi");           // یوزرنیم جدید
        request.setEmail("ali@gmail.com");           // ایمیل جدید
        request.setNewPassword("MyNewPass@123");     // رمز جدید
        request.setConfirmPassword("MyNewPass@123");
        request.setFirstName("علی");
        request.setLastName("احمدی");
        request.setPhoneNumber("09123456789");
        request.setGender(Gender.MAN);
        request.setBloodType(BloodType.A_POSITIVE);
        request.setAddress("تهران، خیابان ولیعصر");
        request.setBirthDate(LocalDate.of(1990, 5, 15));
        request.setAllergies("پنی‌سیلین");
    }

    @Test
    @DisplayName("✅ یوزرنیم، ایمیل و رمز عبور باید آپدیت بشن")
    void shouldUpdateUsernameEmailAndPassword() {
        // Mock‌ها
        when(patientRepository.findById(3L)).thenReturn(Optional.of(mockPatient));
        when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode("MyNewPass@123")).thenReturn("hashedPassword");
        when(patientRepository.save(any(Patient.class))).thenReturn(mockPatient);
        when(patientMapper.toResponseDto(any(Patient.class))).thenReturn(new PatientResponseDto());

        // اجرا
        patientService.completeRegistration(3L, request);

        // بررسی یوزر
        verify(userRepository).save(argThat(user -> {
            assertThat(user.getUsername()).isEqualTo("ali_ahmadi");
            assertThat(user.getEmail()).isEqualTo("ali@gmail.com");
            assertThat(user.getPasswordHash()).isEqualTo("hashedPassword");
            return true;
        }));
    }

    @Test
    @DisplayName("✅ اطلاعات بیمار (جنسیت، گروه خونی، آدرس) باید آپدیت بشن")
    void shouldUpdatePatientInfo() {
        when(patientRepository.findById(3L)).thenReturn(Optional.of(mockPatient));
        when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(patientRepository.save(any(Patient.class))).thenAnswer(inv -> inv.getArgument(0));
        when(patientMapper.toResponseDto(any(Patient.class))).thenReturn(new PatientResponseDto());

        patientService.completeRegistration(3L, request);

        // بیمار ذخیره شده باید اطلاعات جدید داشته باشه
        verify(patientRepository).save(argThat(patient -> {
            assertThat(patient.getGender()).isEqualTo(Gender.MAN);
            assertThat(patient.getBloodType()).isEqualTo(BloodType.A_POSITIVE);
            assertThat(patient.getAddress()).isEqualTo("تهران، خیابان ولیعصر");
            assertThat(patient.getAllergies()).isEqualTo("پنی‌سیلین");
            assertThat(patient.getBirthDate()).isEqualTo(LocalDate.of(1990, 5, 15));
            return true;
        }));
    }

    @Test
    @DisplayName("✅ اگه یوزرنیم خالی بود، یوزرنیم قبلی نباید تغییر کنه")
    void shouldNotUpdateUsernameIfBlank() {
        request.setUsername("");  // خالی

        when(patientRepository.findById(3L)).thenReturn(Optional.of(mockPatient));
        when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(patientRepository.save(any())).thenReturn(mockPatient);
        when(patientMapper.toResponseDto(any())).thenReturn(new PatientResponseDto());

        patientService.completeRegistration(3L, request);

        verify(userRepository).save(argThat(user -> {
            // یوزرنیم باید همون قبلی بمونه
            assertThat(user.getUsername()).isEqualTo("0123456789");
            return true;
        }));
    }

    @Test
    @DisplayName("✅ اگه بیمار یوزر نداشت، باید بدون خطا کار کنه")
    void shouldWorkEvenIfPatientHasNoUser() {
        mockPatient.setUser(null);  // بیمار بدون یوزر

        when(patientRepository.findById(3L)).thenReturn(Optional.of(mockPatient));
        when(patientRepository.save(any())).thenReturn(mockPatient);
        when(patientMapper.toResponseDto(any())).thenReturn(new PatientResponseDto());

        // نباید exception بده
        assertThatNoException().isThrownBy(() ->
                patientService.completeRegistration(3L, request)
        );

        // userRepository نباید صدا زده بشه
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }
}
