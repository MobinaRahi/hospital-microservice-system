# Nova Hospital — React Frontend

A modern, professional React SPA for the Hospital Microservice System, replacing the
legacy Thymeleaf UI with an **Indigo/Violet premium** design system, full
**dark/light mode**, and responsive dashboards.

## ✨ Features

- ⚛️ **React 18 + Vite** — fast dev server and optimized production build
- 🎨 **Premium design system** — indigo→violet gradient theme, glassmorphism, soft shadows
- 🌗 **Dark / Light mode** — toggle in the navbar/topbar, persisted in `localStorage`,
  respects the OS preference and applied before first paint (no flash)
- 📱 **Fully responsive** — RTL, Persian (Vazirmatn font), mobile sidebar, adaptive grids
- 📊 **Live dashboards** — Recharts area/pie charts, stat cards, data tables
- 🔌 **API-ready** — thin fetch client wired to the Spring Boot REST API (`/api/v1/**`)
  via a Vite proxy (port 8082). Falls back to built-in demo data when the backend is offline,
  so the UI is always viewable.
- 🧩 **Pages** — Landing, Login, Admin dashboard, Doctors, Patients, Appointments,
  Departments, Rooms, Nurses, Shifts, multi-step Booking wizard, Doctor & Patient panels.

## 🚀 Getting Started

```bash
cd frontend
npm install      # install dependencies
npm run dev      # start dev server at http://localhost:5173
```

The dev server proxies `/api/**` to `http://localhost:8082` (CoreService).
Start the Spring Boot backend (CoreService + AuthService) for live data — otherwise the
app shows realistic demo data so you can preview every screen.

### Build for production

```bash
npm run build    # outputs to dist/
npm run preview  # preview the production build locally
```

## 🗂️ Structure

```
frontend/
├── index.html
├── vite.config.js          # proxy + code-splitting config
├── src/
│   ├── main.jsx
│   ├── App.jsx             # routes
│   ├── context/            # ThemeContext (dark/light)
│   ├── api/                # client + endpoint helpers
│   ├── hooks/              # useFetch
│   ├── styles/             # theme.css (tokens) · base.css · components.css
│   ├── components/
│   │   ├── ui/             # Button, Card, Badge, StatCard, ThemeToggle…
│   │   └── layout/         # Sidebar, Topbar, DashboardLayout, Navbar, Footer
│   ├── pages/
│   │   ├── public/         # Home, Login
│   │   └── dashboard/      # 11 dashboard pages
│   └── utils/              # Persian formatting + label dictionaries
└── public/favicon.svg
```

## 🎨 Theming

All colors are CSS custom properties defined in `src/styles/theme.css`.
Switching the `data-theme` attribute on `<html>` (`light` | `dark`) re-themes the
entire app instantly. Brand colors: indigo `#6366f1` → violet `#a855f7`.

## 🔌 Connecting to the backend

`src/api/endpoints.js` mirrors the CoreService REST controllers
(`/api/v1/doctor`, `/api/v1/patient`, `/api/v1/appointments`, …).
Login posts to `/api/v1/auth/login` and stores the JWT in `localStorage` as `nova-token`.
