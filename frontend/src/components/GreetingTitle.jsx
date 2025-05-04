import { Box, Typography } from '@mui/material';
import logo from '../assets/igym-logo.png';

export default function GreetingTitle() {
  return (
    <Box display="flex" alignItems="center" gap={2} height="100%" sx={{ flexShrink: 0 }}>
  <Box
    component="img"
    src={logo}
    alt="iGym Logo"
    sx={{
      height: '100%',
      width: 'auto',
      objectFit: 'contain',
    }}
  />
  <Typography variant="h3" component="h1" sx={{ fontWeight: 500 }}>
    Hello User!
  </Typography>
</Box>
  );
}