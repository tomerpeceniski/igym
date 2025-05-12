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
    throw json.errors?.[0] || json.message;
  }
  return json;
};

export const createGym = async (userId, gymData) => {
  const response = await fetch(`${API_BASE_URL}/gyms/${userId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(gymData),
  });
  const json = await response.json();
  if (!response.ok) {
    throw json.errors?.[0] || json.message;
  }
  return json;
};

export const deleteGym = async (gymId) => {
  const response = await axios.delete(`${API_BASE_URL}/gyms/${gymId}`);
  return response.data;
}; 