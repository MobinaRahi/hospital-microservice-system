package hospital.coreservice.dto.patient;

import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PatientResponseDto {

    private Long id;

    private Long userId;

    private String username;

    private String email;

    private String fullName;

    private String nationalId;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String phoneNumber;

    private String address;

    private BloodType bloodType;

    private Long insuranceId;

    private PatientStatus status;

    private String allergies;

    private RoomResponseDto currentRoom;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long createdBy;

    private LocalDate birthDate;
}