import { Card, CardContent, IconButton, Typography, Box } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CloseIcon from '@mui/icons-material/Close';
import ExercisesTable from './ExercisesTable';

const WorkoutCard = ({ workout }) => {
    return (
        <Box sx={{  width: 'fit-content' }}>

            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 2 }}>
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

            <Card sx={{ minWidth: 275, borderRadius: 2, boxShadow: 3, p: 2 }}>
                <Box sx={{ textAlign: 'center', mt: 2, mb: 2 }}>
                    <Typography variant="h5">
                        {workout.name}
                    </Typography>
                </Box>

                <CardContent>
                    <ExercisesTable exercises={workout.exercises} />
                </CardContent>
            </Card>

        </Box>


    );
};

export default WorkoutCard;
