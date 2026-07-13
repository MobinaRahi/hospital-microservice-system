import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import {
  ArrowRight, User, Lock, Eye, EyeOff, LogIn, ShieldCheck,
  HeartPulse, Activity, AlertCircle,
} from 'lucide-react';
import ThemeToggle from '../../components/ui/ThemeToggle';
import EcgMonitor from '../../components/ui/EcgMonitor';
import { Spinner } from '../../components/ui';
import { useAuth } from '../../context/AuthContext';
import { faNumber } from '../../utils/format';

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();
  const [form, setForm] = useState({ username: '', password: '' });
  const [show, setShow] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await login(form);
      // If user came from a specific page, go there; otherwise go to /app (which routes by role)
      const dest = location.state?.from || '/app';
      navigate(dest, { replace: true });
    } catch (err) {
      setError(typeof err === 'string' ? err : (err?.message || 'نام کاربری یا رمز عبور نادرست است.'));
    } finally {
      setLoading(false);
    }
  };

  const quickFill = (u, p) => setForm({ username: u, password: p });

  return (
    <div className="auth-wrap">
      {/* ---- Branded aside ---- */}
      <aside className="auth-aside">
        <div className="auth-aside-bg" />
        <Link to="/" className="auth-logo">
          <span className="mark" style={{ borderRadius: 12, overflow: 'hidden', padding: 0 }}>
            <img src="/logo.png" alt="Nova" style={{ width: 44, height: 44, objectFit: 'cover', display: 'block' }} />
          </span>
          <span>
            نووا<small>NOVA HOSPITAL</small>
          </span>
        </Link>

        <div className="auth-aside-body">
          <h1>به سامانه یکپارچه نووا خوش آمدید</h1>
          <p>
            مدیریت هوشمند بیماران، پزشکان، نوبت‌ها و بخش‌ها —
            همه در یک داشبورد حرفه‌ای، امن و سریع.
          </p>

          {/* ECG monitor mini-card */}
          <div className="auth-ecg-card">
            <EcgMonitor bpm={72} />
            <div className="auth-ecg-label">
              <span className="lbl-dot" /> ECG • {faNumber(72)} bpm — سیستم فعال
            </div>
          </div>

          <div className="auth-feats">
            {[
              { icon: ShieldCheck, t: 'احراز هویت توکنی (JWT)' },
              { icon: HeartPulse, t: 'پایش لحظه‌ای بیماران' },
              { icon: Activity, t: 'گزارش‌ها و نمودارهای زنده' },
            ].map((x) => (
              <div key={x.t} className="auth-feat">
                <span className="fi"><x.icon size={18} /></span>
                <span>{x.t}</span>
              </div>
            ))}
          </div>
        </div>

        <p className="auth-aside-foot">© {faNumber(1404)} بیمارستان فوق‌تخصصی نووا</p>
      </aside>

      {/* ---- Form side ---- */}
      <div className="auth-form-side">
        <div className="auth-top-actions">
          <ThemeToggle />
        </div>

        <div className="auth-card fade-in-up">
          <Link to="/" className="auth-back">
            <ArrowRight size={18} /> بازگشت به خانه
          </Link>

          <h2>ورود به حساب</h2>
          <p className="auth-sub">برای دسترسی به داشبورد، اطلاعات خود را وارد کنید.</p>

          {error && (
            <div className="auth-error">
              <AlertCircle size={18} /> {error}
            </div>
          )}

          <form onSubmit={submit}>
            <div className="field">
              <label className="field-label">نام کاربری <span className="req">*</span></label>
              <div className="input-group has-icon">
                <span className="input-icon"><User size={18} /></span>
                <input
                  className="input"
                  placeholder="نام کاربری خود را وارد کنید"
                  value={form.username}
                  onChange={(e) => setForm({ ...form, username: e.target.value })}
                  required
                  autoFocus
                />
              </div>
            </div>

            <div className="field">
              <label className="field-label">رمز عبور <span className="req">*</span></label>
              <div className="input-group has-icon">
                <span className="input-icon"><Lock size={18} /></span>
                <input
                  className="input"
                  placeholder="رمز عبور"
                  type={show ? 'text' : 'password'}
                  value={form.password}
                  onChange={(e) => setForm({ ...form, password: e.target.value })}
                  required
                />
                <button
                  type="button"
                  className="input-icon input-icon-action"
                  onClick={() => setShow((s) => !s)}
                  aria-label={show ? 'پنهان کردن رمز' : 'نمایش رمز'}
                >
                  {show ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>

            <div className="auth-row">
              <label className="auth-remember">
                <span className="switch"><input type="checkbox" defaultChecked /><span className="slider" /></span>
                مرا به خاطر بسپار
              </label>
              <Link to="/forgot-password" className="auth-forgot">فراموشی رمز؟</Link>
            </div>

            <button className="auth-submit" type="submit" disabled={loading}>
              {loading ? <><Spinner /> ورود…</> : <><LogIn size={19} /> ورود به سامانه</>}
            </button>
          </form>

          <div className="auth-demo">
            <div className="auth-demo-label">حساب‌های نمایشی (کلیک = پر کردن سریع):</div>
            <div className="auth-demo-grid">
              <button type="button" onClick={() => quickFill('admin', 'Admin@123')}>
                <b>admin</b><span>مدیر</span>
              </button>
              <button type="button" onClick={() => quickFill('dr.ali', 'Doctor@123')}>
                <b>dr.ali</b><span>پزشک</span>
              </button>
              <button type="button" onClick={() => quickFill('nurse.fatemeh', 'Nurse@123')}>
                <b>nurse.fatemeh</b><span>پرستار</span>
              </button>
              <button type="button" onClick={() => quickFill('patient.reza', 'Patient@123')}>
                <b>patient.reza</b><span>بیمار</span>
              </button>
            </div>
          </div>

          <div style={{ textAlign: 'center', marginTop: 20 }}>
            <p style={{ fontSize: '0.9rem', color: 'var(--text-muted)' }}>
              حساب ندارید؟ <Link to="/register" style={{ color: 'var(--brand)', fontWeight: 700 }}>ثبت‌نام کنید</Link>
            </p>
            <p style={{ fontSize: '0.85rem', marginTop: 8 }}>
              <Link to="/forgot-password" style={{ color: 'var(--text-muted)', textDecoration: 'none' }}>رمز عبور را فراموش کرده‌اید؟</Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
