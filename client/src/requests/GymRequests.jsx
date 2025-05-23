import axiosInstance from './axiosInstance';

export const getGymsByUserId = (userId) => {
  return axiosInstance.get(`/users/${userId}/gyms`);
};

export const updateGym = (gymId, gymData) => {
  return axiosInstance.patch(`/gyms/${gymId}`, gymData);
};

export const createGym = (userId, gymData) => {
  return axiosInstance.post(`/gyms/${userId}`, gymData);
};

export const deleteGym = (gymId) => {
  return axiosInstance.delete(`/gyms/${gymId}`);
};