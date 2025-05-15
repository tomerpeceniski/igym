import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const backendHost = env.VITE_HOST;
  
  return {
    plugins: [react()],
    server: {
      proxy: {
        '/api': {
          target: `http://${backendHost}`,
          changeOrigin: true,
          secure: false,
        },
      },
    },
  };
});