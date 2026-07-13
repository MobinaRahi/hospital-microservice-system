import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { PageLoader } from './index';

/**
 * Routes users to their role-specific dashboard.
 * Handles both "DOCTOR" and "ROLE_DOCTOR" formats.
 */
export default function DashboardRouter() {
  const { user, loading } = useAuth();

  if (loading) return <PageLoader label="در حال بارگذاری…" />;

  const roles = user?.roles || [];

  // Normalize roles - remove ROLE_ prefix if present
  const normalizedRoles = roles.map(r => r.replace('ROLE_', ''));

  if (normalizedRoles.includes('SUPER_ADMIN') || normalizedRoles.includes('ADMIN')) {
    return <Navigate to="/app/admin" replace />;
  }
  if (normalizedRoles.includes('DOCTOR')) {
    return <Navigate to="/app/doctor" replace />;
  }
  if (normalizedRoles.includes('NURSE')) {
    return <Navigate to="/app/nurse" replace />;
  }
  if (normalizedRoles.includes('PATIENT')) {
    return <Navigate to="/app/patient" replace />;
  }
  if (normalizedRoles.includes('RECEPTIONIST')) {
    return <Navigate to="/reception" replace />;
  }

  // Default fallback
  return <Navigate to="/app/admin" replace />;
}
