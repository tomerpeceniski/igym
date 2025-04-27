import { useState } from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';
import ExercisesTable from './ExercisesTable';
import WorkoutCardActions from './WorkoutCardActions';

const WorkoutCard = ({ workout }) => {
    const [isEditing, setIsEditing] = useState(false);

    const handleEditClick = () => setIsEditing(true);
    const handleSaveClick = () => setIsEditing(false);
    const handleCancelClick = () => setIsEditing(false);
    const handleDeleteClick = () => { };
    const handleCloseClick = () => { };

    return (
        <Box sx={{ width: '100%', maxWidth: { xs: '100%', sm: 600, md: 800 }, mx: 'auto', px: 2 }}>
            <WorkoutCardActions
                isEditing={isEditing}
                onEdit={handleEditClick}
                onSave={handleSaveClick}
                onCancel={handleCancelClick}
                onDelete={handleDeleteClick}
                onClose={handleCloseClick}
            />

            <Card sx={{ minWidth: 275, borderRadius: 2, boxShadow: 3, p: 2 }}>
                <Box sx={{ textAlign: 'center', margin: 2 }}>
                    <Typography variant="h5">
                        {workout.name}
                    </Typography>
                </Box>

                <CardContent>
                    <ExercisesTable exercises={workout.exercises} isEditing={isEditing} />
                </CardContent>
            </Card>
        </Box>
    );
};

export default WorkoutCard;
