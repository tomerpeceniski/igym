import { Button, styled } from '@mui/material';
import LogoutIcon from '@mui/icons-material/Logout';

const StyledButton = styled(Button)(({ theme }) => ({
  backgroundColor: theme.palette.error.light,
  color: theme.palette.error.contrastText,
  borderColor: theme.palette.error.main,
  '&:hover': {
    backgroundColor: theme.palette.error.main,
  },
  marginLeft: theme.spacing(2),
}));

export default function LogoutButton({ onLogout }) {
  return (
    <StyledButton
      variant="outlined"
      startIcon={<LogoutIcon />}
      onClick={onLogout}
    >
      Log Out
    </StyledButton>
  );
} 