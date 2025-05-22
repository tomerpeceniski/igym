import * as React from 'react';
import { FormControl, InputLabel, MenuItem, Select, styled } from '@mui/material';

const CustomFormControl = styled(FormControl)(({ theme, ownerState }) => {
  const white = theme.palette.text.secondary;
  const gray = theme.palette.grey[500];
  const disabledBg = theme.palette.action.disabledBackground;
  const isDisabled = ownerState.disabled;

  return {
    height: '100%',
    width: 180,
    color: isDisabled ? gray : white,
    '& .MuiInputLabel-root': {
      color: isDisabled ? gray : white,
      '&.Mui-focused': {
        color: isDisabled ? gray : white,
      },
      '&.Mui-disabled': {
        color: gray,
      },
    },
    '& .MuiOutlinedInput-root': {
      color: isDisabled ? gray : white,
      '& fieldset': {
        borderColor: isDisabled ? gray : white,
        backgroundColor: isDisabled ? disabledBg : 'transparent',
      },
      '&:hover fieldset': {
        borderColor: isDisabled ? gray : white,
      },
      '&.Mui-focused fieldset': {
        borderColor: isDisabled ? gray : white,
      },
      '&.Mui-disabled fieldset': {
        borderColor: gray,
        backgroundColor: disabledBg,
      },
      '& svg': {
        color: isDisabled ? gray : white,
      },
    },
  };
});

export default function GymSelector({ gyms, selectedGym, onChange, disabled = false }) {
  return (
    <CustomFormControl fullWidth disabled={disabled || gyms.length === 0} ownerState={{ disabled: disabled || gyms.length === 0 }}>
      <InputLabel id="gym-select-label">Select a Gym</InputLabel>
      <Select
        labelId="gym-select-label"
        value={selectedGym ?? ""}
        label="Select a Gym"
        onChange={onChange}
        disabled={disabled || gyms.length === 0}
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
