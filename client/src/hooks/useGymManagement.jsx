import { useState, useEffect } from 'react';
import { useGymsByUserId } from './useGymsByUserId';
import { updateGym, createGym, deleteGym } from '../requests/GymRequests';

export function useGymManagement(userId) {
  const { gyms: gymsData, loading: gymsLoading, error: gymsError } = useGymsByUserId(userId);
  const [gyms, setGyms] = useState([]);
  const [selectedGym, setSelectedGym] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isCreating, setIsCreating] = useState(false);
  const [editedName, setEditedName] = useState('');

  useEffect(() => {
    if (!gymsLoading && gymsData.length > 0) {
      setGyms(gymsData);
      setSelectedGym(gymsData[0]);
    } else if (!gymsLoading && gymsData.length === 0) {
      setGyms([]);
      setSelectedGym(null);
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
        const response = await createGym(userId, { name: editedName });
        const newGym = response.data;
        setGyms(prevGyms => [...prevGyms, newGym]);
        setSelectedGym(newGym);
        setIsEditing(false);
        setIsCreating(false);
        alert('Gym was successfully created');
      } catch (error) {
        const errorMsg = error.response?.data?.errors?.[0] || error.response?.data?.message || 'Failed to create gym';
        handleError(errorMsg, 'create');
      }
    } else {
      try {
        const response = await updateGym(selectedGym.id, { name: editedName });
        const updatedGym = response.data;
        setSelectedGym(updatedGym);
        setGyms((prevGyms) => prevGyms.map(g => g.id === updatedGym.id ? { ...g, name: updatedGym.name } : g));
        setIsEditing(false);
        alert('Gym name was successfully updated');
      } catch (error) {
        const errorMsg = error.response?.data?.errors?.[0] || error.response?.data?.message || 'Failed to update gym';
        handleError(errorMsg, 'update');
      }
    }
  };

  const handleError = (errorMsg, operation) => {
     let msg = `There was an error trying to ${operation} the gym: ${errorMsg}`;
    alert(msg);
    setIsEditing(false);
    setIsCreating(false);
    setEditedName('');
  };

  const handleGymSelect = (e) => {
    const gymId = e.target.value;
    const gym = gyms.find(g => g.id === gymId);
    setSelectedGym(gym);
  };

  const handleDeleteGym = async (gymId) => {
    const confirmed = window.confirm('Are you sure you want to delete this gym?');
    if (!confirmed) return;

    try {
      await deleteGym(gymId);
      setGyms(prevGyms => prevGyms.filter(gym => gym.id !== gymId));
      if (selectedGym?.id === gymId) {
        const remainingGyms = gyms.filter(gym => gym.id !== gymId);
        setSelectedGym(remainingGyms.length > 0 ? remainingGyms[0] : null);
      }
    } catch (error) {
      alert(error.response?.data?.errors?.[0] || error.response?.data?.message || 'Failed to delete gym');
    }
  };

  return {
    gyms,
    selectedGym,
    isEditing,
    isCreating,
    editedName,
    gymsLoading,
    gymsError,
    handleEditClick,
    handleCancelEdit,
    handleCreateClick,
    handleSaveEdit,
    handleGymSelect,
    handleDeleteGym
  };
} 