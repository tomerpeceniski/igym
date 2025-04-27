import { Container, Typography } from '@mui/material';

const WorkoutPage = () => {
    return (
        <Container maxWidth="md" sx={{
            backgroundColor: 'background.default',
            borderRadius: 2,
            padding: 4,
        }}>

            <Typography variant="h2" align="center" gutterBottom>
                Workout A
            </Typography>
        </Container>
    );
};

export default WorkoutPage;