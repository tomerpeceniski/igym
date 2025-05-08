import axios from 'axios';

const API_BASE_URL = '/api/v1';

export const getGymsByUserId = (userId) => {
  return axios.get(`${API_BASE_URL}/users/${userId}/gyms`);
};