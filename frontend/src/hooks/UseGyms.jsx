import { useEffect, useState } from 'react';
import { getGymsByUserId } from '../api/GymApi';

export const useGyms = (userId) => {
  const [gyms, setGyms] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    getGymsByUserId(userId)
      .then((response) => {
        setGyms(response.data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err);
        setLoading(false);
      });
  }, [userId]);

  return { gyms, loading, error };
};
