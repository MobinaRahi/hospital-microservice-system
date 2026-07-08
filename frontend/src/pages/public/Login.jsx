import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  Activity, User, Lock, Eye, EyeOff, LogIn, ShieldCheck, HeartPulse, ArrowRight,
} from 'lucide-react';
import ThemeToggle from '../../components/ui/ThemeToggle';
import { Button, Spinner } from '../../components/ui';
import { endpoints } from '../../api/endpoints';

export default function Login() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', password: '' });
  const [show, setShow] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await endpoints.auth.login(form);
      const token = res?.accessToken || res?.token;
      if (token) localStorage.setItem('nova-token', token);
      navigate('/app');
    } catch (err) {
      // Backend may be down in demo — still allow entering the dashboard
      setError(typeof err === 'string' ? err : 'ورود ناموفق بود. در حالت دمو وارد داشبورد می‌شوید.');
      setTimeout(() => navigate('/app'), 900);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-wrap">
      {/* Branded aside */}
      <aside className="auth-aside">
        <Link to="/" className="flex items-center gap-12" style={{ position: 'relative', zIndex: 2 }}>
          <span className="brand-mark" style={{ width: 42, height: 42, borderRadius: 12, background: 'rgba(255,255,255,0.18)', display: 'grid', placeItems: 'center' }}>
            <Activity size={24} color="#fff" />
          </span>
          <span>
            <span className="fw-800" style={{ fontSize: '1.1rem' }}>نووا</span>
            <span style={{ display: 'block', fontSize: '0.78rem', opacity: 0.8 }}>سامانه مدیریت بیمارستان</span>
          </span>
        </Link>

        <div style={{ position: 'relative', zIndex: 2 }}>
          <h1 style={{ fontSize: '2.2rem', fontWeight: 800, lineHeight: 1.3 }}>به سامانه یکپارچه نووا خوش آمدید</h1>
          <p style={{ marginTop: 16, opacity: 0.9, fontSize: '1.05rem', lineHeight: 1.9 }}>
            مدیریت هوشمند بیماران، پزشکان، نوبت‌ها و بخش‌ها — همه در یک داشبورد حرفه‌ای و امن.
          </p>
          <div className="flex-col gap-16" style={{ marginTop: 32 }}>
            {[
              { icon: ShieldCheck, t: 'امنیت بالا با احراز هویت توکنی (JWT)' },
              { icon: HeartPulse, t: 'پایش لحظه‌ای وضعیت بیماران' },
              { icon: Activity, t: 'گزارش‌ها و نمودارهای زنده' },
            ].map((x) => (
              <div key={x.t} className="flex items-center gap-12">
                <span style={{ width: 38, height: 38, borderRadius: 10, background: 'rgba(255,255,255,0.16)', display: 'grid', placeItems: 'center' }}>
                  <x.icon size={19} />
                </span>
                <span style={{ fontSize: '0.94rem' }}>{x.t}</span>
              </div>
            ))}
          </div>
        </div>

        <p style={{ position: 'relative', zIndex: 2, opacity: 0.7, fontSize: '0.82rem' }}>© ۱۴۰۵ بیمارستان فوق‌تخصصی نووا</p>
      </aside>

      {/* Form side */}
      <div className="auth-form-side">
        <div style={{ position: 'absolute', top: 24, left: 24 }}>
          <ThemeToggle />
        </div>

        <div className="auth-card fade-in-up">
          <div className="flex items-center gap-8 mb-8">
            <Link to="/" className="theme-toggle btn-icon" style={{ width: 38, height: 38 }}><ArrowRight size={18} /></Link>
            <span className="text-sm text-muted">بازگشت به خانه</span>
          </div>

          <h2 className="text-2xl fw-800">ورود به حساب</h2>
          <p className="text-muted mt-8 mb-24">برای دسترسی به داشبورد وارد شوید.</p>

          {error && (
            <div className="badge badge-danger w-full mb-16" style={{ padding: '11px 14px', borderRadius: 12 }}>
              {error}
            </div>
          )}

          <form onSubmit={submit}>
            <div className="field">
              <label className="field-label">نام کاربری <span className="req">*</span></label>
              <div className="input-group has-icon">
                <span className="input-icon"><User size={18} /></span>
                <input
                  className="input" placeholder="نام کاربری خود را وارد کنید"
                  value={form.username}
                  onChange={(e) => setForm({ ...form, username: e.target.value })}
                  required autoFocus
                />
              </div>
            </div>

            <div className="field">
              <label className="field-label">رمز عبور <span className="req">*</span></label>
              <div className="input-group has-icon">
                <span className="input-icon"><Lock size={18} /></span>
                <input
                  className="input" placeholder="رمز عبور"
                  type={show ? 'text' : 'password'}
                  value={form.password}
                  onChange={(e) => setForm({ ...form, password: e.target.value })}
                  required
                />
                <button type="button" className="input-icon" style={{ pointerEvents: 'auto', left: 14, right: 'auto' }} onClick={() => setShow((s) => !s)}>
                  {show ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>

            <div className="flex items-center justify-between mb-24">
              <label className="flex items-center gap-8 text-sm text-secondary">
                <span className="switch"><input type="checkbox" defaultChecked /><span className="slider" /></span>
                مرا به خاطر بسپار
              </label>
              <a href="#" className="text-sm text-brand">فراموشی رمز؟</a>
            </div>

            <Button type="submit" size="lg" className="btn-block" icon={loading ? undefined : LogIn} disabled={loading}>
              {loading ? <><Spinner /> ورود…</> : 'ورود به سامانه'}
            </Button>
          </form>

          <div className="card mt-24" style={{ background: 'var(--surface-2)' }}>
            <div className="card-body card-body-sm">
              <div className="text-xs text-muted mb-8">حساب‌های نمایشی:</div>
              <div className="text-sm flex-col gap-8">
                <span><b>admin</b> / Admin@123 — مدیر</span>
                <span><b>dr.ali</b> / Doctor@123 — پزشک</span>
                <span><b>patient.reza</b> / Patient@123 — بیمار</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
