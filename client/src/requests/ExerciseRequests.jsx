import { API_BASE_URL } from '../constants/RequestsConstants';
import axios from 'axios';

export const deleteExercise = async (exerciseId) => {
  const response = await axios.delete(`${API_BASE_URL}/exercises/${exerciseId}`);
  return response.data;
};