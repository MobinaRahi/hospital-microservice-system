import { NavLink } from 'react-router-dom';
import {
  Activity, LayoutDashboard, Stethoscope, Users, CalendarDays,
  Building2, BedDouble, UserRound, Clock, CalendarPlus, HeartPulse,
  ClipboardList, LogOut, X,
} from 'lucide-react';

const sections = [
  {
    label: 'نمای کلی',
    items: [
      { to: '/app', label: 'داشبورد', icon: LayoutDashboard, end: true },
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
    label: 'پنل‌های تخصصی',
    items: [
      { to: '/app/doctor', label: 'پنل پزشک', icon: ClipboardList },
      { to: '/app/patient', label: 'پنل بیمار', icon: UserRound },
    ],
  },
];

export default function Sidebar({ open, onClose }) {
  return (
    <>
      {open && <div className="sidebar-backdrop" onClick={onClose} />}
      <aside className={`sidebar ${open ? 'open' : ''}`}>
        <div className="sidebar-brand">
          <span className="brand-mark"><Activity size={22} color="#fff" /></span>
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
            <span className="avatar avatar-sm">اد</span>
            <div style={{ flex: 1, minWidth: 0 }}>
              <div className="fw-600 text-sm">مدیر سیستم</div>
              <div className="text-xs text-muted">admin</div>
            </div>
            <a href="/login" className="theme-toggle" style={{ width: 36, height: 36 }} title="خروج">
              <LogOut size={17} />
            </a>
          </div>
        </div>
      </aside>
    </>
  );
}
