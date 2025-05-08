import { useParams } from 'react-router-dom';
import { Container, Typography, Box, CircularProgress } from '@mui/material';
import WorkoutCard from '../components/WorkoutCard';
import { useWorkoutById } from '../hooks/useWorkoutById';

const WorkoutPage = () => {
    const { id } = useParams();
    const { workout, loading, error } = useWorkoutById(id);

    if (loading) return <CircularProgress />;
    if (error || !workout) return <Typography>Error loading workout.</Typography>;

    return (
        <Container maxWidth="md" sx={{ padding: 2, }}>
            <Box display="flex" justifyContent="center">
                <WorkoutCard workout={workout} />
            </Box>
        </Container>
    );
};

export default WorkoutPage;