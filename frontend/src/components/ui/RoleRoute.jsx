import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { PageLoader } from './index';

/**
 * Role-based route guard.
 * Handles both "DOCTOR" and "ROLE_DOCTOR" formats.
 */
export default function RoleRoute({ allowedRoles = [] }) {
  const { user, isAuthenticated, loading } = useAuth();
  const location = useLocation();

  if (loading) return <PageLoader label="در حال بررسی دسترسی…" />;
  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }

  // Get user roles and normalize them
  const userRoles = (user?.roles || []).map(r => r.replace('ROLE_', ''));
  const normalizedAllowed = allowedRoles.map(r => r.replace('ROLE_', ''));
  const hasAccess = normalizedAllowed.some(role => userRoles.includes(role));

  if (!hasAccess) {
    return <Navigate to="/app" replace />;
  }

  return <Outlet />;
}
