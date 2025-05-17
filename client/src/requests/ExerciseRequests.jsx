import { API_BASE_URL } from '../constants/RequestsConstants';
import axiosInstance from './axiosInstance';

export const deleteExercise = async (exerciseId) => {
  const response = await axiosInstance.delete(`${API_BASE_URL}/exercises/${exerciseId}`);
  return response.data;
};