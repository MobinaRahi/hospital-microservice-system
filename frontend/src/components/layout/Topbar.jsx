import { Menu, Search, Bell, LogOut } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import ThemeToggle from '../ui/ThemeToggle';
import { useAuth } from '../../context/AuthContext';

export default function Topbar({ onMenu, title }) {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

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
        {user && (
          <span className="text-sm text-muted hidden" style={{ display: 'none' }}>{user.username}</span>
        )}
        <button className="theme-toggle" onClick={handleLogout} aria-label="خروج" title="خروج از حساب">
          <LogOut size={20} />
        </button>
      </div>
    </header>
  );
}
