import * as React from 'react';
import { Card, CardContent, Typography, IconButton, Divider, Box, styled } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const CustomCardContent = styled(CardContent)(({ theme }) => ({
  flexGrow: 1,
  overflow: 'auto',
  '&::-webkit-scrollbar-thumb': {
    backgroundColor: theme.palette.background.default,
  },
  scrollbarWidth: 'thin',
  scrollbarColor: `${theme.palette.background.default} transparent`,
}))

export default function WorkoutSummary({ workout }) {
  return (
    <Card sx={{ height: 320, display: 'flex', flexDirection: 'column', boxSizing: 'border-box' }}>

      <Box sx={{ p: 2 }}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h6">{workout.name}</Typography>
          <Box display="flex" gap={1}>
            <IconButton size="small">
              <EditIcon fontSize="small" />
            </IconButton>
            <IconButton size="small">
              <DeleteIcon fontSize="small" />
            </IconButton>
          </Box>
        </Box>
        <Divider sx={{ mt: 1.25 }} />
      </Box>

      <CustomCardContent>
        {workout.exerciseList.map((ex, idx) => (
          <Typography key={idx} variant="body2" sx={{ mb: 0.75 }}>
            <strong>{ex.name}</strong> - {ex.numReps} reps x {ex.numSets} sets
            <br />
            Weight: {ex.weight} kg
            {ex.note && (
              <> <br /> <em>Note: {ex.note}</em> </>
            )}
          </Typography>
        ))}
      </CustomCardContent>
    </Card>
  );
}