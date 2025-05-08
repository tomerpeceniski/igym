import React, { useState, useEffect } from 'react';
import { Box, Typography, Grid, styled, Button, CircularProgress } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle.jsx';
import GymSelector from '../components/GymSelector.jsx';
import WorkoutSummary from '../components/WorkoutSummary.jsx';
import AddIcon from '@mui/icons-material/Add';
import { useGyms } from '../hooks/useGyms.jsx';
import { useWorkouts } from '../hooks/useWorkouts.jsx';
import mockedUsers from '../data/mockedUsers.js';

const user = mockedUsers[0];

const CustomButton = styled(Button)(({ theme }) => ({
  width: '100%',
  height: '100%',
  color: theme.palette.secondary.main,
  borderColor: theme.palette.secondary.main
}))

export default function HomePage() {
  const { gyms, loading: gymsLoading, error: gymsError } = useGyms(user.id);
  const [selectedGym, setSelectedGym] = useState(null);
  const { workouts, loading: workoutsLoading, error: workoutsError } = useWorkouts(selectedGym?.id);


  useEffect(() => {
    if (!gymsLoading && gyms.length > 0) {
      setSelectedGym(gyms[1]);
    }
  }, [gymsLoading, gyms]);

  return (
    <Box>
      <Box
        display="flex"
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
        textAlign="center"
        mb={3}
      >
        <GreetingTitle name={user.name} />
      </Box>

      <Box
        display="flex"
        flexDirection={{ xs: 'column', sm: 'row' }}
        justifyContent={{ xs: 'center', sm: 'space-between' }}
        alignItems="center"
        textAlign={{ xs: 'center', sm: 'left' }}
        rowGap={2}
        py={2}
        px={8}
      >
        <Typography variant="h2" align="center" gutterBottom sx={{ color: 'text.secondary' }}>
          {selectedGym?.name}
        </Typography>

        <Box display="flex" gap={4} width="100%" maxWidth={450} alignItems="stretch" justifyContent={{ xs: 'center', sm: 'space-between' }} flexDirection={{ xs: 'column', sm: 'row' }}>
          <Box sx={{ flex: 1 }}>
            <CustomButton variant="outlined" startIcon={<AddIcon />}>
              New Workout
            </CustomButton>

          </Box>
          <Box sx={{ flex: 1 }}>
            <GymSelector
              gyms={gyms || []}
              selectedGym={selectedGym ? selectedGym.id : undefined}
              onChange={(e) => {
                const gymId = e.target.value;
                const gym = gyms?.find(g => g.id === gymId);
                setSelectedGym(gym);
              }}
            />

          </Box>
        </Box>
      </Box>

      <Box sx={{ width: '100%', px: 2 }}>
        {gymsLoading ? (
          <Box display="flex" justifyContent="center" mt={2}>
            <CircularProgress />
          </Box>
        ) : gymsError ? (
          <Typography variant="h6" align="center" color="error.main" mt={4}>
            Failed to load gyms.
          </Typography>
        ) : gyms.length === 0 ? (
          <Typography variant="h6" align="center" color="text.secondary" mt={4}>
            You have no gyms to show. Please create your first gym.
          </Typography>
        ) : workoutsError ? (
          <Typography variant="h6" align="center" color="error.main" mt={4}>
            Failed to load workouts.
          </Typography>
        ) : workoutsLoading ? (
          <Box display="flex" justifyContent="center" mt={2}>
            <CircularProgress />
          </Box>
        ) : workouts.length === 0 ? (
          <Typography variant="h6" align="center" color="text.secondary" mt={4}>
            You have no workouts for this gym.
          </Typography>
        ) : (
          <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
            {workouts.map((workout, index) => (
              <Grid key={index} size={{ xs: 4, sm: 4, md: 4 }}>
                <WorkoutSummary workout={workout} />
              </Grid>
            ))}
          </Grid>
        )}
      </Box>
    </Box>
  )
}