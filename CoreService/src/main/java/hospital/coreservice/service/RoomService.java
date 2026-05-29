package hospital.coreservice.service;

public class RoomService {
    //@Transactional
    //public void assignPatientToRoom(Long roomId, Long patientId) {
    //    // 1. پیدا کردن اتاق
    //    Room room = roomRepository.findById(roomId)
    //        .orElseThrow(() -> new RuntimeException("Room not found"));
    //
    //    // 2. پیدا کردن بیمار
    //    Patient patient = patientRepository.findById(patientId)
    //        .orElseThrow(() -> new RuntimeException("Patient not found"));
    //
    //    // 3. اعتبارسنجی (قبلاً اشغال نباشد)
    //    if (room.isOccupied()) {
    //        throw new RuntimeException("Room is already occupied");
    //    }
    //
    //    // 4. تخصیص اتاق به بیمار
    //    patient.setCurrentRoom(room);
    //    patientRepository.save(patient);
    //
    //    // 5. اشغال کردن اتاق
    //    roomRepository.occupy(roomId);
    //}
}
