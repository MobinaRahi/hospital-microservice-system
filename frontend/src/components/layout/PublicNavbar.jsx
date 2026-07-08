import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { CalendarPlus, Menu as MenuIcon } from 'lucide-react';
import ThemeToggle from '../ui/ThemeToggle';

const links = [
  { to: '/', label: 'خانه', hash: 'home' },
  { to: '/#how', label: 'نحوه کار', hash: 'how' },
  { to: '/#services', label: 'خدمات', hash: 'services' },
  { to: '/#departments', label: 'بخش‌ها', hash: 'departments' },
  { to: '/#doctors', label: 'پزشکان', hash: 'doctors' },
];

export default function PublicNavbar() {
  const [open, setOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);

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
          <span className="mark">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M11 2h2a2 2 0 0 1 2 2v5h5a2 2 0 0 1 2 2v2a2 2 0 0 1-2 2h-5v5a2 2 0 0 1-2 2h-2a2 2 0 0 1-2-2v-5H4a2 2 0 0 1-2-2v-2a2 2 0 0 1 2-2h5V4a2 2 0 0 1 2-2z" />
            </svg>
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

        <ThemeToggle />
        <button className="lp-burger" onClick={() => setOpen((o) => !o)} aria-label="منو">
          <span /><span /><span />
        </button>
      </div>
    </header>
  );
}
