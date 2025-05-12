import React, { useState } from 'react';
import { Box, Typography, Dialog, useMediaQuery, useTheme } from '@mui/material';
import GreetingTitle from '../components/GreetingTitle';
import GymHeader from '../components/GymHeader';
import WorkoutsList from '../components/WorkoutsList';
import WorkoutCard from '../components/WorkoutCard';
import WorkoutCardActions from '../components/WorkoutCardActions';
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

  const [openWorkout, setOpenWorkout] = useState(null);
  const [isEditingWorkout, setIsEditingWorkout] = useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));

  const handleOpenWorkout = (workout) => {
    setOpenWorkout(workout);
    setIsEditingWorkout(false);
  };
  const handleCloseWorkout = () => {
    setOpenWorkout(null);
    setIsEditingWorkout(false);
  };
  const handleEditWorkout = () => setIsEditingWorkout(true);
  const handleSaveWorkout = () => setIsEditingWorkout(false);
  const handleCancelWorkout = () => setIsEditingWorkout(false);
  const handleDeleteWorkout = () => {/* implement if needed */};

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
            />
          </Box>
          <Box sx={{ width: '100%', flex: 1, overflow: 'auto', display: 'flex', justifyContent: 'center', alignItems: 'flex-start', maxHeight: 'calc(100vh - 120px)' }}>
            {openWorkout && (
              <WorkoutCard workout={openWorkout} isEditing={isEditingWorkout} />
            )}
          </Box>
        </Box>
      </Dialog>
    </Box>
  );
}