import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Search, Stethoscope, CalendarPlus, MapPin, Star, Filter } from 'lucide-react';
import PublicNavbar from '../../components/layout/PublicNavbar';
import Footer from '../../components/layout/Footer';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { specialityLabel, initials } from '../../utils/format';

const DEMO_DOCTORS = [
  { id: 1, fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY', departmentName: 'قلب و عروق', yearsOfExperience: 12, rating: 4.9, available: true },
  { id: 2, fullName: 'دکتر رضا کریمی', speciality: 'NEUROLOGY', departmentName: 'مغز و اعصاب', yearsOfExperience: 8, rating: 4.8, available: true },
  { id: 3, fullName: 'دکتر مریم احمدی', speciality: 'PEDIATRICS', departmentName: 'اطفال', yearsOfExperience: 15, rating: 4.9, available: true },
  { id: 4, fullName: 'دکتر علی رضایی', speciality: 'ORTHOPEDICS', departmentName: 'ارتوپدی', yearsOfExperience: 10, rating: 4.7, available: false },
  { id: 5, fullName: 'دکتر نیلوفر حسینی', speciality: 'DERMATOLOGY', departmentName: 'پوست و مو', yearsOfExperience: 6, rating: 4.8, available: true },
  { id: 6, fullName: 'دکتر محمد جوادی', speciality: 'OPHTHALMOLOGY', departmentName: 'چشم پزشکی', yearsOfExperience: 20, rating: 5.0, available: true },
];

const SPECIALITIES = ['همه', 'CARDIOLOGY', 'NEUROLOGY', 'PEDIATRICS', 'ORTHOPEDICS', 'DERMATOLOGY', 'OPHTHALMOLOGY'];

export default function PublicDoctors() {
  const { data: doctors } = useFetch(() => endpoints.doctors.active().catch(() => null), []);
  const [search, setSearch] = useState('');
  const [spec, setSpec] = useState('همه');

  const list = (Array.isArray(doctors) && doctors.length ? doctors : DEMO_DOCTORS).filter(d => {
    const matchSearch = d.fullName.includes(search) || d.departmentName?.includes(search);
    const matchSpec = spec === 'همه' || d.speciality === spec;
    return matchSearch && matchSpec;
  });

  return (
    <>
      <PublicNavbar />
      <main style={{ paddingTop: 100, minHeight: '100vh' }}>
        <section className="lp-section">
          <div className="container">
            <div className="lp-head">
              <span className="lp-eyebrow"><span className="dot" /> تیم پزشکی</span>
              <h2>پزشکان <span className="lp-grad">متخصص ما</span></h2>
              <p>از میان بهترین پزشکان بیمارستان نووا، متخصص مورد نظر خود را انتخاب کنید</p>
            </div>

            {/* Search & Filter */}
            <div style={{ display: 'flex', gap: 12, marginBottom: 32, flexWrap: 'wrap', justifyContent: 'center' }}>
              <div style={{ position: 'relative', minWidth: 280 }}>
                <Search size={18} style={{ position: 'absolute', right: 14, top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                <input
                  type="text"
                  placeholder="جستجوی پزشک یا تخصص..."
                  value={search}
                  onChange={e => setSearch(e.target.value)}
                  style={{ width: '100%', padding: '12px 40px 12px 16px', borderRadius: 12, border: '1px solid var(--border)', background: 'var(--surface)', color: 'var(--text)', fontSize: '0.9rem' }}
                />
              </div>
              <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                {SPECIALITIES.map(s => (
                  <button
                    key={s}
                    onClick={() => setSpec(s)}
                    style={{
                      padding: '8px 16px', borderRadius: 20, border: '1px solid var(--border)',
                      background: spec === s ? 'var(--brand)' : 'var(--surface)',
                      color: spec === s ? '#000' : 'var(--text)',
                      fontWeight: spec === s ? 700 : 500, fontSize: '0.85rem', cursor: 'pointer'
                    }}
                  >
                    {s === 'همه' ? 'همه' : specialityLabel(s)}
                  </button>
                ))}
              </div>
            </div>

            {/* Doctor Cards */}
            <div className="lp-grid3">
              {list.map(d => (
                <div className="lp-doc" key={d.id}>
                  <div className="av" style={{ background: `linear-gradient(135deg, ${d.id % 2 ? '#0ea5e9' : '#14b8a6'}, ${d.id % 3 ? '#10b981' : '#6366f1'})` }}>
                    {initials(d.fullName)}
                  </div>
                  <div className="nm">{d.fullName}</div>
                  <div className="sp">{specialityLabel(d.speciality)}</div>
                  <div className="dp">{d.departmentName}</div>
                  <div className="stars">{'★'.repeat(Math.round(d.rating || 5))} {(d.rating || 4.9).toFixed(1)}</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', margin: '6px 0' }}>
                    {d.yearsOfExperience} سال سابقه
                  </div>
                  <div style={{ marginBottom: 12 }}>
                    {d.available !== false ? (
                      <span style={{ color: '#10b981', fontSize: '0.8rem', fontWeight: 700 }}>● در دسترس</span>
                    ) : (
                      <span style={{ color: '#ef4444', fontSize: '0.8rem', fontWeight: 700 }}>● غیرفعال</span>
                    )}
                  </div>
                  <Link className="bk" to="/book"><CalendarPlus size={15} /> رزرو نوبت</Link>
                </div>
              ))}
            </div>

            {list.length === 0 && (
              <div style={{ textAlign: 'center', padding: 40, color: 'var(--text-muted)' }}>
                <Stethoscope size={48} style={{ marginBottom: 12, opacity: 0.3 }} />
                <p>پزشکی با این مشخصات یافت نشد</p>
              </div>
            )}
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
}
