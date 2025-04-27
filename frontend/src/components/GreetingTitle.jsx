import { Box, Typography } from '@mui/material';
import logo from '../assets/igym-logo.png';

export default function GreetingTitle() {
  return (
    <Box display="flex" alignItems="center" gap={2}>
      <Box component="img" src={logo} alt="iGym Logo" sx={{ height: 40 }}/>
      <Typography variant="h4" component="h1">
        Hello User!
      </Typography>
    </Box>
  );
}