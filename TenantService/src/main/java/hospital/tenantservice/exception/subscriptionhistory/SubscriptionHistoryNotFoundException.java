package hospital.tenantservice.exception.subscriptionhistory;

/**
 * Exception thrown when subscription history is not found.
 *
 * @author MobinaRahi
 */
public class SubscriptionHistoryNotFoundException extends RuntimeException {

    public SubscriptionHistoryNotFoundException(String message) {
        super(message);
    }

    public static SubscriptionHistoryNotFoundException byId(Long id) {
        return new SubscriptionHistoryNotFoundException("Subscription history with id " + id + " not found");
    }

    public static SubscriptionHistoryNotFoundException byTenant(Long tenantId) {
        return new SubscriptionHistoryNotFoundException("No subscription history found for tenant " + tenantId);
    }
}
