package hospital.coreservice.dto.room;

import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoomResponseDto {

    private Long id;

    private String roomNumber;

    private DepartmentResponseDto department;

    private Integer capacity;

    private Boolean isOccupied;

    private Integer currentOccupancy;

    private Integer availableCapacity;

    private List<PatientResponseDto> currentPatients = new ArrayList<>();

    private String features;
}