import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';

const ExercisesTable = ({ exercises }) => {
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
              <TableCell>{exercise.name}</TableCell>
              <TableCell align="right">{exercise.weight}</TableCell>
              <TableCell align="right">{exercise.repetitions}</TableCell>
              <TableCell align="right">{exercise.sets}</TableCell>
              <TableCell>{exercise.note || '-'}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ExercisesTable;