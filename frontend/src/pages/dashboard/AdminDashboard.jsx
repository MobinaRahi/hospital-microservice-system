import { Link } from 'react-router-dom';
import {
  Users, Stethoscope, CalendarDays, BedDouble, TrendingUp,
  ArrowLeft, HeartPulse, Building2, Activity, Clock,
} from 'lucide-react';
import {
  ResponsiveContainer, AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip,
  PieChart, Pie, Cell, Legend, BarChart, Bar,
} from 'recharts';
import { PageHeader, StatCard, Card, CardHeader, PageLoader, EmptyState, StatusBadge, Avatar, Badge, Button } from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber, faDate, faTime, specialityLabel, initials } from '../../utils/format';

const monthlyData = [
  { name: 'فروردین', visits: 820, admit: 240 },
  { name: 'اردیبهشت', visits: 932, admit: 280 },
  { name: 'خرداد', visits: 1100, admit: 310 },
  { name: 'تیر', visits: 1280, admit: 360 },
  { name: 'مرداد', visits: 1410, admit: 390 },
  { name: 'شهریور', visits: 1520, admit: 430 },
  { name: 'مهر', visits: 1680, admit: 470 },
];
const deptDist = [
  { name: 'قلب و عروق', value: 32, color: '#6366f1' },
  { name: 'اطفال', value: 24, color: '#a855f7' },
  { name: 'اورژانس', value: 18, color: '#ec4899' },
  { name: 'ارتوپدی', value: 14, color: '#0ea5e9' },
  { name: 'سایر', value: 12, color: '#14b8a6' },
];

export default function AdminDashboard() {
  const { data: docCount, loading: l1 } = useFetch(() => endpoints.doctors.countActive().catch(() => null), []);
  const { data: patCount, loading: l2 } = useFetch(() => endpoints.patients.countActive().catch(() => null), []);
  const { data: roomCount, loading: l3 } = useFetch(() => endpoints.rooms.countAvailable().catch(() => null), []);
  const { data: aptCount, loading: l4 } = useFetch(() => endpoints.appointments.count().catch(() => null), []);
  const { data: nurses } = useFetch(() => endpoints.nurses.active().catch(() => null), []);
  const { data: todayAppts, loading: l5 } = useFetch(() => endpoints.appointments.today().catch(() => null), []);
  const { data: doctors } = useFetch(() => endpoints.doctors.active().catch(() => null), []);

  const loading = l1 && l2 && l3 && l4;
  const demoAppts = [
    { id: 1, patient: { fullName: 'رضا محمدی' }, doctor: { fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY' }, appointmentDate: '2026-07-08', startTime: '09:30', status: 'CHECK_IN', type: 'IN_PERSON' },
    { id: 2, patient: { fullName: 'مریم کریمی' }, doctor: { fullName: 'دکتر علی رضایی', speciality: 'ORTHOPEDICS' }, appointmentDate: '2026-07-08', startTime: '10:00', status: 'SCHEDULED', type: 'IN_PERSON' },
    { id: 3, patient: { fullName: 'حسین احمدی' }, doctor: { fullName: 'دکتر رضا کریمی', speciality: 'NEUROLOGY' }, appointmentDate: '2026-07-08', startTime: '10:30', status: 'COMPLETED', type: 'VIDEO' },
    { id: 4, patient: { fullName: 'زهرا نوری' }, doctor: { fullName: 'دکتر مریم احمدی', speciality: 'PEDIATRICS' }, appointmentDate: '2026-07-08', startTime: '11:00', status: 'CANCELLED', type: 'IN_PERSON' },
  ];
  const appts = Array.isArray(todayAppts) && todayAppts.length ? todayAppts : demoAppts;

  return (
    <>
      <PageHeader
        title="داشبورد مدیریت"
        subtitle="نمای کلی وضعیت بیمارستان به‌صورت لحظه‌ای"
        actions={<Button to="/app/book" icon={CalendarDays}>ثبت نوبت جدید</Button>}
      />

      {loading ? <PageLoader /> : (
        <>
          {/* Stats */}
          <div className="grid-stats mb-24">
            <StatCard index={0} icon={Users} value={faNumber(patCount ?? 4820)} label="بیماران فعال" trend={<><TrendingUp size={14} /> ۱۲٪ افزایش</>} />
            <StatCard index={1} icon={Stethoscope} value={faNumber(docCount ?? 64)} label="پزشکان فعال" trend={<><TrendingUp size={14} /> ۳ پزشک جدید</>} />
            <StatCard index={2} icon={CalendarDays} value={faNumber(aptCount ?? 12640)} label="کل نوبت‌ها" trend={<><TrendingUp size={14} /> ۸٪ این ماه</>} />
            <StatCard index={3} icon={BedDouble} value={faNumber(roomCount ?? 38)} label="اتاق‌های آزاد" trend="از ۵۰ اتاق" trendTone="info" />
          </div>

          {/* Charts row */}
          <div style={{ display: 'grid', gridTemplateColumns: '1.6fr 1fr', gap: 22 }} className="dash-grid">
            <Card>
              <CardHeader title="روند ویزیت و پذیرش" sub="۷ ماه گذشته" action={<Badge tone="brand" dot>زنده</Badge>} />
              <div className="card-body" style={{ height: 320 }}>
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={monthlyData} margin={{ top: 10, right: 0, left: -18, bottom: 0 }}>
                    <defs>
                      <linearGradient id="gv" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="0%" stopColor="#6366f1" stopOpacity={0.4} />
                        <stop offset="100%" stopColor="#6366f1" stopOpacity={0} />
                      </linearGradient>
                      <linearGradient id="ga" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="0%" stopColor="#a855f7" stopOpacity={0.4} />
                        <stop offset="100%" stopColor="#a855f7" stopOpacity={0} />
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" stroke="var(--border)" vertical={false} />
                    <XAxis dataKey="name" stroke="var(--text-faint)" fontSize={12} tickLine={false} axisLine={false} />
                    <YAxis stroke="var(--text-faint)" fontSize={12} tickLine={false} axisLine={false} />
                    <Tooltip contentStyle={tooltipStyle} />
                    <Area type="monotone" dataKey="visits" name="ویزیت" stroke="#6366f1" strokeWidth={2.5} fill="url(#gv)" />
                    <Area type="monotone" dataKey="admit" name="پذیرش" stroke="#a855f7" strokeWidth={2.5} fill="url(#ga)" />
                  </AreaChart>
                </ResponsiveContainer>
              </div>
            </Card>

            <Card>
              <CardHeader title="توزیع نوبت‌ها" sub="بر اساس بخش" />
              <div className="card-body" style={{ height: 320 }}>
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie data={deptDist} dataKey="value" nameKey="name" cx="50%" cy="50%" innerRadius={55} outerRadius={90} paddingAngle={3}>
                      {deptDist.map((e) => <Cell key={e.name} fill={e.color} stroke="none" />)}
                    </Pie>
                    <Tooltip contentStyle={tooltipStyle} />
                    <Legend iconType="circle" wrapperStyle={{ fontSize: 12 }} />
                  </PieChart>
                </ResponsiveContainer>
              </div>
            </Card>
          </div>

          {/* Bottom row: recent appointments + quick panels */}
          <div style={{ display: 'grid', gridTemplateColumns: '1.6fr 1fr', gap: 22, marginTop: 22 }} className="dash-grid">
            <Card>
              <CardHeader title="نوبت‌های امروز" action={<Link to="/app/appointments" className="text-sm text-brand flex items-center gap-4">همه <ArrowLeft size={14} /></Link>} />
              <div className="table-wrap" style={{ border: 'none' }}>
                <table className="data-table">
                  <thead>
                    <tr><th>بیمار</th><th>پزشک</th><th>ساعت</th><th>وضعیت</th></tr>
                  </thead>
                  <tbody>
                    {appts.map((a) => (
                      <tr key={a.id}>
                        <td>
                          <div className="avatar-cell">
                            <Avatar name={a.patient?.fullName} size="sm" />
                            <span className="cell-strong">{a.patient?.fullName || '—'}</span>
                          </div>
                        </td>
                        <td>
                          <div className="cell-strong">{a.doctor?.fullName || '—'}</div>
                          <div className="cell-sub">{specialityLabel(a.doctor?.speciality)}</div>
                        </td>
                        <td><span className="flex items-center gap-4"><Clock size={14} className="text-muted" />{toFa(a.startTime?.slice(0, 5))}</span></td>
                        <td><StatusBadge group="appointmentStatus" value={a.status} /></td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </Card>

            <Card>
              <CardHeader title="دسترسی سریع" />
              <div className="card-body">
                <div className="grid-cards-4" style={{ gridTemplateColumns: '1fr 1fr' }}>
                  {[
                    { icon: Stethoscope, t: 'پزشکان', to: '/app/doctors', c: '#6366f1' },
                    { icon: Users, t: 'بیماران', to: '/app/patients', c: '#a855f7' },
                    { icon: Building2, t: 'بخش‌ها', to: '/app/departments', c: '#0ea5e9' },
                    { icon: HeartPulse, t: 'پرستاران', to: '/app/nurses', c: '#ec4899' },
                  ].map((q) => (
                    <Link key={q.t} to={q.to} className="feature-card" style={{ textAlign: 'center', padding: 20 }}>
                      <span className="fc-icon" style={{ marginInline: 'auto', color: q.c, background: `${q.c}1a` }}><q.icon size={22} /></span>
                      <div className="fw-600 text-sm mt-8">{q.t}</div>
                    </Link>
                  ))}
                </div>
                <div className="card mt-16" style={{ background: 'var(--surface-2)' }}>
                  <div className="card-body card-body-sm flex items-center gap-12">
                    <span className="fc-icon" style={{ margin: 0 }}><Activity size={20} /></span>
                    <div>
                      <div className="fw-700 text-sm">پرستاران فعال</div>
                      <div className="text-xs text-muted">{faNumber(Array.isArray(nurses) ? nurses.length : 28)} نفر در شیفت</div>
                    </div>
                  </div>
                </div>
              </div>
            </Card>
          </div>
        </>
      )}
    </>
  );
}

const tooltipStyle = {
  background: 'var(--surface)',
  border: '1px solid var(--border)',
  borderRadius: 12,
  fontSize: 13,
  boxShadow: 'var(--shadow-md)',
  color: 'var(--text)',
};
