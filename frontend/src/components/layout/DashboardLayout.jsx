import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import Topbar from './Topbar';

export default function DashboardLayout() {
  const [open, setOpen] = useState(false);
  return (
    <div className="dash-shell">
      <Sidebar open={open} onClose={() => setOpen(false)} />
      <div className="dash-main">
        <Topbar onMenu={() => setOpen(true)} />
        <main className="dash-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
