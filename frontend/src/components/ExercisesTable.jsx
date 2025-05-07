import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Box, styled , useTheme} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import OutlinedButton from './OutlinedButton';

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

const ExercisesTable = ({ exercises, isEditing }) => {
    const theme = useTheme();
    
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
                                        <CustomTextField
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
                                        <CustomTextField
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
                                        <CustomTextField
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
                                        <CustomTextField
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
                                        <CustomTextField
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

            {isEditing && (
                <Box display="flex" justifyContent="center" mt={2}>
                    <OutlinedButton inputText="New Exercise" color={theme.palette.background.default} startIcon={<AddIcon />} />
                </Box>
            )}
        </>
    );
};

export default ExercisesTable;
