import React from 'react';
import { Box, Typography } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle';
import GymHeader from '../components/GymHeader';
import WorkoutsList from '../components/WorkoutsList';
import { useGymManagement } from '../hooks/useGymManagement';
import { useWorkoutsByGymId } from '../hooks/useWorkoutsByGymId';
import mockedUsers from '../data/mockedUsers';

const user = mockedUsers[0];

export default function HomePage() {
  const {
    gyms,
    selectedGym,
    isEditing,
    editedName,
    setEditedName,
    gymsLoading,
    gymsError,
    updateLoading,
    handleEditClick,
    handleCancelEdit,
    handleCreateClick,
    handleSaveEdit,
    handleGymSelect
  } = useGymManagement(user.id);

  const { workouts, loading: workoutsLoading, error: workoutsError } = useWorkoutsByGymId(selectedGym?.id);

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

      <GymHeader
        selectedGym={selectedGym}
        isEditing={isEditing}
        editedName={editedName}
        onEditClick={handleEditClick}
        onCancelEdit={handleCancelEdit}
        onSaveEdit={handleSaveEdit}
        onCreateClick={handleCreateClick}
        onGymSelect={handleGymSelect}
        gyms={gyms}
        updateLoading={updateLoading}
      />

      <Box sx={{ width: '100%', px: 2 }}>
        {gymsLoading ? (
          <Typography variant="h6" align="center" color="text.secondary" mt={4}>
            Loading gyms...
          </Typography>
        ) : gymsError ? (
          <Typography variant="h6" align="center" color="error.main" mt={4}>
            Failed to load gyms.
          </Typography>
        ) : gyms.length === 0 ? (
          <Typography variant="h6" align="center" color="text.secondary" mt={4}>
            You have no gyms to show. Please create your first gym.
          </Typography>
        ) : (
          <WorkoutsList
            workouts={workouts}
            loading={workoutsLoading}
            error={workoutsError}
          />
        )}
      </Box>
    </Box>
  );
}