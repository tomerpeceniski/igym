import { createTheme } from '@mui/material/styles';
import '@fontsource/poppins';

const theme = createTheme({
    spacing: 8,
    palette: {
        primary: {
            main: '#1976d2',
        },
        secondary: {
            main: '#f50057',
        },
        background: {
            default: '#f9f9f9',
        },
        text: {
            primary: '#333',
        }
    },

    typography: {
        fontFamily: 'Poppins, Roboto, sans-serif',
        h1: {
            fontSize: '2.2rem',
            fontWeight: 600,
        },
        h2: {
            fontSize: '1.6rem',
            fontWeight: 500,
        },
        body1: {
            fontSize: '1rem',
        }
    },
    shape: {
        borderRadius: 8,
    }
});

export default theme;
