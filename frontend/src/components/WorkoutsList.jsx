import React from 'react';
import { Box, Typography, Grid, CircularProgress } from '@mui/material';
import WorkoutSummary from './WorkoutSummary';

export default function WorkoutsList({ workouts, loading, error }) {
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
    <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
      {workouts.map((workout, index) => (
        <Grid key={index} size={{ xs: 4, sm: 4, md: 4 }}>
          <WorkoutSummary workout={workout} />
        </Grid>
      ))}
    </Grid>
  );
} 