import { useState, useMemo } from 'react';
import { HeartPulse, Search, Plus, Phone, Award } from 'lucide-react';
import {
  PageHeader, Card, Avatar, Badge, Button, PageLoader, EmptyState,
} from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber } from '../../utils/format';

const POS = { HEAD_NURSE: 'سرویس‌دار', REGISTERED_NURSE: 'پرستار رسمی', PRACTICAL_NURSE: 'پرستار کمک', NURSE_AIDE: 'بهیار', MIDWIFE: 'ماما' };

const DEMO = [
  { id: 1, fullName: 'فاطمه عبدی', position: 'HEAD_NURSE', department: { departmentName: 'قلب و عروق' }, yearsOfExperience: 14, phoneNumber: '09121120001', isActive: true },
  { id: 2, fullName: 'زهرا موسوی', position: 'REGISTERED_NURSE', department: { departmentName: 'اورژانس' }, yearsOfExperience: 6, phoneNumber: '09121120002', isActive: true },
  { id: 3, fullName: 'سحر قاسمی', position: 'MIDWIFE', department: { departmentName: 'زنان' }, yearsOfExperience: 9, phoneNumber: '09121120003', isActive: true },
  { id: 4, fullName: 'نگار اکبری', position: 'NURSE_AIDE', department: { departmentName: 'اطفال' }, yearsOfExperience: 3, phoneNumber: '09121120004', isActive: false },
  { id: 5, fullName: 'الهام رستمی', position: 'REGISTERED_NURSE', department: { departmentName: 'ICU' }, yearsOfExperience: 11, phoneNumber: '09121120005', isActive: true },
];

export default function Nurses() {
  const { data, loading } = useFetch(() => endpoints.nurses.all().catch(() => DEMO), []);
  const [q, setQ] = useState('');
  const list = Array.isArray(data) && data.length ? data : DEMO;
  const filtered = useMemo(() => list.filter((n) => (n.fullName || '').toLowerCase().includes(q.toLowerCase())), [list, q]);

  return (
    <>
      <PageHeader title="مدیریت پرستاران" subtitle={`${faNumber(filtered.length)} پرستار`} actions={<Button icon={Plus}>افزودن پرستار</Button>} />

      <Card className="mb-24">
        <div className="card-body card-body-sm">
          <div className="input-group has-icon">
            <span className="input-icon"><Search size={18} /></span>
            <input className="input" placeholder="جستجوی پرستار…" value={q} onChange={(e) => setQ(e.target.value)} />
          </div>
        </div>
      </Card>

      {loading && !data ? <PageLoader /> : filtered.length === 0 ? (
        <Card><div className="card-body"><EmptyState icon={HeartPulse} title="پرستاری یافت نشد" /></div></Card>
      ) : (
        <Card>
          <div className="table-wrap" style={{ border: 'none' }}>
            <table className="data-table">
              <thead><tr><th>پرستار</th><th>سمت</th><th>بخش</th><th>تجربه</th><th>تماس</th><th>وضعیت</th></tr></thead>
              <tbody>
                {filtered.map((n) => (
                  <tr key={n.id}>
                    <td>
                      <div className="avatar-cell">
                        <Avatar name={n.fullName} size="sm" />
                        <span className="cell-strong">{n.fullName}</span>
                      </div>
                    </td>
                    <td><Badge tone="brand">{POS[n.position] || n.position}</Badge></td>
                    <td>{n.department?.departmentName || '—'}</td>
                    <td><span className="flex items-center gap-4"><Award size={14} className="text-muted" />{toFa(n.yearsOfExperience)} سال</span></td>
                    <td className="cell-sub" dir="ltr">{toFa(n.phoneNumber)}</td>
                    <td>{n.isActive === false ? <Badge tone="neutral" dot>غیرفعال</Badge> : <Badge tone="success" dot>فعال</Badge>}</td>
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
