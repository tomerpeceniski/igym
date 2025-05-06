import { Box, Typography } from '@mui/material';
import logo from '../assets/igym-logo.png';

export default function GreetingTitle() {
  return (
    <Box display="flex" alignItems="center" gap={2} >
      <Box component="img" src={logo} alt="iGym Logo" maxHeight={64}  />
      <Typography
        variant="h1"
        component="h2"
        sx={{  color: 'text.secondary' }}
      >
        Hello User!
      </Typography>
    </Box>
  );
}