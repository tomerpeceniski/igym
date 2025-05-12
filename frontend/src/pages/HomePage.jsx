import React from 'react';
import { Box, Typography } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle';
import GymHeader from '../components/GymHeader';
import WorkoutsList from '../components/WorkoutsList';
import WorkoutDialog from '../components/WorkoutDialog';
import { useGymManagement } from '../hooks/useGymManagement';
import { useWorkoutsByGymId } from '../hooks/useWorkoutsByGymId';
import { useWorkoutManagement } from '../hooks/useWorkoutManagement';
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
    isDeleting,
    deleteError,
    handleEditClick,
    handleCancelEdit,
    handleCreateClick,
    handleSaveEdit,
    handleGymSelect,
    handleDeleteGym
  } = useGymManagement(user.id);

  const { workouts, loading: workoutsLoading, error: workoutsError, refresh: refreshWorkouts } = useWorkoutsByGymId(selectedGym?.id);

  const {
    openWorkout,
    isEditingWorkout,
    isCreatingWorkout,
    editedWorkout,
    isUpdating,
    handleCreateWorkoutClick,
    handleOpenWorkout,
    handleCloseWorkout,
    handleEditWorkout,
    handleWorkoutChange,
    handleSaveWorkout,
    handleCancelWorkout,
    handleDeleteWorkout
  } = useWorkoutManagement(selectedGym?.id, refreshWorkouts);

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
        updateLoading={updateLoading}
        isDeleting={isDeleting}
        onCreateWorkoutClick={handleCreateWorkoutClick}
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
            onWorkoutClick={handleOpenWorkout}
            onWorkoutDeleted={refreshWorkouts}
          />
        )}
      </Box>

      <WorkoutDialog
        open={!!openWorkout || isCreatingWorkout}
        onClose={handleCloseWorkout}
        editedWorkout={editedWorkout}
        isEditingWorkout={isEditingWorkout}
        isDeleting={isDeleting}
        isUpdating={isUpdating}
        onEdit={handleEditWorkout}
        onSave={handleSaveWorkout}
        onCancel={handleCancelWorkout}
        onDelete={handleDeleteWorkout}
        onWorkoutChange={handleWorkoutChange}
      />
    </Box>
  );
}