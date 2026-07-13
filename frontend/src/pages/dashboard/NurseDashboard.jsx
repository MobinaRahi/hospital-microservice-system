import {
  HeartPulse, CalendarDays, Users, Clock, TrendingUp, CheckCircle2,
  ClipboardList, Activity, BedDouble, Stethoscope,
} from 'lucide-react';
import {
  PageHeader, Card, CardHeader, Avatar, Badge, Button, StatusBadge, EmptyState,
} from '../../components/ui';
import { toFa, faNumber, faDate, specialityLabel, initials } from '../../utils/format';

const NURSE = { id: 1, fullName: 'فاطمه رضایی', department: { departmentName: 'قلب و عروق' }, shift: 'صبح', yearsOfExperience: 8 };

const ASSIGNED_PATIENTS = [
  { id: 1, fullName: 'رضا محمدی', room: '۲۰۱', bed: 'الف', status: 'ACTIVE', diagnosis: 'آنژین صدری', doctor: 'دکتر سارا محمدی' },
  { id: 2, fullName: 'مریم کریمی', room: '۲۰۳', bed: 'ب', status: 'ACTIVE', diagnosis: 'آریتمی قلبی', doctor: 'دکتر سارا محمدی' },
  { id: 3, fullName: 'حسین احمدی', room: '۲۰۵', bed: 'الف', status: 'ACTIVE', diagnosis: 'فشار خون بالا', doctor: 'دکتر علی رضایی' },
];

const TODAY_TASKS = [
  { id: 1, time: '۰۸:۰۰', task: 'ویزیت صبحگاهی و کنترل علائم حیاتی', patient: 'رضا محمدی', status: 'COMPLETED' },
  { id: 2, time: '۰۹:۰۰', task: 'تزریق دارو و سرم‌درمانی', patient: 'مریم کریمی', status: 'COMPLETED' },
  { id: 3, time: '۱۰:۳۰', task: 'اندازه‌گیری فشار خون و ضربان قلب', patient: 'حسین احمدی', status: 'IN_PROGRESS' },
  { id: 4, time: '۱۲:۰۰', task: 'تعویض پانسمان و بررسی زخم', patient: 'رضا محمدی', status: 'SCHEDULED' },
  { id: 5, time: '۱۴:۰۰', task: 'تزریق آنتی‌بیوتیک', patient: 'مریم کریمی', status: 'SCHEDULED' },
  { id: 6, time: '۱۶:۰۰', task: 'ویزیت عصرگاهی و ثبت گزارش', patient: 'همه بیماران', status: 'SCHEDULED' },
];

const SHIFT_SCHEDULE = [
  { day: 'شنبه', shift: 'صبح (۷-۱۵)', status: 'ACTIVE' },
  { day: 'یکشنبه', shift: 'صبح (۷-۱۵)', status: 'ACTIVE' },
  { day: 'دوشنبه', shift: 'صبح (۷-۱۵)', status: 'ACTIVE' },
  { day: 'سه‌شنبه', shift: 'عصر (۱۵-۲۳)', status: 'UPCOMING' },
  { day: 'چهارشنبه', shift: 'عصر (۱۵-۲۳)', status: 'UPCOMING' },
  { day: 'پنجشنبه', shift: 'استراحت', status: 'OFF' },
  { day: 'جمعه', shift: 'استراحت', status: 'OFF' },
];

export default function NurseDashboard() {
  const completedTasks = TODAY_TASKS.filter(t => t.status === 'COMPLETED').length;
  const inProgressTasks = TODAY_TASKS.filter(t => t.status === 'IN_PROGRESS').length;

  return (
    <>
      <PageHeader
        title={`سلام، ${NURSE.fullName.split(' ')[0]} 👋`}
        subtitle={`${NURSE.department?.departmentName} • شیفت ${NURSE.shift}`}
        actions={<Button icon={ClipboardList}>گزارش شیفت</Button>}
      />

      {/* Nurse summary card */}
      <Card className="mb-24">
        <div className="card-body flex items-center gap-24" style={{ flexWrap: 'wrap' }}>
          <span className="avatar avatar-lg" style={{ background: 'linear-gradient(135deg, #ec4899, #f472b6)' }}>
            {initials(NURSE.fullName)}
          </span>
          <div style={{ flex: 1, minWidth: 200 }}>
            <h2 className="text-xl fw-800">{NURSE.fullName}</h2>
            <div className="flex items-center gap-12 mt-8" style={{ flexWrap: 'wrap' }}>
              <Badge tone="danger">پرستار</Badge>
              <Badge tone="neutral">{NURSE.department?.departmentName}</Badge>
              <Badge tone="success" dot>در حال شیفت</Badge>
            </div>
          </div>
          <div className="flex gap-24">
            {[['سابقه', `${faNumber(NURSE.yearsOfExperience)} سال`], ['بیماران', faNumber(ASSIGNED_PATIENTS.length)]].map(([k, v]) => (
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
          { i: Users, l: 'بیماران من', v: ASSIGNED_PATIENTS.length, color: '#ec4899' },
          { i: ClipboardList, l: 'وظایف امروز', v: TODAY_TASKS.length, color: '#a855f7' },
          { i: CheckCircle2, l: 'انجام‌شده', v: completedTasks, color: '#10b981' },
          { i: Activity, l: 'در حال انجام', v: inProgressTasks, color: '#f59e0b' },
        ].map((s, idx) => (
          <div key={s.l} className="stat-card fade-in-up" style={{ animationDelay: `${idx * 0.05}s` }}>
            <div className="stat-glow" />
            <div className="stat-icon"><s.i size={24} /></div>
            <div className="stat-value">{faNumber(s.v)}</div>
            <div className="stat-label">{s.l}</div>
          </div>
        ))}
      </div>

      {/* Main content grid */}
      <div style={{ display: 'grid', gridTemplateColumns: '1.6fr 1fr', gap: 22 }} className="dash-grid">
        {/* Today's Tasks */}
        <Card>
          <CardHeader title="برنامه امروز" sub={`${faNumber(TODAY_TASKS.length)} وظیفه`} />
          <div className="table-wrap" style={{ border: 'none' }}>
            <table className="data-table">
              <thead>
                <tr><th>ساعت</th><th>وظیفه</th><th>بیمار</th><th>وضعیت</th></tr>
              </thead>
              <tbody>
                {TODAY_TASKS.map((t) => (
                  <tr key={t.id}>
                    <td>
                      <span className="flex items-center gap-4">
                        <Clock size={14} className="text-muted" />
                        <span className="fw-600">{t.time}</span>
                      </span>
                    </td>
                    <td><span className="cell-strong">{t.task}</span></td>
                    <td><span className="cell-sub">{t.patient}</span></td>
                    <td>
                      <span className={`badge badge-sm ${
                        t.status === 'COMPLETED' ? 'badge-success' :
                        t.status === 'IN_PROGRESS' ? 'badge-warning' : 'badge-neutral'
                      }`}>
                        {t.status === 'COMPLETED' ? '✅ انجام شده' :
                         t.status === 'IN_PROGRESS' ? '🔄 در حال انجام' : '⏳ برنامه‌ریزی شده'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>

        {/* Assigned Patients + Shift Schedule */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 22 }}>
          {/* Assigned Patients */}
          <Card>
            <CardHeader title="بیماران تحت مراقبت" />
            <div className="card-body">
              {ASSIGNED_PATIENTS.map((p) => (
                <div key={p.id} className="flex items-center gap-12 mb-12" style={{ padding: '10px 0', borderBottom: '1px solid var(--border)' }}>
                  <Avatar name={p.fullName} size="sm" />
                  <div style={{ flex: 1 }}>
                    <div className="fw-600 text-sm">{p.fullName}</div>
                    <div className="text-xs text-muted">اتاق {p.room} • تخت {p.bed}</div>
                  </div>
                  <Badge tone="neutral" className="text-xs">اتاق {p.room}</Badge>
                </div>
              ))}
            </div>
          </Card>

          {/* Shift Schedule */}
          <Card>
            <CardHeader title="برنامه هفتگی شیفت" />
            <div className="card-body">
              {SHIFT_SCHEDULE.map((s, idx) => (
                <div key={idx} className="flex items-center justify-between mb-8" style={{ padding: '6px 0' }}>
                  <span className="text-sm fw-600">{s.day}</span>
                  <span className={`text-sm ${
                    s.status === 'ACTIVE' ? 'text-success' :
                    s.status === 'UPCOMING' ? 'text-brand' : 'text-muted'
                  }`}>{s.shift}</span>
                </div>
              ))}
            </div>
          </Card>
        </div>
      </div>
    </>
  );
}
