import { useState } from 'react';
import { deleteWorkout, updateWorkout, createWorkout } from '../api/WorkoutApi';
import { deleteExercise } from '../api/ExerciseApi.jsx';
import { useWorkoutsByGymId } from './useWorkoutsByGymId';

export const useWorkoutManagement = (gymId, onWorkoutChange) => {
  const { workouts, loading: workoutsLoading, error: workoutsError, refresh: refreshWorkouts } = useWorkoutsByGymId(gymId);
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

  const handleOpenWorkout = (workout, editMode = false) => {
    setOpenWorkout(workout);
    if (editMode) {
      setEditedWorkout(workout);
    }
    setIsEditingWorkout(editMode);
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
      let savedWorkout;
      if (isCreatingWorkout) {
        savedWorkout = await createWorkout(gymId, {
          name: editedWorkout.name,
          exerciseList: editedWorkout.exerciseList
        });
      } else {
        savedWorkout = await updateWorkout(editedWorkout.id, {
          name: editedWorkout.name,
          exerciseList: editedWorkout.exerciseList
        });
      }
      setIsEditingWorkout(false);
      setIsCreatingWorkout(false);
      setOpenWorkout(savedWorkout.data);
      setEditedWorkout(savedWorkout.data);
      refreshWorkouts();
    } catch (error) {
      const errorMsg = error.response?.data?.errors?.[0] || error.response?.data?.message || 'Failed to save workout';
      alert(errorMsg);
    } finally {
      setIsUpdating(false);
    }
  };

  const handleCancelWorkout = () => {
    setEditedWorkout(null);
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

  const handleExerciseDelete = async (exerciseId) => {
    try {
      await deleteExercise(exerciseId);
      
      if (editedWorkout) {
        const updatedExerciseList = editedWorkout.exerciseList.filter(
          exercise => exercise.id !== exerciseId
        );
        setEditedWorkout({
          ...editedWorkout,
          exerciseList: updatedExerciseList
        });
      }
      
      if (openWorkout) {
        const updatedExerciseList = openWorkout.exerciseList.filter(
          exercise => exercise.id !== exerciseId
        );
        setOpenWorkout({
          ...openWorkout,
          exerciseList: updatedExerciseList
        });
      }
    } catch (error) {
      const errorMsg = error.response?.data?.errors[0] || 'Failed to delete exercise';
      alert(errorMsg);
    }
  };

  return {
    workouts,
    workoutsLoading,
    workoutsError,
    refreshWorkouts,
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
    handleDeleteWorkout,
    handleExerciseDelete
  };
}; 