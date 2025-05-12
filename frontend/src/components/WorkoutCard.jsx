import React from 'react';
import { Card, CardContent, Typography, Box, TextField, Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import ExercisesTable from './ExercisesTable';

const WorkoutCard = ({ workout, isEditing, onWorkoutChange, onExerciseDelete }) => {
    const handleNameChange = (e) => {
        if (onWorkoutChange) {
            onWorkoutChange({
                ...workout,
                name: e.target.value
            });
        }
    };

    const handleExerciseChange = (updatedExercises) => {
        if (onWorkoutChange) {
            onWorkoutChange({
                ...workout,
                exerciseList: updatedExercises
            });
        }
    };

    const handleAddExercise = () => {
        const newExercise = {
            name: '',
            weight: 0,
            numReps: 0,
            numSets: 0,
            note: ''
        };
        if (onWorkoutChange) {
            onWorkoutChange({
                ...workout,
                exerciseList: [...workout.exerciseList, newExercise]
            });
        }
    };

    return (
        <Box sx={{ width: '100%', maxWidth: { xs: '100%', md: 800 }, mx: 'auto', px: 2 }}>
            <Card sx={{ minWidth: 275, borderRadius: 2, boxShadow: 3, p: 2 }}>
                <Box sx={{ textAlign: 'center', margin: 2 }}>
                    {isEditing ? (
                        <TextField
                            value={workout.name}
                            onChange={handleNameChange}
                            variant="standard"
                            fullWidth
                            inputProps={{
                                style: {
                                    fontSize: '1.5rem',
                                    textAlign: 'center',
                                    fontWeight: 'bold'
                                }
                            }}
                        />
                    ) : (
                        <Typography variant="h5">
                            {workout.name}
                        </Typography>
                    )}
                </Box>
                <CardContent>
                    <ExercisesTable
                        exercises={workout.exerciseList}
                        isEditing={isEditing}
                        onExercisesChange={handleExerciseChange}
                        onExerciseDelete={onExerciseDelete}
                    />
                </CardContent>
            </Card>
        </Box>
    );
};

export default WorkoutCard;
