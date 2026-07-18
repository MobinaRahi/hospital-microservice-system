import { useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import {
  User, FileText, Pill, Activity, CalendarDays, Clock,
  ArrowLeft, AlertTriangle, HeartPulse, Thermometer,
} from 'lucide-react';
import {
  PageHeader, Card, CardHeader, Button, Avatar, Badge, StatusBadge, EmptyState,
} from '../../../components/ui';
import { useFetch } from '../../../hooks/useFetch';
import { endpoints } from '../../../api/endpoints';
import { toFa, faNumber, faDate, specialityLabel } from '../../../utils/format';

const DEMO_PATIENT = { id: 1, fullName: 'رضا محمدی', age: 34, gender: 'مرد', bloodType: 'A+', phone: '09121234567' };
const DEMO_ENCOUNTERS = [
  { id: 1, encounterDate: '2026-07-10', type: 'OUTPATIENT', status: 'COMPLETED', diagnoses: [{ name: 'فشار خون بالا', icd10Code: 'I10' }], prescriptions: [{ drugName: 'آملودیپین', dosage: '5mg' }] },
  { id: 2, encounterDate: '2026-06-25', type: 'OUTPATIENT', status: 'COMPLETED', diagnoses: [{ name: 'دیابت نوع دو', icd10Code: 'E11' }], prescriptions: [{ drugName: 'متفورمین', dosage: '500mg' }] },
];
const DEMO_ALLERGIES = [
  { id: 1, allergenName: 'پنی‌سیلین', severity: 'SEVERE', type: 'DRUG' },
  { id: 2, allergenName: 'بادام زمینی', severity: 'MODERATE', type: 'FOOD' },
];

export default function PatientRecord() {
  const { id: patientId } = useParams();
  const navigate = useNavigate();

  const [tab, setTab] = useState('history');

  const patient = DEMO_PATIENT;
  const encounters = DEMO_ENCOUNTERS;
  const allergies = DEMO_ALLERGIES;

  return (
    <>
      <PageHeader
        title="پرونده بیمار"
        actions={<Button variant="ghost" icon={ArrowLeft} onClick={() => navigate('/app/doctor')}>بازگشت</Button>}
      />

      {/* Patient Info */}
      <Card className="mb-24">
        <div className="card-body flex items-center gap-24" style={{ flexWrap: 'wrap' }}>
          <span className="avatar avatar-lg" style={{ background: 'linear-gradient(135deg, #0ea5e9, #14b8a6)' }}>
            {initials(patient.fullName)}
          </span>
          <div style={{ flex: 1 }}>
            <h2 className="text-xl fw-800">{patient.fullName}</h2>
            <div className="flex items-center gap-12 mt-8" style={{ flexWrap: 'wrap' }}>
              <Badge tone="neutral">{patient.age} ساله</Badge>
              <Badge tone="neutral">{patient.gender}</Badge>
              <Badge tone="danger">گروه خونی {patient.bloodType}</Badge>
            </div>
          </div>
          <div className="flex gap-16">
            <Link to={`/app/doctor/visit/new?patientId=${patientId}`}>
              <Button icon={FileText}>ویزیت جدید</Button>
            </Link>
          </div>
        </div>
      </Card>

      {/* Allergies Alert */}
      {allergies.length > 0 && (
        <Card className="mb-24" style={{ border: '2px solid var(--danger)' }}>
          <div className="card-body flex items-center gap-16">
            <AlertTriangle size={24} style={{ color: 'var(--danger)', flexShrink: 0 }} />
            <div>
              <div className="fw-700" style={{ color: 'var(--danger)' }}>⚠️ حساسیت‌های بیمار</div>
              <div className="flex gap-8 mt-8" style={{ flexWrap: 'wrap' }}>
                {allergies.map(a => (
                  <Badge key={a.id} tone="danger">{a.allergenName} ({a.severity === 'SEVERE' ? 'شدید' : 'متوسط'})</Badge>
                ))}
              </div>
            </div>
          </div>
        </Card>
      )}

      {/* Tabs */}
      <div className="segmented mb-24">
        {[
          ['history', 'سابقه ویزیت‌ها'],
          ['prescriptions', 'نسخه‌ها'],
          ['vitals', 'علائم حیاتی'],
        ].map(([v, l]) => (
          <button key={v} className={tab === v ? 'active' : ''} onClick={() => setTab(v)}>{l}</button>
        ))}
      </div>

      {/* History Tab */}
      {tab === 'history' && (
        <div>
          {encounters.map(enc => (
            <Card key={enc.id} className="mb-16" hover>
              <div className="card-body">
                <div className="flex items-center justify-between mb-12">
                  <div className="flex items-center gap-8">
                    <CalendarDays size={16} className="text-muted" />
                    <span className="fw-600">{faDate(enc.encounterDate)}</span>
                    <Badge tone={enc.status === 'COMPLETED' ? 'success' : 'info'}>
                      {enc.status === 'COMPLETED' ? 'تکمیل شده' : 'در حال انجام'}
                    </Badge>
                  </div>
                  <Badge tone="neutral">{enc.type === 'OUTPATIENT' ? 'سرپایی' : 'بستری'}</Badge>
                </div>
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
                  <div>
                    <div className="text-xs text-muted mb-4">تشخیص</div>
                    {enc.diagnoses.map((d, i) => (
                      <div key={i} className="fw-600">
                        <span style={{ color: 'var(--brand)' }}>{d.icd10Code}</span> {d.name}
                      </div>
                    ))}
                  </div>
                  <div>
                    <div className="text-xs text-muted mb-4">نسخه</div>
                    {enc.prescriptions.map((p, i) => (
                      <div key={i} className="fw-600">
                        <Pill size={14} style={{ marginRight: 4 }} /> {p.drugName} ({p.dosage})
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </Card>
          ))}
          {encounters.length === 0 && <EmptyState icon={CalendarDays} title="سابقه ویزیتی وجود ندارد" />}
        </div>
      )}

      {/* Prescriptions Tab */}
      {tab === 'prescriptions' && (
        <Card>
          <div className="table-wrap" style={{ border: 'none' }}>
            <table className="data-table">
              <thead><tr><th>دارو</th><th>دوز</th><th>تاریخ</th><th>وضعیت</th></tr></thead>
              <tbody>
                {encounters.flatMap(enc => enc.prescriptions.map(p => ({ ...p, date: enc.encounterDate }))).map((p, i) => (
                  <tr key={i}>
                    <td><div className="flex items-center gap-8"><Pill size={16} className="text-muted" /><span className="fw-600">{p.drugName}</span></div></td>
                    <td>{p.dosage}</td>
                    <td className="cell-sub">{faDate(p.date)}</td>
                    <td><Badge tone="success">فعال</Badge></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      )}

      {/* Vitals Tab */}
      {tab === 'vitals' && (
        <Card>
          <div className="card-body" style={{ textAlign: 'center', padding: 40 }}>
            <Activity size={48} style={{ color: 'var(--text-muted)', opacity: 0.3, marginBottom: 12 }} />
            <p className="text-muted">تاریخچه علائم حیاتی به زودی اضافه می‌شود</p>
          </div>
        </Card>
      )}
    </>
  );
}

function initials(name = '') {
  const parts = name.trim().split(/\s+/).filter(Boolean);
  if (!parts.length) return '؟';
  return parts.length === 1 ? parts[0].slice(0, 2) : (parts[0][0] || '') + (parts[1][0] || '');
}
