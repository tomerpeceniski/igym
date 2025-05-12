import { IconButton, Box, useTheme, CircularProgress } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import ClearIcon from '@mui/icons-material/Clear';
import DeleteIcon from '@mui/icons-material/Delete';
import CloseIcon from '@mui/icons-material/Close';

const WorkoutCardActions = ({ isEditing, onEdit, onSave, onCancel, onDelete, onClose, isDeleting }) => {
  const theme = useTheme();
  const color = theme.palette.background.paper;

  return ( 
    <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 2, gap: 1 }}>
      {!isEditing ? (
        <>
          <IconButton aria-label="edit workout" size="small" onClick={onEdit}>
            <EditIcon sx={{ color }} />
          </IconButton>
          <IconButton 
            aria-label="delete workout" 
            size="small" 
            onClick={onDelete}
            disabled={isDeleting}
          >
            {isDeleting ? (
              <CircularProgress size={20} sx={{ color }} />
            ) : (
              <DeleteIcon sx={{ color }} />
            )}
          </IconButton>
          <IconButton aria-label="close workout" size="small" onClick={onClose}>
            <CloseIcon sx={{ color }} />
          </IconButton>
        </>
      ) : (
        <>
          <IconButton aria-label="save workout" size="small" onClick={onSave}>
            <SaveIcon sx={{ color }} />
          </IconButton>
          <IconButton aria-label="discard changes" size="small" onClick={onCancel}>
            <ClearIcon sx={{ color }} />
          </IconButton>
        </>
      )}
    </Box>
  );
};

export default WorkoutCardActions;
