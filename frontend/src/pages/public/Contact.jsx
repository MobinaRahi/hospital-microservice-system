import { Phone, Mail, MapPin, Clock, Send, MessageSquare } from 'lucide-react';
import PublicNavbar from '../../components/layout/PublicNavbar';
import Footer from '../../components/layout/Footer';
import { useReveal } from '../../hooks/useReveal';

function Reveal({ className = '', style, children }) {
  const ref = useReveal();
  return <div ref={ref} className={`reveal ${className}`.trim()} style={style}>{children}</div>;
}

const CONTACTS = [
  { icon: Phone, label: 'تلفن', value: '۰۲۱-۱۲۳۴۵۶۷۸', desc: 'شنبه تا پنجشنبه ۸ صبح تا ۸ شب' },
  { icon: Mail, label: 'ایمیل', value: 'info@nova-hospital.ir', desc: 'پاسخ در کمتر از ۲۴ ساعت' },
  { icon: MapPin, label: 'آدرس', value: 'تهران، خیابان ولیعصر، بیمارستان نووا', desc: 'نزدیک مترو ولیعصر' },
  { icon: Clock, label: 'ساعات کاری', value: '۲۴ ساعته', desc: 'اورژانس همیشه فعال' },
];

export default function Contact() {
  return (
    <>
      <PublicNavbar />
      <main style={{ paddingTop: 100, minHeight: '100vh' }}>
        <section className="lp-section">
          <div className="container">
            <Reveal>
              <div className="lp-head">
                <span className="lp-eyebrow"><span className="dot" /> تماس با ما</span>
                <h2>با ما در <span className="lp-grad">تماس</span> باشید</h2>
                <p>برای هرگونه سؤال، پیشنهاد یا درخواست با ما تماس بگیرید</p>
              </div>
            </Reveal>

            {/* Contact Cards */}
            <div className="lp-grid3" style={{ marginBottom: 48 }}>
              {CONTACTS.map(c => (
                <div className="lp-card" key={c.label} style={{ textAlign: 'center' }}>
                  <div className="fc-ic" style={{ marginInline: 'auto' }}><c.icon size={27} /></div>
                  <h3>{c.label}</h3>
                  <p style={{ fontWeight: 700, color: 'var(--text)', fontSize: '1rem' }}>{c.value}</p>
                  <p style={{ fontSize: '0.85rem' }}>{c.desc}</p>
                </div>
              ))}
            </div>

            {/* Contact Form */}
            <Reveal>
              <div style={{
                display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 40,
                background: 'var(--surface)', border: '1px solid var(--border)',
                borderRadius: 24, padding: 40
              }}>
                <div>
                  <h3 style={{ fontSize: '1.3rem', fontWeight: 900, marginBottom: 8 }}>
                    <MessageSquare size={24} style={{ verticalAlign: 'middle', marginLeft: 8 }} />
                    پیام بگذارید
                  </h3>
                  <p style={{ color: 'var(--text-muted)', marginBottom: 24 }}>فرم زیر را پر کنید، در اسرع وقت با شما تماس می‌گیریم</p>

                  <form onSubmit={e => e.preventDefault()}>
                    <div style={{ marginBottom: 16 }}>
                      <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 6, fontWeight: 600 }}>نام و نام خانوادگی</label>
                      <input type="text" placeholder="نام خود را وارد کنید" style={{
                        width: '100%', padding: '12px 16px', borderRadius: 10,
                        border: '1px solid var(--border)', background: 'var(--surface-2)',
                        color: 'var(--text)', fontSize: '0.9rem'
                      }} />
                    </div>
                    <div style={{ marginBottom: 16 }}>
                      <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 6, fontWeight: 600 }}>شماره تماس</label>
                      <input type="tel" placeholder="۰۹۱۲۱۲۳۴۵۶۷" style={{
                        width: '100%', padding: '12px 16px', borderRadius: 10,
                        border: '1px solid var(--border)', background: 'var(--surface-2)',
                        color: 'var(--text)', fontSize: '0.9rem'
                      }} />
                    </div>
                    <div style={{ marginBottom: 16 }}>
                      <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 6, fontWeight: 600 }}>موضوع</label>
                      <input type="text" placeholder="موضوع پیام" style={{
                        width: '100%', padding: '12px 16px', borderRadius: 10,
                        border: '1px solid var(--border)', background: 'var(--surface-2)',
                        color: 'var(--text)', fontSize: '0.9rem'
                      }} />
                    </div>
                    <div style={{ marginBottom: 20 }}>
                      <label style={{ display: 'block', fontSize: '0.85rem', marginBottom: 6, fontWeight: 600 }}>پیام</label>
                      <textarea rows={4} placeholder="پیام خود را بنویسید..." style={{
                        width: '100%', padding: '12px 16px', borderRadius: 10,
                        border: '1px solid var(--border)', background: 'var(--surface-2)',
                        color: 'var(--text)', fontSize: '0.9rem', resize: 'vertical'
                      }} />
                    </div>
                    <button type="submit" className="lp-btn lp-btn-pri" style={{ width: '100%', justifyContent: 'center' }}>
                      <Send size={18} /> ارسال پیام
                    </button>
                  </form>
                </div>

                <div>
                  <div style={{
                    borderRadius: 20, overflow: 'hidden', height: '100%',
                    minHeight: 400, background: 'var(--surface-2)',
                    border: '1px solid var(--border)', display: 'grid', placeItems: 'center'
                  }}>
                    <div style={{ textAlign: 'center', padding: 40 }}>
                      <MapPin size={60} style={{ color: 'var(--brand)', marginBottom: 16 }} />
                      <h3 style={{ fontWeight: 800, marginBottom: 8 }}>موقعیت ما</h3>
                      <p style={{ color: 'var(--text-muted)' }}>تهران، خیابان ولیعصر</p>
                      <p style={{ color: 'var(--text-muted)', marginTop: 8, fontSize: '0.85rem' }}>
                        نقشه گوگل به زودی اضافه می‌شود
                      </p>
                    </div>
                  </div>
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
