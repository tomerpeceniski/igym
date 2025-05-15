import React from 'react';
import { Box, Typography, TextField, IconButton, Button, styled, Tooltip } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import GymSelector from './GymSelector';

const CustomButton = styled(Button)(({ theme }) => ({
  width: '100%',
  height: '100%',
  color: theme.palette.secondary.main,
  borderColor: theme.palette.secondary.main,
  '&.Mui-disabled': {
    color: theme.palette.grey[500],
    borderColor: theme.palette.grey[500],
    backgroundColor: theme.palette.action.disabledBackground,
  },
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
  onDeleteGym,
  gyms,
  onCreateWorkoutClick,
  disabled = false
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
        {(selectedGym || isEditing) && (
          <>
            {isEditing ? (
              <Box display="flex" alignItems="center" gap={1}>
                <TextField
              value={editedName}
              onChange={(e) => onEditClick(e.target.value)}
              autoFocus
              variant="standard"
              sx={{
                '& .MuiInputBase-input': {
                  fontSize: '2.5rem',
                  color: 'text.secondary',
                },
              }}
            />
                <IconButton
                  aria-label="save"
                  color="secondary"
                  onClick={onSaveEdit}
                >
                  <CheckIcon />
                </IconButton>
                <IconButton
                  aria-label="cancel"
                  color="secondary"
                  onClick={onCancelEdit}
                >
                  <CloseIcon />
                </IconButton>
              </Box>
            ) : (
              <>
                <Typography
                  variant="h2"
                  align="center"
                  sx={{ color: 'text.secondary' }}
                >
                  {selectedGym?.name}
                </Typography>
                <Tooltip title="Edit gym name">
                  <IconButton
                    aria-label="edit"
                    color="secondary"
                    onClick={() => onEditClick(selectedGym?.name)}
                  >
                    <EditIcon />
                  </IconButton>
                </Tooltip>
                <Tooltip title="Delete gym">
                  <IconButton
                    onClick={() => onDeleteGym(selectedGym.id)}
                    color="secondary"
                  >
                    <DeleteIcon />
                  </IconButton>
                </Tooltip>
              </>
            )}
          </>
        )}
      </Box>

      <Box display="flex" gap={4} width="100%" maxWidth={600} alignItems="stretch" justifyContent={{ xs: 'center', sm: 'space-between' }} flexDirection={{ xs: 'column', sm: 'row' }}>
        <Box sx={{ flex: 1 }}>
          <CustomButton variant="outlined" startIcon={<AddIcon />} onClick={onCreateWorkoutClick} disabled={disabled || gyms.length === 0}>
            New Workout
          </CustomButton>
        </Box>
        <Box sx={{ flex: 1 }}>
          <CustomButton variant="outlined" startIcon={<AddIcon />} onClick={onCreateClick} disabled={disabled}>
            New Gym
          </CustomButton>
        </Box>
        <Box sx={{ flex: 1 }}>
          <GymSelector
            gyms={gyms || []}
            selectedGym={selectedGym ? selectedGym.id : undefined}
            onChange={onGymSelect}
            disabled={disabled}
          />
        </Box>
      </Box>
    </Box>
  );
} 