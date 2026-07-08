import { Building2, Search, Plus, MapPin, Stethoscope, Users } from 'lucide-react';
import {
  PageHeader, Card, Badge, Button, PageLoader, EmptyState,
} from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { useState, useMemo } from 'react';
import { toFa, faNumber, deptEmoji } from '../../utils/format';

const DEMO = [
  { id: 1, departmentName: 'قلب و عروق', departmentCode: 'CARD-001', location: 'طبقه ۲', headDoctor: { fullName: 'دکتر سارا محمدی' }, isActive: true },
  { id: 2, departmentName: 'مغز و اعصاب', departmentCode: 'NEUR-001', location: 'طبقه ۳', headDoctor: { fullName: 'دکتر رضا کریمی' }, isActive: true },
  { id: 3, departmentName: 'اطفال', departmentCode: 'PEDS-001', location: 'طبقه ۴', headDoctor: { fullName: 'دکتر مریم احمدی' }, isActive: true },
  { id: 4, departmentName: 'ارتوپدی', departmentCode: 'ORTH-001', location: 'طبقه ۱', headDoctor: null, isActive: true },
  { id: 5, departmentName: 'اورژانس', departmentCode: 'EMERG-001', location: 'همکف', headDoctor: null, isActive: true },
  { id: 6, departmentName: 'رادیولوژی', departmentCode: 'RAD-001', location: 'زیرزمین', headDoctor: null, isActive: false },
];

export default function Departments() {
  const { data, loading } = useFetch(() => endpoints.departments.all().catch(() => DEMO), []);
  const [q, setQ] = useState('');
  const list = Array.isArray(data) && data.length ? data : DEMO;
  const filtered = useMemo(() => list.filter((d) => (d.departmentName || '').toLowerCase().includes(q.toLowerCase())), [list, q]);

  return (
    <>
      <PageHeader title="مدیریت بخش‌ها" subtitle={`${faNumber(filtered.length)} بخش`} actions={<Button icon={Plus}>افزودن بخش</Button>} />

      <Card className="mb-24">
        <div className="card-body card-body-sm">
          <div className="input-group has-icon">
            <span className="input-icon"><Search size={18} /></span>
            <input className="input" placeholder="جستجوی بخش…" value={q} onChange={(e) => setQ(e.target.value)} />
          </div>
        </div>
      </Card>

      {loading && !data ? <PageLoader /> : filtered.length === 0 ? (
        <Card><div className="card-body"><EmptyState icon={Building2} title="بخشی یافت نشد" /></div></Card>
      ) : (
        <div className="grid-cards-3 stagger">
          {filtered.map((d) => (
            <Card key={d.id} hover>
              <div className="card-body">
                <div className="flex items-center justify-between mb-16">
                  <span style={{ width: 52, height: 52, borderRadius: 14, background: 'var(--gradient-brand-soft)', display: 'grid', placeItems: 'center', fontSize: 26 }}>
                    {deptEmoji(d.departmentCode)}
                  </span>
                  {d.isActive === false ? <Badge tone="neutral" dot>غیرفعال</Badge> : <Badge tone="success" dot>فعال</Badge>}
                </div>
                <h3 className="fw-700">{d.departmentName}</h3>
                <div className="text-sm text-muted mt-8 flex items-center gap-8"><MapPin size={15} /> {d.location || '—'}</div>
                <div className="text-sm text-muted mt-8 flex items-center gap-8"><Stethoscope size={15} /> سرپرست: {d.headDoctor?.fullName || '—'}</div>
                <div className="flex gap-8 mt-16">
                  <Button variant="ghost" size="sm" className="flex-1">جزئیات</Button>
                  <Button variant="soft" size="sm" className="flex-1" to="/app/doctors">پزشکان</Button>
                </div>
              </div>
            </Card>
          ))}
        </div>
      )}
    </>
  );
}
