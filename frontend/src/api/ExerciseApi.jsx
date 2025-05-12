import axios from 'axios';

const API_BASE_URL = '/api/v1';

export const deleteExercise = async (exerciseId) => {
  const response = await axios.delete(`${API_BASE_URL}/exercises/${exerciseId}`);
  return response.data;
}; 