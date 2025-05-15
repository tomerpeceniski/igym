import axios from 'axios';
import { API_BASE_URL } from '../constants/RequestsConstants';

export const getGymsByUserId = (userId) => {
  return axios.get(`${API_BASE_URL}/users/${userId}/gyms`);
};

export const updateGym = (gymId, gymData) => {
  return axios.patch(`${API_BASE_URL}/gyms/${gymId}`, gymData);
};

export const createGym = (userId, gymData) => {
  return axios.post(`${API_BASE_URL}/gyms/${userId}`, gymData);
};

export const deleteGym = (gymId) => {
  return axios.delete(`${API_BASE_URL}/gyms/${gymId}`);
}; 