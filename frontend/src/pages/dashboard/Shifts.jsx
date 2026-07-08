import { useState, useMemo } from 'react';
import { Clock, Search, Plus, Sun, Moon, Sunset } from 'lucide-react';
import {
  PageHeader, Card, Badge, Button, PageLoader, EmptyState,
} from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber, faTime } from '../../utils/format';

const DEMO = [
  { id: 1, name: 'شیفت صبح', type: 'MORNING', startTime: '06:00', endTime: '14:00', extraPay: 0, isActive: true },
  { id: 2, name: 'شیفت عصر', type: 'EVENING', startTime: '14:00', endTime: '22:00', extraPay: 10, isActive: true },
  { id: 3, name: 'شیفت شب', type: 'NIGHT', startTime: '22:00', endTime: '06:00', extraPay: 25, isActive: true },
  { id: 4, name: 'شیفت on-call', type: 'ON_CALL', startTime: '00:00', endTime: '23:59', extraPay: 40, isActive: true },
  { id: 5, name: 'شیفت تعطیلات', type: 'HOLIDAY', startTime: '08:00', endTime: '20:00', extraPay: 50, isActive: false },
];
const TYPE_META = {
  MORNING: { label: 'صبح', icon: Sun, color: '#f59e0b' },
  EVENING: { label: 'عصر', icon: Sunset, color: '#f97316' },
  NIGHT: { label: 'شب', icon: Moon, color: '#6366f1' },
  ON_CALL: { label: 'آنی', icon: Clock, color: '#a855f7' },
  HOLIDAY: { label: 'تعطیلات', icon: Clock, color: '#ec4899' },
};

export default function Shifts() {
  const { data, loading } = useFetch(() => endpoints.shifts.all().catch(() => DEMO), []);
  const [q, setQ] = useState('');
  const list = Array.isArray(data) && data.length ? data : DEMO;
  const filtered = useMemo(() => list.filter((s) => (s.name || '').toLowerCase().includes(q.toLowerCase())), [list, q]);

  return (
    <>
      <PageHeader title="مدیریت شیفت‌ها" subtitle={`${faNumber(filtered.length)} شیفت`} actions={<Button icon={Plus}>افزودن شیفت</Button>} />

      <Card className="mb-24">
        <div className="card-body card-body-sm">
          <div className="input-group has-icon">
            <span className="input-icon"><Search size={18} /></span>
            <input className="input" placeholder="جستجوی شیفت…" value={q} onChange={(e) => setQ(e.target.value)} />
          </div>
        </div>
      </Card>

      {loading && !data ? <PageLoader /> : filtered.length === 0 ? (
        <Card><div className="card-body"><EmptyState icon={Clock} title="شیفتی یافت نشد" /></div></Card>
      ) : (
        <div className="grid-cards-3 stagger">
          {filtered.map((s) => {
            const meta = TYPE_META[s.type] || TYPE_META.MORNING;
            const Icon = meta.icon;
            return (
              <Card key={s.id} hover>
                <div className="card-body">
                  <div className="flex items-center justify-between mb-16">
                    <span style={{ width: 48, height: 48, borderRadius: 12, background: `${meta.color}1a`, color: meta.color, display: 'grid', placeItems: 'center' }}>
                      <Icon size={24} />
                    </span>
                    {s.isActive === false ? <Badge tone="neutral" dot>غیرفعال</Badge> : <Badge tone="success" dot>فعال</Badge>}
                  </div>
                  <h3 className="fw-700">{s.name}</h3>
                  <div className="text-sm text-muted mt-8">{meta.label}</div>
                  <div className="flex items-center justify-between mt-16 card" style={{ background: 'var(--surface-2)', padding: '12px 14px', borderRadius: 12 }}>
                    <div>
                      <div className="text-xs text-muted">ساعات</div>
                      <div className="fw-600 text-sm" dir="ltr">{toFa(s.startTime?.slice(0, 5))} - {toFa(s.endTime?.slice(0, 5))}</div>
                    </div>
                    <div className="text-left">
                      <div className="text-xs text-muted">اضافه‌کار</div>
                      <div className="fw-600 text-sm">{toFa(s.extraPay)}٪</div>
                    </div>
                  </div>
                </div>
              </Card>
            );
          })}
        </div>
      )}
    </>
  );
}
