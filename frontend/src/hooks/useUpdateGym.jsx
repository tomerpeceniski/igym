import { useState } from 'react';
import { updateGym } from '../api/GymApi';

export const useUpdateGym = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const updateGymDetails = async (gymId, gymData) => {
    try {
      setLoading(true);
      setError(null);
      const updatedGym = await updateGym(gymId, gymData);
      return updatedGym;
    } catch (err) {
      setError(err.message || 'Failed to update gym');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { updateGymDetails, loading, error };
}; 