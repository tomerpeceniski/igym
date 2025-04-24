import * as React from 'react';
import { Card, CardContent, Typography, IconButton, Divider } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

export default function WorkoutCard({ workout }) {
  return (
    <Card className="workout-card">
      <CardContent>
        <div className="workout-card-header">
          <Typography variant="h6">{workout.name}</Typography>
          <div>
            <IconButton size="small">
              <EditIcon fontSize="small" />
            </IconButton>
            <IconButton size="small">
              <DeleteIcon fontSize="small" />
            </IconButton>
          </div>
        </div>

        <Divider style={{ margin: '10px 0' }} />

        {workout.exercises.map((ex, idx) => (
          <Typography key={idx} variant="body2" style={{ marginBottom: 6 }}>
            <strong>{ex.name}</strong> - {ex.repetitions} reps x {ex.sets} sets  
            <br />
            Weight: {ex.weight} kg
            {ex.note && <><br /><em>Note: {ex.note}</em></>}
          </Typography>
        ))}
      </CardContent>
    </Card>
  );
}
