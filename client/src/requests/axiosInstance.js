import axios from "axios";
import { isTokenExpired } from "../utils/decoder";

// Create the singleton instance
const axiosInstance = axios.create();

axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            if (isTokenExpired(token)) {
                localStorage.removeItem("token");
                window.location.href = "/login";
                return Promise.reject(new axios.Cancel("Token expired"));
            }
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export default axiosInstance;
