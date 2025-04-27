import { Card, CardHeader, CardContent, Typography } from '@mui/material';

const WorkoutCard = ({ workout }) => {
    return (
        <Card sx={{ minWidth: 275, borderRadius: 2, boxShadow: 3 }}>
            <CardHeader title={workout.name} sx={{ textAlign: 'center' }}/>

            <CardContent>
                <Typography variant="body1">
                    Exercise Table
                </Typography>
            </CardContent>
        </Card>
    );
};

export default WorkoutCard;

