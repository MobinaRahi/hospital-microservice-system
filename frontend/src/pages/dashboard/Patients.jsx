import { useState, useMemo } from 'react';
import { Users, Search, UserPlus, Phone, Droplet, BedDouble } from 'lucide-react';
import {
  PageHeader, Card, Avatar, Badge, Button, PageLoader, EmptyState, StatusBadge,
} from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber } from '../../utils/format';

const DEMO = [
  { id: 1, fullName: 'رضا محمدی', nationalId: '0012345678', phoneNumber: '09121110001', gender: 'MAN', bloodType: 'A_POSITIVE', status: 'ACTIVE', currentRoom: null },
  { id: 2, fullName: 'مریم کریمی', nationalId: '0023456789', phoneNumber: '09121110002', gender: 'FEMALE', bloodType: 'O_NEGATIVE', status: 'ACTIVE', currentRoom: { roomNumber: '203' } },
  { id: 3, fullName: 'حسین احمدی', nationalId: '0034567890', phoneNumber: '09121110003', gender: 'MAN', bloodType: 'B_POSITIVE', status: 'ACTIVE', currentRoom: null },
  { id: 4, fullName: 'زهرا نوری', nationalId: '0045678901', phoneNumber: '09121110004', gender: 'FEMALE', bloodType: 'AB_POSITIVE', status: 'ARCHIVED', currentRoom: null },
  { id: 5, fullName: 'محمد رحیمی', nationalId: '0056789012', phoneNumber: '09121110005', gender: 'MAN', bloodType: 'A_NEGATIVE', status: 'ACTIVE', currentRoom: { roomNumber: '105' } },
];

export default function Patients() {
  const { data, loading } = useFetch(() => endpoints.patients.all().catch(() => DEMO), []);
  const [q, setQ] = useState('');
  const list = Array.isArray(data) && data.length ? data : DEMO;
  const filtered = useMemo(() => list.filter((p) => {
    if (!q) return true;
    const t = `${p.fullName || ''} ${p.nationalId || ''} ${p.phoneNumber || ''}`.toLowerCase();
    return t.includes(q.toLowerCase());
  }), [list, q]);

  return (
    <>
      <PageHeader
        title="مدیریت بیماران"
        subtitle={`${faNumber(filtered.length)} بیمار`}
        actions={<Button to="/app/patients" icon={UserPlus}>افزودن بیمار</Button>}
      />

      <Card className="mb-24">
        <div className="card-body card-body-sm">
          <div className="input-group has-icon">
            <span className="input-icon"><Search size={18} /></span>
            <input className="input" placeholder="جستجو بر اساس نام، کد ملی یا تلفن…" value={q} onChange={(e) => setQ(e.target.value)} />
          </div>
        </div>
      </Card>

      {loading && !data ? <PageLoader /> : filtered.length === 0 ? (
        <Card><div className="card-body"><EmptyState icon={Users} title="بیماری یافت نشد" /></div></Card>
      ) : (
        <Card>
          <div className="table-wrap" style={{ border: 'none' }}>
            <table className="data-table">
              <thead>
                <tr><th>بیمار</th><th>کد ملی</th><th>تماس</th><th>گروه خونی</th><th>اتاق</th><th>وضعیت</th></tr>
              </thead>
              <tbody>
                {filtered.map((p) => (
                  <tr key={p.id}>
                    <td>
                      <div className="avatar-cell">
                        <Avatar name={p.fullName} size="sm" />
                        <div>
                          <div className="cell-strong">{p.fullName || '—'}</div>
                          <div className="cell-sub">{p.gender === 'FEMALE' ? 'زن' : p.gender === 'MAN' ? 'مرد' : '—'}</div>
                        </div>
                      </div>
                    </td>
                    <td className="cell-sub" dir="ltr">{toFa(p.nationalId)}</td>
                    <td className="cell-sub" dir="ltr">{toFa(p.phoneNumber)}</td>
                    <td><span className="flex items-center gap-4"><Droplet size={14} className="text-danger" />{toFa(p.bloodType?.replace('_', '') || '—')}</span></td>
                    <td>{p.currentRoom ? <Badge tone="info"><BedDouble size={12} /> {toFa(p.currentRoom.roomNumber)}</Badge> : <span className="text-muted text-sm">—</span>}</td>
                    <td><StatusBadge group="patientStatus" value={p.status} /></td>
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
