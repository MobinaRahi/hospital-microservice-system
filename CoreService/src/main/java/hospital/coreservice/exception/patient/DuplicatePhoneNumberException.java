package hospital.coreservice.exception.patient;

/**
 * Thrown when attempting to create a duplicate patient.
 *
 * @author Mobina
 */
public class DuplicatePhoneNumberException extends RuntimeException {

        public DuplicatePhoneNumberException(String phoneNumber){
            super ("phone number already exists : "+ phoneNumber);
        }

    }

