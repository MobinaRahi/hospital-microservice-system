package com.hospital.superadmin.model.enums;

/**
 * Status of an admin user in the system.
 *
 * @author MobinaRahi
 */
public enum AdminStatus {

    /** Admin is active and can access the system. */
    ACTIVE,

    /** Admin account is suspended (security reason, policy violation). */
    SUSPENDED,

    /** Admin account is inactive (left organization). */
    INACTIVE
}
