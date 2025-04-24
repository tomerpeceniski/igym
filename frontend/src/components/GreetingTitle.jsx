import Typography from '@mui/material/Typography';
import logo from '../assets/igym-logo.png'; 

export default function GreetingTitle() {
  return (
    <div style={{ display: 'flex'}}>
      <img src={logo} alt="iGym Logo" style={{ height: '40px' }} />
      <Typography variant="h4" component="h1">
        Hello User!
      </Typography>
    </div>
  );
}