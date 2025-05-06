import * as React from 'react';
import { FormControl, InputLabel, MenuItem, Select } from '@mui/material';

export default function GymSelector({ gyms, selectedGym, onChange }) {
  return (
    <FormControl
      fullWidth
      sx={{
        height: '100%',
        color: 'text.secondary',
        '& .MuiInputLabel-root': {
          color: 'text.secondary',
        },
        '& .MuiOutlinedInput-root': {
          color: 'text.secondary',
          '& fieldset': {
            borderColor: 'text.secondary',
          },
          '&:hover fieldset': {
            borderColor: 'text.secondary',
          },
          '&.Mui-focused fieldset': {
            borderColor: 'text.secondary',
          },
        },
      }}
    >
      <InputLabel id="gym-select-label">Select a Gym</InputLabel>
      <Select
        labelId="gym-select-label"
        value={selectedGym}
        label="Select a Gym"
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
