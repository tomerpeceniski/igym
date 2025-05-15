import axios from 'axios';
import { API_BASE_URL } from '../constants/RequestsConstants';

export const deleteExercise = async (exerciseId) => {
  const response = await axios.delete(`${API_BASE_URL}/exercises/${exerciseId}`);
  return response.data;
}; 