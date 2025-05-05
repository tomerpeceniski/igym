import { createTheme } from '@mui/material/styles';
import { blueGrey } from '@mui/material/colors';
import '@fontsource/poppins';

const theme = createTheme({
    spacing: 8,
    palette: {
        primary: {
            main: blueGrey[500],
        },
        secondary: {
            main: '#fff',
            contrastText: '#000',
        },
        background: {
            default: blueGrey[800],
            paper: blueGrey[300],
        },
        text: {
            primary: '#000',
            secondary: '#fff',
        },
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
