import { useEffect, useState, useCallback } from 'react';
import { getGymsByUserId } from '../requests/GymRequests';

export const useGymsByUserId = (userId) => {
  const [gyms, setGyms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchGyms = useCallback(async () => {
    if (!userId) return;
    setLoading(true);
    setError(null);
    try {
      const response = await getGymsByUserId(userId);
      setGyms(response.data);
    } catch (err) {
      const backendMessage = err.response?.data?.message || 'Failed to fetch gyms';
      setError(backendMessage);
    } finally {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    fetchGyms();
  }, [fetchGyms]);

  return {
    gyms,
    loading,
    error,
    refresh: fetchGyms,
  };
};
