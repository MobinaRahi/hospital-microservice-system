import { Routes, Route, Navigate } from 'react-router-dom';
import Home from './pages/public/Home';
import Login from './pages/public/Login';
import Register from './pages/public/Register';
import ForgotPassword from './pages/public/ForgotPassword';
import PublicDoctors from './pages/public/PublicDoctors';
import PublicDepartments from './pages/public/PublicDepartments';
import DepartmentDetail from './pages/public/DepartmentDetail';
import About from './pages/public/About';
import Contact from './pages/public/Contact';
import TrackAppointment from './pages/public/TrackAppointment';
import PublicBook from './pages/public/PublicBook';
import DashboardLayout from './components/layout/DashboardLayout';
import AdminDashboard from './pages/dashboard/AdminDashboard';
import DoctorDashboard from './pages/dashboard/DoctorPanel';
import PatientDashboard from './pages/dashboard/PatientPanel';
import NurseDashboard from './pages/dashboard/NurseDashboard';
import Doctors from './pages/dashboard/Doctors';
import Patients from './pages/dashboard/Patients';
import Appointments from './pages/dashboard/Appointments';
import Departments from './pages/dashboard/Departments';
import Rooms from './pages/dashboard/Rooms';
import Nurses from './pages/dashboard/Nurses';
import Shifts from './pages/dashboard/Shifts';
import Book from './pages/dashboard/Book';
import Settings from './pages/dashboard/Settings';
import Reports from './pages/dashboard/Reports';
import Staff from './pages/dashboard/Staff';
import DoctorVisit from './pages/dashboard/doctor/DoctorVisit';
import PatientRecord from './pages/dashboard/doctor/PatientRecord';
import PatientRecords from './pages/dashboard/PatientRecords';
import PatientVitals from './pages/dashboard/PatientVitals';
import NurseNotes from './pages/dashboard/NurseNotes';
import SuperAdminDashboard from './pages/dashboard/SuperAdminDashboard';
import ReceptionDashboard from './pages/dashboard/ReceptionDashboard';
import ProtectedRoute from './components/ui/ProtectedRoute';
import RoleRoute from './components/ui/RoleRoute';
import DashboardRouter from './components/ui/DashboardRouter';

export default function App() {
  return (
    <Routes>
      {/* Public */}
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />
      <Route path="/doctors" element={<PublicDoctors />} />
      <Route path="/departments" element={<PublicDepartments />} />
      <Route path="/departments/:id" element={<DepartmentDetail />} />
      <Route path="/about" element={<About />} />
      <Route path="/contact" element={<Contact />} />
      <Route path="/track" element={<TrackAppointment />} />

      {/* Dashboard (protected) */}
      <Route
        path="/app"
        element={
          <ProtectedRoute>
            <DashboardLayout />
          </ProtectedRoute>
        }
      >
        {/* Role-based default dashboard */}
        <Route index element={<DashboardRouter />} />

        {/* Admin-only routes */}
        <Route element={<RoleRoute allowedRoles={['SUPER_ADMIN', 'ADMIN']} />}>
          <Route path="admin" element={<AdminDashboard />} />
          <Route path="doctors" element={<Doctors />} />
          <Route path="patients" element={<Patients />} />
          <Route path="departments" element={<Departments />} />
          <Route path="rooms" element={<Rooms />} />
          <Route path="nurses" element={<Nurses />} />
          <Route path="shifts" element={<Shifts />} />
          <Route path="settings" element={<Settings />} />
          <Route path="reports" element={<Reports />} />
          <Route path="staff" element={<Staff />} />
        </Route>

        {/* Doctor routes */}
        <Route element={<RoleRoute allowedRoles={['DOCTOR']} />}>
          <Route path="doctor" element={<DoctorDashboard />} />
          <Route path="doctor/visit/:id" element={<DoctorVisit />} />
          <Route path="doctor/patient/:id" element={<PatientRecord />} />
        </Route>

        {/* Nurse routes */}
        <Route element={<RoleRoute allowedRoles={['NURSE']} />}>
          <Route path="nurse" element={<NurseDashboard />} />
          <Route path="nurse/vitals" element={<PatientVitals />} />
          <Route path="nurse/notes" element={<NurseNotes />} />
        </Route>

        {/* Patient routes */}
        <Route element={<RoleRoute allowedRoles={['PATIENT']} />}>
          <Route path="patient" element={<PatientDashboard />} />
          <Route path="patient/records" element={<PatientRecords />} />
        </Route>

        {/* Shared routes */}
        <Route path="appointments" element={<Appointments />} />
        <Route path="book" element={<Book />} />
      </Route>

      {/* Super Admin (protected) */}
      <Route
        path="/super"
        element={
          <ProtectedRoute>
            <DashboardLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<SuperAdminDashboard />} />
      </Route>

      {/* Reception (protected) */}
      <Route
        path="/reception"
        element={
          <ProtectedRoute>
            <DashboardLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<ReceptionDashboard />} />
      </Route>

      {/* Public booking */}
      <Route path="/book" element={<PublicBook />} />

      {/* alias used by the public navbar */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
