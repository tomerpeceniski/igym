import { Box, Typography } from '@mui/material';
import logo from '../assets/igym-logo.png';

export default function GreetingTitle({ name }) {

  const greeting = ((name) => {
    return (
      <Typography
        variant="h3"
        sx={{
          color: 'text.secondary',
          fontSize: { xs: '1.8rem', sm: '2.5rem' }
        }}
      >
        Hello {name}!
      </Typography>
    )
  })

  return (
    <Box
      display="flex"
      alignItems="center"
      gap={2} >
      <Box
        component="img"
        src={logo}
        alt="iGym Logo"
        sx={{
          maxHeight: { xs: 40, sm: 64 }
        }}
      />
      {greeting(name)}
    </Box>
  );
}