import { Button } from "@mui/material";

export default function OutlinedButton({ inputText, color, startIcon, ...rest }) {
  return (
    <Button
      variant="outlined"
      fullWidth
      startIcon={startIcon}
      sx={{
        width: '100%',
        height: '100%',
        color: color,
        borderColor: color
      }}
      {...rest}
    >
      {inputText}
    </Button>
  );
}