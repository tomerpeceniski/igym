import { Box, TextField, Typography, useTheme, Button } from '@mui/material';
import logo from '../assets/igym-logo-text.png';
import OutlinedButton from '../components/OutlinedButton';
import AddIcon from '@mui/icons-material/Add';

export default function LoginPage() {
    const theme = useTheme();
    const textFieldSx = {

        '& .MuiOutlinedInput-root': {
            '& fieldset': {
                borderColor: theme.palette.secondary.main,
            },
            '&:hover fieldset': {
                borderColor: theme.palette.secondary.main,
            },
            '&.Mui-focused fieldset': {
                borderColor: theme.palette.secondary.main,
            },
        },

        '& .MuiInputLabel-root': {
            color: theme.palette.secondary.main,
        },
        '& label.Mui-focused': {
            color: theme.palette.secondary.main,
        },

        input: {
            color: theme.palette.secondary.main,
        },
    }

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            minHeight="100vh"
        >
            <Box
                component="img"
                src={logo}
                alt="iGym logo"
                maxWidth="80%"
                width={{ xs: '200px', sm: '250px', md: '300px' }}
                mb={4}
            />
            <Typography variant="h5" color="text.secondary" mb={2}>
                Welcome to iGym
            </Typography>
            <Box width="100%" maxWidth={400} display="flex" flexDirection="column" gap={2}>
                <TextField label="Email" fullWidth sx={textFieldSx} />
                <TextField label="Password" type="password" fullWidth sx={textFieldSx} />
                <Button variant="contained" fullWidth sx={{ backgroundColor: theme.palette.background.paper, color: theme.palette.background.default }}>
                    LOG IN
                </Button>
                <Button variant="contained" fullWidth
                    sx={{ backgroundColor: theme.palette.background.paper, color: theme.palette.background.default }} startIcon={<AddIcon />} >
                    CREATE ACCOUNT
                </Button>
            </Box>
        </Box>
    );
}
