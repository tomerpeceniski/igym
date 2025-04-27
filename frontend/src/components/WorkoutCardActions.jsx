import { IconButton, Box } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import ClearIcon from '@mui/icons-material/Clear';
import DeleteIcon from '@mui/icons-material/Delete';
import CloseIcon from '@mui/icons-material/Close';

const WorkoutCardActions = ({ isEditing, onEdit, onSave, onCancel, onDelete, onClose }) => {
  return (
    <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 2, gap: 1 }}>
      {!isEditing ? (
        <>
          <IconButton aria-label="edit workout" size="small" onClick={onEdit}>
            <EditIcon />
          </IconButton>
          <IconButton aria-label="delete workout" size="small" onClick={onDelete}>
            <DeleteIcon />
          </IconButton>
          <IconButton aria-label="close workout" size="small" onClick={onClose}>
            <CloseIcon />
          </IconButton>
        </>
      ) : (
        <>
          <IconButton aria-label="save workout" size="small" onClick={onSave}>
            <SaveIcon />
          </IconButton>
          <IconButton aria-label="discard changes" size="small" onClick={onCancel}>
            <ClearIcon />
          </IconButton>
        </>
      )}
    </Box>
  );
};

export default WorkoutCardActions;
