import { useState } from 'react';
import { deleteWorkout, updateWorkout, createWorkout } from '../api/WorkoutApi';

export const useWorkoutManagement = (gymId, onWorkoutChange) => {
  const [openWorkout, setOpenWorkout] = useState(null);
  const [isEditingWorkout, setIsEditingWorkout] = useState(false);
  const [isCreatingWorkout, setIsCreatingWorkout] = useState(false);
  const [editedWorkout, setEditedWorkout] = useState(null);
  const [isUpdating, setIsUpdating] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [deleteError, setDeleteError] = useState(null);

  const handleCreateWorkoutClick = () => {
    setIsCreatingWorkout(true);
    setOpenWorkout(null);
    setEditedWorkout({
      name: 'Name your new workout',
      exerciseList: [{ name: '', weight: 0, numReps: 0, numSets: 0, note: '' }]
    });
    setIsEditingWorkout(true);
  };

  const handleOpenWorkout = (workout) => {
    setOpenWorkout(workout);
    setEditedWorkout(workout);
    setIsEditingWorkout(true);
    setIsCreatingWorkout(false);
  };

  const handleCloseWorkout = () => {
    setOpenWorkout(null);
    setEditedWorkout(null);
    setIsEditingWorkout(false);
    setIsCreatingWorkout(false);
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
      if (isCreatingWorkout) {
        await createWorkout(gymId, {
          name: editedWorkout.name,
          exerciseList: editedWorkout.exerciseList
        });
      } else {
        await updateWorkout(editedWorkout.id, {
          name: editedWorkout.name,
          exerciseList: editedWorkout.exerciseList
        });
      }
      setIsEditingWorkout(false);
      setIsCreatingWorkout(false);
      handleCloseWorkout();
      onWorkoutChange?.();
    } catch (error) {
      const errorMsg = error.response?.data?.errors?.[0] || error.response?.data?.message || 'Failed to save workout';
      alert(errorMsg);
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
      onWorkoutChange?.();
    } catch (error) {
      setDeleteError(error.response?.data?.message || 'Failed to delete workout');
    } finally {
      setIsDeleting(false);
    }
  };

  return {
    openWorkout,
    isEditingWorkout,
    isCreatingWorkout,
    editedWorkout,
    isUpdating,
    isDeleting,
    deleteError,
    handleCreateWorkoutClick,
    handleOpenWorkout,
    handleCloseWorkout,
    handleEditWorkout,
    handleWorkoutChange,
    handleSaveWorkout,
    handleCancelWorkout,
    handleDeleteWorkout
  };
}; 