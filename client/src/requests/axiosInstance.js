import axios from 'axios';

const baseURL = import.meta.env.DEV
  ? '/api/v1'                                  // dev
  : `${import.meta.env.VITE_API_BASE}/api/v1`; // prod

const axiosInstance = axios.create({ baseURL });

axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');  
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default axiosInstance;
