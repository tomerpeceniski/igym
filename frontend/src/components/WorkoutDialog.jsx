import React from 'react';
import { Box, Dialog, useMediaQuery, useTheme } from '@mui/material';
import WorkoutCard from './WorkoutCard';
import WorkoutCardActions from './WorkoutCardActions';

export default function WorkoutDialog({
  open,
  onClose,
  editedWorkout,
  isEditingWorkout,
  isUpdating,
  onEdit,
  onSave,
  onCancel,
  onDelete,
  onWorkoutChange,
  onExerciseDelete,
  openWorkout
}) {
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));

  if (!open) return null;

  return (
    <Dialog
      open={open}
      onClose={onClose}
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
      <Box sx={{ 
        width: '100%', 
        maxWidth: { xs: '100%', md: 800 }, 
        display: 'flex', 
        flexDirection: 'column', 
        alignItems: 'center', 
        height: '100%' 
      }}>
        <Box sx={{ 
          width: '100%', 
          px: 2, 
        }}>
          <WorkoutCardActions
            isEditing={isEditingWorkout}
            onEdit={onEdit}
            onSave={onSave}
            onCancel={onCancel}
            onDelete={onDelete}
            onClose={onClose}
            isUpdating={isUpdating}
          />
        </Box>
        <Box sx={{ 
          width: '100%', 
          flex: 1, 
          overflow: 'auto', 
          display: 'flex', 
          justifyContent: 'center', 
          alignItems: 'flex-start', 
          maxHeight: 'calc(100vh - 120px)' 
        }}>
          {(editedWorkout || openWorkout) && (
            <WorkoutCard 
              workout={isEditingWorkout ? editedWorkout : openWorkout}
              isEditing={isEditingWorkout}
              onWorkoutChange={onWorkoutChange}
              onExerciseDelete={onExerciseDelete}
            />
          )}
        </Box>
      </Box>
    </Dialog>
  );
} 