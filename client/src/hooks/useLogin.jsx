import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../requests/LoginRequests';

const useLogin = () => {
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (name, password) => {
        setError('');
        try {
            const res = await loginRequest(name, password);
            localStorage.setItem('token', res.data.token);
            localStorage.setItem('name', res.data.name);
            navigate('/');
        } catch (err) {
            const backendMessage = err.response?.data?.message || 'Login failed';
            setError(backendMessage);
        }
    };

    return {
        name,
        setName,
        password,
        setPassword,
        error,
        setError,
        handleLogin
    };
};

export default useLogin;