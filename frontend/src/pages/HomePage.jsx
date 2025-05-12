import React, { useState } from 'react';
import { Box, Typography, Dialog, useMediaQuery, useTheme, Alert, Snackbar } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle';
import GymHeader from '../components/GymHeader';
import WorkoutsList from '../components/WorkoutsList';
import WorkoutCard from '../components/WorkoutCard';
import WorkoutCardActions from '../components/WorkoutCardActions';
import { useGymManagement } from '../hooks/useGymManagement';
import { useWorkoutsByGymId } from '../hooks/useWorkoutsByGymId';
import { deleteWorkout, updateWorkout } from '../api/WorkoutApi';
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

  const { workouts, loading: workoutsLoading, error: workoutsError, refresh: refreshWorkouts } = useWorkoutsByGymId(selectedGym?.id);

  const [openWorkout, setOpenWorkout] = useState(null);
  const [isEditingWorkout, setIsEditingWorkout] = useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));
  const [deleteError, setDeleteError] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const [editedWorkout, setEditedWorkout] = useState(null);
  const [updateError, setUpdateError] = useState(null);
  const [isUpdating, setIsUpdating] = useState(false);

  const handleOpenWorkout = (workout) => {
    setOpenWorkout(workout);
    setEditedWorkout(workout);
    setIsEditingWorkout(false);
  };

  const handleCloseWorkout = () => {
    setOpenWorkout(null);
    setEditedWorkout(null);
    setIsEditingWorkout(false);
  };

  const handleEditWorkout = () => {
    setIsEditingWorkout(true);
    setEditedWorkout(openWorkout);
  };

  const handleWorkoutChange = (updatedWorkout) => {
    setEditedWorkout(updatedWorkout);
  };

  const handleSaveWorkout = async () => {
    if (!editedWorkout) return;

    try {
      setIsUpdating(true);
      await updateWorkout(editedWorkout.id, {
        name: editedWorkout.name,
        exerciseList: editedWorkout.exerciseList
      });
      setIsEditingWorkout(false);
      refreshWorkouts();
    } catch (error) {
      setUpdateError(error.response?.data?.message || 'Failed to update workout');
    } finally {
      setIsUpdating(false);
    }
  };

  const handleCancelWorkout = () => {
    setEditedWorkout(openWorkout);
    setIsEditingWorkout(false);
  };

  const handleDeleteWorkout = async () => {
    if (!openWorkout) return;
    
    const confirmed = window.confirm('Are you sure you want to delete this workout?');
    if (!confirmed) return;

    try {
      setIsDeleting(true);
      await deleteWorkout(openWorkout.id);
      handleCloseWorkout();
      refreshWorkouts();
    } catch (error) {
      setDeleteError(error.response?.data?.message || 'Failed to delete workout');
    } finally {
      setIsDeleting(false);
    }
  };

  const handleCloseError = () => {
    setDeleteError(null);
  };

  const handleCloseUpdateError = () => {
    setUpdateError(null);
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
            onWorkoutClick={handleOpenWorkout}
            onWorkoutDeleted={refreshWorkouts}
          />
        )}
      </Box>

      <Dialog
        open={!!openWorkout}
        onClose={handleCloseWorkout}
        fullScreen={fullScreen}
        maxWidth="md"
        fullWidth
        aria-labelledby="workout-dialog-title"
        PaperProps={{
          sx: {
            background: 'none',
            boxShadow: 'none',
            overflow: 'visible',
            m: { xs: 1, sm: 3 },
            maxHeight: 'calc(100vh - 32px)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }
        }}
      >
        <Box sx={{ width: '100%', maxWidth: { xs: '100%', md: 800 }, display: 'flex', flexDirection: 'column', alignItems: 'center', height: '100%' }}>
          <Box sx={{ width: '100%', px: 2, pt: 2, pb: 1, position: 'sticky', top: 0, zIndex: 2, background: 'background.paper', borderTopLeftRadius: 16, borderTopRightRadius: 16 }}>
            <WorkoutCardActions
              isEditing={isEditingWorkout}
              onEdit={handleEditWorkout}
              onSave={handleSaveWorkout}
              onCancel={handleCancelWorkout}
              onDelete={handleDeleteWorkout}
              onClose={handleCloseWorkout}
              isDeleting={isDeleting}
              isUpdating={isUpdating}
            />
          </Box>
          <Box sx={{ width: '100%', flex: 1, overflow: 'auto', display: 'flex', justifyContent: 'center', alignItems: 'flex-start', maxHeight: 'calc(100vh - 120px)' }}>
            {openWorkout && editedWorkout && (
              <WorkoutCard 
                workout={editedWorkout} 
                isEditing={isEditingWorkout}
                onWorkoutChange={handleWorkoutChange}
              />
            )}
          </Box>
        </Box>
      </Dialog>

      <Snackbar open={!!deleteError} autoHideDuration={6000} onClose={handleCloseError}>
        <Alert onClose={handleCloseError} severity="error" sx={{ width: '100%' }}>
          {deleteError}
        </Alert>
      </Snackbar>

      <Snackbar open={!!updateError} autoHideDuration={6000} onClose={handleCloseUpdateError}>
        <Alert onClose={handleCloseUpdateError} severity="error" sx={{ width: '100%' }}>
          {updateError}
        </Alert>
      </Snackbar>
    </Box>
  );
}