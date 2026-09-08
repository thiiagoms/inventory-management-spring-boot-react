import react from '@vitejs/plugin-react'
import { defineConfig, loadEnv } from 'vite'

export default defineConfig(({ mode }) => {
  const envDir = '..'
  const apiUrl = loadEnv(mode, envDir, '').VITE_API_URL

  return {
    envDir,
    plugins: [react()],
    server: apiUrl
      ? {
          proxy: {
            '/backend': {
              target: apiUrl,
              changeOrigin: true,
              rewrite: (path) => path.replace(/^\/backend/, ''),
            },
          },
        }
      : undefined,
  }
})
