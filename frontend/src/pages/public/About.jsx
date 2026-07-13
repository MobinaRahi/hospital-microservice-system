import { Link } from 'react-router-dom';
import {
  HeartPulse, ShieldCheck, Clock3, Users, Award, Building2,
  Stethoscope, Phone, MapPin, CalendarDays, CheckCircle2,
} from 'lucide-react';
import PublicNavbar from '../../components/layout/PublicNavbar';
import Footer from '../../components/layout/Footer';
import { useReveal } from '../../hooks/useReveal';
import { faNumber } from '../../utils/format';

function Reveal({ className = '', style, children }) {
  const ref = useReveal();
  return <div ref={ref} className={`reveal ${className}`.trim()} style={style}>{children}</div>;
}

const STATS = [
  { icon: Stethoscope, value: 64, label: 'پزشک متخصص' },
  { icon: Users, value: 4820, label: 'بیمار راضی' },
  { icon: Building2, value: 18, label: 'بخش تخصصی' },
  { icon: CalendarDays, value: 20, label: 'سال تجربه' },
];

const VALUES = [
  { icon: HeartPulse, t: 'سلامت بیمار اولویت ماست', d: 'تمام تلاش ما ارائه بهترین خدمات درمانی با بالاترین استانداردهای جهانی است.' },
  { icon: ShieldCheck, t: 'امنیت و حریم خصوصی', d: 'حفاظت از اطلاعات پزشکی بیماران با بالاترین استانداردهای امنیتی.' },
  { icon: Award, t: 'کیفیت و تعالی', d: 'تلاش مستمر برای بهبود کیفیت خدمات و رضایت بیماران.' },
  { icon: Clock3, t: 'دسترسی سریع', d: 'کاهش زمان انتظار و ارائه خدمات سریع و به‌موقع.' },
];

export default function About() {
  return (
    <>
      <PublicNavbar />
      <main style={{ paddingTop: 100, minHeight: '100vh' }}>
        {/* Hero */}
        <section className="lp-section" style={{ paddingBottom: 40 }}>
          <div className="container">
            <Reveal>
              <div className="lp-head">
                <span className="lp-eyebrow"><span className="dot" /> درباره ما</span>
                <h2>بیمارستان فوق‌تخصصی <span className="lp-grad">نووا</span></h2>
                <p>بیش از ۲۰ سال خدمت صادقانه در حوزه سلامت و درمان</p>
              </div>
            </Reveal>
          </div>
        </section>

        {/* Stats */}
        <section className="lp-stats-sec" style={{ paddingTop: 0 }}>
          <div className="container">
            <Reveal>
              <div className="lp-stats">
                {STATS.map(s => (
                  <div className="lp-stat" key={s.label}>
                    <div className="ic"><s.icon size={23} /></div>
                    <div className="n">{faNumber(s.value)}+</div>
                    <div className="l">{s.label}</div>
                  </div>
                ))}
              </div>
            </Reveal>
          </div>
        </section>

        {/* Story */}
        <section className="lp-section">
          <div className="container">
            <div className="lp-split">
              <Reveal>
                <span className="lp-eyebrow"><span className="dot" /> داستان ما</span>
                <h2 style={{ fontSize: 'clamp(1.6rem, 3vw, 2.2rem)', fontWeight: 900, lineHeight: 1.3, margin: '0.4em 0 0.5em' }}>
                  از یک رویا تا <span className="lp-grad">واقعیت</span>
                </h2>
                <p style={{ color: 'var(--text-secondary)', lineHeight: 2 }}>
                  بیمارستان نووا در سال ۱۳۸۴ با هدف ارائه خدمات درمانی با کیفیت تأسیس شد.
                  از همان ابتدا، تمرکز ما بر استفاده از فناوری‌های نوین و جذب بهترین متخصصان
                  بوده است. امروز با بیش از ۲۰ سال تجربه، بیش از ۶۴ پزشک متخصص و ۱۸ بخش
                  تخصصی، آماده خدمت‌رسانی به هموطنان عزیز هستیم.
                </p>
                <p style={{ color: 'var(--text-secondary)', lineHeight: 2, marginTop: 16 }}>
                  ما معتقدیم که سلامت حق همه انسان‌هاست و تلاش می‌کنیم با استفاده از
                  فناوری‌های نوین، دسترسی به خدمات درمانی را آسان‌تر و سریع‌تر کنیم.
                </p>
              </Reveal>
              <Reveal style={{ transitionDelay: '0.1s' }}>
                <div style={{
                  background: 'linear-gradient(135deg, var(--surface-2), var(--surface-3))',
                  borderRadius: 24, padding: 40, textAlign: 'center',
                  border: '1px solid var(--border)'
                }}>
                  <HeartPulse size={60} style={{ color: 'var(--brand)', marginBottom: 20 }} />
                  <h3 style={{ fontSize: '1.5rem', fontWeight: 900, marginBottom: 12 }}>چرا نووا؟</h3>
                  <p style={{ color: 'var(--text-muted)', lineHeight: 2 }}>
                    ترکیب تجربه، فناوری و تعهد به سلامت بیماران
                  </p>
                </div>
              </Reveal>
            </div>
          </div>
        </section>

        {/* Values */}
        <section className="lp-section lp-section-alt">
          <div className="container">
            <Reveal>
              <div className="lp-head">
                <span className="lp-eyebrow"><span className="dot" /> ارزش‌های ما</span>
                <h2>اصولی که به آن <span className="lp-grad">پایبندیم</span></h2>
              </div>
            </Reveal>
            <Reveal>
              <div className="lp-grid3">
                {VALUES.map(v => (
                  <div className="lp-card" key={v.t}>
                    <div className="fc-ic"><v.icon size={27} /></div>
                    <h3>{v.t}</h3>
                    <p>{v.d}</p>
                  </div>
                ))}
              </div>
            </Reveal>
          </div>
        </section>

        {/* CTA */}
        <section className="lp-section">
          <div className="container">
            <Reveal>
              <div className="lp-cta">
                <h2>آماده دریافت نوبت هستید؟</h2>
                <p>همین الان به صورت آنلاین نوبت بگیرید</p>
                <div className="lp-btns" style={{ justifyContent: 'center', marginTop: 24 }}>
                  <Link className="lp-btn lp-btn-pri" to="/book">رزرو نوبت آنلاین</Link>
                  <Link className="lp-btn lp-btn-gh" to="/contact">تماس با ما</Link>
                </div>
              </div>
            </Reveal>
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
}
