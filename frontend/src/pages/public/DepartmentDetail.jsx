import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import {
  Building2, CalendarPlus, Stethoscope, MapPin, ArrowRight,
  HeartPulse, Brain, Bone, Baby, Ambulance, Microscope,
  Smile, Eye, Pill, Users,
} from 'lucide-react';
import PublicNavbar from '../../components/layout/PublicNavbar';
import Footer from '../../components/layout/Footer';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { specialityLabel, initials, faNumber } from '../../utils/format';

const DEPT_ICONS = {
  'قلب': HeartPulse, 'cardio': HeartPulse,
  'مغز': Brain, 'اعصاب': Brain, 'neuro': Brain,
  'ارتوپد': Bone, 'ortho': Bone,
  'اطفال': Baby, 'کودک': Baby, 'pedia': Baby,
  'اورژانس': Ambulance, 'emergency': Ambulance,
  'رادیولوژی': Microscope, 'radio': Microscope,
  'دندان': Smile, 'dental': Smile,
  'چشم': Eye, 'ophthal': Eye,
  'دارو': Pill, 'pharma': Pill,
  'داخلی': Stethoscope, 'internal': Stethoscope,
};

function getDeptIcon(name = '') {
  const n = name.toLowerCase();
  const key = Object.keys(DEPT_ICONS).find(k => n.includes(k));
  return key ? DEPT_ICONS[key] : Building2;
}

const COLORS = [
  'linear-gradient(135deg,#0ea5e9,#14b8a6)',
  'linear-gradient(135deg,#10b981,#34d399)',
  'linear-gradient(135deg,#0d9488,#2dd4bf)',
  'linear-gradient(135deg,#14b8a6,#0ea5e9)',
  'linear-gradient(135deg,#6366f1,#a855f7)',
  'linear-gradient(135deg,#ec4899,#f472b6)',
];

const DEMO_DEPT = { id: 1, departmentName: 'قلب و عروق', location: 'طبقه ۲', description: 'تشخیص و درمان تخصصی بیماری‌های قلبی با تیم بوردتخصص و تجهیزات پیشرفته.' };
const DEMO_DOCTORS = [
  { id: 1, fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY', yearsOfExperience: 12, rating: 4.9 },
  { id: 2, fullName: 'دکتر رضا کریمی', speciality: 'CARDIOLOGY', yearsOfExperience: 8, rating: 4.7 },
];

export default function DepartmentDetail() {
  const { id } = useParams();

  const { data: dept } = useFetch(() => {
    if (!id) return Promise.resolve(null);
    return endpoints.departments.all().then(depts => {
      const list = Array.isArray(depts) ? depts : [];
      return list.find(d => String(d.id) === String(id)) || null;
    }).catch(() => null);
  }, [id]);

  const { data: doctors } = useFetch(() => {
    if (!id) return Promise.resolve([]);
    return endpoints.doctors.active().then(docs => {
      const list = Array.isArray(docs) ? docs : [];
      return list.filter(d => String(d.departmentId) === String(id));
    }).catch(() => []);
  }, [id]);

  const department = dept || DEMO_DEPT;
  const doctorList = (Array.isArray(doctors) && doctors.length) ? doctors : DEMO_DOCTORS;
  const DeptIcon = getDeptIcon(department.departmentName || department.name || '');

  return (
    <>
      <PublicNavbar />
      <main style={{ paddingTop: 100, minHeight: '100vh' }}>
        <section className="lp-section">
          <div className="container">
            {/* Back button */}
            <Link to="/" style={{
              display: 'inline-flex', alignItems: 'center', gap: 8,
              color: 'var(--text-muted)', marginBottom: 24, textDecoration: 'none',
              fontSize: '0.9rem'
            }}>
              <ArrowRight size={18} /> بازگشت به خانه
            </Link>

            {/* Department Header */}
            <div style={{
              display: 'flex', alignItems: 'center', gap: 24, marginBottom: 40,
              background: 'var(--surface)', border: '1px solid var(--border)',
              borderRadius: 20, padding: 32
            }}>
              <div style={{
                width: 80, height: 80, borderRadius: 20,
                background: 'var(--gradient-brand)', display: 'grid',
                placeItems: 'center', color: '#fff', flexShrink: 0
              }}>
                <DeptIcon size={40} />
              </div>
              <div style={{ flex: 1 }}>
                <h1 style={{ fontSize: '1.8rem', fontWeight: 900, marginBottom: 8 }}>
                  {department.departmentName || department.name}
                </h1>
                <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
                  <span><MapPin size={16} style={{ verticalAlign: 'middle', marginLeft: 4 }} />{department.location || '—'}</span>
                  <span><Users size={16} style={{ verticalAlign: 'middle', marginLeft: 4 }} />{faNumber(doctorList.length)} پزشک</span>
                </div>
                {department.description && (
                  <p style={{ marginTop: 12, color: 'var(--text-secondary)', lineHeight: 1.8 }}>
                    {department.description}
                  </p>
                )}
              </div>
            </div>

            {/* Doctors Section */}
            <div className="lp-head">
              <span className="lp-eyebrow"><span className="dot" /> پزشکان بخش</span>
              <h2>متخصصان <span className="lp-grad">{department.departmentName || department.name}</span></h2>
            </div>

            <div className="lp-grid3">
              {doctorList.map((d, i) => (
                <div className="lp-doc" key={d.id}>
                  <div className="av" style={{ background: COLORS[i % COLORS.length] }}>
                    {initials(d.fullName)}
                  </div>
                  <div className="nm">{d.fullName}</div>
                  <div className="sp">{specialityLabel(d.speciality)}</div>
                  <div style={{ fontSize: '0.85rem', color: 'var(--text-muted)', margin: '6px 0' }}>
                    {d.yearsOfExperience} سال سابقه
                  </div>
                  <div className="stars">{'★'.repeat(Math.round(d.rating || 5))} {(d.rating || 4.9).toFixed(1)}</div>
                  <Link className="bk" to={`/book?doctorId=${d.id}&departmentId=${id}`}>
                    <CalendarPlus size={15} /> رزرو نوبت
                  </Link>
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
