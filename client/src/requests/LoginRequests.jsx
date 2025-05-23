import axiosInstance from './axiosInstance';

export const loginRequest = (name, password) => {
    return axiosInstance.post(`/login`, {
        name,
        password,
    });
};