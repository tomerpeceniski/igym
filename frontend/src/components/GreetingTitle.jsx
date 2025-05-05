import { Box, Typography } from '@mui/material';
import logo from '../assets/igym-logo.png';

export default function GreetingTitle() {
  return (
    <Box display="flex" alignItems="center" gap={2} sx={(theme) => ({ height: theme.spacing(8) })}>
      <Box component="img" src={logo} alt="iGym Logo" sx={{ height: '100%' }} />
      <Typography
        variant="h3"
        component="h1"
        sx={{ fontWeight: 500, color: 'text.secondary' }}
      >
        Hello User!
      </Typography>
    </Box>
  );
}