import { useEffect, useState, useCallback } from 'react';
import { getWorkoutsByGymId } from '../requests/WorkoutRequests';

export const useWorkoutsByGymId = (gymId) => {
  const [workouts, setWorkouts] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const fetchWorkouts = useCallback(async () => {
    if (!gymId) return;

    setLoading(true);
    try {
      const response = await getWorkoutsByGymId(gymId);
      setWorkouts(response.data);
      setError(null);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  }, [gymId]);

  useEffect(() => {
    fetchWorkouts();
  }, [fetchWorkouts, refreshTrigger]);

  const refresh = useCallback(() => {
    setRefreshTrigger(prev => prev + 1);
  }, []);

  return { workouts, loading, error, refresh };
};