import * as React from 'react';
import { FormControl, InputLabel, MenuItem, Select } from '@mui/material';

export default function GymSelector({ gyms, selectedGym, onChange }) {
  return (
    <FormControl fullWidth sx={{ maxWidth: 200}}>
      <InputLabel id="gym-select-label">Gyms</InputLabel>
      <Select
        labelId="gym-select-label"
        value={selectedGym}
        label="Gyms"
        onChange={onChange}
      >
        {gyms.map((gym, index) => (
          <MenuItem key={index} value={gym.name}>
            {gym.name}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}
