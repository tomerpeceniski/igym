import React from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    IconButton,
    TextField,
    Tooltip,
    Box,
    styled,
    useTheme,
    Button
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';

const CustomTextField = styled(TextField)(({ theme }) => ({
    '& .MuiInput-underline:before': {
        borderBottomColor: theme.palette.background.default,
    },
    '& .MuiInput-underline:hover:before': {
        borderBottomColor: theme.palette.background.default,
    },
    '& .MuiInput-underline:after': {
        borderBottomColor: theme.palette.background.default,
    }
}))

const CustomButton = styled(Button)(({ theme }) => ({
    backgroundColor: theme.palette.background.default,
    color: theme.palette.background.paper,
    width: '100%',
}))

const ExercisesTable = ({ exercises, isEditing, onExercisesChange, onExerciseDelete }) => {
    const theme = useTheme();

    const handleExerciseChange = (index, field, value) => {
        const updatedExercises = exercises.map((exercise, i) => {
            if (i === index) {
                return {
                    ...exercise,
                    [field]: value
                };
            }
            return exercise;
        });
        onExercisesChange?.(updatedExercises);
    };

    const handleAddExercise = () => {
        const newExercise = {
            name: '',
            weight: 0,
            numReps: 0,
            numSets: 0,
            note: ''
        };
        onExercisesChange?.([...exercises, newExercise]);
    };

    const handleDeleteExercise = (index, exerciseId) => {
        const confirmed = window.confirm('Are you sure you want to delete this exercise?');
        if (!confirmed) return;

        if (exerciseId) {
            onExerciseDelete(exerciseId);
        } else {
            const updatedExercises = exercises.filter((_, i) => i !== index);
            onExercisesChange?.(updatedExercises);
        }
    };

    return (
        <>
            <TableContainer component={Paper} sx={{ mt: 2 }}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Exercise</TableCell>
                            <TableCell align="right">Weight (kg)</TableCell>
                            <TableCell align="right">Repetitions</TableCell>
                            <TableCell align="right">Sets</TableCell>
                            <TableCell>Note</TableCell>
                            {isEditing && <TableCell align="center">Actions</TableCell>}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {exercises.map((exercise, index) => (
                            <TableRow key={exercise.id || index}>
                                <TableCell>
                                    {isEditing ? (
                                        <CustomTextField
                                            variant="standard"
                                            value={exercise.name}
                                            onChange={(e) => handleExerciseChange(index, 'name', e.target.value)}
                                            fullWidth
                                            multiline
                                            maxRows={2}
                                        />
                                    ) : (
                                        exercise.name
                                    )}
                                </TableCell>
                                <TableCell align="right">
                                    {isEditing ? (
                                        <CustomTextField
                                            variant="standard"
                                            type="number"
                                            value={exercise.weight}
                                            onChange={(e) => handleExerciseChange(index, 'weight', parseFloat(e.target.value) || 0)}
                                            fullWidth
                                        />
                                    ) : (
                                        exercise.weight
                                    )}
                                </TableCell>
                                <TableCell align="right">
                                    {isEditing ? (
                                        <CustomTextField
                                            variant="standard"
                                            type="number"
                                            value={exercise.numReps}
                                            onChange={(e) => handleExerciseChange(index, 'numReps', parseInt(e.target.value) || 0)}
                                            fullWidth
                                        />
                                    ) : (
                                        exercise.numReps
                                    )}
                                </TableCell>
                                <TableCell align="right">
                                    {isEditing ? (
                                        <CustomTextField
                                            variant="standard"
                                            type="number"
                                            value={exercise.numSets}
                                            onChange={(e) => handleExerciseChange(index, 'numSets', parseInt(e.target.value) || 0)}
                                            fullWidth
                                        />
                                    ) : (
                                        exercise.numSets
                                    )}
                                </TableCell>
                                <TableCell>
                                    {isEditing ? (
                                        <CustomTextField
                                            variant="standard"
                                            value={exercise.note || ''}
                                            onChange={(e) => handleExerciseChange(index, 'note', e.target.value)}
                                            fullWidth
                                            multiline
                                            maxRows={4}
                                        />
                                    ) : (
                                        exercise.note || '-'
                                    )}
                                </TableCell>
                                {isEditing && (
                                    <TableCell align="center">
                                        <Tooltip title="Delete exercise">
                                            <IconButton
                                                onClick={() => handleDeleteExercise(index, exercise.id)}
                                                color="error"
                                                size="small"
                                            >
                                                <DeleteIcon />
                                            </IconButton>
                                        </Tooltip>
                                    </TableCell>
                                )}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            {isEditing && (
                <Box display="flex" justifyContent="center" mt={2}>
                    <CustomButton variant="contained" startIcon={<AddIcon />} onClick={handleAddExercise}>
                        New Exercise
                    </CustomButton>
                </Box>
            )}
        </>
    );
};

export default ExercisesTable;
