package com.hospital.superadmin.model.enums;

/**
 * Subscription plan types for SaaS tenants.
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
