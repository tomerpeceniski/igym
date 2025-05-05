import React, { useState } from 'react';
import { Box, Typography, Grid, colors } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle.jsx';
import GymSelector from '../components/GymSelector.jsx';
import WorkoutCard from '../components/WorkoutCard.jsx';
import gyms from '../data/mockedGyms';
import { blue } from '@mui/material/colors';

export default function HomePage() {
  const [selectedGym, setSelectedGym] = useState(gyms[0].name);
  const currentGym = gyms.find(g => g.name === selectedGym);

  return (
    <Box>
      <Box
        display="flex"
        flexWrap="wrap"
        justifyContent="space-between"
        alignItems="center"
        mb={4}
        sx={(theme) => ({
          height: theme.spacing(8),
          rowGap: theme.spacing(2),
        })}
      >
        <GreetingTitle />
        <GymSelector
          gyms={gyms}
          selectedGym={selectedGym}
          onChange={(e) => setSelectedGym(e.target.value)}
        />
      </Box>

      <Box>
        <Typography variant="h5" align="center" gutterBottom>
          {selectedGym}
        </Typography>
      </Box>

      <Box sx={{ width: '100', px: 2 }}>
        <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
          {currentGym.workouts.map((workout, index) => (
            <Grid key={index} size={{ xs: 4, sm: 4, md: 4 }}>
              <WorkoutCard workout={workout} />
            </Grid>
          ))}
        </Grid>
      </Box>

    </Box>
  );
}