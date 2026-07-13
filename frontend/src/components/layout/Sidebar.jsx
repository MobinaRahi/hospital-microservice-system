import { NavLink, useNavigate } from 'react-router-dom';
import {
  Activity, LayoutDashboard, Stethoscope, Users, CalendarDays,
  Building2, BedDouble, UserRound, Clock, CalendarPlus, HeartPulse,
  ClipboardList, LogOut, X, Settings, BarChart3, UserCog,
  Syringe, FileText,
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { initials } from '../../utils/format';

/* Menu sections for each role */
const adminSections = [
  {
    label: 'نمای کلی',
    items: [
      { to: '/app/admin', label: 'داشبورد', icon: LayoutDashboard, end: true },
    ],
  },
  {
    label: 'مدیریت درمانگاه',
    items: [
      { to: '/app/doctors', label: 'پزشکان', icon: Stethoscope },
      { to: '/app/patients', label: 'بیماران', icon: Users },
      { to: '/app/appointments', label: 'نوبت‌ها', icon: CalendarDays },
      { to: '/app/book', label: 'ثبت نوبت', icon: CalendarPlus },
    ],
  },
  {
    label: 'منابع و امکانات',
    items: [
      { to: '/app/departments', label: 'بخش‌ها', icon: Building2 },
      { to: '/app/rooms', label: 'اتاق‌ها', icon: BedDouble },
      { to: '/app/nurses', label: 'پرستاران', icon: HeartPulse },
      { to: '/app/shifts', label: 'شیفت‌ها', icon: Clock },
    ],
  },
  {
    label: 'مدیریتی',
    items: [
      { to: '/app/staff', label: 'کارمندان', icon: UserCog },
      { to: '/app/reports', label: 'گزارشات', icon: BarChart3 },
      { to: '/app/settings', label: 'تنظیمات', icon: Settings },
    ],
  },
];

const doctorSections = [
  {
    label: 'پنل پزشک',
    items: [
      { to: '/app/doctor', label: 'داشبورد من', icon: LayoutDashboard, end: true },
      { to: '/app/appointments', label: 'نوبت‌ها', icon: CalendarDays },
    ],
  },
];

const nurseSections = [
  {
    label: 'پنل پرستار',
    items: [
      { to: '/app/nurse', label: 'داشبورد من', icon: LayoutDashboard, end: true },
      { to: '/app/appointments', label: 'نوبت‌ها', icon: CalendarDays },
      { to: '/app/nurse/vitals', label: 'علائم حیاتی', icon: Syringe },
      { to: '/app/nurse/notes', label: 'گزارش پرستاری', icon: FileText },
    ],
  },
];

const patientSections = [
  {
    label: 'پنل بیمار',
    items: [
      { to: '/app/patient', label: 'داشبورد من', icon: LayoutDashboard, end: true },
      { to: '/app/appointments', label: 'نوبت‌های من', icon: CalendarDays },
      { to: '/app/book', label: 'رزرو نوبت', icon: CalendarPlus },
      { to: '/app/patient/records', label: 'پرونده پزشکی', icon: ClipboardList },
    ],
  },
];

function getSections(roles = []) {
  const normalized = roles.map(r => r.replace('ROLE_', ''));
  if (normalized.includes('SUPER_ADMIN') || normalized.includes('ADMIN')) return adminSections;
  if (normalized.includes('DOCTOR')) return doctorSections;
  if (normalized.includes('NURSE')) return nurseSections;
  if (normalized.includes('PATIENT')) return patientSections;
  if (normalized.includes('RECEPTIONIST')) return adminSections; // TODO: receptionist sections
  return adminSections;
}

function getRoleLabel(roles = []) {
  const normalized = roles.map(r => r.replace('ROLE_', ''));
  if (normalized.includes('SUPER_ADMIN')) return 'مدیر ارشد';
  if (normalized.includes('ADMIN')) return 'مدیر سیستم';
  if (normalized.includes('DOCTOR')) return 'پزشک';
  if (normalized.includes('NURSE')) return 'پرستار';
  if (normalized.includes('PATIENT')) return 'بیمار';
  if (normalized.includes('RECEPTIONIST')) return 'منشی';
  return 'کاربر';
}

export default function Sidebar({ open, onClose }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const roles = user?.roles || [];
  const sections = getSections(roles);
  const roleLabel = getRoleLabel(roles);
  const userName = user?.username || 'کاربر';
  const displayName = user?.firstName || userName;

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <>
      {open && <div className="sidebar-backdrop" onClick={onClose} />}
      <aside className={`sidebar ${open ? 'open' : ''}`}>
        <div className="sidebar-brand">
          <span className="brand-mark" style={{ borderRadius: 10, overflow: 'hidden', padding: 0 }}>
            <img src="/logo.png" alt="Nova" style={{ width: 40, height: 40, objectFit: 'cover', display: 'block' }} />
          </span>
          <div>
            <div className="brand-name">نووا</div>
            <div className="brand-sub">سامانه مدیریت بیمارستان</div>
          </div>
          <button className="btn btn-ghost btn-icon btn-sm" style={{ marginRight: 'auto' }} onClick={onClose}>
            <X size={18} />
          </button>
        </div>

        <nav className="sidebar-nav">
          {sections.map((s) => (
            <div key={s.label}>
              <div className="sidebar-section-label">{s.label}</div>
              {s.items.map((it) => (
                <NavLink key={it.to} to={it.to} end={it.end} className="sidebar-link" onClick={onClose}>
                  <it.icon size={19} />
                  <span>{it.label}</span>
                </NavLink>
              ))}
            </div>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="sidebar-user">
            <span className="avatar avatar-sm">{initials(displayName)}</span>
            <div style={{ flex: 1, minWidth: 0 }}>
              <div className="fw-600 text-sm">{displayName}</div>
              <div className="text-xs text-muted">{roleLabel}</div>
            </div>
            <button
              className="theme-toggle"
              style={{ width: 36, height: 36, cursor: 'pointer' }}
              title="خروج"
              onClick={handleLogout}
            >
              <LogOut size={17} />
            </button>
          </div>
        </div>
      </aside>
    </>
  );
}
