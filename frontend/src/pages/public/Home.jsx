import { useState, useEffect } from 'react';
import {
  Activity, CalendarPlus, Stethoscope, Users, Building2, CalendarDays,
  ShieldCheck, Clock3, HeartPulse, Microscope, Pill, Siren,
  ArrowLeft, CheckCircle2, Star, PhoneCall,
} from 'lucide-react';
import PublicNavbar from '../../components/layout/PublicNavbar';
import Footer from '../../components/layout/Footer';
import { Button, Badge } from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber, specialityLabel, initials } from '../../utils/format';

/* Demo fallback data so the page looks alive even before the backend is up */
const DEMO_STATS = { doctors: 64, patients: 4820, departments: 18, appointments: 12640 };
const DEMO_DEPTS = [
  { id: 1, name: 'قلب و عروق', code: 'CARD', location: 'طبقه ۲' },
  { id: 2, name: 'مغز و اعصاب', code: 'NEUR', location: 'طبقه ۳' },
  { id: 3, name: 'ارتوپدی', code: 'ORTH', location: 'طبقه ۱' },
  { id: 4, name: 'اطفال', code: 'PEDS', location: 'طبقه ۴' },
  { id: 5, name: 'اورژانس', code: 'EMERG', location: 'همکف' },
  { id: 6, name: 'رادیولوژی', code: 'RAD', location: 'زیرزمین' },
  { id: 7, name: 'دندانپزشکی', code: 'DENT', location: 'طبقه ۱' },
  { id: 8, name: 'چشم پزشکی', code: 'EYE', location: 'طبقه ۲' },
];
const DEMO_DOCTORS = [
  { id: 1, fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY', dept: 'قلب و عروق', active: true },
  { id: 2, fullName: 'دکتر رضا کریمی', speciality: 'NEUROLOGY', dept: 'مغز و اعصاب', active: true },
  { id: 3, fullName: 'دکتر مریم احمدی', speciality: 'PEDIATRICS', dept: 'اطفال', active: true },
  { id: 4, fullName: 'دکتر علی رضایی', speciality: 'ORTHOPEDICS', dept: 'ارتوپدی', active: false },
];

const deptEmoji = (code) => ({
  CARD: '🫀', NEUR: '🧠', ORTH: '🦴', PEDS: '👶', DERM: '🧴', DENT: '🦷',
  EYE: '👁️', RAD: '🩻', EMERG: '🚑', ICU: '🏥', LAB: '🔬',
}[String(code || '').slice(0, 4).toUpperCase()] || '🏥');

export default function Home() {
  const { data: docCount } = useFetch(() => endpoints.doctors.countActive().catch(() => null), []);
  const { data: patCount } = useFetch(() => endpoints.patients.countActive().catch(() => null), []);
  const { data: deptCount } = useFetch(() => endpoints.departments.countActive().catch(() => null), []);
  const { data: aptCount } = useFetch(() => endpoints.appointments.count().catch(() => null), []);
  const { data: doctorsRaw } = useFetch(() => endpoints.doctors.active().catch(() => null), []);
  const { data: deptsRaw } = useFetch(() => endpoints.departments.active().catch(() => null), []);

  // Smooth-scroll to hash sections
  useEffect(() => {
    if (window.location.hash) {
      const el = document.querySelector(window.location.hash);
      if (el) setTimeout(() => el.scrollIntoView({ behavior: 'smooth' }), 100);
    }
  }, []);

  const stats = {
    doctors: docCount ?? DEMO_STATS.doctors,
    patients: patCount ?? DEMO_STATS.patients,
    departments: deptCount ?? DEMO_STATS.departments,
    appointments: aptCount ?? DEMO_STATS.appointments,
  };

  const doctors = (Array.isArray(doctorsRaw) ? doctorsRaw : DEMO_DOCTORS).slice(0, 4);
  const depts = (Array.isArray(deptsRaw) ? deptsRaw : DEMO_DEPTS).slice(0, 8);

  return (
    <>
      <PublicNavbar />

      {/* ===== HERO ===== */}
      <section className="hero">
        <div className="hero-inner">
          <div className="hero-content fade-in-up">
            <span className="hero-badge">
              <span className="dot" />
              {`بیش از ${toFa(stats.doctors)} پزشک متخصص فعال`}
            </span>
            <h1 className="hero-title">
              مراقبت درمانی <span className="grad">هوشمند و انسانی</span>
              <br />
              برای سلامت شما و عزیزانتان
            </h1>
            <p className="hero-desc">
              سامانه یکپارچه مدیریت بیمارستان نووا، با ترکیب فناوری روز و تیمی از بهترین
              متخصصان، تجربه‌ای سریع، شفاف و مطمئن از پذیرش تا درمان را برای شما رقم می‌زند.
            </p>
            <div className="hero-actions">
              <Button to="/book" size="lg" icon={CalendarPlus} iconRight>رزرو نوبت آنلاین</Button>
              <Button to="/login" size="lg" variant="outline" icon={Activity}>ورود به سامانه</Button>
            </div>
            <div className="flex items-center gap-16 mt-24" style={{ flexWrap: 'wrap' }}>
              {[
                { icon: ShieldCheck, t: 'تأییدیه وزارت بهداشت' },
                { icon: Clock3, t: 'اورژانس ۲۴ ساعته' },
                { icon: CheckCircle2, t: 'بیمه‌های پایه و تکمیلی' },
              ].map((x) => (
                <div key={x.t} className="flex items-center gap-8 text-sm text-secondary">
                  <x.icon size={18} className="text-brand" /> {x.t}
                </div>
              ))}
            </div>
          </div>

          {/* Visual */}
          <div className="hero-visual fade-in-up" style={{ animationDelay: '0.15s' }}>
            <div className="hero-card-stack">
              <div style={{
                background: 'var(--gradient-brand)', borderRadius: 'var(--radius-xl)',
                aspectRatio: '4/3', padding: 32, color: '#fff', position: 'relative',
                boxShadow: 'var(--shadow-brand)', overflow: 'hidden',
              }}>
                <div style={{ position: 'absolute', inset: 0, background: 'radial-gradient(circle at 30% 20%, rgba(255,255,255,0.25), transparent 55%)' }} />
                <div className="flex items-center justify-between" style={{ position: 'relative' }}>
                  <div className="fw-800 text-lg">سلامت در یک نگاه</div>
                  <Activity size={26} />
                </div>
                <div style={{ position: 'relative', marginTop: 28 }}>
                  <div className="text-sm" style={{ opacity: 0.85 }}>بیماران فعال این ماه</div>
                  <div style={{ fontSize: '2.6rem', fontWeight: 800, lineHeight: 1.1 }}>{faNumber(stats.patients)}+</div>
                </div>
                <div style={{ position: 'relative', marginTop: 22, display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                  {[['رضایت بیماران', '۹۸٪'], ['نرخ درمان', '۹۶٪']].map(([k, v]) => (
                    <div key={k} style={{ background: 'rgba(255,255,255,0.15)', borderRadius: 14, padding: '14px 16px' }}>
                      <div style={{ fontSize: '1.3rem', fontWeight: 800 }}>{v}</div>
                      <div className="text-xs" style={{ opacity: 0.85 }}>{k}</div>
                    </div>
                  ))}
                </div>
              </div>

              <div className="hero-float" style={{ top: -18, left: -18 }}>
                <span className="hf-ic"><HeartPulse size={20} /></span>
                <div>
                  <div className="fw-700 text-sm">ضربان قلب</div>
                  <div className="text-xs text-muted">۷۸ ضربه/دقیقه</div>
                </div>
              </div>
              <div className="hero-float" style={{ bottom: -20, right: -10 }}>
                <span className="hf-ic"><CalendarDays size={20} /></span>
                <div>
                  <div className="fw-700 text-sm">نوبت‌های امروز</div>
                  <div className="text-xs text-muted">{faNumber(120)} ویزیت</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Stats strip */}
        <div className="hero-stats stagger">
          <Stat icon={Stethoscope} value={`${faNumber(stats.doctors)}+`} label="پزشک متخصص" />
          <Stat icon={Users} value={`${faNumber(stats.patients)}+`} label="بیمار تحت درمان" />
          <Stat icon={Building2} value={faNumber(stats.departments)} label="بخش تخصصی" />
          <Stat icon={CalendarDays} value={`${faNumber(stats.appointments)}+`} label="نوبت ثبت‌شده" />
        </div>
      </section>

      {/* ===== SERVICES ===== */}
      <section className="section section-alt" id="services">
        <div className="container">
          <div className="section-head">
            <span className="section-eyebrow">خدمات ما</span>
            <h2 className="section-title">مجموعه‌ای کامل از خدمات درمانی</h2>
            <p className="section-sub">از پذیرش و تشخیص تا درمان و پیگیری، همه چیز در یک سامانه یکپارچه.</p>
          </div>
          <div className="grid-cards-3 stagger">
            {[
              { icon: Siren, t: 'اورژانس ۲۴ ساعته', d: 'مراقبت فوری و تخصصی برای مواقع حساس، در تمام ساعات شبانه‌روز.' },
              { icon: Microscope, t: 'رادیولوژی و آزمایشگاه', d: 'تجهیزات پیشرفته تصویربرداری و آزمایش برای تشخیص دقیق و سریع.' },
              { icon: HeartPulse, t: 'قلب و عروق', d: 'تشخیص و درمان تخصصی بیماری‌های قلبی با تیم بوردتخصص.' },
              { icon: Pill, t: 'داروخانه داخلی', d: 'تأمین سریع داروهای تجویزی و مشاوره دارویی در محل بیمارستان.' },
              { icon: Stethoscope, t: 'ویزیت تخصصی', d: 'دسترسی آسان به متخصصان رشته‌های مختلف با نوبت‌دهی آنلاین.' },
              { icon: ShieldCheck, t: 'بیمه و تسویه', d: 'پشتیبانی بیمه‌های پایه و تکمیلی و تسویه شفاف در صندوق.' },
            ].map((s) => (
              <div className="feature-card" key={s.t}>
                <div className="fc-icon"><s.icon size={26} /></div>
                <h3>{s.t}</h3>
                <p>{s.d}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ===== DEPARTMENTS ===== */}
      <section className="section" id="departments">
        <div className="container">
          <div className="section-head">
            <span className="section-eyebrow">بخش‌های تخصصی</span>
            <h2 className="section-title">بخش مورد نظر خود را انتخاب کنید</h2>
            <p className="section-sub">برای دریافت خدمات تخصصی، روی هر بخش کلیک کنید.</p>
          </div>
          <div className="grid-depts stagger">
            {depts.map((d) => (
              <a key={d.id || d.code} className="dept-chip" href="/book">
                <div className="dc-emoji">{deptEmoji(d.departmentCode || d.code)}</div>
                <div className="fw-700 text-sm">{d.departmentName || d.name}</div>
                <div className="text-xs text-muted mt-8">{d.location}</div>
              </a>
            ))}
          </div>
        </div>
      </section>

      {/* ===== DOCTORS ===== */}
      <section className="section section-alt" id="doctors">
        <div className="container">
          <div className="section-head">
            <span className="section-eyebrow">تیم پزشکی</span>
            <h2 className="section-title">با متخصصان ما آشنا شوید</h2>
            <p className="section-sub">تیمی مجرب و متعهد از بهترین پزشکان هر تخصص.</p>
          </div>
          <div className="grid-cards-4 stagger">
            {doctors.map((d) => (
              <div className="doctor-card-pub" key={d.id}>
                <div className="dc-photo">
                  <div className="dc-avatar">{initials(d.fullName)}</div>
                  <span className={`dc-status ${d.isActive === false || d.active === false ? 'off' : ''}`} />
                </div>
                <div className="fw-700 text-sm">{d.fullName}</div>
                <div className="text-sm text-brand fw-600 mt-8">{specialityLabel(d.speciality)}</div>
                <div className="text-xs text-muted mt-8">{d.department?.departmentName || d.dept}</div>
                <div className="flex items-center justify-center gap-4 mt-16">
                  <span className="stars" style={{ color: 'var(--warning)' }}>★★★★★</span>
                  <span className="text-xs text-muted">۵.۰</span>
                </div>
                <Button to="/book" size="sm" variant="soft" className="mt-16 w-full" icon={CalendarPlus}>رزرو نوبت</Button>
              </div>
            ))}
          </div>
          <div className="text-center mt-32">
            <Button to="/app/doctors" variant="outline" icon={ArrowLeft} iconRight>مشاهده همه پزشکان</Button>
          </div>
        </div>
      </section>

      {/* ===== WHY US ===== */}
      <section className="section">
        <div className="container">
          <div className="section-head">
            <span className="section-eyebrow">چرا نووا؟</span>
            <h2 className="section-title">تجربه‌ای متفاوت از درمان</h2>
          </div>
          <div className="grid-cards-4 stagger">
            {[
              { icon: ShieldCheck, t: 'پزشکان بوردتخصص', d: 'دهه‌ها تجربه بالینی' },
              { icon: Clock3, t: 'دسترسی سریع', d: 'کمترین زمان انتظار' },
              { icon: HeartPulse, t: 'بیمار‌محور', d: 'برنامه درمان شخصی‌سازی‌شده' },
              { icon: Star, t: 'رضایت ۹۸٪', d: 'بیماران راضی' },
            ].map((x) => (
              <div className="feature-card" key={x.t} style={{ textAlign: 'center' }}>
                <div className="fc-icon" style={{ marginInline: 'auto' }}><x.icon size={26} /></div>
                <h3 style={{ fontSize: '1rem' }}>{x.t}</h3>
                <p style={{ fontSize: '0.85rem' }}>{x.d}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ===== CTA ===== */}
      <section className="section" style={{ paddingTop: 0 }}>
        <div className="container">
          <div className="cta-band">
            <div style={{ position: 'relative', zIndex: 1 }}>
              <h2 style={{ fontSize: 'clamp(1.5rem, 3vw, 2.2rem)', fontWeight: 800 }}>همین حالا نوبت خود را رزرو کنید</h2>
              <p style={{ opacity: 0.9, marginTop: 12, fontSize: '1.05rem' }}>بدون معطلی، آنلاین و در چند ثانیه.</p>
              <div className="flex gap-12 justify-center mt-24" style={{ flexWrap: 'wrap' }}>
                <Button to="/book" size="lg" variant="ghost" icon={CalendarPlus}>رزرو نوبت</Button>
                <Button to="/login" size="lg" variant="ghost" icon={PhoneCall}>تماس با پذیرش</Button>
              </div>
            </div>
          </div>
        </div>
      </section>

      <Footer />
    </>
  );
}

function Stat({ icon: Icon, value, label }) {
  return (
    <div className="hero-stat">
      <Icon size={22} className="text-brand" />
      <div className="hs-num mt-8">{value}</div>
      <div className="hs-label">{label}</div>
    </div>
  );
}
