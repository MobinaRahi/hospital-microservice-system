import { api } from './client';

/**
 * Endpoint helpers grouped by domain.
 * Mirrors the CoreService (8082) + AuthService (8081) REST controllers.
 */
export const endpoints = {
  // ---------- Auth (AuthService :8081) ----------
  auth: {
    login: (body) => api.post('/auth/login', body, { isAuth: false }),
    refresh: (refreshToken) => api.post('/auth/refresh', { refreshToken }, { isAuth: false }),
    me: () => api.get('/auth/me'),
  },

  // ---------- Doctors ----------
  doctors: {
    all: () => api.get('/doctor'),
    active: () => api.get('/doctor/active'),
    byId: (id) => api.get(`/doctor/${id}`),
    countActive: () => api.get('/doctor/active/count'),
    countByDept: (departmentId) => api.get('/doctor/count/by-department-id', { query: { departmentId } }),
  },

  // ---------- Patients ----------
  patients: {
    all: () => api.get('/patient'),
    byId: (id) => api.get(`/patient/${id}`),
    search: (q) => api.get('/patient/search', { query: { q } }),
    countActive: () => api.get('/patient/active/count'),
  },

  // ---------- Appointments ----------
  appointments: {
    count: () => api.get('/appointments/count'),
    byDoctor: (doctorId) => api.get(`/appointments/doctor/${doctorId}`),
    byPatient: (patientId) => api.get(`/appointments/patient/${patientId}`),
    byStatus: (status) => api.get('/appointments/by-status', { query: { status } }),
    today: () => api.get('/appointments/time/today'),
    thisWeek: () => api.get('/appointments/time/this-week'),
    thisMonth: () => api.get('/appointments/time/this-month'),
    upcoming: (patientId) => api.get(`/appointments/patient/${patientId}/upcoming`),
    past: (patientId) => api.get(`/appointments/patient/${patientId}/past`),
  },

  // ---------- Departments ----------
  departments: {
    all: () => api.get('/departments/all'),
    active: () => api.get('/departments/active'),
    countActive: () => api.get('/departments/count/active'),
    countTotal: () => api.get('/departments/count/total'),
  },

  // ---------- Rooms ----------
  rooms: {
    all: () => api.get('/room/all'),
    available: () => api.get('/room/available'),
    occupied: () => api.get('/room/occupied'),
    countAvailable: () => api.get('/room/count/available'),
    countOccupied: () => api.get('/room/count/occupied'),
  },

  // ---------- Nurses ----------
  nurses: {
    all: () => api.get('/nurse/all'),
    active: () => api.get('/nurse/active'),
    countActive: () => api.get('/nurse/active/count'),
  },

  // ---------- Shifts ----------
  shifts: {
    all: () => api.get('/shift/all'),
    active: () => api.get('/shift/active'),
    countActive: () => api.get('/shift/active/count'),
  },
};
