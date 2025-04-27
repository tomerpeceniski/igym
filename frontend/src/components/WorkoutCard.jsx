import { Card, CardHeader, CardContent, Typography } from '@mui/material';
import ExercisesTable from './ExercisesTable';

const WorkoutCard = ({ workout }) => {
    return (
        <Card sx={{ minWidth: 275, borderRadius: 2, boxShadow: 3 }}>
            <CardHeader title={workout.name} sx={{ textAlign: 'center' }} />
            <CardContent>
                <ExercisesTable exercises={workout.exercises} />
            </CardContent>
        </Card>
    );
};

export default WorkoutCard;

