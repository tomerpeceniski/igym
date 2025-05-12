import React, { useState } from 'react';
import { Box, Typography, Grid, CircularProgress, Alert, Snackbar } from '@mui/material';
import WorkoutSummary from './WorkoutSummary';
import { deleteWorkout } from '../api/WorkoutApi';

export default function WorkoutsList({ workouts, loading, error, onWorkoutView, onWorkoutEdit, onWorkoutDeleted }) {
  const [deleteError, setDeleteError] = useState(null);
  const [deletingWorkoutId, setDeletingWorkoutId] = useState(null);

  const handleDelete = async (workout) => {
    try {
      setDeletingWorkoutId(workout.id);
      await deleteWorkout(workout.id);
      if (onWorkoutDeleted) {
        onWorkoutDeleted(workout);
      }
    } catch (error) {
      setDeleteError(error.response?.data?.message || 'Failed to delete workout');
    } finally {
      setDeletingWorkoutId(null);
    }
  };

  const handleCloseError = () => {
    setDeleteError(null);
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" mt={2}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Typography variant="h6" align="center" color="error.main" mt={4}>
        Failed to load workouts.
      </Typography>
    );
  }

  if (workouts.length === 0) {
    return (
      <Typography variant="h6" align="center" color="text.secondary" mt={4}>
        You have no workouts for this gym.
      </Typography>
    );
  }

  return (
    <>
      <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
        {workouts.map((workout) => (
          <Grid key={workout.id} item size={{ xs: 4, sm: 4, md: 4 }}>
            <WorkoutSummary 
              workout={workout} 
              onView={onWorkoutView}
              onEdit={onWorkoutEdit}
              onDelete={handleDelete}
              isDeleting={deletingWorkoutId === workout.id}
            />
          </Grid>
        ))}
      </Grid>

      <Snackbar open={!!deleteError} autoHideDuration={6000} onClose={handleCloseError}>
        <Alert onClose={handleCloseError} severity="error" sx={{ width: '100%' }}>
          {deleteError}
        </Alert>
      </Snackbar>
    </>
  );
} 