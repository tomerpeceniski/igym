import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signUpRequest } from "../requests/SignUpRequests";

const useSignUp = () => {
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errors, setErrors] = useState({ name: "", password: "", confirmPassword: "" });
    const navigate = useNavigate();

    const handleSignUp = async (name, password) => {
        const res = await signUpRequest(name, password);
        localStorage.setItem("token", res.data.token);
        localStorage.setItem("name", res.data.name);
        navigate("/");
    };

    return {
        name,
        setName,
        password,
        setPassword,
        confirmPassword,
        setConfirmPassword,
        errors,
        setErrors,
        handleSignUp,
    };
}

export default useSignUp;
