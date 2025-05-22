import axiosInstance from './axiosInstance';
import { API_BASE_URL } from '../constants/RequestsConstants';

export const getWorkoutsByGymId = (gymId) => {
  return axiosInstance.get(`${API_BASE_URL}/gyms/${gymId}/workouts`);
};

export const deleteWorkout = (workoutId) => {
  return axiosInstance.delete(`${API_BASE_URL}/workouts/${workoutId}`);
};

export const updateWorkout = (workoutId, workoutData) => {
  return axiosInstance.patch(`${API_BASE_URL}/workouts/${workoutId}`, workoutData);
};

export const createWorkout = (gymId, workoutData) => {
  return axiosInstance.post(`${API_BASE_URL}/gyms/${gymId}/workouts`, workoutData);
};