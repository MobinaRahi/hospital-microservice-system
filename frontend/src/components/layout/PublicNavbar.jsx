import { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Menu, Activity, CalendarPlus, LogIn } from 'lucide-react';
import ThemeToggle from '../ui/ThemeToggle';
import { Button } from '../ui';

const links = [
  { to: '/', label: 'خانه' },
  { to: '/#services', label: 'خدمات' },
  { to: '/#departments', label: 'بخش‌ها' },
  { to: '/#doctors', label: 'پزشکان' },
  { to: '/#contact', label: 'تماس' },
];

export default function PublicNavbar() {
  const [open, setOpen] = useState(false);
  const { pathname } = useLocation();

  return (
    <header className="public-nav">
      <div className="public-nav-inner">
        <Link to="/" className="flex items-center gap-12" style={{ flexShrink: 0 }}>
          <span className="sidebar-brand brand-mark" style={{ width: 40, height: 40, borderRadius: 12, background: 'var(--gradient-brand)', display: 'grid', placeItems: 'center' }}>
            <Activity size={22} color="#fff" />
          </span>
          <span>
            <span className="fw-800" style={{ display: 'block', lineHeight: 1.2 }}>نووا</span>
            <span className="text-xs text-muted">بیمارستان فوق‌تخصصی</span>
          </span>
        </Link>

        <nav className="nav-links" style={{ flex: 1, justifyContent: 'center' }} id="navLinks" data-open={open}>
          {links.map((l) => (
            <Link key={l.to} to={l.to} className={pathname === l.to ? 'active' : ''} onClick={() => setOpen(false)}>
              {l.label}
            </Link>
          ))}
        </nav>

        <div className="flex items-center gap-8" style={{ marginLeft: 'auto' }}>
          <ThemeToggle />
          <Button to="/book" variant="soft" size="sm" icon={CalendarPlus}>رزرو نوبت</Button>
          <Button to="/login" variant="primary" size="sm" icon={LogIn}>ورود</Button>
          <button className="btn btn-ghost btn-icon nav-burger" onClick={() => setOpen((o) => !o)} aria-label="منو">
            <Menu size={20} />
          </button>
        </div>
      </div>
    </header>
  );
}
