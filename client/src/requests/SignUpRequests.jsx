import { loginRequest } from "./LoginRequests";
import axios from 'axios';
import { API_BASE_URL } from '../constants/RequestsConstants';

export const signUpRequest = async (name, password) => {
    await axios.post(`${API_BASE_URL}/users`, {
        name,
        password,
    });

    return loginRequest(name, password);
}
