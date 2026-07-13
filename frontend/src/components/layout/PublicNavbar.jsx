import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { CalendarPlus, Menu as MenuIcon, LogIn } from 'lucide-react';
import ThemeToggle from '../ui/ThemeToggle';
import { useAuth } from '../../context/AuthContext';

const links = [
  { to: '/', label: 'خانه', hash: 'home' },
  { to: '/doctors', label: 'پزشکان' },
  { to: '/departments', label: 'بخش‌ها' },
  { to: '/about', label: 'درباره ما' },
  { to: '/contact', label: 'تماس' },
  { to: '/track', label: 'پیگیری نوبت' },
];

export default function PublicNavbar() {
  const [open, setOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    onScroll();
    window.addEventListener('scroll', onScroll, { passive: true });
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  return (
    <header className={`lp-nav ${scrolled ? 'scrolled' : ''}`}>
      <div className="bar">
        <Link to="/" className="lp-brand">
          <span className="mark" style={{ borderRadius: 12, overflow: 'hidden', padding: 0 }}>
            <img src="/logo.png" alt="Nova" style={{ width: 42, height: 42, objectFit: 'cover', display: 'block' }} />
          </span>
          <span>
            نووا
            <small>NOVA HOSPITAL</small>
          </span>
        </Link>

        <nav className={`lp-menu ${open ? 'open' : ''}`}>
          {links.map((l) => (
            <Link key={l.to} to={l.to} onClick={() => setOpen(false)}>
              {l.label}
            </Link>
          ))}
          <Link to="/book" className="cta" onClick={() => setOpen(false)}>
            رزرو نوبت <CalendarPlus size={17} />
          </Link>
        </nav>

        <div className="lp-actions">
          <ThemeToggle />
          {isAuthenticated ? (
            <Link to="/app" className="lp-btn lp-btn-pri" style={{ padding: '8px 18px', fontSize: '0.85rem' }}>
              داشبورد
            </Link>
          ) : (
            <Link to="/login" className="lp-btn lp-btn-ghost" style={{ padding: '8px 18px', fontSize: '0.85rem' }}>
              <LogIn size={16} /> ورود
            </Link>
          )}
        </div>

        <button className="lp-burger" onClick={() => setOpen((o) => !o)} aria-label="منو">
          <span /><span /><span />
        </button>
      </div>
    </header>
  );
}
