import * as React from 'react';
import { Card, CardContent, Typography, IconButton, Divider, Box } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

export default function WorkoutCard({ workout }) {
  return (
    <Card
      sx={{ flex: '1 1 300px', maxWidth: 350, minWidth: 280, m: 1.5, }}>
      <CardContent>

        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h6">{workout.name}</Typography>
          <Box>
            <IconButton size="small">
              <EditIcon fontSize="small" />
            </IconButton>
            <IconButton size="small">
              <DeleteIcon fontSize="small" />
            </IconButton>
          </Box>
        </Box>

        <Divider sx={{ my: 1.25 }} />

        {workout.exercises.map((ex, idx) => (
          <Typography key={idx} variant="body2" sx={{ mb: 0.75 }}>
            <strong>{ex.name}</strong> - {ex.repetitions} reps x {ex.sets} sets
            <br />
            Weight: {ex.weight} kg
            {ex.note && (<> <br /> <em>Note: {ex.note}</em> </>)}
          </Typography>
        ))}
      </CardContent>
    </Card>
  );
}
