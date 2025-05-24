import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signUpRequest } from "../requests/SignUpRequests";

const useSignUp = () => {
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSignUp = async (name, password) => {
        setError("");
        try {
            const res = await signUpRequest(name, password);
            localStorage.setItem("token", res.data.token);
            localStorage.setItem("name", res.data.name);
            navigate("/");
        } catch (err) {
            const backendMessage = err.response?.data?.message || 'Sign up failed';
            setError(backendMessage);
        }
    };

    return {
        name,
        setName,
        password,
        setPassword,
        confirmPassword,
        setConfirmPassword,
        error,
        setError,
        handleSignUp,
    };
}

export default useSignUp;
