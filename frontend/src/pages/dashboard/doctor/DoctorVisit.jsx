import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Stethoscope, Search, Plus, Trash2, Save, CheckCircle2,
  FileText, Pill, Activity, ArrowLeft, Clock, AlertTriangle,
  HeartPulse, Thermometer, Wind, Droplet,
} from 'lucide-react';
import {
  PageHeader, Card, CardHeader, Button, Avatar, Badge, Spinner,
} from '../../../components/ui';
import { useFetch } from '../../../hooks/useFetch';
import { useAuth } from '../../../context/AuthContext';
import { endpoints } from '../../../api/endpoints';
import { toFa, faNumber, faDate } from '../../../utils/format';

// ==================== ICD-10 Search ====================
function IcdSearch({ onSelect }) {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showResults, setShowResults] = useState(false);

  useEffect(() => {
    if (!query || query.length < 2) { setResults([]); return; }
    const timer = setTimeout(async () => {
      setLoading(true);
      try {
        const res = await fetch(`/api/v1/codes/icd/search?query=${encodeURIComponent(query)}`);
        const data = await res.json();
        setResults(data.data || []);
        setShowResults(true);
      } catch { setResults([]); }
      finally { setLoading(false); }
    }, 300);
    return () => clearTimeout(timer);
  }, [query]);

  return (
    <div style={{ position: 'relative' }}>
      <div className="input-group has-icon">
        <span className="input-icon"><Search size={18} /></span>
        <input className="input" placeholder="جستجوی تشخیص (مثال: فشار خون، I10...)" value={query} onChange={(e) => setQuery(e.target.value)} />
      </div>
      {showResults && results.length > 0 && (
        <div style={{ position: 'absolute', top: '100%', left: 0, right: 0, zIndex: 10, background: 'var(--surface)', border: '1px solid var(--border)', borderRadius: '0 0 12px 12px', maxHeight: 200, overflow: 'auto', boxShadow: '0 8px 24px rgba(0,0,0,0.2)' }}>
          {results.map((item) => (
            <div key={item.code} onClick={() => { onSelect(item); setQuery(''); setShowResults(false); }}
              style={{ padding: '10px 16px', cursor: 'pointer', borderBottom: '1px solid var(--border)' }}
              onMouseEnter={(e) => e.currentTarget.style.background = 'var(--surface-2)'}
              onMouseLeave={(e) => e.currentTarget.style.background = 'transparent'}>
              <span className="fw-700" style={{ color: 'var(--brand)', marginRight: 8 }}>{item.code}</span>
              <span>{item.persianName || item.description}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

// ==================== Main Component ====================
export default function DoctorVisit() {
  const { id: appointmentId } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [diagnoses, setDiagnoses] = useState([]);
  const [prescriptions, setPrescriptions] = useState([]);
  const [vitals, setVitals] = useState({ bp: '', hr: '', temp: '', spo2: '' });
  const [chiefComplaint, setChiefComplaint] = useState('');
  const [doctorNotes, setDoctorNotes] = useState('');
  const [newDrug, setNewDrug] = useState({ drugName: '', dosage: '', frequency: '', duration: '', instructions: '' });
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);

  const addDiagnosis = (icd) => {
    if (diagnoses.find(d => d.icd10Code === icd.code)) return;
    setDiagnoses(prev => [...prev, { id: Date.now(), icd10Code: icd.code, name: icd.persianName || icd.description, primary: prev.length === 0 }]);
  };

  const addDrug = () => {
    if (!newDrug.drugName || !newDrug.dosage) return;
    setPrescriptions(prev => [...prev, { id: Date.now(), ...newDrug }]);
    setNewDrug({ drugName: '', dosage: '', frequency: '', duration: '', instructions: '' });
  };

  const handleSave = async () => {
    setSaving(true);
    // TODO: API calls
    setTimeout(() => { setSaving(false); setSaved(true); }, 1500);
  };

  if (saved) {
    return (
      <>
        <PageHeader title="ویزیت بیمار" />
        <Card>
          <div className="card-body" style={{ textAlign: 'center', padding: 60 }}>
            <CheckCircle2 size={64} style={{ color: '#10b981', marginBottom: 16 }} />
            <h2 className="text-xl fw-800 mb-8">ویزیت ثبت شد!</h2>
            <p className="text-muted mb-24">تشخیص: {diagnoses.map(d => d.name).join('، ')}</p>
            <Button onClick={() => navigate('/app/doctor')}>بازگشت به داشبورد</Button>
          </div>
        </Card>
      </>
    );
  }

  return (
    <>
      <PageHeader
        title="ویزیت بیمار"
        subtitle={`دکتر: ${user?.firstName || user?.username || 'پزشک'}`}
        actions={<>
          <Button variant="ghost" icon={ArrowLeft} onClick={() => navigate('/app/doctor')}>بازگشت</Button>
          <Button icon={saving ? Spinner : Save} onClick={handleSave} disabled={saving || diagnoses.length === 0}>
            {saving ? 'ذخیره...' : 'ثبت ویزیت'}
          </Button>
        </>}
      />

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 22 }} className="dash-grid">
        {/* RIGHT */}
        <div>
          {/* Chief Complaint */}
          <Card className="mb-24">
            <CardHeader title="شرح حال" icon={FileText} />
            <div className="card-body">
              <textarea className="textarea" placeholder="علت مراجعه..." value={chiefComplaint} onChange={(e) => setChiefComplaint(e.target.value)} rows={3} style={{ width: '100%' }} />
            </div>
          </Card>

          {/* Vitals */}
          <Card className="mb-24">
            <CardHeader title="علائم حیاتی" icon={Activity} />
            <div className="card-body">
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                {[
                  { icon: HeartPulse, label: 'فشار خون', key: 'bp', ph: '120/80' },
                  { icon: Activity, label: 'ضربان قلب', key: 'hr', ph: '72' },
                  { icon: Thermometer, label: 'دما', key: 'temp', ph: '37' },
                  { icon: Droplet, label: 'SpO2', key: 'spo2', ph: '98' },
                ].map(v => (
                  <div key={v.key}>
                    <label className="field-label"><v.icon size={14} /> {v.label}</label>
                    <input className="input" placeholder={v.ph} value={vitals[v.key]} onChange={(e) => setVitals({ ...vitals, [v.key]: e.target.value })} />
                  </div>
                ))}
              </div>
            </div>
          </Card>

          {/* Diagnosis */}
          <Card className="mb-24">
            <CardHeader title="تشخیص (ICD-10)" icon={Stethoscope} />
            <div className="card-body">
              <IcdSearch onSelect={addDiagnosis} />
              {diagnoses.map(d => (
                <div key={d.id} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '10px 14px', background: 'var(--surface-2)', borderRadius: 10, marginTop: 8, border: d.primary ? '2px solid var(--brand)' : '1px solid var(--border)' }}>
                  <span className="fw-700" style={{ color: 'var(--brand)', minWidth: 50 }}>{d.icd10Code}</span>
                  <span style={{ flex: 1 }}>{d.name}</span>
                  {d.primary && <Badge tone="success">اصلی</Badge>}
                  <button onClick={() => setDiagnoses(prev => prev.filter(x => x.id !== d.id))} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--danger)' }}><Trash2 size={16} /></button>
                </div>
              ))}
            </div>
          </Card>

          {/* Notes */}
          <Card>
            <CardHeader title="یادداشت پزشک" icon={FileText} />
            <div className="card-body">
              <textarea className="textarea" placeholder="توضیحات تکمیلی..." value={doctorNotes} onChange={(e) => setDoctorNotes(e.target.value)} rows={3} style={{ width: '100%' }} />
            </div>
          </Card>
        </div>

        {/* LEFT */}
        <div>
          {/* Prescription */}
          <Card className="mb-24">
            <CardHeader title="نسخه‌نویسی" icon={Pill} />
            <div className="card-body">
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginBottom: 12 }}>
                <div><label className="field-label">نام دارو *</label><input className="input" placeholder="آملودیپین" value={newDrug.drugName} onChange={e => setNewDrug({ ...newDrug, drugName: e.target.value })} /></div>
                <div><label className="field-label">دوز *</label><input className="input" placeholder="5mg" value={newDrug.dosage} onChange={e => setNewDrug({ ...newDrug, dosage: e.target.value })} /></div>
                <div><label className="field-label">تعداد دفعات *</label><input className="input" placeholder="روزی ۲ بار" value={newDrug.frequency} onChange={e => setNewDrug({ ...newDrug, frequency: e.target.value })} /></div>
                <div><label className="field-label">مدت</label><input className="input" placeholder="۷ روز" value={newDrug.duration} onChange={e => setNewDrug({ ...newDrug, duration: e.target.value })} /></div>
              </div>
              <Button variant="soft" icon={Plus} onClick={addDrug} disabled={!newDrug.drugName || !newDrug.dosage} style={{ width: '100%' }}>افزودن دارو</Button>
              {prescriptions.map(p => (
                <div key={p.id} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '10px 14px', background: 'var(--surface-2)', borderRadius: 10, marginTop: 8, border: '1px solid var(--border)' }}>
                  <Pill size={18} style={{ color: 'var(--brand-2)' }} />
                  <div style={{ flex: 1 }}>
                    <div className="fw-600">{p.drugName} <span className="text-muted">({p.dosage})</span></div>
                    <div className="text-xs text-muted">{p.frequency} {p.duration && `• ${p.duration}`}</div>
                  </div>
                  <button onClick={() => setPrescriptions(prev => prev.filter(x => x.id !== p.id))} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--danger)' }}><Trash2 size={16} /></button>
                </div>
              ))}
            </div>
          </Card>

          {/* Lab Request */}
          <Card className="mb-24">
            <CardHeader title="درخواست آزمایش" icon={Activity} />
            <div className="card-body" style={{ textAlign: 'center', padding: 30 }}>
              <Activity size={40} style={{ color: 'var(--text-muted)', opacity: 0.3, marginBottom: 12 }} />
              <p className="text-muted">این بخش به زودی فعال می‌شود</p>
              <p className="text-xs text-muted">اتصال به LabService</p>
            </div>
          </Card>

          {/* Summary */}
          <Card>
            <CardHeader title="خلاصه" icon={CheckCircle2} />
            <div className="card-body">
              <div style={{ display: 'grid', gap: 10 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}><span className="text-muted">تشخیص</span><span className="fw-600">{diagnoses.length}</span></div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}><span className="text-muted">دارو</span><span className="fw-600">{prescriptions.length}</span></div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}><span className="text-muted">شرح حال</span><span className="fw-600">{chiefComplaint ? '✓' : '—'}</span></div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}><span className="text-muted">علائم حیاتی</span><span className="fw-600">{vitals.bp || vitals.hr ? '✓' : '—'}</span></div>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </>
  );
}
