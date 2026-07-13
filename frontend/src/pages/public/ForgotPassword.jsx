import { useState } from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight, Phone, Lock, KeyRound, ShieldCheck, AlertCircle, CheckCircle2 } from 'lucide-react';
import ThemeToggle from '../../components/ui/ThemeToggle';
import EcgMonitor from '../../components/ui/EcgMonitor';
import { Spinner } from '../../components/ui';
import { faNumber } from '../../utils/format';

export default function ForgotPassword() {
  const [step, setStep] = useState(1); // 1=phone, 2=code, 3=newPassword, 4=done
  const [phone, setPhone] = useState('');
  const [code, setCode] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSendCode = (e) => {
    e.preventDefault();
    setLoading(true);
    // TODO: Connect to API
    setTimeout(() => { setStep(2); setLoading(false); }, 1000);
  };

  const handleVerifyCode = (e) => {
    e.preventDefault();
    setLoading(true);
    setTimeout(() => { setStep(3); setLoading(false); }, 1000);
  };

  const handleResetPassword = (e) => {
    e.preventDefault();
    setLoading(true);
    setTimeout(() => { setStep(4); setLoading(false); }, 1000);
  };

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
          <h1>بازیابی رمز عبور</h1>
          <p>نگران نباشید! رمز عبور خود را به راحتی بازیابی کنید</p>
          <div className="auth-ecg-card">
            <EcgMonitor bpm={72} />
            <div className="auth-ecg-label"><span className="lbl-dot" /> بازیابی امن</div>
          </div>
        </div>
        <p className="auth-aside-foot">© {faNumber(1404)} بیمارستان فوق‌تخصصی نووا</p>
      </aside>

      <div className="auth-form-side">
        <div className="auth-top-actions"><ThemeToggle /></div>
        <div className="auth-card fade-in-up">
          <Link to="/login" className="auth-back"><ArrowRight size={18} /> بازگشت به ورود</Link>

          {/* Step 1: Phone */}
          {step === 1 && (
            <>
              <h2>فراموشی رمز عبور</h2>
              <p className="auth-sub">شماره موبایل خود را وارد کنید</p>
              {error && <div className="auth-error"><AlertCircle size={18} /> {error}</div>}
              <form onSubmit={handleSendCode}>
                <div className="field">
                  <label className="field-label">شماره موبایل</label>
                  <div className="input-group has-icon">
                    <span className="input-icon"><Phone size={18} /></span>
                    <input className="input" type="tel" placeholder="۰۹۱۲۱۲۳۴۵۶۷" value={phone} onChange={e => setPhone(e.target.value)} required autoFocus />
                  </div>
                </div>
                <button className="auth-submit" type="submit" disabled={loading}>
                  {loading ? <><Spinner /> ارسال…</> : 'ارسال کد تأیید'}
                </button>
              </form>
            </>
          )}

          {/* Step 2: Verify Code */}
          {step === 2 && (
            <>
              <h2>کد تأیید</h2>
              <p className="auth-sub">کد ۶ رقمی ارسال شده به {phone} را وارد کنید</p>
              <form onSubmit={handleVerifyCode}>
                <div className="field">
                  <label className="field-label">کد تأیید</label>
                  <div className="input-group has-icon">
                    <span className="input-icon"><KeyRound size={18} /></span>
                    <input className="input" placeholder="۱۲۳۴۵۶" value={code} onChange={e => setCode(e.target.value)} required autoFocus maxLength={6} style={{ letterSpacing: 8, textAlign: 'center', fontSize: '1.3rem' }} />
                  </div>
                </div>
                <button className="auth-submit" type="submit" disabled={loading}>
                  {loading ? <><Spinner /> بررسی…</> : 'تأیید کد'}
                </button>
              </form>
            </>
          )}

          {/* Step 3: New Password */}
          {step === 3 && (
            <>
              <h2>رمز عبور جدید</h2>
              <p className="auth-sub">رمز عبور جدید خود را وارد کنید</p>
              <form onSubmit={handleResetPassword}>
                <div className="field">
                  <label className="field-label">رمز عبور جدید</label>
                  <div className="input-group has-icon">
                    <span className="input-icon"><Lock size={18} /></span>
                    <input className="input" type="password" placeholder="رمز عبور جدید" value={password} onChange={e => setPassword(e.target.value)} required autoFocus />
                  </div>
                </div>
                <button className="auth-submit" type="submit" disabled={loading}>
                  {loading ? <><Spinner /> در حال تغییر…</> : 'تغییر رمز عبور'}
                </button>
              </form>
            </>
          )}

          {/* Step 4: Done */}
          {step === 4 && (
            <div style={{ textAlign: 'center', padding: '40px 0' }}>
              <CheckCircle2 size={64} style={{ color: '#10b981', marginBottom: 16 }} />
              <h2>رمز عبور تغییر کرد!</h2>
              <p style={{ color: 'var(--text-muted)', margin: '12px 0 24px' }}>اکنون می‌توانید با رمز جدید وارد شوید</p>
              <Link to="/login" className="auth-submit" style={{ display: 'inline-flex', textDecoration: 'none' }}>
                ورود به حساب
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
