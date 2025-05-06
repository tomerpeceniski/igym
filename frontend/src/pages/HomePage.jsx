import React, { useState } from 'react';
import { Box, Typography, Grid, useTheme } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle.jsx';
import GymSelector from '../components/GymSelector.jsx';
import WorkoutSummary from '../components/WorkoutSummary.jsx';
import gyms from '../data/mockedGyms';
import OutlinedButton from '../components/OutlinedButton.jsx';
import AddIcon from '@mui/icons-material/Add';

export default function HomePage() {
  const [selectedGym, setSelectedGym] = useState(gyms[0].name);
  const currentGym = gyms.find(g => g.name === selectedGym);
  const theme = useTheme();

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
        <GreetingTitle name={"User"}/>
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

        <Box display="flex" gap={4} width="100%" maxWidth={450} alignItems="stretch" justifyContent={{ xs: 'center', sm: 'space-between' }} flexDirection={{ xs: 'column', sm: 'row' }}>
          <Box sx={{ flex: 1 }}>
          <OutlinedButton inputText="New Workout" color={theme.palette.secondary.main} startIcon={<AddIcon />}/>
          </Box>
          <Box sx={{ flex: 1 }}>
            <GymSelector
              gyms={gyms}
              selectedGym={selectedGym}
              onChange={(e) => setSelectedGym(e.target.value)}
            />
          </Box>
        </Box>
      </Box>

      <Box sx={{ width: '100', px: 2 }}>
        <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
          {currentGym.workouts.map((workout, index) => (
            <Grid key={index} size={{ xs: 4, sm: 4, md: 4 }}>
              <WorkoutSummary workout={workout} />
            </Grid>
          ))}
        </Grid>
      </Box>

    </Box>
  );
}