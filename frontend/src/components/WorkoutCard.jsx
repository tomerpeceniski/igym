import { Card, CardContent, IconButton, Typography, Box } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CloseIcon from '@mui/icons-material/Close';
import ExercisesTable from './ExercisesTable';

const WorkoutCard = ({ workout }) => {
    return (
        <Card sx={{ minWidth: 275, borderRadius: 2, boxShadow: 3, px: 2, pt: 4, pb: 2, position: 'relative' }}>
            
            <Box sx={{ position: 'absolute', top: 16, right: 16, display: 'flex', gap: 1 }}>
                <IconButton aria-label="edit workout" size="small">
                    <EditIcon />
                </IconButton>
                <IconButton aria-label="delete workout" size="small">
                    <DeleteIcon />
                </IconButton>
                <IconButton aria-label="close" size="small">
                    <CloseIcon />
                </IconButton>
            </Box>

            <Typography variant="h5" align="center">
                {workout.name}
            </Typography>

            <CardContent>
                <ExercisesTable exercises={workout.exercises} />
            </CardContent>
        </Card>
    );
};

export default WorkoutCard;
