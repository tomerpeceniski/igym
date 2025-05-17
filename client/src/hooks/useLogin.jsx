import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../requests/LoginRequests';

const useLogin = () => {
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (name, password) => {
        const res = await loginRequest(name, password);
        localStorage.setItem('token', res.token);
        localStorage.setItem('name', res.user.name);
        navigate('/');
    };

    return {
        name,
        setName,
        password,
        setPassword,
        handleLogin
    };
};

export default useLogin;