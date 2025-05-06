import { Button } from "@mui/material";
import { useTheme } from '@mui/material/styles';

export default function OutlinedButton({ inputText }) {
  const theme = useTheme();

  return (
    <Button
      variant="outlined"
      sx={{
        color: theme.palette.white.main,
        borderColor: theme.palette.white.main,
        '&:hover': {
          borderColor: theme.palette.white.main,
          backgroundColor: 'rgba(255, 255, 255, 0.08)',
        },
      }}
    >
      {inputText}
    </Button>
  );
}
