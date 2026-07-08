import { Menu, Search, Bell } from 'lucide-react';
import ThemeToggle from '../ui/ThemeToggle';

export default function Topbar({ onMenu, title }) {
  return (
    <header className="topbar">
      <button className="btn btn-ghost btn-icon nav-burger" onClick={onMenu} aria-label="منو">
        <Menu size={20} />
      </button>

      <div className="topbar-search">
        <Search className="s-icon" size={18} />
        <input placeholder="جستجوی بیمار، پزشک، نوبت…" />
      </div>

      <div style={{ marginRight: 'auto' }} className="flex items-center gap-8">
        <ThemeToggle />
        <button className="theme-toggle" style={{ position: 'relative' }} aria-label="اعلان‌ها">
          <Bell size={20} />
          <span style={{ position: 'absolute', top: 8, left: 9, width: 8, height: 8, borderRadius: '50%', background: 'var(--danger)', border: '2px solid var(--surface)' }} />
        </button>
      </div>
    </header>
  );
}
