import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          react: ['react', 'react-dom', 'react-router-dom'],
          charts: ['recharts'],
          icons: ['lucide-react'],
        },
      },
    },
    chunkSizeWarningLimit: 700,
  },
  server: {
    port: 5373,
    host: true,
    // Dual-backend proxy:
    //  - AuthService (login/JWT)  -> :8281
    //  - CoreService  (everything else) -> :8382
    proxy: {
      '/api/v1/auth': {
        target: 'http://localhost:8281',
        changeOrigin: true,
      },
      '/api': {
        target: 'http://localhost:8382',
        changeOrigin: true,
      },
    },
  },
});