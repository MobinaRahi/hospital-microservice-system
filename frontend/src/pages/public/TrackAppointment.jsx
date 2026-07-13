import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Search, CalendarDays, Clock, User, Stethoscope, CheckCircle2, XCircle, AlertCircle } from 'lucide-react';
import PublicNavbar from '../../components/layout/PublicNavbar';
import Footer from '../../components/layout/Footer';

export default function TrackAppointment() {
  const [code, setCode] = useState('');
  const [result, setResult] = useState(null);
  const [searched, setSearched] = useState(false);

  const handleSearch = (e) => {
    e.preventDefault();
    setSearched(true);
    // TODO: Connect to API
    // Demo result
    if (code.length >= 5) {
      setResult({
        trackingCode: code,
        patientName: 'علی رضایی',
        doctorName: 'دکتر سارا محمدی',
        department: 'قلب و عروق',
        date: '۱۴۰۴/۰۴/۲۰',
        time: '۱۰:۰۰',
        status: 'SCHEDULED',
      });
    } else {
      setResult(null);
    }
  };

  const statusMap = {
    SCHEDULED: { label: 'برنامه‌ریزی شده', color: '#0ea5e9', icon: CalendarDays },
    CHECKED_IN: { label: 'پذیرش شده', color: '#f59e0b', icon: Clock },
    COMPLETED: { label: 'تکمیل شده', color: '#10b981', icon: CheckCircle2 },
    CANCELLED: { label: 'لغو شده', color: '#ef4444', icon: XCircle },
  };

  return (
    <>
      <PublicNavbar />
      <main style={{ paddingTop: 100, minHeight: '100vh' }}>
        <section className="lp-section">
          <div className="container" style={{ maxWidth: 600 }}>
            <div className="lp-head">
              <span className="lp-eyebrow"><span className="dot" /> پیگیری نوبت</span>
              <h2>وضعیت نوبت خود را <span className="lp-grad">بررسی کنید</span></h2>
              <p>کد رهگیری خود را وارد کنید</p>
            </div>

            <form onSubmit={handleSearch} style={{
              background: 'var(--surface)', border: '1px solid var(--border)',
              borderRadius: 16, padding: 24, marginBottom: 24
            }}>
              <div style={{ display: 'flex', gap: 12 }}>
                <input
                  type="text"
                  placeholder="کد رهگیری را وارد کنید"
                  value={code}
                  onChange={e => setCode(e.target.value)}
                  style={{
                    flex: 1, padding: '14px 18px', borderRadius: 12,
                    border: '1px solid var(--border)', background: 'var(--surface-2)',
                    color: 'var(--text)', fontSize: '1rem'
                  }}
                />
                <button type="submit" className="lp-btn lp-btn-pri" style={{ padding: '14px 24px' }}>
                  <Search size={18} /> جستجو
                </button>
              </div>
            </form>

            {searched && !result && (
              <div style={{
                textAlign: 'center', padding: 40,
                background: 'var(--surface)', border: '1px solid var(--border)',
                borderRadius: 16
              }}>
                <AlertCircle size={48} style={{ color: 'var(--warning)', marginBottom: 12 }} />
                <h3>نوبتی یافت نشد</h3>
                <p style={{ color: 'var(--text-muted)' }}>کد رهگیری را بررسی کنید و دوباره تلاش کنید</p>
              </div>
            )}

            {result && (() => {
              const StatusIcon = statusMap[result.status]?.icon;
              const statusColor = statusMap[result.status]?.color;
              const statusLabel = statusMap[result.status]?.label;
              return (
              <div style={{
                background: 'var(--surface)', border: '1px solid var(--border)',
                borderRadius: 16, padding: 24
              }}>
                <div style={{
                  display: 'flex', alignItems: 'center', gap: 12, marginBottom: 20,
                  paddingBottom: 16, borderBottom: '1px solid var(--border)'
                }}>
                  <div style={{
                    width: 50, height: 50, borderRadius: 14,
                    background: `${statusColor}22`,
                    display: 'grid', placeItems: 'center'
                  }}>
                    {StatusIcon && <StatusIcon size={24} style={{ color: statusColor }} />}
                  </div>
                  <div>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>کد رهگیری: {result.trackingCode}</div>
                    <div style={{ fontWeight: 800, color: statusColor }}>
                      {statusLabel}
                    </div>
                  </div>
                </div>

                <div style={{ display: 'grid', gap: 12 }}>
                  {[
                    { icon: User, label: 'بیمار', value: result.patientName },
                    { icon: Stethoscope, label: 'پزشک', value: result.doctorName },
                    { icon: CalendarDays, label: 'بخش', value: result.department },
                    { icon: CalendarDays, label: 'تاریخ', value: result.date },
                    { icon: Clock, label: 'ساعت', value: result.time },
                  ].map(item => (
                    <div key={item.label} style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                      <item.icon size={18} style={{ color: 'var(--text-muted)' }} />
                      <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)', minWidth: 60 }}>{item.label}:</span>
                      <span style={{ fontWeight: 600 }}>{item.value}</span>
                    </div>
                  ))}
                </div>

                <div style={{ marginTop: 20, textAlign: 'center' }}>
                  <Link to="/login" style={{
                    display: 'inline-flex', alignItems: 'center', gap: 8,
                    padding: '10px 20px', borderRadius: 10,
                    background: 'var(--gradient-brand-soft)', color: 'var(--brand)',
                    fontWeight: 700, fontSize: '0.9rem', textDecoration: 'none'
                  }}>
                    ورود به پنل برای مدیریت نوبت
                  </Link>
                </div>
              </div>
              );
            })()}
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
}
