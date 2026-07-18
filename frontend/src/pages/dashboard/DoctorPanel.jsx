import { useState } from 'react';
import { Link } from 'react-router-dom';
import {
  Stethoscope, CalendarDays, Users, Clock, TrendingUp, CheckCircle2,
  ClipboardList, Calendar,
} from 'lucide-react';
import {
  PageHeader, Card, CardHeader, Avatar, Badge, Button, StatusBadge, EmptyState,
} from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { useAuth } from '../../context/AuthContext';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber, faDate, specialityLabel, initials } from '../../utils/format';

const TABS = [['today', 'نوبت‌های امروز'], ['upcoming', 'نوبت‌های آینده'], ['patients', 'بیماران من']];

const DEMO_APPTS = [
  { id: 1, patient: { fullName: 'رضا محمدی' }, appointmentDate: '2026-07-16', startTime: '09:30', status: 'CHECK_IN', reason: 'ویزیت قلب' },
  { id: 2, patient: { fullName: 'مریم کریمی' }, appointmentDate: '2026-07-16', startTime: '10:00', status: 'SCHEDULED', reason: 'پیگیری فشار' },
  { id: 3, patient: { fullName: 'حسین احمدی' }, appointmentDate: '2026-07-16', startTime: '11:00', status: 'COMPLETED', reason: 'نوار قلب' },
  { id: 4, patient: { fullName: 'زهرا نوری' }, appointmentDate: '2026-07-17', startTime: '09:00', status: 'SCHEDULED', reason: 'مشاوره' },
];

export default function DoctorPanel() {
  const { user } = useAuth();
  const [tab, setTab] = useState('today');

  const { data: myAppointments } = useFetch(() => {
    if (!user?.id) return Promise.resolve([]);
    return endpoints.appointments.byDoctor(user.id).catch(() => []);
  }, [user?.id]);

  const appts = Array.isArray(myAppointments) && myAppointments.length ? myAppointments : DEMO_APPTS;
  const today = new Date().toISOString().split('T')[0];
  const todayAppts = appts.filter(a => a.appointmentDate === today);
  const upcomingAppts = appts.filter(a => a.appointmentDate >= today);

  return (
    <>
      <PageHeader
        title={`سلام، ${user?.firstName || user?.username || 'دکتر'}`}
        subtitle="پنل مدیریت پزشک"
        actions={<Button icon={Calendar}>برنامه هفتگی</Button>}
      />

      {/* Stats */}
      <div className="grid-stats mb-24">
        {[
          { i: CalendarDays, l: 'نوبت امروز', v: todayAppts.length },
          { i: Users, l: 'کل بیماران', v: appts.length },
          { i: TrendingUp, l: 'نوبت آینده', v: upcomingAppts.length },
          { i: CheckCircle2, l: 'تکمیل‌شده', v: appts.filter(a => a.status === 'COMPLETED').length },
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
                {appts.map((a) => (
                  <tr key={a.id}>
                    <td>
                      <div className="avatar-cell">
                        <Avatar name={a.patient?.fullName || 'بیمار'} size="sm" />
                        <span className="cell-strong">{a.patient?.fullName || '—'}</span>
                      </div>
                    </td>
                    <td className="cell-sub">{faDate(a.appointmentDate)}</td>
                    <td><StatusBadge group="appointmentStatus" value={a.status} /></td>
                    <td>
                      <div className="flex gap-8">
                        <Link to={`/app/doctor/patient/${a.patient?.id || a.id}`}>
                          <Button variant="ghost" size="sm" icon={FileText}>پرونده</Button>
                        </Link>
                        <Link to={`/app/doctor/visit/${a.id}`}>
                          <Button variant="soft" size="sm" icon={Stethoscope}>ویزیت</Button>
                        </Link>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      ) : (
        <div className="grid-cards-3 stagger">
          {(tab === 'today' ? todayAppts : upcomingAppts).map((a) => (
            <Card key={a.id} hover>
              <div className="card-body">
                <div className="flex items-center justify-between mb-16">
                  <span className="flex items-center gap-4 text-muted text-sm"><Clock size={15} />{toFa(a.startTime?.slice(0, 5))}</span>
                  <StatusBadge group="appointmentStatus" value={a.status} />
                </div>
                <div className="avatar-cell mb-8">
                  <Avatar name={a.patient?.fullName || 'بیمار'} size="sm" />
                  <span className="cell-strong">{a.patient?.fullName || '—'}</span>
                </div>
                <div className="text-sm text-muted">{a.reason || '—'}</div>
                <div className="text-xs text-muted mt-8">{faDate(a.appointmentDate)}</div>
                <div className="flex gap-8 mt-16">
                  <Link to={`/app/doctor/visit/${a.id}`} style={{ flex: 1 }}>
                    <Button variant="soft" size="sm" className="flex-1" style={{ width: '100%' }}>
                      شروع ویزیت
                    </Button>
                  </Link>
                </div>
              </div>
            </Card>
          ))}
          {(tab === 'today' ? todayAppts : upcomingAppts).length === 0 && (
            <Card className="card-body"><EmptyState icon={CalendarDays} title="نوبتی در این بازه نیست" /></Card>
          )}
        </div>
      )}
    </>
  );
}
