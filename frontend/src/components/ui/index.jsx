/* Reusable presentational UI primitives, all theme-aware via CSS vars. */
import { Link } from 'react-router-dom';
import { initials, statusLabel } from '../../utils/format';

/* ---------- Button ---------- */
export function Button({ as, to, href, variant = 'primary', size, icon: Icon, iconRight, className = '', children, ...rest }) {
  const cls = ['btn', `btn-${variant}`, size ? `btn-${size}` : '', className].filter(Boolean).join(' ');
  const content = (
    <>
      {Icon && !iconRight && <Icon size={size === 'sm' ? 16 : 18} />}
      {children}
      {Icon && iconRight && <Icon size={size === 'sm' ? 16 : 18} />}
    </>
  );
  if (to) return <Link to={to} className={cls} {...rest}>{content}</Link>;
  if (href) return <a href={href} className={cls} {...rest}>{content}</a>;
  return <button className={cls} {...rest}>{content}</button>;
}

/* ---------- Card ---------- */
/* Renders a bare `.card` container; pages add `.card-body` / `.card-header`
   themselves so the structure stays correct (no double nesting). */
export function Card({ hover, className = '', children, ...rest }) {
  return (
    <div className={`card ${hover ? 'card-hover' : ''} ${className}`} {...rest}>
      {children}
    </div>
  );
}

export function CardHeader({ title, sub, action, className = '' }) {
  return (
    <div className={`card-header ${className}`}>
      <div>
        {title && <div className="card-title">{title}</div>}
        {sub && <div className="text-xs text-muted mt-8">{sub}</div>}
      </div>
      {action}
    </div>
  );
}

/* ---------- Badge ---------- */
export function Badge({ tone = 'neutral', dot, children, className = '' }) {
  return <span className={`badge badge-${tone} ${dot ? 'badge-dot' : ''} ${className}`}>{children}</span>;
}

export function StatusBadge({ group, value }) {
  const { label, tone } = statusLabel(group, value);
  return <Badge tone={tone} dot>{label}</Badge>;
}

/* ---------- Avatar ---------- */
export function Avatar({ name = '', size = 'md', className = '' }) {
  return <div className={`avatar avatar-${size} ${className}`}>{initials(name)}</div>;
}

/* ---------- Stat Card ---------- */
export function StatCard({ icon: Icon, label, value, trend, trendTone = 'success', index = 0 }) {
  return (
    <div className="stat-card fade-in-up" style={{ animationDelay: `${index * 0.05}s` }}>
      <div className="stat-glow" />
      {Icon && (
        <div className="stat-icon">
          <Icon size={24} />
        </div>
      )}
      <div className="stat-value">{value}</div>
      <div className="stat-label">{label}</div>
      {trend && (
        <div className={`stat-trend text-${trendTone}`}>{trend}</div>
      )}
    </div>
  );
}

/* ---------- Section header for dashboard pages ---------- */
export function PageHeader({ title, subtitle, actions }) {
  return (
    <div className="page-head">
      <div>
        <h1 className="page-title">{title}</h1>
        {subtitle && <p className="page-subtitle">{subtitle}</p>}
      </div>
      {actions && <div className="flex gap-8 flex-wrap">{actions}</div>}
    </div>
  );
}

/* ---------- Empty state ---------- */
export function EmptyState({ icon: Icon, title = 'موردی یافت نشد', desc }) {
  return (
    <div className="empty-state">
      {Icon && (
        <div className="es-icon">
          <Icon size={30} />
        </div>
      )}
      <p className="fw-600">{title}</p>
      {desc && <p className="text-sm mt-8">{desc}</p>}
    </div>
  );
}

/* ---------- Loader ---------- */
export function Spinner({ className = '' }) {
  return <span className={`spinner ${className}`} />;
}

export function PageLoader({ label = 'در حال بارگذاری…' }) {
  return (
    <div className="page-loader">
      <div className="flex-col items-center gap-12">
        <Spinner />
        <span className="text-muted text-sm">{label}</span>
      </div>
    </div>
  );
}
