import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Stethoscope, Search, Plus, Trash2, Save, CheckCircle2,
  FileText, Pill, Activity, ArrowLeft, Clock, AlertTriangle,
} from 'lucide-react';
import {
  PageHeader, Card, CardHeader, Button, Avatar, Badge, StatusBadge, Spinner,
} from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { useAuth } from '../../context/AuthContext';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber, faDate, specialityLabel } from '../../utils/format';

// ==================== ICD-10 Search Component ====================
function IcdSearch({ onSelect }) {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showResults, setShowResults] = useState(false);

  const searchIcd = async (q) => {
    if (!q || q.length < 2) { setResults([]); return; }
    setLoading(true);
    try {
      const res = await fetch(`/api/v1/codes/icd/search?query=${encodeURIComponent(q)}`);
      const data = await res.json();
      setResults(data.data || []);
      setShowResults(true);
    } catch {
      setResults([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => searchIcd(query), 300);
    return () => clearTimeout(timer);
  }, [query]);

  return (
    <div style={{ position: 'relative' }}>
      <div className="input-group has-icon">
        <span className="input-icon"><Search size={18} /></span>
        <input
          className="input"
          placeholder="جستجوی تشخیص (مثال: فشار خون، دیابت، I10...)"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onFocus={() => results.length > 0 && setShowResults(true)}
        />
        {loading && <span style={{ position: 'absolute', right: 12, top: '50%', transform: 'translateY(-50%)' }}><Spinner /></span>}
      </div>
      {showResults && results.length > 0 && (
        <div style={{
          position: 'absolute', top: '100%', left: 0, right: 0, zIndex: 10,
          background: 'var(--surface)', border: '1px solid var(--border)',
          borderRadius: '0 0 12px 12px', maxHeight: 250, overflow: 'auto',
          boxShadow: '0 8px 24px rgba(0,0,0,0.2)',
        }}>
          {results.map((item) => (
            <div
              key={item.code}
              onClick={() => { onSelect(item); setQuery(''); setShowResults(false); }}
              style={{
                padding: '10px 16px', cursor: 'pointer', borderBottom: '1px solid var(--border)',
                transition: '0.15s',
              }}
              onMouseEnter={(e) => e.currentTarget.style.background = 'var(--surface-2)'}
              onMouseLeave={(e) => e.currentTarget.style.background = 'transparent'}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <span className="fw-700" style={{ color: 'var(--brand)', marginRight: 8 }}>{item.code}</span>
                  <span>{item.persianName || item.description}</span>
                </div>
                <span className="badge badge-neutral" style={{ fontSize: '0.7rem' }}>{item.category}</span>
              </div>
              {item.description !== item.persianName && (
                <div className="text-xs text-muted" style={{ marginTop: 2 }}>{item.description}</div>
              )}
            </div>
          ))}
        </div>
      )}
      {showResults && query.length >= 2 && results.length === 0 && !loading && (
        <div style={{
          position: 'absolute', top: '100%', left: 0, right: 0, zIndex: 10,
          background: 'var(--surface)', border: '1px solid var(--border)',
          borderRadius: '0 0 12px 12px', padding: '16px', textAlign: 'center',
          boxShadow: '0 8px 24px rgba(0,0,0,0.2)',
        }}>
          <span className="text-muted">نتیجه‌ای یافت نشد</span>
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

  // ==================== State ====================
  const [encounter, setEncounter] = useState(null);
  const [diagnoses, setDiagnoses] = useState([]);
  const [prescriptions, setPrescriptions] = useState([]);
  const [chiefComplaint, setChiefComplaint] = useState('');
  const [doctorNotes, setDoctorNotes] = useState('');
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);

  // New prescription item state
  const [newDrug, setNewDrug] = useState({ drugName: '', dosage: '', frequency: '', duration: '', instructions: '' });

  // ==================== Handlers ====================

  // Add diagnosis from ICD-10 search
  const addDiagnosis = (icdItem) => {
    const exists = diagnoses.find(d => d.icd10Code === icdItem.code);
    if (exists) return;
    setDiagnoses(prev => [...prev, {
      id: Date.now(),
      icd10Code: icdItem.code,
      name: icdItem.persianName || icdItem.description,
      description: icdItem.description,
      primary: prev.length === 0, // First one is primary
    }]);
  };

  // Remove diagnosis
  const removeDiagnosis = (id) => {
    setDiagnoses(prev => {
      const filtered = prev.filter(d => d.id !== id);
      // Make first remaining diagnosis primary
      if (filtered.length > 0 && !filtered.some(d => d.primary)) {
        filtered[0].primary = true;
      }
      return filtered;
    });
  };

  // Toggle primary diagnosis
  const togglePrimary = (id) => {
    setDiagnoses(prev => prev.map(d => ({ ...d, primary: d.id === id })));
  };

  // Add prescription item
  const addPrescriptionItem = () => {
    if (!newDrug.drugName || !newDrug.dosage || !newDrug.frequency) return;
    setPrescriptions(prev => [...prev, { id: Date.now(), ...newDrug }]);
    setNewDrug({ drugName: '', dosage: '', frequency: '', duration: '', instructions: '' });
  };

  // Remove prescription item
  const removePrescriptionItem = (id) => {
    setPrescriptions(prev => prev.filter(p => p.id !== id));
  };

  // Save encounter
  const handleSave = async () => {
    setSaving(true);
    // TODO: Call API to save encounter, diagnoses, prescriptions
    // POST /api/v1/encounters
    // POST /api/v1/diagnoses (for each)
    // POST /api/v1/prescriptions
    setTimeout(() => {
      setSaving(false);
      setSaved(true);
    }, 1500);
  };

  // ==================== Success Screen ====================
  if (saved) {
    return (
      <>
        <PageHeader title="ویزیت بیمار" />
        <Card>
          <div className="card-body" style={{ textAlign: 'center', padding: 60 }}>
            <div style={{
              width: 80, height: 80, margin: '0 auto 20px', borderRadius: '50%',
              background: '#10b98122', display: 'grid', placeItems: 'center',
            }}>
              <CheckCircle2 size={48} style={{ color: '#10b981' }} />
            </div>
            <h2 className="text-xl fw-800 mb-8">ویزیت با موفقیت ثبت شد!</h2>
            <p className="text-muted mb-24">
              تشخیص: {diagnoses.map(d => d.name).join('، ')}
            </p>
            <p className="text-muted mb-24">
              تعداد داروها: {faNumber(prescriptions.length)} قلم
            </p>
            <div className="flex gap-8" style={{ justifyContent: 'center' }}>
              <Button onClick={() => { setSaved(false); setDiagnoses([]); setPrescriptions([]); setChiefComplaint(''); setDoctorNotes(''); }}>
                ویزیت جدید
              </Button>
              <Button variant="ghost" onClick={() => navigate('/app/doctor')}>
                بازگشت به داشبورد
              </Button>
            </div>
          </div>
        </Card>
      </>
    );
  }

  // ==================== Main Form ====================
  return (
    <>
      <PageHeader
        title="ویزیت بیمار"
        subtitle={`دکتر: ${user?.firstName || user?.username || 'پزشک'}`}
        actions={
          <div className="flex gap-8">
            <Button variant="ghost" icon={ArrowLeft} onClick={() => navigate('/app/doctor')}>
              بازگشت
            </Button>
            <Button icon={saving ? Spinner : Save} onClick={handleSave} disabled={saving || diagnoses.length === 0}>
              {saving ? 'در حال ذخیره...' : 'ثبت ویزیت'}
            </Button>
          </div>
        }
      />

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 22 }} className="dash-grid">
        {/* ==================== LEFT COLUMN ==================== */}
        <div>
          {/* Chief Complaint */}
          <Card className="mb-24">
            <CardHeader title="شرح حال بیمار" icon={FileText} />
            <div className="card-body">
              <textarea
                className="textarea"
                placeholder="علت مراجعه و شرح حال بیمار را بنویسید..."
                value={chiefComplaint}
                onChange={(e) => setChiefComplaint(e.target.value)}
                rows={4}
                style={{ width: '100%', resize: 'vertical' }}
              />
            </div>
          </Card>

          {/* ICD-10 Diagnosis */}
          <Card className="mb-24">
            <CardHeader title="تشخیص (ICD-10)" icon={Stethoscope} />
            <div className="card-body">
              <IcdSearch onSelect={addDiagnosis} />

              {/* Selected Diagnoses */}
              {diagnoses.length > 0 && (
                <div style={{ marginTop: 16 }}>
                  {diagnoses.map((d) => (
                    <div key={d.id} style={{
                      display: 'flex', alignItems: 'center', gap: 12,
                      padding: '12px 16px', background: 'var(--surface-2)',
                      borderRadius: 10, marginBottom: 8, border: d.primary ? '2px solid var(--brand)' : '1px solid var(--border)',
                    }}>
                      <span className="fw-700" style={{ color: 'var(--brand)', minWidth: 50 }}>{d.icd10Code}</span>
                      <div style={{ flex: 1 }}>
                        <div className="fw-600">{d.name}</div>
                        {d.description !== d.name && (
                          <div className="text-xs text-muted">{d.description}</div>
                        )}
                      </div>
                      {d.primary ? (
                        <Badge tone="success">اصلی</Badge>
                      ) : (
                        <button
                          onClick={() => togglePrimary(d.id)}
                          className="text-xs"
                          style={{ color: 'var(--brand)', cursor: 'pointer', background: 'none', border: 'none' }}
                        >
                          اصلی کن
                        </button>
                      )}
                      <button
                        onClick={() => removeDiagnosis(d.id)}
                        style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--danger)' }}
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  ))}
                </div>
              )}

              {diagnoses.length === 0 && (
                <div style={{ textAlign: 'center', padding: 20, color: 'var(--text-muted)' }} className="mt-16">
                  <Search size={32} style={{ opacity: 0.3, marginBottom: 8 }} />
                  <p className="text-sm">تشخیص را از لیست بالا انتخاب کنید</p>
                </div>
              )}
            </div>
          </Card>

          {/* Doctor Notes */}
          <Card>
            <CardHeader title="یادداشت پزشک" icon={FileText} />
            <div className="card-body">
              <textarea
                className="textarea"
                placeholder="توضیحات تکمیلی، دستورات غذایی، توصیه‌ها..."
                value={doctorNotes}
                onChange={(e) => setDoctorNotes(e.target.value)}
                rows={3}
                style={{ width: '100%', resize: 'vertical' }}
              />
            </div>
          </Card>
        </div>

        {/* ==================== RIGHT COLUMN ==================== */}
        <div>
          {/* Prescription */}
          <Card className="mb-24">
            <CardHeader title="نسخه‌نویسی" icon={Pill} />
            <div className="card-body">
              {/* Add Drug Form */}
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginBottom: 16 }}>
                <div>
                  <label className="field-label">نام دارو <span className="req">*</span></label>
                  <input
                    className="input"
                    placeholder="مثال: آملودیپین"
                    value={newDrug.drugName}
                    onChange={(e) => setNewDrug({ ...newDrug, drugName: e.target.value })}
                  />
                </div>
                <div>
                  <label className="field-label">دوز <span className="req">*</span></label>
                  <input
                    className="input"
                    placeholder="مثال: 5mg"
                    value={newDrug.dosage}
                    onChange={(e) => setNewDrug({ ...newDrug, dosage: e.target.value })}
                  />
                </div>
                <div>
                  <label className="field-label">تعداد دفعات <span className="req">*</span></label>
                  <input
                    className="input"
                    placeholder="مثال: روزی ۲ بار"
                    value={newDrug.frequency}
                    onChange={(e) => setNewDrug({ ...newDrug, frequency: e.target.value })}
                  />
                </div>
                <div>
                  <label className="field-label">مدت مصرف</label>
                  <input
                    className="input"
                    placeholder="مثال: ۷ روز"
                    value={newDrug.duration}
                    onChange={(e) => setNewDrug({ ...newDrug, duration: e.target.value })}
                  />
                </div>
                <div style={{ gridColumn: '1 / -1' }}>
                  <label className="field-label">دستور مصرف</label>
                  <input
                    className="input"
                    placeholder="مثال: بعد از غذا با آب فراوان"
                    value={newDrug.instructions}
                    onChange={(e) => setNewDrug({ ...newDrug, instructions: e.target.value })}
                  />
                </div>
              </div>
              <Button
                variant="soft"
                icon={Plus}
                onClick={addPrescriptionItem}
                disabled={!newDrug.drugName || !newDrug.dosage || !newDrug.frequency}
                style={{ width: '100%' }}
              >
                افزودن دارو
              </Button>

              {/* Prescription Items */}
              {prescriptions.length > 0 && (
                <div style={{ marginTop: 16 }}>
                  {prescriptions.map((p) => (
                    <div key={p.id} style={{
                      display: 'flex', alignItems: 'center', gap: 12,
                      padding: '12px 16px', background: 'var(--surface-2)',
                      borderRadius: 10, marginBottom: 8, border: '1px solid var(--border)',
                    }}>
                      <Pill size={20} style={{ color: 'var(--brand-2)', flexShrink: 0 }} />
                      <div style={{ flex: 1 }}>
                        <div className="fw-600">{p.drugName} <span className="text-muted">({p.dosage})</span></div>
                        <div className="text-xs text-muted">
                          {p.frequency} {p.duration && `• ${p.duration}`} {p.instructions && `• ${p.instructions}`}
                        </div>
                      </div>
                      <button
                        onClick={() => removePrescriptionItem(p.id)}
                        style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--danger)' }}
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  ))}
                </div>
              )}

              {prescriptions.length === 0 && (
                <div style={{ textAlign: 'center', padding: 20, color: 'var(--text-muted)' }} className="mt-16">
                  <Pill size={32} style={{ opacity: 0.3, marginBottom: 8 }} />
                  <p className="text-sm">دارویی اضافه نشده</p>
                </div>
              )}
            </div>
          </Card>

          {/* Summary */}
          <Card>
            <CardHeader title="خلاصه ویزیت" icon={ClipboardList} />
            <div className="card-body">
              <div style={{ display: 'grid', gap: 12 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span className="text-muted">تشخیص‌ها</span>
                  <span className="fw-600">{faNumber(diagnoses.length)} مورد</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span className="text-muted">داروها</span>
                  <span className="fw-600">{faNumber(prescriptions.length)} قلم</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span className="text-muted">شرح حال</span>
                  <span className="fw-600">{chiefComplaint ? 'ثبت شده' : 'خالی'}</span>
                </div>
              </div>

              {diagnoses.length === 0 && (
                <div style={{
                  marginTop: 16, padding: '12px', background: '#f59e0b22',
                  borderRadius: 10, display: 'flex', alignItems: 'center', gap: 8,
                }}>
                  <AlertTriangle size={18} style={{ color: 'var(--warning)' }} />
                  <span className="text-sm" style={{ color: 'var(--warning)' }}>حداقل یک تشخیص الزامی است</span>
                </div>
              )}
            </div>
          </Card>
        </div>
      </div>
    </>
  );
}
