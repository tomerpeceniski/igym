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
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
        textAlign="center"
      >
        <GreetingTitle />
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
        <Typography
          variant="h2"
          align="center"
          gutterBottom
          sx={{ color: 'text.secondary' }}
        >
          {selectedGym}
        </Typography>
        <GymSelector
          gyms={gyms}
          selectedGym={selectedGym}
          onChange={(e) => setSelectedGym(e.target.value)}
        />
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