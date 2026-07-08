import { useState } from 'react';
import {
  Stethoscope, CalendarDays, Users, Clock, TrendingUp, CheckCircle2,
  ClipboardList, Calendar,
} from 'lucide-react';
import {
  PageHeader, Card, CardHeader, Avatar, Badge, Button, StatusBadge, EmptyState,
} from '../../components/ui';
import { toFa, faNumber, faDate, specialityLabel, initials } from '../../utils/format';

const DOCTOR = { id: 1, fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY', department: { departmentName: 'قلب و عروق' }, yearsOfExperience: 12 };
const APPTS = [
  { id: 1, patient: { fullName: 'رضا محمدی' }, appointmentDate: '2026-07-08', startTime: '09:30', status: 'CHECK_IN', reason: 'ویزیت قلب' },
  { id: 2, patient: { fullName: 'مریم کریمی' }, appointmentDate: '2026-07-08', startTime: '10:00', status: 'SCHEDULED', reason: 'پیگیری فشار' },
  { id: 3, patient: { fullName: 'حسین احمدی' }, appointmentDate: '2026-07-08', startTime: '11:00', status: 'COMPLETED', reason: 'نوار قلب' },
  { id: 4, patient: { fullName: 'زهرا نوری' }, appointmentDate: '2026-07-09', startTime: '09:00', status: 'SCHEDULED', reason: 'مشاوره' },
];
const PATIENTS = [
  { id: 1, fullName: 'رضا محمدی', lastVisit: '2026-07-01', status: 'ACTIVE' },
  { id: 2, fullName: 'مریم کریمی', lastVisit: '2026-06-28', status: 'ACTIVE' },
  { id: 3, fullName: 'حسین احمدی', lastVisit: '2026-07-08', status: 'ACTIVE' },
];
const TABS = [['today', 'نوبت‌های امروز'], ['upcoming', 'نوبت‌های آینده'], ['patients', 'بیماران من']];

export default function DoctorPanel() {
  const [tab, setTab] = useState('today');
  const today = APPTS.filter((a) => a.appointmentDate === '2026-07-08');
  const upcoming = APPTS.filter((a) => a.appointmentDate >= '2026-07-08');

  return (
    <>
      <PageHeader
        title={`سلام، ${DOCTOR.fullName}`}
        subtitle={`${specialityLabel(DOCTOR.speciality)} • ${DOCTOR.department?.departmentName}`}
        actions={<Button icon={Calendar}>برنامه هفتگی</Button>}
      />

      {/* Doctor summary card */}
      <Card className="mb-24">
        <div className="card-body flex items-center gap-24" style={{ flexWrap: 'wrap' }}>
          <span className="avatar avatar-lg">{initials(DOCTOR.fullName)}</span>
          <div style={{ flex: 1, minWidth: 200 }}>
            <h2 className="text-xl fw-800">{DOCTOR.fullName}</h2>
            <div className="flex items-center gap-12 mt-8" style={{ flexWrap: 'wrap' }}>
              <Badge tone="brand">{specialityLabel(DOCTOR.speciality)}</Badge>
              <Badge tone="neutral">{DOCTOR.department?.departmentName}</Badge>
              <Badge tone="success" dot>در دسترس</Badge>
            </div>
          </div>
          <div className="flex gap-24">
            {[['سابقه', `${faNumber(DOCTOR.yearsOfExperience)} سال`], ['امتیاز', '۴.۹ ★']].map(([k, v]) => (
              <div key={k} className="text-center">
                <div className="fw-800 text-lg">{v}</div>
                <div className="text-xs text-muted">{k}</div>
              </div>
            ))}
          </div>
        </div>
      </Card>

      {/* Stats */}
      <div className="grid-stats mb-24">
        {[
          { i: CalendarDays, l: 'نوبت امروز', v: today.length },
          { i: Users, l: 'بیماران من', v: PATIENTS.length },
          { i: TrendingUp, l: 'نوبت این هفته', v: upcoming.length },
          { i: CheckCircle2, l: 'تکمیل‌شده امروز', v: today.filter((a) => a.status === 'COMPLETED').length },
        ].map((s, idx) => (
          <div key={s.l} className="stat-card fade-in-up" style={{ animationDelay: `${idx * 0.05}s` }}>
            <div className="stat-glow" />
            <div className="stat-icon"><s.i size={24} /></div>
            <div className="stat-value">{faNumber(s.v)}</div>
            <div className="stat-label">{s.l}</div>
          </div>
        ))}
      </div>

      {/* Tabs */}
      <div className="segmented mb-24">
        {TABS.map(([v, l]) => <button key={v} className={tab === v ? 'active' : ''} onClick={() => setTab(v)}>{l}</button>)}
      </div>

      {tab === 'patients' ? (
        <Card>
          <div className="table-wrap" style={{ border: 'none' }}>
            <table className="data-table">
              <thead><tr><th>بیمار</th><th>آخرین ویزیت</th><th>وضعیت</th><th></th></tr></thead>
              <tbody>
                {PATIENTS.map((p) => (
                  <tr key={p.id}>
                    <td><div className="avatar-cell"><Avatar name={p.fullName} size="sm" /><span className="cell-strong">{p.fullName}</span></div></td>
                    <td className="cell-sub">{faDate(p.lastVisit)}</td>
                    <td><StatusBadge group="patientStatus" value={p.status} /></td>
                    <td><Button variant="ghost" size="sm" icon={ClipboardList}>پرونده</Button></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      ) : (
        <div className="grid-cards-3 stagger">
          {(tab === 'today' ? today : upcoming).map((a) => (
            <Card key={a.id} hover>
              <div className="card-body">
                <div className="flex items-center justify-between mb-16">
                  <span className="flex items-center gap-4 text-muted text-sm"><Clock size={15} />{toFa(a.startTime?.slice(0, 5))}</span>
                  <StatusBadge group="appointmentStatus" value={a.status} />
                </div>
                <div className="avatar-cell mb-8">
                  <Avatar name={a.patient?.fullName} size="sm" />
                  <span className="cell-strong">{a.patient?.fullName}</span>
                </div>
                <div className="text-sm text-muted">{a.reason}</div>
                <div className="text-xs text-muted mt-8">{faDate(a.appointmentDate)}</div>
                <div className="flex gap-8 mt-16">
                  <Button variant="soft" size="sm" className="flex-1">شروع ویزیت</Button>
                  <Button variant="ghost" size="sm" icon={ClipboardList} />
                </div>
              </div>
            </Card>
          ))}
          {(tab === 'today' ? today : upcoming).length === 0 && (
            <Card className="card-body"><EmptyState icon={CalendarDays} title="نوبتی در این بازه نیست" /></Card>
          )}
        </div>
      )}
    </>
  );
}
