import axios from 'axios';
import { API_BASE_URL } from '../constants/RequestsConstants';

export const loginRequest = (name, password) => {
    return axios.post(`${API_BASE_URL}/login`, {
        name,
        password,
    });
};