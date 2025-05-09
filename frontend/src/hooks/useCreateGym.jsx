import { useState } from 'react';
import { createGym } from '../api/GymApi';

export const useCreateGym = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const createNewGym = async (userId, gymData) => {
    try {
      setLoading(true);
      setError(null);
      const newGym = await createGym(userId, gymData);
      return newGym;
    } catch (err) {
      setError(err.message || 'Failed to create gym');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { createNewGym, loading, error };
}; 