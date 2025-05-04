import React, { useState } from 'react';
import { Box, Typography } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle.jsx';
import GymSelector from '../components/GymSelector.jsx';
import WorkoutCard from '../components/WorkoutCard.jsx';
import gyms from '../data/mockedGyms';

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

      <Box display="flex" gap={7.5} alignItems="flex-start">
        <Box sx={{ flexGrow: 1 }}>
          <Typography variant="h5" align="center" gutterBottom>
            {selectedGym}
          </Typography>

          <Box display="flex" flexWrap="wrap" justifyContent="flex-start">
            {currentGym.workouts.map((workout, index) => (
              <WorkoutCard key={index} workout={workout} />
            ))}
          </Box>
        </Box>
      </Box>
    </Box>
  );
}