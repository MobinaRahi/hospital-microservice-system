package hospital.coreservice.service;

public class NurseService {
    //@Service
    //public class NurseService {
    //
    //    private final NurseRepository nurseRepository;
    //    private final ShiftRepository shiftRepository;
    //
    //    @Transactional
    //    public void assignShiftToNurse(Long nurseId, Long shiftId) {
    //        // 1. پیدا کردن پرستار
    //        Nurse nurse = nurseRepository.findById(nurseId)
    //            .orElseThrow(() -> new RuntimeException("Nurse not found"));
    //
    //        // 2. پیدا کردن شیفت
    //        Shift shift = shiftRepository.findById(shiftId)
    //            .orElseThrow(() -> new RuntimeException("Shift not found"));
    //
    //        // 3. اضافه کردن شیفت به لیست شیفت‌های پرستار
    //        nurse.getShiftPreferenceList().add(shift);
    //
    //        // 4. ذخیره کردن
    //        nurseRepository.save(nurse);
    //    }
    //
    //    @Transactional
    //    public void removeShiftFromNurse(Long nurseId, Long shiftId) {
    //        Nurse nurse = nurseRepository.findById(nurseId)
    //            .orElseThrow(() -> new RuntimeException("Nurse not found"));
    //
    //        Shift shift = shiftRepository.findById(shiftId)
    //            .orElseThrow(() -> new RuntimeException("Shift not found"));
    //
    //        nurse.getShiftPreferenceList().remove(shift);
    //        nurseRepository.save(nurse);
    //    }
    //}/**
    // * Removes a shift assignment from a nurse.
    // * This removes the shift from the nurse's shift preference list.
    // *
    // * @param nurseId the ID of the nurse
    // * @param shiftId the ID of the shift to remove
    // */
    //@Transactional
    //public void removeShiftFromNurse(Long nurseId, Long shiftId) {
    //    // 1. Find the nurse
    //    Nurse nurse = nurseRepository.findById(nurseId)
    //        .orElseThrow(() -> new RuntimeException("Nurse not found with id: " + nurseId));
    //
    //    // 2. Find the shift
    //    Shift shift = shiftRepository.findById(shiftId)
    //        .orElseThrow(() -> new RuntimeException("Shift not found with id: " + shiftId));
    //
    //    // 3. Check if the nurse has this shift assigned
    //    if (!nurse.getShiftPreferenceList().contains(shift)) {
    //        throw new RuntimeException("Shift " + shiftId + " is not assigned to nurse " + nurseId);
    //    }
    //
    //    // 4. Remove the shift from the nurse's list
    //    nurse.getShiftPreferenceList().remove(shift);
    //
    //    // 5. Save the changes
    //    nurseRepository.save(nurse);
    //}
}
