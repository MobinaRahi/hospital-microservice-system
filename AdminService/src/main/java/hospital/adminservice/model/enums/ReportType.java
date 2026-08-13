package hospital.adminservice.model.enums;

/**
 * Types of reports available in the system.
 * Each type determines the report content and data source.
 *
 * @author MobinaRahi
 */
public enum ReportType {
    /** Patient statistics and demographics */
    PATIENT_STATS,

    /** Appointment analytics and trends */
    APPOINTMENT_STATS,

    /** Daily financial summary */
    FINANCIAL_DAILY,

    /** Monthly financial report */
    FINANCIAL_MONTHLY,

    /** Yearly financial report */
    FINANCIAL_YEARLY,

    /** Doctor performance metrics */
    DOCTOR_PERFORMANCE,

    /** Bed occupancy and utilization rates */
    BED_OCCUPANCY,

    /** Revenue breakdown by insurance company */
    REVENUE_BY_INSURANCE,

    /** Revenue breakdown by service type */
    REVENUE_BY_SERVICE,

    /** Payment collection report */
    COLLECTION_REPORT,

    /** Delayed and outstanding payments report */
    DELAYED_PAYMENTS
}
