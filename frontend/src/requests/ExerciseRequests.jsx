import axios from 'axios';

const API_BASE_URL =  import.meta.env.VITE_API_BASE_URL;

export const deleteExercise = async (exerciseId) => {
  const response = await axios.delete(`${API_BASE_URL}/exercises/${exerciseId}`);
  return response.data;
}; 