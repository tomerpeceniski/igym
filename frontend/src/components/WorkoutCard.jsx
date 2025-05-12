import { Card, CardContent, Typography, Box } from '@mui/material';
import ExercisesTable from './ExercisesTable';

const WorkoutCard = ({ workout, isEditing }) => {
    return (
        <Box sx={{ width: '100%', maxWidth: { xs: '100%', md: 800 }, mx: 'auto', px: 2 }}>
            <Card sx={{ minWidth: 275, borderRadius: 2, boxShadow: 3, p: 2 }}>
                <Box sx={{ textAlign: 'center', margin: 2 }}>
                    <Typography variant="h5">
                        {workout.name}
                    </Typography>
                </Box>
                <CardContent>
                    <ExercisesTable exercises={workout.exerciseList} isEditing={isEditing} />
                </CardContent>
            </Card>
        </Box>
    );
};

export default WorkoutCard;
