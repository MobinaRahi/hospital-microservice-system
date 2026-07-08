import { useState } from 'react';
import {
  User, CalendarDays, Clock, CheckCircle2, FileText, Plus,
  Droplet, Activity, HeartPulse,
} from 'lucide-react';
import {
  PageHeader, Card, CardHeader, Avatar, Badge, Button, StatusBadge, EmptyState,
} from '../../components/ui';
import { toFa, faNumber, faDate, initials } from '../../utils/format';

const PATIENT = { id: 1, fullName: 'رضا محمدی', gender: 'MAN', bloodType: 'A_POSITIVE', nationalId: '0012345678', age: 34 };
const UPCOMING = [
  { id: 1, doctor: { fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY' }, appointmentDate: '2026-07-12', startTime: '10:00', status: 'SCHEDULED', reason: 'ویزیت قلب' },
];
const PAST = [
  { id: 2, doctor: { fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY' }, appointmentDate: '2026-06-28', startTime: '09:30', status: 'COMPLETED', reason: 'نوار قلب' },
  { id: 3, doctor: { fullName: 'دکتر علی رضایی', speciality: 'ORTHOPEDICS' }, appointmentDate: '2026-05-14', startTime: '14:00', status: 'COMPLETED', reason: 'پیگیری زانو' },
];

export default function PatientPanel() {
  const [tab, setTab] = useState('upcoming');

  return (
    <>
      <PageHeader
        title={`سلام، ${PATIENT.fullName.split(' ')[0]}`}
        subtitle="خلاصه‌ی پرونده و نوبت‌های شما"
        actions={<Button to="/app/book" icon={Plus}>رزرو نوبت جدید</Button>}
      />

      {/* Patient summary */}
      <Card className="mb-24">
        <div className="card-body flex items-center gap-24" style={{ flexWrap: 'wrap' }}>
          <span className="avatar avatar-lg">{initials(PATIENT.fullName)}</span>
          <div style={{ flex: 1, minWidth: 200 }}>
            <h2 className="text-xl fw-800">{PATIENT.fullName}</h2>
            <div className="flex items-center gap-12 mt-8" style={{ flexWrap: 'wrap' }}>
              <Badge tone="brand" dot>بیمار فعال</Badge>
              <span className="text-sm text-muted flex items-center gap-4"><Droplet size={14} className="text-danger" />گروه خونی {toFa(PATIENT.bloodType?.replace('_', ''))}</span>
              <span className="text-sm text-muted">{toFa(PATIENT.age)} ساله</span>
            </div>
          </div>
          <div className="flex gap-24">
            {[['نوبت‌ها', faNumber(3)], ['ویزیت‌ها', faNumber(2)]].map(([k, v]) => (
              <div key={k} className="text-center">
                <div className="fw-800 text-lg">{v}</div>
                <div className="text-xs text-muted">{k}</div>
              </div>
            ))}
          </div>
        </div>
      </Card>

      {/* Quick stats */}
      <div className="grid-stats mb-24">
        {[
          { i: CalendarDays, l: 'نوبت‌های آینده', v: UPCOMING.length },
          { i: CheckCircle2, l: 'ویزیت‌های انجام‌شده', v: PAST.length },
          { i: FileText, l: 'نسخه‌ها', v: 2 },
          { i: Activity, l: 'آخرین ویزیت', v: '۸ روز' },
        ].map((s, idx) => (
          <div key={s.l} className="stat-card fade-in-up" style={{ animationDelay: `${idx * 0.05}s` }}>
            <div className="stat-glow" />
            <div className="stat-icon"><s.i size={24} /></div>
            <div className="stat-value">{s.v}</div>
            <div className="stat-label">{s.l}</div>
          </div>
        ))}
      </div>

      {/* Next appointment highlight */}
      {UPCOMING[0] && (
        <Card className="mb-24" hover>
          <div className="card-body flex items-center gap-16" style={{ flexWrap: 'wrap' }}>
            <span style={{ width: 54, height: 54, borderRadius: 14, background: 'var(--gradient-brand)', display: 'grid', placeItems: 'center' }}>
              <HeartPulse size={26} color="#fff" />
            </span>
            <div style={{ flex: 1, minWidth: 200 }}>
              <div className="text-xs text-brand fw-700">نوبت بعدی شما</div>
              <div className="fw-700">{UPCOMING[0].doctor.fullName}</div>
              <div className="text-sm text-muted">{faDate(UPCOMING[0].appointmentDate)} • ساعت {toFa(UPCOMING[0].startTime?.slice(0, 5))} • {UPCOMING[0].reason}</div>
            </div>
            <div className="flex gap-8">
              <Button variant="ghost" size="sm">لغو</Button>
              <Button variant="soft" size="sm">جزئیات</Button>
            </div>
          </div>
        </Card>
      )}

      {/* Tabs */}
      <div className="segmented mb-24">
        {[['upcoming', 'نوبت‌های آینده'], ['past', 'تاریخچه ویزیت']].map(([v, l]) => (
          <button key={v} className={tab === v ? 'active' : ''} onClick={() => setTab(v)}>{l}</button>
        ))}
      </div>

      <Card>
        <CardHeader title={tab === 'upcoming' ? 'نوبت‌های آینده' : 'تاریخچه ویزیت‌ها'} />
        {(() => {
          const list = tab === 'upcoming' ? UPCOMING : PAST;
          return list.length === 0 ? <EmptyState icon={CalendarDays} title="موردی یافت نشد" /> : (
            <div className="table-wrap" style={{ border: 'none' }}>
              <table className="data-table">
                <thead><tr><th>پزشک</th><th>تاریخ</th><th>ساعت</th><th>علت</th><th>وضعیت</th></tr></thead>
                <tbody>
                  {list.map((a) => (
                    <tr key={a.id}>
                      <td><div className="avatar-cell"><Avatar name={a.doctor.fullName} size="sm" /><span className="cell-strong">{a.doctor.fullName}</span></div></td>
                      <td className="cell-sub">{faDate(a.appointmentDate)}</td>
                      <td><span className="flex items-center gap-4"><Clock size={14} className="text-muted" />{toFa(a.startTime?.slice(0, 5))}</span></td>
                      <td className="cell-sub">{a.reason}</td>
                      <td><StatusBadge group="appointmentStatus" value={a.status} /></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          );
        })()}
      </Card>
    </>
  );
}
