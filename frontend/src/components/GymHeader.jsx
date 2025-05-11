import React from 'react';
import { Box, Typography, TextField, IconButton, Button, styled } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import AddIcon from '@mui/icons-material/Add';
import GymSelector from './GymSelector';

const CustomButton = styled(Button)(({ theme }) => ({
  width: '100%',
  height: '100%',
  color: theme.palette.secondary.main,
  borderColor: theme.palette.secondary.main
}));

export default function GymHeader({
  selectedGym,
  isEditing,
  editedName,
  onEditClick,
  onCancelEdit,
  onSaveEdit,
  onCreateClick,
  onGymSelect,
  gyms,
  updateLoading
}) {
  return (
    <Box
      display="flex"
      flexDirection={{ xs: 'column', sm: 'row' }}
      justifyContent={{ xs: 'center', sm: 'space-between' }}
      alignItems="center"
      textAlign={{ xs: 'center', sm: 'left' }}
      rowGap={2}
      py={2}
      px={8}
    >
      <Box display="flex" alignItems="center" gap={1}>
        {isEditing ? (
          <>
            <TextField
              value={editedName}
              onChange={(e) => onEditClick(e.target.value)}
              autoFocus
              variant="standard"
              sx={{
                '& .MuiInputBase-input': {
                  fontSize: '2.5rem',
                  fontWeight: 'bold',
                  color: 'text.secondary',
                  textAlign: 'center',
                },
              }}
            />
            <IconButton aria-label="save" color="secondary" onClick={onSaveEdit} disabled={updateLoading}>
              <CheckIcon />
            </IconButton>
            <IconButton aria-label="cancel" color="secondary" onClick={onCancelEdit} disabled={updateLoading}>
              <CloseIcon />
            </IconButton>
          </>
        ) : (
          <>
            <Typography
              variant="h2"
              align="center"
              sx={{ color: 'text.secondary' }}
            >
              {selectedGym?.name}
            </Typography>
            <IconButton aria-label="edit" color="secondary" onClick={() => onEditClick(selectedGym?.name)}>
              <EditIcon />
            </IconButton>
          </>
        )}
      </Box>

      <Box display="flex" gap={4} width="100%" maxWidth={600} alignItems="stretch" justifyContent={{ xs: 'center', sm: 'space-between' }} flexDirection={{ xs: 'column', sm: 'row' }}>
        <Box sx={{ flex: 1 }}>
          <CustomButton variant="outlined" startIcon={<AddIcon />} onClick={onCreateClick}>
            New Gym
          </CustomButton>
        </Box>
        <Box sx={{ flex: 1 }}>
          <CustomButton variant="outlined" startIcon={<AddIcon />}>
            New Workout
          </CustomButton>
        </Box>
        <Box sx={{ flex: 1 }}>
          <GymSelector
            gyms={gyms || []}
            selectedGym={selectedGym ? selectedGym.id : undefined}
            onChange={onGymSelect}
          />
        </Box>
      </Box>
    </Box>
  );
} 