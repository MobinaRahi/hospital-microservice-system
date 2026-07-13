import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ArrowRight, User, Phone, Lock, Eye, EyeOff, UserPlus, ShieldCheck, AlertCircle, CheckCircle2 } from 'lucide-react';
import ThemeToggle from '../../components/ui/ThemeToggle';
import EcgMonitor from '../../components/ui/EcgMonitor';
import { Spinner } from '../../components/ui';
import { faNumber } from '../../utils/format';

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ fullName: '', nationalId: '', phone: '', password: '', confirmPassword: '' });
  const [show, setShow] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    if (form.password !== form.confirmPassword) {
      setError('رمز عبور و تکرار آن مطابقت ندارند.');
      return;
    }
    setLoading(true);
    // TODO: Connect to API
    setTimeout(() => {
      setSuccess(true);
      setLoading(false);
    }, 1500);
  };

  if (success) {
    return (
      <div className="auth-wrap">
        <aside className="auth-aside">
          <div className="auth-aside-bg" />
          <div className="auth-aside-body" style={{ display: 'grid', placeItems: 'center' }}>
            <div style={{ textAlign: 'center' }}>
              <CheckCircle2 size={80} style={{ color: '#10b981', marginBottom: 20 }} />
              <h1 style={{ fontSize: '2rem', fontWeight: 900 }}>ثبت‌نام موفق!</h1>
              <p style={{ marginTop: 12, opacity: 0.8 }}>حساب کاربری شما با موفقیت ایجاد شد</p>
            </div>
          </div>
        </aside>
        <div className="auth-form-side">
          <div className="auth-card" style={{ textAlign: 'center' }}>
            <h2 style={{ marginBottom: 16 }}>خوش آمدید! 🎉</h2>
            <p style={{ color: 'var(--text-muted)', marginBottom: 24 }}>اکنون می‌توانید وارد حساب خود شوید</p>
            <Link to="/login" className="auth-submit" style={{ display: 'inline-flex', textDecoration: 'none' }}>
              ورود به حساب
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-wrap">
      <aside className="auth-aside">
        <div className="auth-aside-bg" />
        <Link to="/" className="auth-logo">
          <span className="mark" style={{ borderRadius: 12, overflow: 'hidden', padding: 0 }}>
            <img src="/logo.png" alt="Nova" style={{ width: 44, height: 44, objectFit: 'cover', display: 'block' }} />
          </span>
          <span>نووا<small>NOVA HOSPITAL</small></span>
        </Link>

        <div className="auth-aside-body">
          <h1>عضو خانواده نووا شوید</h1>
          <p>با ثبت‌نام، به تمام امکانات سامانه دسترسی خواهید داشت</p>
          <div className="auth-ecg-card">
            <EcgMonitor bpm={72} />
            <div className="auth-ecg-label"><span className="lbl-dot" /> ثبت‌نام سریع و امن</div>
          </div>
          <div className="auth-feats">
            {[
              { icon: ShieldCheck, t: 'رزرو نوبت آنلاین' },
              { icon: User, t: 'دسترسی به پرونده پزشکی' },
              { icon: Phone, t: 'اعلان یادآوری نوبت' },
            ].map(x => (
              <div key={x.t} className="auth-feat">
                <span className="fi"><x.icon size={18} /></span>
                <span>{x.t}</span>
              </div>
            ))}
          </div>
        </div>
        <p className="auth-aside-foot">© {faNumber(1404)} بیمارستان فوق‌تخصصی نووا</p>
      </aside>

      <div className="auth-form-side">
        <div className="auth-top-actions"><ThemeToggle /></div>
        <div className="auth-card fade-in-up">
          <Link to="/" className="auth-back"><ArrowRight size={18} /> بازگشت به خانه</Link>
          <h2>ثبت‌نام</h2>
          <p className="auth-sub">اطلاعات خود را وارد کنید</p>

          {error && <div className="auth-error"><AlertCircle size={18} /> {error}</div>}

          <form onSubmit={submit}>
            <div className="field">
              <label className="field-label">نام و نام خانوادگی <span className="req">*</span></label>
              <div className="input-group has-icon">
                <span className="input-icon"><User size={18} /></span>
                <input className="input" placeholder="نام کامل" value={form.fullName} onChange={e => setForm({ ...form, fullName: e.target.value })} required autoFocus />
              </div>
            </div>
            <div className="field">
              <label className="field-label">کد ملی <span className="req">*</span></label>
              <div className="input-group has-icon">
                <span className="input-icon"><ShieldCheck size={18} /></span>
                <input className="input" placeholder="کد ملی ۱۰ رقمی" value={form.nationalId} onChange={e => setForm({ ...form, nationalId: e.target.value })} required />
              </div>
            </div>
            <div className="field">
              <label className="field-label">شماره موبایل <span className="req">*</span></label>
              <div className="input-group has-icon">
                <span className="input-icon"><Phone size={18} /></span>
                <input className="input" type="tel" placeholder="۰۹۱۲۱۲۳۴۵۶۷" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} required />
              </div>
            </div>
            <div className="field">
              <label className="field-label">رمز عبور <span className="req">*</span></label>
              <div className="input-group has-icon">
                <span className="input-icon"><Lock size={18} /></span>
                <input className="input" type={show ? 'text' : 'password'} placeholder="رمز عبور" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} required />
                <button type="button" className="input-icon input-icon-action" onClick={() => setShow(s => !s)}>
                  {show ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>
            <div className="field">
              <label className="field-label">تکرار رمز عبور <span className="req">*</span></label>
              <div className="input-group has-icon">
                <span className="input-icon"><Lock size={18} /></span>
                <input className="input" type="password" placeholder="تکرار رمز عبور" value={form.confirmPassword} onChange={e => setForm({ ...form, confirmPassword: e.target.value })} required />
              </div>
            </div>
            <button className="auth-submit" type="submit" disabled={loading}>
              {loading ? <><Spinner /> در حال ثبت‌نام…</> : <><UserPlus size={19} /> ثبت‌نام</>}
            </button>
          </form>

          <p style={{ textAlign: 'center', marginTop: 20, fontSize: '0.9rem', color: 'var(--text-muted)' }}>
            قبلاً ثبت‌نام کرده‌اید؟ <Link to="/login" style={{ color: 'var(--brand)', fontWeight: 700 }}>ورود</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
