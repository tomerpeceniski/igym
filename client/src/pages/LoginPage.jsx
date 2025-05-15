import { Box, TextField, Typography, Button, styled } from '@mui/material';
import { useState } from 'react';
import logo from '../assets/igym-logo-text.png';
import AddIcon from '@mui/icons-material/Add';

const CustomTextField = styled(TextField)(({ theme }) => ({
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
}))

const CustomButton = styled(Button)(({ theme }) => ({
    backgroundColor: theme.palette.background.paper,
    color: theme.palette.background.default
}))

export default function LoginPage() {
    const [isCreatingAccount, setIsCreatingAccount] = useState(false);

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            mt={10}
        >
            <Box
                component="img"
                src={logo}
                alt="iGym logo"
                width={{ xs: '200px', sm: '250px', md: '300px' }}
                mb={4}
            />
            <Typography variant="h5" color="text.secondary" mb={2}>
                {isCreatingAccount ? 'Create Your Account' : 'Welcome to iGym'}
            </Typography>
            <Box width="100%" maxWidth={400} display="flex" flexDirection="column" gap={2}>
                <CustomTextField label="Name" fullWidth />
                <CustomTextField label="Password" type="password" fullWidth />
                {isCreatingAccount && (
                    <CustomTextField label="Confirm Password" type="password" fullWidth />
                )}
                {!isCreatingAccount ? (
                    <>
                        <CustomButton variant="contained" fullWidth >
                            LOG IN
                        </CustomButton>
                        <CustomButton variant="contained" fullWidth startIcon={<AddIcon />} onClick={() => setIsCreatingAccount(true)} >
                            CREATE ACCOUNT
                        </CustomButton>
                    </>
                ) : (
                    <>
                        <CustomButton variant="contained" fullWidth startIcon={<AddIcon />} >
                            CREATE ACCOUNT
                        </CustomButton>
                        <CustomButton variant="contained" fullWidth onClick={() => setIsCreatingAccount(false)} >
                            BACK TO LOGIN
                        </CustomButton>
                    </>
                )}
            </Box>

        </Box>
    );
}
