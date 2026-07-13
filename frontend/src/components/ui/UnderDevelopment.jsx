import { Construction, ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';

/**
 * صفحه "در حال توسعه" — برای بخش‌هایی که هنوز آماده نیستند
 */
export default function UnderDevelopment({ title = 'در حال توسعه', subtitle, backTo = '/app', backLabel = 'بازگشت به داشبورد' }) {
  return (
    <div style={{
      display: 'flex', flexDirection: 'column', alignItems: 'center',
      justifyContent: 'center', minHeight: '60vh', textAlign: 'center',
      padding: 40
    }}>
      <div style={{
        width: 100, height: 100, borderRadius: '50%',
        background: 'linear-gradient(135deg, var(--brand), var(--brand-2))',
        display: 'grid', placeItems: 'center', marginBottom: 24,
        animation: 'pulse 2s infinite'
      }}>
        <Construction size={48} color="#fff" />
      </div>
      <h2 style={{ fontSize: '1.8rem', fontWeight: 900, marginBottom: 8 }}>{title}</h2>
      <p style={{ color: 'var(--text-muted)', fontSize: '1rem', maxWidth: 400, marginBottom: 8 }}>
        {subtitle || 'این بخش در حال توسعه است و به زودی آماده خواهد شد'}
      </p>
      <p style={{ color: 'var(--text-faint)', fontSize: '0.85rem', marginBottom: 32 }}>
        از شکیبایی شما متشکریم 🙏
      </p>
      <Link to={backTo} style={{
        display: 'inline-flex', alignItems: 'center', gap: 8,
        padding: '12px 24px', borderRadius: 12,
        background: 'var(--surface-2)', border: '1px solid var(--border)',
        color: 'var(--text)', fontWeight: 700, textDecoration: 'none',
        transition: '0.2s'
      }}>
        <ArrowRight size={18} /> {backLabel}
      </Link>
      <style>{`
        @keyframes pulse {
          0%, 100% { transform: scale(1); opacity: 1; }
          50% { transform: scale(1.05); opacity: 0.8; }
        }
      `}</style>
    </div>
  );
}
