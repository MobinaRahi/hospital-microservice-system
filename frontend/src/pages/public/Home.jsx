import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  CalendarPlus, Play, ShieldCheck, Clock3, CheckCircle2, HeartPulse,
  Stethoscope, Users, Building2, CalendarDays, Microscope,
  Pill, Ambulance, PhoneCall, Brain, Bone, Baby, Eye, Smile,
} from 'lucide-react';
import PublicNavbar from '../../components/layout/PublicNavbar';
import Footer from '../../components/layout/Footer';
import EcgMonitor from '../../components/ui/EcgMonitor';
import { useReveal } from '../../hooks/useReveal';
import { useCountUp } from '../../hooks/useCountUp';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { faNumber, specialityLabel } from '../../utils/format';

/* Demo fallback data so the page looks alive even before the backend is up */
const DEMO_STATS = { doctors: 64, patients: 4820, departments: 18, appointments: 12640 };
const DEMO_DEPTS = [
  { id: 1, name: 'قلب و عروق', icon: HeartPulse, location: 'طبقه ۲' },
  { id: 2, name: 'مغز و اعصاب', icon: Brain, location: 'طبقه ۳' },
  { id: 3, name: 'ارتوپدی', icon: Bone, location: 'طبقه ۱' },
  { id: 4, name: 'اطفال', icon: Baby, location: 'طبقه ۴' },
  { id: 5, name: 'اورژانس', icon: Ambulance, location: 'همکف' },
  { id: 6, name: 'رادیولوژی', icon: Microscope, location: 'زیرزمین' },
  { id: 7, name: 'دندانپزشکی', icon: Smile, location: 'طبقه ۱' },
  { id: 8, name: 'چشم پزشکی', icon: Eye, location: 'طبقه ۲' },
];
const DEMO_DOCTORS = [
  { id: 1, fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY', dept: 'قلب و عروق', grad: 'linear-gradient(135deg,#0ea5e9,#14b8a6)' },
  { id: 2, fullName: 'دکتر رضا کریمی', speciality: 'NEUROLOGY', dept: 'مغز و اعصاب', grad: 'linear-gradient(135deg,#10b981,#34d399)' },
  { id: 3, fullName: 'دکتر مریم احمدی', speciality: 'PEDIATRICS', dept: 'اطفال', grad: 'linear-gradient(135deg,#0d9488,#2dd4bf)' },
  { id: 4, fullName: 'دکتر علی رضایی', speciality: 'ORTHOPEDICS', dept: 'ارتوپدی', grad: 'linear-gradient(135deg,#14b8a6,#0ea5e9)' },
];

const SERVICES = [
  { icon: Ambulance, t: 'اورژانس ۲۴ ساعته', d: 'مراقبت فوری و تخصصی برای مواقع حساس، در تمام ساعات شبانه‌روز.' },
  { icon: Microscope, t: 'رادیولوژی و آزمایشگاه', d: 'تجهیزات پیشرفته تصویربرداری و آزمایش برای تشخیص دقیق و سریع.' },
  { icon: HeartPulse, t: 'قلب و عروق', d: 'تشخیص و درمان تخصصی بیماری‌های قلبی با تیم بوردتخصص.' },
  { icon: Pill, t: 'داروخانه داخلی', d: 'تأمین سریع داروهای تجویزی و مشاوره دارویی در محل بیمارستان.' },
  { icon: Stethoscope, t: 'ویزیت تخصصی', d: 'دسترسی آسان به متخصصان رشته‌های مختلف با نوبت‌دهی آنلاین.' },
  { icon: ShieldCheck, t: 'بیمه و تسویه', d: 'پشتیبانی بیمه‌های پایه و تکمیلی و تسویه شفاف در صندوق.' },
];

const STEPS = [
  { num: '۱', t: 'بخش و پزشک را انتخاب کنید', d: 'از میان ده‌ها تخصص و متخصص مجرب، مناسب‌ترین را بر اساس نیاز خود پیدا کنید.' },
  { num: '۲', t: 'زمان نوبت را رزرو کنید', d: 'تقویم آنلاین پزشک را ببینید و زمان دلخواهتان را در چند ثانیه ثبت کنید.' },
  { num: '۳', t: 'به بیمارستان مراجعه کنید', d: 'با کد رهروی نوبت، بدون انتظار و مستقیماً به مطب متخصص بروید.' },
];

const FEATURES = [
  { icon: ShieldCheck, t: 'پزشکان بوردتخصص', d: 'دهه‌ها تجربه بالینی در رشته‌های مختلف تخصصی.' },
  { icon: Clock3, t: 'دسترسی سریع و بدون معطلی', d: 'کمترین زمان انتظار با نوبت‌دهی هوشمند آنلاین.' },
  { icon: HeartPulse, t: 'برنامه درمان شخصی‌سازی‌شده', d: 'روند درمان متناسب با شرایط منحصربه‌فرد هر بیمار.' },
];

function initials(fullName = '') {
  const parts = fullName.trim().split(/\s+/).filter(Boolean);
  if (!parts.length) return '؟';
  if (parts.length === 1) return parts[0].slice(0, 2);
  return (parts[0][0] || '') + (parts[1][0] || '');
}

/* scroll-reveal wrapper — each instance gets its own ref */
function Reveal({ stagger, className = '', style, children, ...rest }) {
  const ref = useReveal();
  return (
    <div ref={ref} className={`reveal ${stagger ? 'stagger' : ''} ${className}`.trim()} style={style} {...rest}>
      {children}
    </div>
  );
}

/* animated stat counter */
function Stat({ icon: Icon, value, label }) {
  const [ref, n] = useCountUp(value);
  return (
    <div className="lp-stat">
      <div className="ic"><Icon size={23} /></div>
      <div className="n" ref={ref}>{faNumber(n)}</div>
      <div className="l">{label}</div>
    </div>
  );
}

export default function Home() {
  /* ---- Live data (falls back to demo) ---- */
  const { data: docCount } = useFetch(() => endpoints.doctors.countActive().catch(() => null), []);
  const { data: patCount } = useFetch(() => endpoints.patients.countActive().catch(() => null), []);
  const { data: deptCount } = useFetch(() => endpoints.departments.countActive().catch(() => null), []);
  const { data: aptCount } = useFetch(() => endpoints.appointments.count().catch(() => null), []);

  const stats = {
    doctors: docCount ?? DEMO_STATS.doctors,
    patients: patCount ?? DEMO_STATS.patients,
    departments: deptCount ?? DEMO_STATS.departments,
    appointments: aptCount ?? DEMO_STATS.appointments,
  };
  const depts = DEMO_DEPTS;

  /* ---- smooth-scroll to hash on mount ---- */
  useEffect(() => {
    if (window.location.hash) {
      const el = document.querySelector(window.location.hash);
      if (el) setTimeout(() => el.scrollIntoView({ behavior: 'smooth' }), 120);
    }
  }, []);

  return (
    <>
      <PublicNavbar />

      {/* ===== HERO ===== */}
      <header className="lp-hero" id="home">
        <span className="blob1" />
        <span className="blob2" />
        <div className="container">
          <div className="lp-hero-grid">
            <Reveal>
              <span className="lp-eyebrow"><span className="dot" /> سامانه سلامت هوشمند نسل جدید</span>
              <h1>سلامتی <span className="lp-grad">ساده‌تر از همیشه</span>، در دستان شما</h1>
              <p className="lead">
                نووا، پلتفرم یکپارچه درمانی که با تجربه‌ای روان، دوستانه و مطمئن،
                از اولین نوبت تا درمان کامل در کنار شماست.
              </p>
              <div className="lp-btns">
                <Link className="lp-btn lp-btn-pri" to="/book"><CalendarPlus size={19} /> رزرو نوبت آنلاین</Link>
                <Link className="lp-btn lp-btn-gh" to="/#how"><Play size={19} /> چطور کار می‌کند؟</Link>
              </div>
              <div className="lp-trust">
                <div className="lp-av-grp">
                  <span style={{ background: 'linear-gradient(135deg,#0ea5e9,#14b8a6)' }}>س م</span>
                  <span style={{ background: 'linear-gradient(135deg,#10b981,#34d399)' }}>ر ک</span>
                  <span style={{ background: 'linear-gradient(135deg,#0d9488,#2dd4bf)' }}>م ا</span>
                  <span style={{ background: 'linear-gradient(135deg,#14b8a6,#0ea5e9)' }}>+</span>
                </div>
                <div className="tx">
                  <span className="stars">★★★★★</span>
                  <b>{faNumber(4820)}+ بیمار راضی</b>
                </div>
              </div>
            </Reveal>

            {/* hero visual with ECG */}
            <Reveal className="lp-hvis" style={{ transitionDelay: '0.12s' }}>
              <span className="lp-ring" />
              <div className="lp-frame">
                <img src="/assets/3d-hero.jpg" alt="گرافیک سلامت نووا" />
                <div className="lp-ecg">
                  <div className="lp-ecg-label"><span className="lbl-dot" /> ECG • {faNumber(78)} bpm</div>
                  <EcgMonitor bpm={78} />
                </div>
              </div>
              <div className="lp-float lp-float-1">
                <span className="ic"><HeartPulse size={20} /></span>
                <div><div className="t">{faNumber(78)} bpm</div><div className="s">ضربان قلب سالم</div></div>
              </div>
              <div className="lp-float lp-float-2">
                <span className="ic"><CalendarDays size={20} /></span>
                <div><div className="t">{faNumber(120)} ویزیت امروز</div><div className="s">نوبت‌های فعال</div></div>
              </div>
            </Reveal>
          </div>
        </div>
      </header>

      {/* ===== STATS ===== */}
      <section className="lp-stats-sec">
        <div className="container">
          <Reveal stagger>
            <div className="lp-stats">
              <Stat icon={Stethoscope} value={stats.doctors} label="پزشک متخصص" />
              <Stat icon={Users} value={stats.patients} label="بیمار تحت درمان" />
              <Stat icon={Building2} value={stats.departments} label="بخش تخصصی" />
              <Stat icon={CalendarDays} value={stats.appointments} label="نوبت ثبت‌شده" />
            </div>
          </Reveal>
        </div>
      </section>

      {/* ===== HOW IT WORKS ===== */}
      <section className="lp-section" id="how">
        <div className="container">
          <Reveal>
            <div className="lp-head">
              <span className="lp-eyebrow"><span className="dot" /> نحوه کار</span>
              <h2>در سه گام ساده، <span className="lp-grad">نوبت بگیرید</span></h2>
              <p>بدون معطلی و پیچیدگی، فقط چند کلیک تا ویزیت متخصص.</p>
            </div>
          </Reveal>
          <Reveal stagger>
            <div className="lp-steps">
              {STEPS.map((s) => (
                <div className="lp-step" key={s.num}>
                  <div className="num">{s.num}</div>
                  <h3>{s.t}</h3>
                  <p>{s.d}</p>
                </div>
              ))}
            </div>
          </Reveal>
        </div>
      </section>

      {/* ===== SERVICES ===== */}
      <section className="lp-section lp-section-alt" id="services">
        <div className="container">
          <Reveal>
            <div className="lp-head">
              <span className="lp-eyebrow"><span className="dot" /> خدمات ما</span>
              <h2>هرآنچه برای سلامت <span className="lp-grad">نیاز دارید</span></h2>
              <p>از پذیرش و تشخیص تا درمان و پیگیری، همه چیز در یک پلتفرم یکپارچه.</p>
            </div>
          </Reveal>
          <Reveal stagger>
            <div className="lp-grid3">
              {SERVICES.map((s) => (
                <div className="lp-card" key={s.t}>
                  <div className="fc-ic"><s.icon size={27} /></div>
                  <h3>{s.t}</h3>
                  <p>{s.d}</p>
                </div>
              ))}
            </div>
          </Reveal>
        </div>
      </section>

      {/* ===== DEPARTMENTS ===== */}
      <section className="lp-section" id="departments">
        <div className="container">
          <Reveal>
            <div className="lp-head">
              <span className="lp-eyebrow"><span className="dot" /> بخش‌های تخصصی</span>
              <h2>بخش مورد نظر خود را انتخاب کنید</h2>
            </div>
          </Reveal>
          <Reveal stagger>
            <div className="lp-depts">
              {depts.map((d) => (
                <Link className="lp-dept" to="/book" key={d.id}>
                  <div className="emo"><d.icon size={29} /></div>
                  <div className="nm">{d.name}</div>
                  <div className="lc">{d.location}</div>
                </Link>
              ))}
            </div>
          </Reveal>
        </div>
      </section>

      {/* ===== DOCTORS ===== */}
      <section className="lp-section lp-section-alt" id="doctors">
        <div className="container">
          <Reveal>
            <div className="lp-head">
              <span className="lp-eyebrow"><span className="dot" /> تیم پزشکی</span>
              <h2>با متخصصان ما <span className="lp-grad">آشنا شوید</span></h2>
              <p>تیمی مجرب و متعهد از بهترین پزشکان هر تخصص.</p>
            </div>
          </Reveal>
          <Reveal stagger>
            <div className="lp-grid4">
              {DEMO_DOCTORS.map((d) => (
                <div className="lp-doc" key={d.id}>
                  <div className="av" style={{ background: d.grad }}>{initials(d.fullName)}</div>
                  <div className="nm">{d.fullName}</div>
                  <div className="sp">{specialityLabel(d.speciality)}</div>
                  <div className="dp">{d.dept}</div>
                  <div className="stars">★★★★★ {faNumber(5)}.۰</div>
                  <Link className="bk" to="/book"><CalendarPlus size={15} /> رزرو نوبت</Link>
                </div>
              ))}
            </div>
          </Reveal>
        </div>
      </section>

      {/* ===== ABOUT split ===== */}
      <section className="lp-section" id="about">
        <div className="container">
          <div className="lp-split">
            <Reveal className="pic">
              <img src="/assets/3d-stats.jpg" alt="داشبورد سلامت نووا" />
              <div className="badge"><b>{faNumber(98)}٪</b><span>رضایت بیماران</span></div>
            </Reveal>
            <Reveal style={{ transitionDelay: '0.1s' }}>
              <span className="lp-eyebrow"><span className="dot" /> چرا نووا؟</span>
              <h2 style={{ fontSize: 'clamp(1.8rem, 3.8vw, 2.7rem)', fontWeight: 900, lineHeight: 1.3, margin: '0.4em 0 0.5em' }}>
                تجربه‌ای <span className="lp-grad">دوستانه</span> از درمان
              </h2>
              {FEATURES.map((f) => (
                <div className="lp-feat" key={f.t}>
                  <span className="ck"><CheckCircle2 size={19} /></span>
                  <div><b>{f.t}</b><p>{f.d}</p></div>
                </div>
              ))}
            </Reveal>
          </div>
        </div>
      </section>

      {/* ===== CTA ===== */}
      <section className="lp-section" style={{ paddingTop: 0 }}>
        <div className="container">
          <Reveal>
            <div className="lp-cta">
              <div className="cta-bg"><img src="/assets/3d-dept.jpg" alt="" /></div>
              <h2>همین حالا نوبت خود را رزرو کنید</h2>
              <p>بدون معطلی، آنلاین و در چند ثانیه — سلامت شما اولویت ماست.</p>
              <div className="lp-btns">
                <Link className="lp-btn lp-btn-pri" to="/book"><CalendarPlus size={19} /> رزرو نوبت</Link>
                <Link className="lp-btn lp-btn-gh" to="/login"><PhoneCall size={19} /> تماس با پذیرش</Link>
              </div>
            </div>
          </Reveal>
        </div>
      </section>

      <Footer />
    </>
  );
}
