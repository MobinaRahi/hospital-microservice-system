import { Routes, Route, Navigate } from 'react-router-dom';
import Home from './pages/public/Home';
import Login from './pages/public/Login';
import DashboardLayout from './components/layout/DashboardLayout';
import AdminDashboard from './pages/dashboard/AdminDashboard';
import Doctors from './pages/dashboard/Doctors';
import Patients from './pages/dashboard/Patients';
import Appointments from './pages/dashboard/Appointments';
import Departments from './pages/dashboard/Departments';
import Rooms from './pages/dashboard/Rooms';
import Nurses from './pages/dashboard/Nurses';
import Shifts from './pages/dashboard/Shifts';
import Book from './pages/dashboard/Book';
import DoctorPanel from './pages/dashboard/DoctorPanel';
import PatientPanel from './pages/dashboard/PatientPanel';

export default function App() {
  return (
    <Routes>
      {/* Public */}
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />

      {/* Dashboard */}
      <Route path="/app" element={<DashboardLayout />}>
        <Route index element={<AdminDashboard />} />
        <Route path="doctors" element={<Doctors />} />
        <Route path="patients" element={<Patients />} />
        <Route path="appointments" element={<Appointments />} />
        <Route path="departments" element={<Departments />} />
        <Route path="rooms" element={<Rooms />} />
        <Route path="nurses" element={<Nurses />} />
        <Route path="shifts" element={<Shifts />} />
        <Route path="book" element={<Book />} />
        <Route path="doctor" element={<DoctorPanel />} />
        <Route path="patient" element={<PatientPanel />} />
      </Route>

      {/* alias used by the public navbar */}
      <Route path="/book" element={<Navigate to="/app/book" replace />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
