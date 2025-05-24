import axiosInstance from './axiosInstance';

export const deleteExercise = async (exerciseId) => {
  const response = await axiosInstance.delete(`/exercises/${exerciseId}`);
  return response.data;
};