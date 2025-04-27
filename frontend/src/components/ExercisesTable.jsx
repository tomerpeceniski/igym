import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@mui/material';

const ExercisesTable = ({ exercises, isEditing }) => {
  return (
    <TableContainer>
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
                  <TextField variant="standard" defaultValue={exercise.name} fullWidth />
                ) : (
                  exercise.name
                )}
              </TableCell>
              <TableCell align="right">
                {isEditing ? (
                  <TextField variant="standard" defaultValue={exercise.weight} fullWidth />
                ) : (
                  exercise.weight
                )}
              </TableCell>
              <TableCell align="right">
                {isEditing ? (
                  <TextField variant="standard" defaultValue={exercise.repetitions} fullWidth />
                ) : (
                  exercise.repetitions
                )}
              </TableCell>
              <TableCell align="right">
                {isEditing ? (
                  <TextField variant="standard" defaultValue={exercise.sets} fullWidth />
                ) : (
                  exercise.sets
                )}
              </TableCell>
              <TableCell>
                {isEditing ? (
                  <TextField variant="standard" defaultValue={exercise.note || ''} fullWidth />
                ) : (
                  exercise.note || '-'
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ExercisesTable;
