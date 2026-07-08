import { useState, useMemo } from 'react';
import {
  Stethoscope, Search, UserPlus, Phone, Award, Eye,
} from 'lucide-react';
import {
  PageHeader, Card, CardHeader, Avatar, Badge, Button,
  PageLoader, EmptyState, Spinner,
} from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber, specialityLabel } from '../../utils/format';

const DEMO = [
  { id: 1, fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY', department: { departmentName: 'قلب و عروق' }, yearsOfExperience: 12, phoneNumber: '09121110001', isActive: true, licenseNumber: '12345' },
  { id: 2, fullName: 'دکتر رضا کریمی', speciality: 'NEUROLOGY', department: { departmentName: 'مغز و اعصاب' }, yearsOfExperience: 9, phoneNumber: '09121110002', isActive: true, licenseNumber: '12346' },
  { id: 3, fullName: 'دکتر مریم احمدی', speciality: 'PEDIATRICS', department: { departmentName: 'اطفال' }, yearsOfExperience: 7, phoneNumber: '09121110003', isActive: true, licenseNumber: '12347' },
  { id: 4, fullName: 'دکتر علی رضایی', speciality: 'ORTHOPEDICS', department: { departmentName: 'ارتوپدی' }, yearsOfExperience: 15, phoneNumber: '09121110004', isActive: false, licenseNumber: '12348' },
  { id: 5, fullName: 'دکتر نازنین صادقی', speciality: 'DERMATOLOGY', department: { departmentName: 'پوست' }, yearsOfExperience: 6, phoneNumber: '09121110005', isActive: true, licenseNumber: '12349' },
  { id: 6, fullName: 'دکتر امیر حسینی', speciality: 'GENERAL_SURGERY', department: { departmentName: 'جراحی' }, yearsOfExperience: 18, phoneNumber: '09121110006', isActive: true, licenseNumber: '12350' },
];
const SPECS = ['CARDIOLOGY', 'NEUROLOGY', 'PEDIATRICS', 'ORTHOPEDICS', 'DERMATOLOGY', 'GENERAL_SURGERY', 'INTERNAL_MEDICINE', 'RADIOLOGY', 'EMERGENCY_MEDICINE'];

export default function Doctors() {
  const { data, loading } = useFetch(() => endpoints.doctors.all().catch(() => DEMO), []);
  const [q, setQ] = useState('');
  const [spec, setSpec] = useState('');
  const [view, setView] = useState('all'); // all | active | inactive

  const list = Array.isArray(data) && data.length ? data : DEMO;
  const filtered = useMemo(() => {
    return list.filter((d) => {
      const name = (d.fullName || `${d.firstName || ''} ${d.lastName || ''}`).toLowerCase();
      if (q && !name.includes(q.toLowerCase())) return false;
      if (spec && d.speciality !== spec) return false;
      if (view === 'active' && d.isActive === false) return false;
      if (view === 'inactive' && d.isActive !== false) return false;
      return true;
    });
  }, [list, q, spec, view]);

  return (
    <>
      <PageHeader
        title="مدیریت پزشکان"
        subtitle={`${faNumber(filtered.length)} پزشک`}
        actions={<Button to="/app/book" icon={UserPlus}>افزودن پزشک</Button>}
      />

      {/* Filters */}
      <Card className="mb-24">
        <div className="card-body card-body-sm flex items-center gap-12" style={{ flexWrap: 'wrap' }}>
          <div className="input-group has-icon" style={{ flex: 1, minWidth: 220 }}>
            <span className="input-icon"><Search size={18} /></span>
            <input className="input" placeholder="جستجو بر اساس نام…" value={q} onChange={(e) => setQ(e.target.value)} />
          </div>
          <select className="select" style={{ maxWidth: 200 }} value={spec} onChange={(e) => setSpec(e.target.value)}>
            <option value="">همه تخصص‌ها</option>
            {SPECS.map((s) => <option key={s} value={s}>{specialityLabel(s)}</option>)}
          </select>
          <div className="segmented">
            {[['all', 'همه'], ['active', 'فعال'], ['inactive', 'غیرفعال']].map(([v, l]) => (
              <button key={v} className={view === v ? 'active' : ''} onClick={() => setView(v)}>{l}</button>
            ))}
          </div>
        </div>
      </Card>

      {loading && !data ? <PageLoader /> : filtered.length === 0 ? (
        <Card><div className="card-body"><EmptyState icon={Stethoscope} title="پزشکی یافت نشد" /></div></Card>
      ) : (
        <Card>
          <div className="table-wrap" style={{ border: 'none' }}>
            <table className="data-table">
              <thead>
                <tr><th>پزشک</th><th>تخصص</th><th>بخش</th><th>تجربه</th><th>تماس</th><th>وضعیت</th><th></th></tr>
              </thead>
              <tbody>
                {filtered.map((d) => (
                  <tr key={d.id}>
                    <td>
                      <div className="avatar-cell">
                        <Avatar name={d.fullName} size="sm" />
                        <div>
                          <div className="cell-strong">{d.fullName || '—'}</div>
                          <div className="cell-sub">پروانه: {toFa(d.licenseNumber)}</div>
                        </div>
                      </div>
                    </td>
                    <td><Badge tone="brand">{specialityLabel(d.speciality)}</Badge></td>
                    <td>{d.department?.departmentName || '—'}</td>
                    <td><span className="flex items-center gap-4"><Award size={14} className="text-muted" />{toFa(d.yearsOfExperience)} سال</span></td>
                    <td className="cell-sub" dir="ltr">{toFa(d.phoneNumber)}</td>
                    <td>{d.isActive === false ? <Badge tone="neutral" dot>غیرفعال</Badge> : <Badge tone="success" dot>فعال</Badge>}</td>
                    <td><Button variant="ghost" size="sm" icon={Eye}>مشاهده</Button></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      )}
    </>
  );
}
