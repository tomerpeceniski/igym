import axiosInstance from './axiosInstance';
import { API_BASE_URL } from '../constants/RequestsConstants';

export const getGymsByUserId = (userId) => {
  return axiosInstance.get(`${API_BASE_URL}/users/${userId}/gyms`);
};

export const updateGym = (gymId, gymData) => {
  return axiosInstance.patch(`${API_BASE_URL}/gyms/${gymId}`, gymData);
};

export const createGym = (userId, gymData) => {
  return axiosInstance.post(`${API_BASE_URL}/gyms/${userId}`, gymData);
};

export const deleteGym = (gymId) => {
  return axiosInstance.delete(`${API_BASE_URL}/gyms/${gymId}`);
};