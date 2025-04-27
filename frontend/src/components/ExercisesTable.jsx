import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Button, Box } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';

const ExercisesTable = ({ exercises, isEditing }) => {
    return (
        <>
            <TableContainer sx={{ overflowX: 'auto' }}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Exercise</TableCell>
                            <TableCell align="right">Weight (kg)</TableCell>
                            <TableCell align="right">Repetitions</TableCell>
                            <TableCell align="right">Sets</TableCell>
                            <TableCell>Note</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {exercises.map((exercise, index) => (
                            <TableRow key={index}>
                                <TableCell>
                                    {isEditing ? (
                                        <TextField
                                            variant="standard"
                                            defaultValue={exercise.name}
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
                                        <TextField
                                            variant="standard"
                                            defaultValue={exercise.weight}
                                            fullWidth
                                        />
                                    ) : (
                                        exercise.weight
                                    )}
                                </TableCell>
                                <TableCell align="right">
                                    {isEditing ? (
                                        <TextField
                                            variant="standard"
                                            defaultValue={exercise.repetitions}
                                            fullWidth
                                        />
                                    ) : (
                                        exercise.repetitions
                                    )}
                                </TableCell>
                                <TableCell align="right">
                                    {isEditing ? (
                                        <TextField
                                            variant="standard"
                                            defaultValue={exercise.sets}
                                            fullWidth
                                        />
                                    ) : (
                                        exercise.sets
                                    )}
                                </TableCell>
                                <TableCell>
                                    {isEditing ? (
                                        <TextField
                                            variant="standard"
                                            defaultValue={exercise.note || ''}
                                            fullWidth
                                            multiline
                                            maxRows={4}
                                        />
                                    ) : (
                                        exercise.note || '-'
                                    )}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            {/* Draft: Add Exercise Button (only visible in edit mode) */}
            {isEditing && (
                <Box display="flex" justifyContent="center" mt={2}>
                    <Button
                        variant="contained"
                        color="primary"
                        startIcon={<AddIcon />}>
                        Add Exercise
                    </Button>
                </Box>
            )}
        </>
    );
};

export default ExercisesTable;
