import { useEffect, useState } from 'react';
import { getWorkoutsByGymId } from '../api/WorkoutApi';

export const useWorkoutsByGymId = (gymId) => {
  const [workouts, setWorkouts] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!gymId) return;

    setLoading(true);
    getWorkoutsByGymId(gymId)
      .then((response) => {
        setWorkouts(response.data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err);
        setLoading(false);
      });
  }, [gymId]);

  return { workouts, loading, error };
};