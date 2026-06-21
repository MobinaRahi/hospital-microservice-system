package hospital.coreservice.service;

import hospital.coreservice.dto.department.DepartmentCreateDto;
import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.dto.department.DepartmentUpdateDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.dto.room.RoomResponseDto;

import java.util.List;

public interface DepartmentService {

    DepartmentResponseDto createDepartment(DepartmentCreateDto createDto);
    DepartmentResponseDto updateDepartment(Long id, DepartmentUpdateDto updateDto);
    DepartmentResponseDto getDepartmentById(Long id);
    DepartmentResponseDto getDepartmentByCode(String code);
    DepartmentResponseDto getDepartmentByCodeAndIsActiveTrue(String code);
    List<DepartmentResponseDto> getDepartmentByName(String name);
    List<DepartmentResponseDto> getDepartmentByNameAndIsActiveTru(String name);
    List<DepartmentResponseDto> getAllDepartments();
    List<DepartmentResponseDto> getActiveDepartments();
    List<DepartmentResponseDto> getInactiveDepartments();
    void assignHeadDoctor(Long departmentId, Long doctorId);
    void assignHeadNurse(Long departmentId, Long nurseId);
    void removeHeadDoctor(Long departmentId);
    void removeHeadNurse(Long departmentId);
    DoctorResponseDto getDepartmentHeadDoctor(Long departmentId);
    NurseResponseDto getDepartmentHeadNurse(Long departmentId);
    void addDoctorToDepartment(Long departmentId, Long doctorId);
    void addNurseToDepartment(Long departmentId, Long nurseId);
    void addRoomToDepartment(Long departmentId, Long roomId);
    void removeDoctorFromDepartment(Long departmentId, Long doctorId);
    void removeNurseFromDepartment(Long departmentId, Long nurseId);
    void removeRoomFromDepartment(Long departmentId, Long roomId);
    void removeAllDoctorsFromDepartment(Long departmentId);
    void removeAllNursesFromDepartment(Long departmentId);
    List<DoctorResponseDto> getDoctorsByDepartmentId(Long departmentId);
    List<NurseResponseDto> getNursesByDepartmentId(Long departmentId);
    List<RoomResponseDto> getRoomsByDepartmentId(Long departmentId);
    void activateDepartment(Long departmentId);
    void deactivateDepartment(Long departmentId);
    List<DepartmentResponseDto> searchDepartmentsByName(String name);
    List<DepartmentResponseDto> searchDepartmentsByNameAndIsActiveTrue(String name);
    List<DepartmentResponseDto> searchDepartmentsByLocation(String location);
    List<DepartmentResponseDto> searchDepartmentsByLocationAndIsActiveTrue(String location);
    List<DepartmentResponseDto> getDepartmentsByLocation(String location);
    List<DepartmentResponseDto> getDepartmentsByLocationAndIsActiveTrue(String location);
    List<DepartmentResponseDto> getDepartmentsByStatus(boolean isActive);
    Long countDoctorsInDepartment(Long departmentId);
    Long countNursesInDepartment(Long departmentId);
    Long countRoomsInDepartment(Long departmentId);
    Long countTotalDepartments();
    Long countActiveDepartments();
    Long countInactiveDepartments();
    boolean isDepartmentCodeUnique(String code);
    boolean existsDepartmentByName(String name);
    boolean existsDepartmentByCode(String code);
    boolean existsDepartmentById(Long id);
}
