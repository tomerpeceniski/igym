import axiosInstance from './axiosInstance';

export const getWorkoutsByGymId = (gymId) => {
  return axiosInstance.get(`/gyms/${gymId}/workouts`);
};

export const deleteWorkout = (workoutId) => {
  return axiosInstance.delete(`/workouts/${workoutId}`);
};

export const updateWorkout = (workoutId, workoutData) => {
  return axiosInstance.patch(`/workouts/${workoutId}`, workoutData);
};

export const createWorkout = (gymId, workoutData) => {
  return axiosInstance.post(`/gyms/${gymId}/workouts`, workoutData);
};