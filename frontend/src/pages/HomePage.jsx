import React, { useEffect } from 'react';
import { Box, Typography } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle';
import GymHeader from '../components/GymHeader';
import WorkoutsList from '../components/WorkoutsList';
import WorkoutDialog from '../components/WorkoutDialog';
import { useGymManagement } from '../hooks/useGymManagement';
import { useWorkoutManagement } from '../hooks/useWorkoutManagement';
import mockedUser from '../data/mockedUser';

const user = mockedUser;

export default function HomePage() {
  const {
    gyms,
    selectedGym,
    isEditing,
    editedName,
    gymsLoading,
    gymsError,
    handleEditClick,
    handleCancelEdit,
    handleCreateClick,
    handleSaveEdit,
    handleGymSelect,
    handleDeleteGym
  } = useGymManagement(user.id);

  const {
    workouts,
    workoutsLoading,
    workoutsError,
    refreshWorkouts,
    openWorkout,
    isEditingWorkout,
    isCreatingWorkout,
    editedWorkout,
    handleCreateWorkoutClick,
    handleOpenWorkout,
    handleCloseWorkout,
    handleEditWorkout,
    handleWorkoutChange,
    handleSaveWorkout,
    handleCancelWorkout,
    handleDeleteWorkout,
    handleExerciseDelete
  } = useWorkoutManagement(selectedGym?.id);

  useEffect(() => {
    if (gymsError) {
      alert(gymsError);
    }
  }, [gymsError]);

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
        onDeleteGym={handleDeleteGym}
        gyms={gyms}
        onCreateWorkoutClick={handleCreateWorkoutClick}
      />

      <Box sx={{ width: '100%', px: 2 }}>
        {gymsLoading ? (
          <Typography variant="h6" align="center" color="text.secondary" mt={4}>
            Loading gyms...
          </Typography>
        ) : gymsError ? (
          <Typography variant="h6" align="center" color="text.secondary" mt={4}>
            Failed to fetch gyms.
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
            onWorkoutView={(workout) => {
              handleOpenWorkout(workout, false);
            }}
            onWorkoutEdit={(workout) => {
              handleOpenWorkout(workout, true);
            }}
            onWorkoutDeleted={refreshWorkouts}
          />
        )}
      </Box>

      <WorkoutDialog
        open={!!openWorkout || isCreatingWorkout}
        onClose={handleCloseWorkout}
        editedWorkout={editedWorkout}
        isEditingWorkout={isEditingWorkout}
        onEdit={handleEditWorkout}
        onSave={handleSaveWorkout}
        onCancel={handleCancelWorkout}
        onDelete={handleDeleteWorkout}
        onWorkoutChange={handleWorkoutChange}
        onExerciseDelete={handleExerciseDelete}
        openWorkout={openWorkout}
      />
    </Box>
  );
}