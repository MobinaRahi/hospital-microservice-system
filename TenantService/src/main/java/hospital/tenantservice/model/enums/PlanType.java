package hospital.tenantservice.model.enums;

/**
 * Subscription plan types for SaaS tenants.
 *
 * <p><strong>Plan Limits:</strong></p>
 * <table>
 *   <tr><th>Plan</th><th>Max Users</th><th>Max Patients</th><th>Max Appointments/Month</th><th>Storage</th><th>Support</th></tr>
 *   <tr><td>{@code FREE}</td><td>5</td><td>50</td><td>100</td><td>1GB</td><td>Email</td></tr>
 *   <tr><td>{@code BASIC}</td><td>20</td><td>500</td><td>1,000</td><td>10GB</td><td>Email</td></tr>
 *   <tr><td>{@code PROFESSIONAL}</td><td>100</td><td>5,000</td><td>10,000</td><td>100GB</td><td>Priority</td></tr>
 *   <tr><td>{@code ENTERPRISE}</td><td>Unlimited</td><td>Unlimited</td><td>Unlimited</td><td>1TB</td><td>24/7</td></tr>
 * </table>
 *
 * @author MobinaRahi
 */
public enum PlanType {

    /** Free plan for small clinics (5 users, 50 patients, 100 appointments/month). */
    FREE,

    /** Basic plan for growing clinics (20 users, 500 patients, 1,000 appointments/month). */
    BASIC,

    /** Professional plan for medium hospitals (100 users, 5,000 patients, 10,000 appointments/month). */
    PROFESSIONAL,

    /** Enterprise plan for large hospitals (unlimited everything). */
    ENTERPRISE
}
