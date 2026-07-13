import { Link } from 'react-router-dom';
import {
  HeartPulse, Brain, Bone, Baby, Ambulance, Microscope,
  Smile, Eye, Stethoscope, Pill, Building2, CalendarPlus,
} from 'lucide-react';
import PublicNavbar from '../../components/layout/PublicNavbar';
import Footer from '../../components/layout/Footer';

const DEPARTMENTS = [
  { id: 1, name: 'قلب و عروق', icon: HeartPulse, location: 'طبقه ۲', doctors: 8, desc: 'تشخیص و درمان بیماری‌های قلبی با تیم بوردتخصص' },
  { id: 2, name: 'مغز و اعصاب', icon: Brain, location: 'طبقه ۳', doctors: 6, desc: 'درمان سردرد، صرع، سکته مغزی و بیماری‌های عصبی' },
  { id: 3, name: 'ارتوپدی', icon: Bone, location: 'طبقه ۱', doctors: 7, desc: 'شکستگی، آسیب‌های ورزشی، جراحی مفاصل' },
  { id: 4, name: 'اطفال', icon: Baby, location: 'طبقه ۴', doctors: 10, desc: 'مراقبت تخصصی نوزادان، کودکان و نوجوانان' },
  { id: 5, name: 'اورژانس', icon: Ambulance, location: 'همکف', doctors: 12, desc: 'اورژانس ۲۴ ساعته با تیم مجرب' },
  { id: 6, name: 'رادیولوژی', icon: Microscope, location: 'زیرزمین', doctors: 4, desc: 'تصویربرداری پیشرفته MRI، CT، سونوگرافی' },
  { id: 7, name: 'دندانپزشکی', icon: Smile, location: 'طبقه ۱', doctors: 5, desc: 'دندانپزشکی عمومی و تخصصی' },
  { id: 8, name: 'چشم پزشکی', icon: Eye, location: 'طبقه ۲', doctors: 3, desc: 'درمان آب مروارید، لیزیک، شبکیه' },
  { id: 9, name: 'داخلی', icon: Stethoscope, location: 'طبقه ۲', doctors: 9, desc: 'تشخیص و درمان بیماری‌های داخلی' },
  { id: 10, name: 'داروخانه', icon: Pill, location: 'همکف', doctors: 0, desc: 'تأمین دارو و مشاوره دارویی' },
];

export default function PublicDepartments() {
  return (
    <>
      <PublicNavbar />
      <main style={{ paddingTop: 100, minHeight: '100vh' }}>
        <section className="lp-section">
          <div className="container">
            <div className="lp-head">
              <span className="lp-eyebrow"><span className="dot" /> بخش‌های تخصصی</span>
              <h2>بخش‌های <span className="lp-grad">بیمارستان نووا</span></h2>
              <p>بیمارستان نووا با بیش از ۱۰ بخش تخصصی آماده خدمت‌رسانی به شماست</p>
            </div>

            <div className="lp-grid3">
              {DEPARTMENTS.map(d => (
                <div className="lp-card" key={d.id}>
                  <div className="fc-ic"><d.icon size={27} /></div>
                  <h3>{d.name}</h3>
                  <p>{d.desc}</p>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 16, paddingTop: 12, borderTop: '1px solid var(--border)' }}>
                    <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                      <Building2 size={14} style={{ verticalAlign: 'middle', marginLeft: 4 }} />
                      {d.location}
                    </span>
                    {d.doctors > 0 && (
                      <span style={{ fontSize: '0.8rem', color: 'var(--brand)' }}>
                        {d.doctors} پزشک
                      </span>
                    )}
                  </div>
                  {d.doctors > 0 && (
                    <Link to="/book" style={{
                      display: 'block', textAlign: 'center', marginTop: 12,
                      padding: '8px 16px', borderRadius: 10,
                      background: 'var(--gradient-brand-soft)', color: 'var(--brand-600)',
                      fontWeight: 700, fontSize: '0.85rem'
                    }}>
                      <CalendarPlus size={14} style={{ verticalAlign: 'middle', marginLeft: 6 }} />
                      رزرو نوبت
                    </Link>
                  )}
                </div>
              ))}
            </div>
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
}
