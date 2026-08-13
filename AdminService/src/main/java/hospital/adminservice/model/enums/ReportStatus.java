package hospital.adminservice.model.enums;

/**
 * Status of report generation lifecycle.
 *
 * @author MobinaRahi
 */
public enum ReportStatus {
    /** Report is queued and waiting to be processed */
    PENDING,

    /** Report is currently being generated */
    PROCESSING,

    /** Report generation completed successfully */
    COMPLETED,

    /** Report generation failed due to an error */
    FAILED,

    /** Report has expired and is no longer available */
    EXPIRED
}
