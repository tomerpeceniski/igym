import axios from 'axios';

const API_BASE_URL = '/api/v1';

export const getGymsByUserId = (userId) => {
  return axios.get(`${API_BASE_URL}/users/${userId}/gyms`);
};

export const updateGym = async (gymId, gymData) => {
  const response = await fetch(`${API_BASE_URL}/gyms/${gymId}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(gymData),
  });
  const json = await response.json();
  if (!response.ok) {
    const error = new Error(json.message || 'Failed to update gym');
    error.backend = json;
    throw error;
  }
  return json;
};