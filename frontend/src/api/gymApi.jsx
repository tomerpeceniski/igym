import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api';

export const getGymsByUserId = (userId) => {
  return axios.get(`${API_BASE_URL}/users/${userId}/gyms`);
};