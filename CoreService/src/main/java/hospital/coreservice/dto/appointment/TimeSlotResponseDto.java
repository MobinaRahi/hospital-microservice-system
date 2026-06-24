package hospital.coreservice.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

/**
 * یک بازه‌ی زمانی قابل رزرو برای یک دکتر در یک روز خاص.
 * چون مدت هر اسلات بسته به برنامه‌ی هر دکتر فرق می‌کند (مثلاً یکی ۲۰ دقیقه، یکی ۳۰ دقیقه)،
 * این DTO هم startTime و هم endTime واقعی را برمی‌گرداند تا فرانت‌اند مجبور به فرض‌سازی نباشد.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotResponseDto {
    private LocalTime startTime;
    private LocalTime endTime;
}