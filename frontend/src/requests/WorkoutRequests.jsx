import axios from 'axios';
import { API_BASE_URL } from '../constants/RequestsConstants';

export const getWorkoutsByGymId = (gymId) => {
  return axios.get(`${API_BASE_URL}/gyms/${gymId}/workouts`);
};

export const deleteWorkout = (workoutId) => {
  return axios.delete(`${API_BASE_URL}/workouts/${workoutId}`);
};

export const updateWorkout = (workoutId, workoutData) => {
  return axios.patch(`${API_BASE_URL}/workouts/${workoutId}`, workoutData);
};

export const createWorkout = (gymId, workoutData) => {
  return axios.post(`${API_BASE_URL}/gyms/${gymId}/workouts`, workoutData);
};