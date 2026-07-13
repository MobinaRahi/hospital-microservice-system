import { useState } from 'react';
import { Link } from 'react-router-dom';
import { CalendarPlus, Clock, User, Phone, Stethoscope, CheckCircle2, ArrowLeft } from 'lucide-react';
import PublicNavbar from '../../components/layout/PublicNavbar';
import Footer from '../../components/layout/Footer';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';

const DEMO_DEPTS = [
  { id: 1, departmentName: 'قلب و عروق' },
  { id: 2, departmentName: 'مغز و اعصاب' },
  { id: 3, departmentName: 'ارتوپدی' },
  { id: 4, departmentName: 'اطفال' },
  { id: 5, departmentName: 'پوست و مو' },
];

const DEMO_DOCTORS = [
  { id: 1, fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY', departmentId: 1 },
  { id: 2, fullName: 'دکتر رضا کریمی', speciality: 'NEUROLOGY', departmentId: 2 },
  { id: 3, fullName: 'دکتر مریم احمدی', speciality: 'PEDIATRICS', departmentId: 4 },
];

const TIME_SLOTS = ['۰۸:۰۰', '۰۸:۱۵', '۰۸:۳۰', '۰۹:۰۰', '۰۹:۱۵', '۰۹:۳۰', '۱۰:۰۰', '۱۰:۱۵', '۱۰:۳۰', '۱۱:۰۰'];

export default function PublicBook() {
  const { data: depts } = useFetch(() => endpoints.departments.active().catch(() => null), []);
  const { data: doctors } = useFetch(() => endpoints.doctors.active().catch(() => null), []);

  const [step, setStep] = useState(1);
  const [form, setForm] = useState({ departmentId: '', doctorId: '', date: '', time: '', fullName: '', phone: '', nationalId: '' });
  const [success, setSuccess] = useState(false);

  const deptList = Array.isArray(depts) && depts.length ? depts : DEMO_DEPTS;
  const docList = Array.isArray(doctors) && doctors.length ? doctors : DEMO_DOCTORS;
  const filteredDoctors = form.departmentId ? docList.filter(d => String(d.departmentId) === String(form.departmentId)) : docList;

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Connect to API
    setSuccess(true);
  };

  if (success) {
    return (
      <>
        <PublicNavbar />
        <main style={{ paddingTop: 100, minHeight: '100vh', display: 'grid', placeItems: 'center' }}>
          <div style={{ textAlign: 'center', padding: 40, maxWidth: 500 }}>
            <div style={{ width: 80, height: 80, borderRadius: '50%', background: '#10b98122', display: 'grid', placeItems: 'center', margin: '0 auto 20px' }}>
              <CheckCircle2 size={48} style={{ color: '#10b981' }} />
            </div>
            <h2 style={{ fontSize: '1.8rem', fontWeight: 900, marginBottom: 12 }}>نوبت شما ثبت شد! 🎉</h2>
            <p style={{ color: 'var(--text-muted)', marginBottom: 8 }}>کد رهگیری شما:</p>
            <div style={{ background: 'var(--surface)', border: '2px solid var(--brand)', borderRadius: 12, padding: '16px 32px', display: 'inline-block', marginBottom: 24 }}>
              <span style={{ fontSize: '1.5rem', fontWeight: 900, color: 'var(--brand)', letterSpacing: 4 }}>NV-۱۲۳۴۵۶</span>
            </div>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: 24 }}>
              این کد را ذخیره کنید. برای پیگیری نوبت خود از بخش "پیگیری نوبت" استفاده کنید.
            </p>
            <div style={{ display: 'flex', gap: 12, justifyContent: 'center', flexWrap: 'wrap' }}>
              <Link to="/track" className="lp-btn lp-btn-pri" style={{ fontSize: '0.9rem' }}>پیگیری نوبت</Link>
              <Link to="/" className="lp-btn lp-btn-gh" style={{ fontSize: '0.9rem' }}>بازگشت به خانه</Link>
            </div>
          </div>
        </main>
        <Footer />
      </>
    );
  }

  return (
    <>
      <PublicNavbar />
      <main style={{ paddingTop: 100, minHeight: '100vh' }}>
        <section className="lp-section">
          <div className="container" style={{ maxWidth: 900 }}>
            <div className="lp-head">
              <span className="lp-eyebrow"><span className="dot" /> رزرو نوبت آنلاین</span>
              <h2>نوبت خود را <span className="lp-grad">رزرو کنید</span></h2>
              <p>در چند مرحله ساده، نوبت ویزیت خود را ثبت کنید</p>
            </div>

            {/* Steps indicator */}
            <div style={{ display: 'flex', justifyContent: 'center', gap: 8, marginBottom: 32 }}>
              {[
                { num: 1, label: 'انتخاب پزشک' },
                { num: 2, label: 'انتخاب زمان' },
                { num: 3, label: 'اطلاعات بیمار' },
              ].map(s => (
                <div key={s.num} style={{
                  display: 'flex', alignItems: 'center', gap: 8,
                  padding: '8px 16px', borderRadius: 20,
                  background: step >= s.num ? 'var(--brand)' : 'var(--surface)',
                  color: step >= s.num ? '#000' : 'var(--text-muted)',
                  fontWeight: 700, fontSize: '0.85rem'
                }}>
                  <span style={{
                    width: 24, height: 24, borderRadius: '50%',
                    background: step >= s.num ? 'rgba(0,0,0,0.2)' : 'var(--border)',
                    display: 'grid', placeItems: 'center', fontSize: '0.75rem'
                  }}>{s.num}</span>
                  {s.label}
                </div>
              ))}
            </div>

            <div style={{ background: 'var(--surface)', border: '1px solid var(--border)', borderRadius: 20, padding: 32 }}>
              {/* Step 1: Select Doctor */}
              {step === 1 && (
                <div>
                  <h3 style={{ marginBottom: 20 }}>بخش و پزشک مورد نظر را انتخاب کنید</h3>
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
                    <div>
                      <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 8, fontWeight: 600 }}>بخش</label>
                      <select value={form.departmentId} onChange={e => setForm({ ...form, departmentId: e.target.value, doctorId: '' })} style={{
                        width: '100%', padding: '12px 16px', borderRadius: 10,
                        border: '1px solid var(--border)', background: 'var(--surface-2)',
                        color: 'var(--text)', fontSize: '0.9rem'
                      }}>
                        <option value="">انتخاب بخش...</option>
                        {deptList.map(d => <option key={d.id} value={d.id}>{d.departmentName || d.name}</option>)}
                      </select>
                    </div>
                    <div>
                      <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 8, fontWeight: 600 }}>پزشک</label>
                      <select value={form.doctorId} onChange={e => setForm({ ...form, doctorId: e.target.value })} style={{
                        width: '100%', padding: '12px 16px', borderRadius: 10,
                        border: '1px solid var(--border)', background: 'var(--surface-2)',
                        color: 'var(--text)', fontSize: '0.9rem'
                      }}>
                        <option value="">انتخاب پزشک...</option>
                        {filteredDoctors.map(d => <option key={d.id} value={d.id}>{d.fullName}</option>)}
                      </select>
                    </div>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: 24 }}>
                    <button onClick={() => setStep(2)} disabled={!form.doctorId} className="lp-btn lp-btn-pri" style={{ opacity: form.doctorId ? 1 : 0.5 }}>
                      مرحله بعد <ArrowLeft size={18} />
                    </button>
                  </div>
                </div>
              )}

              {/* Step 2: Select Time */}
              {step === 2 && (
                <div>
                  <h3 style={{ marginBottom: 20 }}>تاریخ و ساعت نوبت را انتخاب کنید</h3>
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
                    <div>
                      <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 8, fontWeight: 600 }}>تاریخ</label>
                      <input type="date" value={form.date} onChange={e => setForm({ ...form, date: e.target.value })} style={{
                        width: '100%', padding: '12px 16px', borderRadius: 10,
                        border: '1px solid var(--border)', background: 'var(--surface-2)',
                        color: 'var(--text)', fontSize: '0.9rem'
                      }} />
                    </div>
                    <div>
                      <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 8, fontWeight: 600 }}>ساعت</label>
                      <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                        {TIME_SLOTS.map(t => (
                          <button key={t} onClick={() => setForm({ ...form, time: t })} style={{
                            padding: '8px 14px', borderRadius: 8,
                            border: `1px solid ${form.time === t ? 'var(--brand)' : 'var(--border)'}`,
                            background: form.time === t ? 'var(--brand)' : 'var(--surface-2)',
                            color: form.time === t ? '#000' : 'var(--text)',
                            fontWeight: form.time === t ? 700 : 500, fontSize: '0.85rem', cursor: 'pointer'
                          }}>
                            {t}
                          </button>
                        ))}
                      </div>
                    </div>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 24 }}>
                    <button onClick={() => setStep(1)} className="lp-btn lp-btn-gh">مرحله قبل</button>
                    <button onClick={() => setStep(3)} disabled={!form.date || !form.time} className="lp-btn lp-btn-pri" style={{ opacity: form.date && form.time ? 1 : 0.5 }}>
                      مرحله بعد <ArrowLeft size={18} />
                    </button>
                  </div>
                </div>
              )}

              {/* Step 3: Patient Info */}
              {step === 3 && (
                <form onSubmit={handleSubmit}>
                  <h3 style={{ marginBottom: 20 }}>اطلاعات بیمار</h3>
                  <div style={{ display: 'grid', gap: 16 }}>
                    <div>
                      <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 8, fontWeight: 600 }}>نام و نام خانوادگی <span style={{ color: 'var(--danger)' }}>*</span></label>
                      <input type="text" value={form.fullName} onChange={e => setForm({ ...form, fullName: e.target.value })} required placeholder="نام کامل" style={{
                        width: '100%', padding: '12px 16px', borderRadius: 10,
                        border: '1px solid var(--border)', background: 'var(--surface-2)',
                        color: 'var(--text)', fontSize: '0.9rem'
                      }} />
                    </div>
                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
                      <div>
                        <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 8, fontWeight: 600 }}>شماره موبایل <span style={{ color: 'var(--danger)' }}>*</span></label>
                        <input type="tel" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} required placeholder="۰۹۱۲۱۲۳۴۵۶۷" style={{
                          width: '100%', padding: '12px 16px', borderRadius: 10,
                          border: '1px solid var(--border)', background: 'var(--surface-2)',
                          color: 'var(--text)', fontSize: '0.9rem'
                        }} />
                      </div>
                      <div>
                        <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 8, fontWeight: 600 }}>کد ملی</label>
                        <input type="text" value={form.nationalId} onChange={e => setForm({ ...form, nationalId: e.target.value })} placeholder="اختیاری" style={{
                          width: '100%', padding: '12px 16px', borderRadius: 10,
                          border: '1px solid var(--border)', background: 'var(--surface-2)',
                          color: 'var(--text)', fontSize: '0.9rem'
                        }} />
                      </div>
                    </div>
                  </div>

                  {/* Summary */}
                  <div style={{ background: 'var(--surface-2)', borderRadius: 12, padding: 20, marginTop: 24 }}>
                    <h4 style={{ marginBottom: 12, fontSize: '0.9rem' }}>خلاصه نوبت:</h4>
                    <div style={{ display: 'grid', gap: 8, fontSize: '0.85rem' }}>
                      <div>👨‍⚕️ پزشک: <strong>{docList.find(d => String(d.id) === String(form.doctorId))?.fullName}</strong></div>
                      <div>📅 تاریخ: <strong>{form.date}</strong></div>
                      <div>⏰ ساعت: <strong>{form.time}</strong></div>
                    </div>
                  </div>

                  <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 24 }}>
                    <button type="button" onClick={() => setStep(2)} className="lp-btn lp-btn-gh">مرحله قبل</button>
                    <button type="submit" className="lp-btn lp-btn-pri">
                      <CalendarPlus size={18} /> تأیید و رزرو نوبت
                    </button>
                  </div>
                </form>
              )}
            </div>
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
}
