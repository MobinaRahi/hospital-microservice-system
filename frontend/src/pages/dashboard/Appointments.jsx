import { useState, useMemo } from 'react';
import { CalendarDays, Search, CalendarPlus, Clock, Stethoscope, User } from 'lucide-react';
import {
  PageHeader, Card, CardHeader, Avatar, Button, PageLoader, EmptyState, StatusBadge, Badge,
} from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber, faDate } from '../../utils/format';

const DEMO = [
  { id: 1, patient: { fullName: 'رضا محمدی' }, doctor: { fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY' }, appointmentDate: '2026-07-08', startTime: '09:30', endTime: '10:00', status: 'CHECK_IN', type: 'IN_PERSON', reason: 'ویزیت قلب' },
  { id: 2, patient: { fullName: 'مریم کریمی' }, doctor: { fullName: 'دکتر علی رضایی', speciality: 'ORTHOPEDICS' }, appointmentDate: '2026-07-08', startTime: '10:00', endTime: '10:30', status: 'SCHEDULED', type: 'IN_PERSON', reason: 'پیگیری' },
  { id: 3, patient: { fullName: 'حسین احمدی' }, doctor: { fullName: 'دکتر رضا کریمی', speciality: 'NEUROLOGY' }, appointmentDate: '2026-07-07', startTime: '11:00', endTime: '11:30', status: 'COMPLETED', type: 'VIDEO', reason: 'مشاوره' },
  { id: 4, patient: { fullName: 'زهرا نوری' }, doctor: { fullName: 'دکتر مریم احمدی', speciality: 'PEDIATRICS' }, appointmentDate: '2026-07-07', startTime: '14:00', endTime: '14:30', status: 'CANCELLED', type: 'IN_PERSON', reason: 'لغو توسط بیمار' },
  { id: 5, patient: { fullName: 'محمد رحیمی' }, doctor: { fullName: 'دکتر نازنین صادقی', speciality: 'DERMATOLOGY' }, appointmentDate: '2026-07-09', startTime: '09:00', endTime: '09:30', status: 'SCHEDULED', type: 'PHONE', reason: 'نوبت تلفنی' },
];
const STATUSES = ['SCHEDULED', 'CHECK_IN', 'COMPLETED', 'CANCELLED'];

export default function Appointments() {
  const { data, loading } = useFetch(() => endpoints.appointments.thisWeek().catch(() => DEMO), []);
  const [q, setQ] = useState('');
  const [status, setStatus] = useState('');
  const list = Array.isArray(data) && data.length ? data : DEMO;
  const filtered = useMemo(() => list.filter((a) => {
    if (status && a.status !== status) return false;
    if (!q) return true;
    const t = `${a.patient?.fullName || ''} ${a.doctor?.fullName || ''} ${a.reason || ''}`.toLowerCase();
    return t.includes(q.toLowerCase());
  }), [list, q, status]);

  const counts = useMemo(() => ({
    total: list.length,
    scheduled: list.filter((a) => a.status === 'SCHEDULED').length,
    completed: list.filter((a) => a.status === 'COMPLETED').length,
    cancelled: list.filter((a) => a.status === 'CANCELLED').length,
  }), [list]);

  return (
    <>
      <PageHeader
        title="مدیریت نوبت‌ها"
        subtitle={`${faNumber(filtered.length)} نوبت`}
        actions={<Button to="/app/book" icon={CalendarPlus}>ثبت نوبت</Button>}
      />

      <div className="grid-stats mb-24" style={{ gridTemplateColumns: 'repeat(4, 1fr)' }}>
        {[
          { l: 'کل نوبت‌ها', v: counts.total, t: 'brand' },
          { l: 'رزرو شده', v: counts.scheduled, t: 'info' },
          { l: 'تکمیل شده', v: counts.completed, t: 'success' },
          { l: 'لغو شده', v: counts.cancelled, t: 'danger' },
        ].map((s, i) => (
          <div key={s.l} className="stat-card fade-in-up" style={{ animationDelay: `${i * 0.05}s` }}>
            <div className="stat-glow" />
            <div className="stat-value">{faNumber(s.v)}</div>
            <div className="stat-label">{s.l}</div>
          </div>
        ))}
      </div>

      <Card className="mb-24">
        <div className="card-body card-body-sm flex items-center gap-12" style={{ flexWrap: 'wrap' }}>
          <div className="input-group has-icon" style={{ flex: 1, minWidth: 220 }}>
            <span className="input-icon"><Search size={18} /></span>
            <input className="input" placeholder="جستجوی بیمار، پزشک یا علت…" value={q} onChange={(e) => setQ(e.target.value)} />
          </div>
          <div className="segmented">
            <button className={!status ? 'active' : ''} onClick={() => setStatus('')}>همه</button>
            {STATUSES.map((s) => (
              <button key={s} className={status === s ? 'active' : ''} onClick={() => setStatus(s)}>
                {({ SCHEDULED: 'رزرو', CHECK_IN: 'پذیرش', COMPLETED: 'تکمیل', CANCELLED: 'لغو' })[s]}
              </button>
            ))}
          </div>
        </div>
      </Card>

      {loading && !data ? <PageLoader /> : filtered.length === 0 ? (
        <Card><div className="card-body"><EmptyState icon={CalendarDays} title="نوبتی یافت نشد" /></div></Card>
      ) : (
        <Card>
          <div className="table-wrap" style={{ border: 'none' }}>
            <table className="data-table">
              <thead>
                <tr><th>بیمار</th><th>پزشک</th><th>تاریخ</th><th>ساعت</th><th>نوع</th><th>وضعیت</th></tr>
              </thead>
              <tbody>
                {filtered.map((a) => (
                  <tr key={a.id}>
                    <td>
                      <div className="avatar-cell">
                        <Avatar name={a.patient?.fullName} size="sm" />
                        <div>
                          <div className="cell-strong">{a.patient?.fullName || '—'}</div>
                          <div className="cell-sub">{a.reason}</div>
                        </div>
                      </div>
                    </td>
                    <td>
                      <div className="cell-strong">{a.doctor?.fullName}</div>
                      <div className="cell-sub">{({ CARDIOLOGY: 'قلب', ORTHOPEDICS: 'ارتوپدی', NEUROLOGY: 'مغز', PEDIATRICS: 'اطفال', DERMATOLOGY: 'پوست' })[a.doctor?.speciality] || 'متخصص'}</div>
                    </td>
                    <td className="cell-sub">{faDate(a.appointmentDate)}</td>
                    <td><span className="flex items-center gap-4"><Clock size={14} className="text-muted" />{toFa(a.startTime?.slice(0, 5))}</span></td>
                    <td><Badge tone="neutral">{({ IN_PERSON: 'حضوری', VIDEO: 'ویدیویی', PHONE: 'تلفنی', EMERGENCY: 'اورژانسی' })[a.type] || a.type}</Badge></td>
                    <td><StatusBadge group="appointmentStatus" value={a.status} /></td>
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
