import { Container, Typography, Box } from '@mui/material';
import WorkoutCard from '../components/WorkoutCard';
import gyms from '../data/mockedGyms';

const WorkoutPage = () => {
    const workout = gyms[0].workouts[0];

    return (
        <Container maxWidth="md" sx={{ padding: 2, }}>
            <Box display="flex" justifyContent="center">
                <WorkoutCard workout={workout} />
            </Box>
        </Container>
    );
};

export default WorkoutPage;