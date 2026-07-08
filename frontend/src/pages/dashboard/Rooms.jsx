import { useState, useMemo } from 'react';
import { BedDouble, Search, Plus, DoorOpen } from 'lucide-react';
import {
  PageHeader, Card, Badge, Button, PageLoader, EmptyState,
} from '../../components/ui';
import { useFetch } from '../../hooks/useFetch';
import { endpoints } from '../../api/endpoints';
import { toFa, faNumber } from '../../utils/format';

const DEMO = [
  { id: 1, roomNumber: '101', capacity: 2, isActive: true },
  { id: 2, roomNumber: '102', capacity: 1, isActive: true },
  { id: 3, roomNumber: '105', capacity: 4, isActive: true },
  { id: 4, roomNumber: '201', capacity: 2, isActive: true },
  { id: 5, roomNumber: '203', capacity: 1, isActive: true },
  { id: 6, roomNumber: '301', capacity: 6, isActive: false },
  { id: 7, roomNumber: 'ICU-1', capacity: 1, isActive: true },
  { id: 8, roomNumber: 'ICU-2', capacity: 1, isActive: true },
];

function occupancy(id) {
  const seed = (id * 7) % 4;
  return seed;
}

export default function Rooms() {
  const { data, loading } = useFetch(() => endpoints.rooms.all().catch(() => DEMO), []);
  const [q, setQ] = useState('');
  const [view, setView] = useState('all');
  const list = Array.isArray(data) && data.length ? data : DEMO;
  const filtered = useMemo(() => list.filter((r) => {
    if (q && !(r.roomNumber || '').toLowerCase().includes(q.toLowerCase())) return false;
    const occ = occupancy(r.id);
    if (view === 'available' && occ >= r.capacity) return false;
    if (view === 'occupied' && occ < r.capacity) return false;
    return true;
  }), [list, q, view]);

  return (
    <>
      <PageHeader title="مدیریت اتاق‌ها" subtitle={`${faNumber(filtered.length)} اتاق`} actions={<Button icon={Plus}>افزودن اتاق</Button>} />

      <Card className="mb-24">
        <div className="card-body card-body-sm flex items-center gap-12" style={{ flexWrap: 'wrap' }}>
          <div className="input-group has-icon" style={{ flex: 1, minWidth: 200 }}>
            <span className="input-icon"><Search size={18} /></span>
            <input className="input" placeholder="شماره اتاق…" value={q} onChange={(e) => setQ(e.target.value)} />
          </div>
          <div className="segmented">
            {[['all', 'همه'], ['available', 'آزاد'], ['occupied', 'پر']].map(([v, l]) => (
              <button key={v} className={view === v ? 'active' : ''} onClick={() => setView(v)}>{l}</button>
            ))}
          </div>
        </div>
      </Card>

      {loading && !data ? <PageLoader /> : filtered.length === 0 ? (
        <Card><div className="card-body"><EmptyState icon={BedDouble} title="اتاقی یافت نشد" /></div></Card>
      ) : (
        <div className="grid-cards-4 stagger">
          {filtered.map((r) => {
            const occ = occupancy(r.id);
            const full = occ >= r.capacity;
            return (
              <Card key={r.id} hover>
                <div className="card-body">
                  <div className="flex items-center justify-between">
                    <span style={{ width: 46, height: 46, borderRadius: 12, background: full ? 'var(--danger-bg)' : 'var(--gradient-brand-soft)', color: full ? 'var(--danger)' : 'var(--brand-600)', display: 'grid', placeItems: 'center' }}>
                      <DoorOpen size={22} />
                    </span>
                    {r.isActive === false ? <Badge tone="neutral" dot>غیرفعال</Badge> : full ? <Badge tone="danger" dot>پر</Badge> : <Badge tone="success" dot>آزاد</Badge>}
                  </div>
                  <h3 className="fw-800 text-lg mt-16">اتاق {toFa(r.roomNumber)}</h3>
                  <div className="text-sm text-muted mt-8">ظرفیت: {toFa(r.capacity)} نفر</div>
                  <div className="progress mt-16"><div className="progress-bar" style={{ width: `${(occ / r.capacity) * 100}%` }} /></div>
                  <div className="text-xs text-muted mt-8">{toFa(occ)} از {toFa(r.capacity)} اشغال</div>
                </div>
              </Card>
            );
          })}
        </div>
      )}
    </>
  );
}
