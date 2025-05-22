import { loginRequest } from "./LoginRequests";
import axios from 'axios';

export const signUpRequest = async (name, password) => {
    const response = await axios.post(`${API_BASE_URL}/users`, {
        name,
        password,
    });

    loginRequest(name, password);
}
