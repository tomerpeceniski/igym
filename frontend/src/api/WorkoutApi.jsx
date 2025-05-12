import axios from 'axios';

const API_BASE_URL = '/api/v1';

export const getWorkoutsByGymId = (gymId) => {
  return axios.get(`${API_BASE_URL}/gyms/${gymId}/workouts`);
};

export const deleteWorkout = (workoutId) => {
  return axios.delete(`${API_BASE_URL}/workouts/${workoutId}`);
};

export const updateWorkout = (workoutId, workoutData) => {
  return axios.patch(`${API_BASE_URL}/workouts/${workoutId}`, workoutData);
};