import { useEffect, useState } from 'react';
import mockedGyms from '../data/mockedGyms';

export const useWorkoutById = (id) => {
  const [workout, setWorkout] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!id) return;

    setLoading(true);
    setError(null);

    setTimeout(() => {
      try {
        let foundWorkout = null;
        for (const gym of mockedGyms) {
          foundWorkout = gym.workouts.find(w => String(w.id) === String(id));
          if (foundWorkout) break;
        }

        if (!foundWorkout) {
          setError('Workout not found');
        }

        setWorkout(foundWorkout);
      } catch (err) {
        setError('Failed to load workout');
      } finally {
        setLoading(false);
      }
    }, 500);
  }, [id]);

  return { workout, loading, error };
};
