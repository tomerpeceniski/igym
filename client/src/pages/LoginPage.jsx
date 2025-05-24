import { Box, TextField, Typography, Button, styled } from '@mui/material';
import { useState, useEffect } from 'react';
import logo from '../assets/igym-logo-text.png';
import AddIcon from '@mui/icons-material/Add';
import useLogin from '../hooks/useLogin.jsx';
import useSignUp from '../hooks/useSignUp.jsx';
import { Snackbar, Alert } from '@mui/material';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';

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
    '& .MuiFormHelperText-root': {
        color: theme.palette.error.main,
        minHeight: '1.5em',
        marginBottom: '0.25em',
        transition: 'min-height 0.2s',
    },
}))

const CustomButton = styled(Button)(({ theme }) => ({
    backgroundColor: theme.palette.background.paper,
    color: theme.palette.background.default
}))

export default function LoginPage() {
    const [isCreatingAccount, setIsCreatingAccount] = useState(false);
    const [errors, setErrors] = useState({ name: '', password: '', confirmPassword: '', passwordMismatch: '' });
    const { name, setName, password, setPassword, error: loginError, setError: setLoginError, handleLogin } = useLogin();
    const { confirmPassword, setConfirmPassword, error: signUpError, setError: setSignUpError, handleSignUp } = useSignUp();
    const [openSnackbar, setOpenSnackbar] = useState(false);

    const handleLoginClick = async () => {
        let newErrors = { name: '', password: '' };
        if (!name) newErrors.name = 'Name is required';
        if (!password) newErrors.password = 'Password is required';
        setErrors(newErrors);
        if (!newErrors.name && !newErrors.password) {
            await handleLogin(name, password);
        }
    }

    const handleSignUpClick = async () => {
        let newErrors = { name: '', password: '', confirmPassword: '', passwordMismatch: '' };
        if (!name) newErrors.name = 'Name is required';
        if (!password) newErrors.password = 'Password is required';
        if (!confirmPassword) newErrors.confirmPassword = 'Confirm Password is required';
        if (password !== confirmPassword) newErrors.passwordMismatch = 'Passwords do not match';
        setErrors(newErrors);
        if (!newErrors.name && !newErrors.password && !newErrors.confirmPassword && !newErrors.passwordMismatch) {
            handleSignUp(name, password);
        }
    }

    useEffect(() => {
        if (loginError || signUpError) setOpenSnackbar(true);
    }, [loginError, signUpError]);

    const handleCloseSnackbar = (event, reason) => {
        if (reason === 'clickaway') return;
        setOpenSnackbar(false);
        setLoginError('');
        setSignUpError('');
    };

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            minHeight="70vh"
        >
            <Box
                component="img"
                src={logo}
                alt="iGym logo"
                width={{ xs: '150px', sm: '200px', md: '250px' }}
                mb={2}
            />
            <Typography variant="h5" color="text.secondary" mb={1}>
                {isCreatingAccount ? 'Create Your Account' : 'Welcome to iGym'}
            </Typography>
            <Box width="100%" maxWidth={400} display="flex" flexDirection="column">
                {/* Text fields group with its own gap and margin bottom*/}
                <Box display="flex" flexDirection="column" mb={0}>
                    <CustomTextField
                        label="Name"
                        required
                        fullWidth
                        value={name}
                        onChange={e => setName(e.target.value)}
                        error={!!errors.name} helperText={errors.name || " "} />

                    <CustomTextField
                        label="Password"
                        required
                        type="password"
                        fullWidth
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        error={!!errors.password}
                        helperText={errors.password || " "} />

                    {isCreatingAccount && (
                        <CustomTextField
                            label="Confirm Password"
                            required
                            type="password"
                            onChange={e => setConfirmPassword(e.target.value)}
                            error={!!errors.password || !!errors.confirmPassword}
                            helperText={
                                errors.confirmPassword
                                    ? errors.confirmPassword
                                    : errors.passwordMismatch || " "
                            }
                            fullWidth />
                    )}
                    <Snackbar
                        open={openSnackbar}
                        autoHideDuration={3000}
                        onClose={handleCloseSnackbar}
                        anchorOrigin={{ vertical: 'top', horizontal: 'left' }}
                    >
                        <Alert
                            severity="error"
                            variant="filled"
                            sx={{ width: '100%' }}>
                            {loginError || signUpError}
                        </Alert>
                    </Snackbar>
                </Box>
                {/* Buttons group with its own gap */}
                <Box display="flex" flexDirection="column" gap={2}>
                    {!isCreatingAccount ? (
                        <>
                            <CustomButton variant="contained" fullWidth onClick={handleLoginClick}>
                                LOG IN
                            </CustomButton>
                            <CustomButton variant="contained" fullWidth startIcon={<AddIcon />} onClick={() => { setIsCreatingAccount(true) }} >
                                SIGN UP
                            </CustomButton>
                        </>
                    ) : (
                        <>
                            <CustomButton variant="contained" fullWidth startIcon={<AddIcon />} onClick={handleSignUpClick} >
                                SIGN UP
                            </CustomButton>
                            <CustomButton variant="contained" fullWidth onClick={() => setIsCreatingAccount(false)} >
                                BACK TO LOGIN
                            </CustomButton>
                        </>
                    )}
                </Box>
            </Box>

        </Box>
    );
}
