import { Button } from "@mui/material";
import { useTheme } from '@mui/material/styles';
import AddIcon from '@mui/icons-material/Add';

export default function OutlinedButton({ inputText }) {
    const theme = useTheme();

    return (
        <Button
            variant="outlined"
            fullWidth
            startIcon={<AddIcon />}
            sx={{
                width: '100%',
                height: '100%',
                color: theme.palette.white.main,
                borderColor: theme.palette.white.main
            }}
        >
            {inputText}
        </Button>
    );
}
