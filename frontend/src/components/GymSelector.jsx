import * as React from 'react';
import { FormControl, InputLabel, MenuItem, Select, styled } from '@mui/material';

const CustomFormControl = styled(FormControl)(({ theme }) => {
  const white = theme.palette.text.secondary;

  return {
    height: '100%',
    color: white,
    '& .MuiInputLabel-root': {
      color: white,
      '&.Mui-focused': {
        color: white,
      },
      '&.Mui-disabled': {
        color: white,
      },
    },
    '& .MuiOutlinedInput-root': {
      color: white,
      '& fieldset': {
        borderColor: white,
      },
      '&:hover fieldset': {
        borderColor: white,
      },
      '&.Mui-focused fieldset': {
        borderColor: white,
      },
      '&.Mui-disabled fieldset': {
        borderColor: white,
      },
      '& svg': {
        color: white,
      },
    },
  };
});

export default function GymSelector({ gyms, selectedGym, onChange }) {
  return (
    <CustomFormControl fullWidth>
      <InputLabel id="gym-select-label">Select a Gym</InputLabel>
      <Select
        labelId="gym-select-label"
        value={selectedGym ?? ""}
        label="Select a Gym"
        onChange={onChange}
        disabled={gyms.length === 0}
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
