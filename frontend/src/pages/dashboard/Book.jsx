import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import {
  Building2, Stethoscope, CalendarDays, User, CheckCircle2,
  ChevronLeft, ChevronRight, Clock, ArrowRight,
} from 'lucide-react';
import { PageHeader, Card, CardHeader, Button, Avatar, Badge } from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber, specialityLabel } from '../../utils/format';

const STEPS = ['بخش', 'پزشک', 'تاریخ و ساعت', 'اطلاعات', 'تأیید'];
const DEMO_DOCS = [
  { id: 1, fullName: 'دکتر سارا محمدی', speciality: 'CARDIOLOGY', departmentId: 1, department: { departmentName: 'قلب و عروق' } },
  { id: 2, fullName: 'دکتر رضا کریمی', speciality: 'NEUROLOGY', departmentId: 2, department: { departmentName: 'مغز و اعصاب' } },
  { id: 3, fullName: 'دکتر مریم احمدی', speciality: 'PEDIATRICS', departmentId: 4, department: { departmentName: 'اطفال' } },
];
const DEMO_DEPTS = [
  { id: 1, departmentName: 'قلب و عروق', departmentCode: 'CARD' }, { id: 2, departmentName: 'مغز و اعصاب', departmentCode: 'NEUR' },
  { id: 3, departmentName: 'اطفال', departmentCode: 'PEDS' }, { id: 4, departmentName: 'ارتوپدی', departmentCode: 'ORTH' },
];
const SLOTS = ['09:00', '09:30', '10:00', '10:30', '11:00', '14:00', '14:30', '16:00'];

export default function Book() {
  const [searchParams] = useSearchParams();
  const [step, setStep] = useState(0);
  const [dept, setDept] = useState(null);
  const [doctor, setDoctor] = useState(null);
  const [date, setDate] = useState('');
  const [slot, setSlot] = useState('');
  const [info, setInfo] = useState({ name: '', phone: '', nationalId: '', reason: '', type: 'IN_PERSON' });
  const [done, setDone] = useState(false);
  const [prefilled, setPrefilled] = useState(false);

  const { data: doctors } = useFetch(() => endpoints.doctors.active().catch(() => DEMO_DOCS), []);
  const { data: depts } = useFetch(() => endpoints.departments.active().catch(() => DEMO_DEPTS), []);
  const docList = Array.isArray(doctors) && doctors.length ? doctors : DEMO_DOCS;
  const deptList = Array.isArray(depts) && depts.length ? depts : DEMO_DEPTS;

  // Pre-fill from URL params (when coming from department page or doctor card)
  useEffect(() => {
    if (prefilled || !docList.length) return;
    const doctorId = searchParams.get('doctorId');
    const departmentId = searchParams.get('departmentId');

    if (doctorId) {
      const foundDoc = docList.find(d => String(d.id) === String(doctorId));
      if (foundDoc) {
        setDoctor(foundDoc);
        const foundDept = deptList.find(d => String(d.id) === String(foundDoc.departmentId || departmentId));
        if (foundDept) setDept(foundDept);
        setStep(2);
        setPrefilled(true);
        return;
      }
    }

    if (departmentId) {
      const foundDept = deptList.find(d => String(d.id) === String(departmentId));
      if (foundDept) {
        setDept(foundDept);
        setStep(1);
        setPrefilled(true);
      }
    }
  }, [searchParams, docList, deptList, prefilled]);

  const next = () => setStep((s) => Math.min(s + 1, STEPS.length - 1));
  const prev = () => setStep((s) => Math.max(s - 1, 0));

  if (done) {
    return (
      <>
        <PageHeader title="رزرو نوبت" />
        <Card>
          <div className="card-body" style={{ textAlign: 'center', padding: 56 }}>
            <div style={{ width: 76, height: 76, margin: '0 auto 20px', borderRadius: '50%', background: 'var(--success-bg)', color: 'var(--success)', display: 'grid', placeItems: 'center' }}>
              <CheckCircle2 size={40} />
            </div>
            <h2 className="text-xl fw-800">نوبت شما با موفقیت ثبت شد!</h2>
            <p className="text-muted mt-8">جزئیات نوبت برای شما پیامک خواهد شد.</p>
            <div className="card mt-24" style={{ maxWidth: 380, marginInline: 'auto', background: 'var(--surface-2)' }}>
              <div className="card-body card-body-sm flex-col gap-12">
                <Row k="پزشک" v={doctor?.fullName} />
                <Row k="تاریخ" v={date} />
                <Row k="ساعت" v={toFa(slot)} />
                <Row k="بیمار" v={info.name} />
              </div>
            </div>
            <Button className="mt-24" icon={ArrowRight} onClick={() => { setDone(false); setStep(0); setDoctor(null); setDept(null); setSlot(''); }}>رزرو نوبت جدید</Button>
          </div>
        </Card>
      </>
    );
  }

  return (
    <>
      <PageHeader title="رزرو نوبت" subtitle="در چند مرحله ساده نوبت خود را ثبت کنید" />

      {/* Stepper */}
      <Card className="mb-24">
        <div className="card-body card-body-sm">
          <div className="flex items-center" style={{ gap: 8, overflowX: 'auto' }}>
            {STEPS.map((s, i) => (
              <div key={s} className="flex items-center" style={{ flex: '0 0 auto' }}>
                <div className="flex items-center gap-8">
                  <span style={{
                    width: 34, height: 34, borderRadius: '50%', display: 'grid', placeItems: 'center',
                    fontWeight: 700, fontSize: 14,
                    background: i < step ? 'var(--success)' : i === step ? 'var(--gradient-brand)' : 'var(--surface-3)',
                    color: i <= step ? '#fff' : 'var(--text-muted)',
                  }}>
                    {i < step ? <CheckCircle2 size={18} /> : toFa(i + 1)}
                  </span>
                  <span className={i === step ? 'fw-700 text-sm' : 'text-sm text-muted'}>{s}</span>
                </div>
                {i < STEPS.length - 1 && <ChevronLeft size={16} className="text-faint" style={{ margin: '0 6px' }} />}
              </div>
            ))}
          </div>
        </div>
      </Card>

      <Card>
        <div className="card-body">
          {/* Step 0: Department */}
          {step === 0 && (
            <>
              <h3 className="fw-700 mb-16 flex items-center gap-8"><Building2 size={20} className="text-brand" /> بخش مورد نظر را انتخاب کنید</h3>
              <div className="grid-depts">
                {deptList.map((d) => (
                  <button key={d.id} onClick={() => { setDept(d); next(); }} className="dept-chip">
                    <div className="dc-emoji">{({ CARD: '🫀', NEUR: '🧠', PEDS: '👶', ORTH: '🦴', EMERG: '🚑' })[String(d.departmentCode || '').slice(0, 4)] || '🏥'}</div>
                    <div className="fw-700 text-sm">{d.departmentName}</div>
                  </button>
                ))}
              </div>
            </>
          )}

          {/* Step 1: Doctor */}
          {step === 1 && (
            <>
              <h3 className="fw-700 mb-16 flex items-center gap-8"><Stethoscope size={20} className="text-brand" /> پزشک را انتخاب کنید</h3>
              <div className="grid-cards-3">
                {docList.map((d) => (
                  <button key={d.id} onClick={() => { setDoctor(d); next(); }} className={`doctor-card-pub ${doctor?.id === d.id ? 'card-hover' : ''}`} style={{ border: doctor?.id === d.id ? '1.5px solid var(--brand-500)' : undefined }}>
                    <div className="dc-avatar" style={{ width: 64, height: 64, margin: '0 auto 12px' }}><Avatar name={d.fullName} /></div>
                    <div className="fw-700 text-sm">{d.fullName}</div>
                    <div className="text-xs text-brand mt-8">{specialityLabel(d.speciality)}</div>
                  </button>
                ))}
              </div>
            </>
          )}

          {/* Step 2: Date & slot */}
          {step === 2 && (
            <>
              <h3 className="fw-700 mb-16 flex items-center gap-8"><CalendarDays size={20} className="text-brand" /> تاریخ و ساعت را انتخاب کنید</h3>
              <div className="field">
                <label className="field-label">تاریخ ویزیت</label>
                <input type="date" className="input" style={{ maxWidth: 280 }} value={date} min={new Date().toISOString().slice(0, 10)} onChange={(e) => setDate(e.target.value)} />
              </div>
              <label className="field-label">ساعت خالی</label>
              <div className="flex gap-8" style={{ flexWrap: 'wrap' }}>
                {SLOTS.map((s) => (
                  <button key={s} onClick={() => setSlot(s)} className="btn btn-ghost btn-sm" style={{ borderColor: slot === s ? 'var(--brand-500)' : undefined, background: slot === s ? 'var(--brand-50)' : undefined, color: slot === s ? 'var(--brand-700)' : undefined }}>
                    <Clock size={14} /> {toFa(s)}
                  </button>
                ))}
              </div>
            </>
          )}

          {/* Step 3: Info */}
          {step === 3 && (
            <>
              <h3 className="fw-700 mb-16 flex items-center gap-8"><User size={20} className="text-brand" /> اطلاعات بیمار</h3>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
                <div className="field"><label className="field-label">نام و نام خانوادگی <span className="req">*</span></label><input className="input" value={info.name} onChange={(e) => setInfo({ ...info, name: e.target.value })} placeholder="مثال: رضا محمدی" /></div>
                <div className="field"><label className="field-label">شماره تماس <span className="req">*</span></label><input className="input" dir="ltr" value={info.phone} onChange={(e) => setInfo({ ...info, phone: e.target.value })} placeholder="09xxxxxxxxx" /></div>
                <div className="field"><label className="field-label">کد ملی</label><input className="input" dir="ltr" value={info.nationalId} onChange={(e) => setInfo({ ...info, nationalId: e.target.value })} /></div>
                <div className="field"><label className="field-label">نوع ویزیت</label>
                  <select className="select" value={info.type} onChange={(e) => setInfo({ ...info, type: e.target.value })}>
                    <option value="IN_PERSON">حضوری</option><option value="VIDEO">ویدیویی</option><option value="PHONE">تلفنی</option>
                  </select>
                </div>
                <div className="field" style={{ gridColumn: '1 / -1' }}><label className="field-label">علت مراجعه</label><textarea className="textarea" value={info.reason} onChange={(e) => setInfo({ ...info, reason: e.target.value })} placeholder="توضیح کوتاه…" /></div>
              </div>
            </>
          )}

          {/* Step 4: Confirm */}
          {step === 4 && (
            <>
              <h3 className="fw-700 mb-16 flex items-center gap-8"><CheckCircle2 size={20} className="text-brand" /> تأیید اطلاعات نوبت</h3>
              <div className="card" style={{ background: 'var(--surface-2)' }}>
                <div className="card-body flex-col gap-12">
                  <Row k="بخش" v={dept?.departmentName} />
                  <Row k="پزشک" v={doctor?.fullName} />
                  <Row k="تخصص" v={specialityLabel(doctor?.speciality)} />
                  <Row k="تاریخ" v={date || '—'} />
                  <Row k="ساعت" v={slot ? toFa(slot) : '—'} />
                  <Row k="بیمار" v={info.name} />
                  <Row k="تماس" v={info.phone} />
                  <Row k="نوع ویزیت" v={({ IN_PERSON: 'حضوری', VIDEO: 'ویدیویی', PHONE: 'تلفنی' })[info.type]} />
                </div>
              </div>
            </>
          )}

          {/* Nav buttons */}
          <div className="flex items-center justify-between mt-24">
            <Button variant="ghost" icon={ChevronRight} onClick={prev} disabled={step === 0}>مرحله قبل</Button>
            {step < STEPS.length - 1 ? (
              <Button icon={ChevronLeft} iconRight onClick={next} disabled={(step === 0 && !dept) || (step === 1 && !doctor) || (step === 2 && (!date || !slot)) || (step === 3 && (!info.name || !info.phone))}>مرحله بعد</Button>
            ) : (
              <Button variant="success" icon={CheckCircle2} onClick={() => setDone(true)}>ثبت نهایی نوبت</Button>
            )}
          </div>
        </div>
      </Card>
    </>
  );
}

function Row({ k, v }) {
  return (
    <div className="flex items-center justify-between">
      <span className="text-muted text-sm">{k}</span>
      <span className="fw-600 text-sm">{v || '—'}</span>
    </div>
  );
}
