import { Sun, Moon } from 'lucide-react';
import { useTheme } from '../../context/ThemeContext';

export default function ThemeToggle({ className = '' }) {
  const { theme, toggle } = useTheme();
  return (
    <button
      className={`theme-toggle ${className}`}
      onClick={toggle}
      aria-label={theme === 'dark' ? 'حالت روشن' : 'حالت تاریک'}
      title={theme === 'dark' ? 'حالت روشن' : 'حالت تاریک'}
    >
      <span className="icon-wrap">
        <Sun className="sun" size={20} />
        <Moon className="moon" size={20} />
      </span>
    </button>
  );
}
