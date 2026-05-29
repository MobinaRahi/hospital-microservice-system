package hospital.coreservice.exception.patient;

public class DuplicatePhoneNumberException extends RuntimeException {

        public DuplicatePhoneNumberException(String phoneNumber){
            super ("phone number already exists : "+ phoneNumber);
        }

    }

