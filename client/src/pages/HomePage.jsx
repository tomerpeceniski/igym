import { Box, Typography } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle';
import GymHeader from '../components/GymHeader';
import WorkoutsList from '../components/WorkoutsList';
import WorkoutDialog from '../components/WorkoutDialog';
import { useGymManagement } from '../hooks/useGymManagement';
import { useWorkoutManagement } from '../hooks/useWorkoutManagement';
import { useEffect, useState } from 'react';
import { getUserIdFromToken } from '../utils/decoder';

export default function HomePage() {
  const [name, setName] = useState('User');
  const [userId, setUserId] = useState(null);
  useEffect(() => {
    const storedName = localStorage.getItem('name');
    if (storedName) setName(storedName);

    const token = localStorage.getItem('token');
    const id = getUserIdFromToken(token);
    console.log('User ID from token:', id);
    setUserId(id);
  }, []);

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
  } = useGymManagement(userId);

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
        <GreetingTitle name={name} />
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
        disabled={!!gymsError}
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