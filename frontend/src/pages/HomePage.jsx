import React, { useState, useEffect } from 'react';
import { Box, Typography, Grid, styled, Button, CircularProgress, TextField, IconButton } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import GreetingTitle from '../components/GreetingTitle.jsx';
import GymSelector from '../components/GymSelector.jsx';
import WorkoutSummary from '../components/WorkoutSummary.jsx';
import AddIcon from '@mui/icons-material/Add';
import { useGymsByUserId } from '../hooks/useGymsByUserId.jsx';
import { useWorkoutsByGymId } from '../hooks/useWorkoutsByGymId.jsx';
import { useUpdateGym } from '../hooks/useUpdateGym.jsx';
import mockedUsers from '../data/mockedUsers.js';

const user = mockedUsers[0];

const CustomButton = styled(Button)(({ theme }) => ({
  width: '100%',
  height: '100%',
  color: theme.palette.secondary.main,
  borderColor: theme.palette.secondary.main
}))

export default function HomePage() {
  const { gyms: gymsData, loading: gymsLoading, error: gymsError } = useGymsByUserId(user.id);
  const [gyms, setGyms] = useState([]);
  const [selectedGym, setSelectedGym] = useState(null);
  const { workouts, loading: workoutsLoading, error: workoutsError } = useWorkoutsByGymId(selectedGym?.id);
  const { updateGymDetails, loading: updateLoading } = useUpdateGym();
  const [isEditing, setIsEditing] = useState(false);
  const [editedName, setEditedName] = useState('');

  useEffect(() => {
    if (!gymsLoading && gymsData && gymsData.length > 0) {
      setGyms(gymsData);
      setSelectedGym(gymsData[0]);
    }
  }, [gymsLoading, gymsData]);

  const handleEditClick = () => {
    setEditedName(selectedGym?.name || '');
    setIsEditing(true);
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    setEditedName('');
  };

  const handleSaveEdit = async () => {
    if (selectedGym) {
      try {
        const updatedGym = await updateGymDetails(selectedGym.id, { name: editedName });
        setSelectedGym(updatedGym);
        setGyms((prevGyms) => prevGyms.map(g => g.id === updatedGym.id ? { ...g, name: updatedGym.name } : g));
        setIsEditing(false);
        alert('Gym name was successfully updated');
      } catch (error) {
        let errorMsg = 'There was an error trying to update the name of the gym.';
        if (error.backend && error.backend.errors && error.backend.errors[0]) {
          errorMsg = `There was an error trying to update the name of the gym: ${error.backend.errors[0]}`;
        } else if (error.message) {
          errorMsg = error.message;
        }
        alert(errorMsg);
        setIsEditing(false);
        setEditedName(''); // restore previous name
      }
    } else {
      setIsEditing(false);
    }
  };

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
        <Box display="flex" alignItems="center" gap={1}>
          {isEditing ? (
            <>
              <TextField
                value={editedName}
                onChange={(e) => setEditedName(e.target.value)}
                autoFocus
                variant="standard"
                sx={{
                  '& .MuiInputBase-input': {
                    fontSize: '2.5rem',
                    fontWeight: 'bold',
                    color: 'text.secondary',
                    textAlign: 'center',
                  },
                }}
              />
              <IconButton aria-label="save" color="secondary" onClick={handleSaveEdit} disabled={updateLoading}>
                <CheckIcon />
              </IconButton>
              <IconButton aria-label="cancel" color="secondary" onClick={handleCancelEdit} disabled={updateLoading}>
                <CloseIcon />
              </IconButton>
            </>
          ) : (
            <>
              <Typography
                variant="h2"
                align="center"
                sx={{ color: 'text.secondary' }}
              >
                {selectedGym?.name}
              </Typography>
              <IconButton aria-label="edit" color="secondary" onClick={handleEditClick}>
                <EditIcon />
              </IconButton>
            </>
          )}
        </Box>

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