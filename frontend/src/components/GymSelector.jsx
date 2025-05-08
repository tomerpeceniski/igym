import * as React from 'react';
import { FormControl, InputLabel, MenuItem, Select, styled } from '@mui/material';

const CustomFormControl = styled(FormControl)(({ theme }) => ({
  height: '100%',
  color: theme.palette.text.secondary,
  '& .MuiInputLabel-root': {
    color: theme.palette.text.secondary,
    '&.Mui-focused': {
      color: theme.palette.text.secondary,
    },
  },
  '& .MuiOutlinedInput-root': {
    color: theme.palette.text.secondary,
    '& fieldset': {
      borderColor: theme.palette.text.secondary,
    },
    '&:hover fieldset': {
      borderColor: theme.palette.text.secondary,
    },
    '&.Mui-focused fieldset': {
      borderColor: theme.palette.text.secondary,
    },
  },
}))

export default function GymSelector({ gyms, selectedGym, onChange }) {
  return (
    <CustomFormControl fullWidth>
      <InputLabel id="gym-select-label">Select a Gym</InputLabel>
      <Select
        labelId="gym-select-label"
        value={selectedGym}
        label="Select a Gym"
        onChange={onChange}
      >
        {gyms.map((gym, index) => (
          <MenuItem key={gym.id} value={gym.id}>
            {gym.name}
          </MenuItem>
        ))}
      </Select>
    </CustomFormControl>
  );
}
