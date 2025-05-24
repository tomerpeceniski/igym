import { loginRequest } from "./LoginRequests";
import axiosInstance from './axiosInstance';

export const signUpRequest = async (name, password) => {
    try {
        await axiosInstance.post(`/users`, {
            name,
            password,
        });
        return loginRequest(name, password);
    } catch (error) {
        throw error;
    }
}
