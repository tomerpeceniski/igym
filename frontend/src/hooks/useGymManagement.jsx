import { useState, useEffect } from 'react';
import { useGymsByUserId } from './useGymsByUserId';
import { useUpdateGym } from './useUpdateGym';
import { useCreateGym } from './useCreateGym';
import { getGymsByUserId, updateGym, createGym, deleteGym } from '../api/GymApi.jsx';

export function useGymManagement(userId) {
  const { gyms: gymsData, loading: gymsLoading, error: gymsError } = useGymsByUserId(userId);
  const [gyms, setGyms] = useState([]);
  const [selectedGym, setSelectedGym] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isCreating, setIsCreating] = useState(false);
  const [editedName, setEditedName] = useState('');
  const [isDeleting, setIsDeleting] = useState(false);
  const [deleteError, setDeleteError] = useState(null);

  const { updateGymDetails, loading: updateLoading } = useUpdateGym();
  const { createNewGym, loading: createLoading } = useCreateGym();

  useEffect(() => {
    if (!gymsLoading && gymsData && gymsData.length > 0) {
      setGyms(gymsData);
      setSelectedGym(gymsData[0]);
    }
  }, [gymsLoading, gymsData]);

  const handleEditClick = (name) => {
    setEditedName(name || '');
    setIsEditing(true);
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    setIsCreating(false);
    setEditedName('');
  };

  const handleCreateClick = () => {
    setIsCreating(true);
    setEditedName('Name your new gym');
    setIsEditing(true);
  };

  const handleSaveEdit = async () => {
    if (isCreating) {
      try {
        const newGym = await createNewGym(userId, { name: editedName });
        setGyms(prevGyms => [...prevGyms, newGym]);
        setSelectedGym(newGym);
        setIsEditing(false);
        setIsCreating(false);
        alert('Gym was successfully created');
      } catch (error) {
        handleError(error, 'create');
      }
    } else if (selectedGym) {
      try {
        const updatedGym = await updateGymDetails(selectedGym.id, { name: editedName });
        setSelectedGym(updatedGym);
        setGyms((prevGyms) => prevGyms.map(g => g.id === updatedGym.id ? { ...g, name: updatedGym.name } : g));
        setIsEditing(false);
        alert('Gym name was successfully updated');
      } catch (error) {
        handleError(error, 'update');
      }
    } else {
      setIsEditing(false);
    }
  };

  const handleError = (error, operation) => {
    let errorMsg = `There was an error trying to ${operation} the gym.`;
    if (error.backend?.errors?.[0]) {
      errorMsg = `There was an error trying to ${operation} the gym: ${error.backend.errors[0]}`;
    } else if (error.message) {
      errorMsg = error.message;
    }
    alert(errorMsg);
    setIsEditing(false);
    setIsCreating(false);
    setEditedName('');
  };

  const handleGymSelect = (e) => {
    const gymId = e.target.value;
    const gym = gyms?.find(g => g.id === gymId);
    setSelectedGym(gym);
  };

  const handleDeleteGym = async (gymId) => {
    const confirmed = window.confirm('Are you sure you want to delete this gym?');
    if (!confirmed) return;

    try {
      setIsDeleting(true);
      await deleteGym(gymId);
      
      setGyms(prevGyms => prevGyms.filter(gym => gym.id !== gymId));
      
      if (selectedGym?.id === gymId) {
        const remainingGyms = gyms.filter(gym => gym.id !== gymId);
        setSelectedGym(remainingGyms.length > 0 ? remainingGyms[0] : null);
      }
    } catch (error) {
      setDeleteError(error.response?.data?.message || 'Failed to delete gym');
    } finally {
      setIsDeleting(false);
    }
  };

  return {
    gyms,
    selectedGym,
    isEditing,
    isCreating,
    editedName,
    setEditedName,
    gymsLoading,
    gymsError,
    updateLoading,
    createLoading,
    isDeleting,
    deleteError,
    handleEditClick,
    handleCancelEdit,
    handleCreateClick,
    handleSaveEdit,
    handleGymSelect,
    handleDeleteGym
  };
} 