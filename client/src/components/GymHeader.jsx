import { Box, Typography, TextField, IconButton, Button, styled, Tooltip } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import GymSelector from './GymSelector';

const CustomButton = styled(Button)(({ theme }) => ({
  width: 180,
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
      justifyContent="space-between"
      flexDirection={{ xs: 'column', sm: 'row' }}
      py={2}
      px={2}
      sx={{
        alignItems: { xs: 'center', sm: 'center' }
      }}
    >
      <Box
        display="flex"
        alignItems="center"
        sx={{
          minWidth: 0,
          mb: { xs: 2, sm: 0 }
        }}>
        {(selectedGym || isEditing) && (
          <>
            {isEditing ? (
              <Box
                display="flex"
                alignItems="center"
                gap={1}
                sx={{ minWidth: 0, maxWidth: 340 }}
              >
                <TextField
                  value={editedName}
                  onChange={(e) => onEditClick(e.target.value)}
                  autoFocus 
                  variant="standard"
                  sx={{
                    '& .MuiInputBase-input': {
                      fontSize: { xs: '1.2rem', sm: '1.5rem' },
                      color: 'text.secondary',
                      textOverflow: 'ellipsis',
                      overflow: 'hidden',
                      whiteSpace: 'nowrap',
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
              <Box
                sx={{ minWidth: 0, maxWidth: 340 }}
                display="flex"
                alignItems="center"
              >
                <Typography
                  variant="h2"
                  noWrap
                  sx={{
                    color: 'text.secondary',
                    fontSize: { xs: '1.2rem', sm: '1.5rem' },
                  }}
                >
                  {selectedGym?.name}
                </Typography>
                <Tooltip title="Edit gym name">
                  <IconButton
                    aria-label="edit"
                    color="secondary"
                    onClick={() => onEditClick(selectedGym?.name)}
                  >
                    <EditIcon
                      sx={{
                        width: { xs: '16px', sm: '20px' },
                        height: { xs: '16px', sm: '20px' },
                        ml: 1
                      }}
                    />
                  </IconButton>
                </Tooltip>
                <Tooltip title="Delete gym">
                  <IconButton
                    onClick={() => onDeleteGym(selectedGym.id)}
                    color="secondary"
                  >
                    <DeleteIcon
                      sx={{
                        width: { xs: '16px', sm: '20px' },
                        height: { xs: '16px', sm: '20px' }
                      }}
                    />
                  </IconButton>
                </Tooltip>
              </Box>
            )}
          </>
        )}
      </Box>

      <Box
        display="flex"
        width="100%"
        maxWidth={600}
        justifyContent={{ xs: 'center', sm: 'space-between' }}
        flexDirection={{ xs: 'column', sm: 'row' }}
        alignItems={{ xs: 'center', sm: 'stretch' }}
        gap={2}
      >
        <Box >
          <CustomButton
            variant="outlined"
            startIcon={<AddIcon />}
            onClick={onCreateWorkoutClick}
            disabled={disabled || gyms.length === 0}
          >
            New Workout
          </CustomButton>
        </Box>
        <Box >
          <CustomButton
            variant="outlined"
            startIcon={<AddIcon />}
            onClick={onCreateClick}
            disabled={disabled}
          >
            New Gym
          </CustomButton>
        </Box>
        <Box>
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
