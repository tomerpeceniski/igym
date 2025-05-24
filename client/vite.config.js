import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const backendHost = env.VITE_API_BASE;
  return {
    plugins: [react()],
    server: {
      proxy: {
        '/api': {
          target: backendHost,
          changeOrigin: true,
          secure: false,
        },
      },
    },
  };
});
