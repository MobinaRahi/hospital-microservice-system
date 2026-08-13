package hospital.notificationservice.exception.smsgateway;

/**
 * Exception thrown when an SMS gateway record is not found.
 *
 * @author MobinaRahi
 */
public class SMSGatewayNotFoundException extends RuntimeException {

    public SMSGatewayNotFoundException(String message) {
        super(message);
    }

    public static SMSGatewayNotFoundException byId(Long id) {
        return new SMSGatewayNotFoundException("SMS gateway record with id " + id + " not found");
    }

    public static SMSGatewayNotFoundException byPhone(String phone) {
        return new SMSGatewayNotFoundException("SMS gateway record for phone '" + phone + "' not found");
    }
}
