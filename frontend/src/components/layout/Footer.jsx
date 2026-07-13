import { Link } from 'react-router-dom';
import { Activity, MapPin, Phone, Mail, Clock } from 'lucide-react';

export default function Footer() {
  return (
    <footer className="site-footer" id="contact">
      <div className="footer-grid">
        <div>
          <div className="flex items-center gap-12 mb-16">
            <span style={{ width: 40, height: 40, borderRadius: 12, background: 'var(--gradient-brand)', display: 'grid', placeItems: 'center' }}>
              <Activity size={22} color="#fff" />
            </span>
            <span>
              <span className="fw-800" style={{ display: 'block' }}>نووا</span>
              <span className="text-xs text-muted">بیمارستان فوق‌تخصصی</span>
            </span>
          </div>
          <p>ارائه خدمات بهداشتی و درمانی در سطح جهانی با ترکیب فناوری روز و مراقبت انسانی، با تکیه بر رضایت بیماران عزیز.</p>
        </div>

        <div>
          <h4>دسترسی سریع</h4>
          <Link to="/">خانه</Link>
          <Link to="/doctors">پزشکان</Link>
          <Link to="/departments">بخش‌ها</Link>
          <Link to="/about">درباره ما</Link>
          <Link to="/contact">تماس با ما</Link>
          <Link to="/track">پیگیری نوبت</Link>
        </div>

        <div>
          <h4>بخش‌ها</h4>
          <Link to="/departments">قلب و عروق</Link>
          <Link to="/departments">مغز و اعصاب</Link>
          <Link to="/departments">ارتوپدی</Link>
          <Link to="/departments">اورژانس</Link>
          <Link to="/departments">رادیولوژی</Link>
        </div>

        <div>
          <h4>تماس با ما</h4>
          <p className="flex items-center gap-8"><MapPin size={16} /> خیابان ولیعصر، مجتمع پزشکی نووا</p>
          <p className="flex items-center gap-8"><Phone size={16} /> ۰۲۱-۹۱۰۰۲۰۳۰</p>
          <p className="flex items-center gap-8"><Mail size={16} /> info@nova-hospital.ir</p>
          <p className="flex items-center gap-8"><Clock size={16} /> اورژانس ۲۴ ساعته</p>
        </div>
      </div>
      <div className="footer-bottom">© ۱۴۰۵ بیمارستان فوق‌تخصصی نووا — تمامی حقوق محفوظ است.</div>
    </footer>
  );
}
