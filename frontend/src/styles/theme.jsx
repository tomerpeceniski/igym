import { createTheme } from '@mui/material/styles';
import { blueGrey } from '@mui/material/colors';
import '@fontsource/poppins';

const white = '#fff';
const black = '#000';

const theme = createTheme({
    spacing: 8,
    palette: {
        primary: {
            main: blueGrey[500],
        },
        secondary: {
            main: white,
            contrastText: black,
        },
        background: {
            default: blueGrey[800],
            paper: white,
        },
        text: {
            primary: black,
            secondary: white,
        },
    },

    typography: {
        fontFamily: 'Poppins, Roboto, sans-serif',
        h1: {
            fontSize: '4rem',
            fontWeight: 500,
        },
        h2: {
            fontSize: '2.5rem',
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
