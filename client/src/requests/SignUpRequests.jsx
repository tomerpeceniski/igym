import { loginRequest } from "./LoginRequests";

export const signUpRequest = async (name, password) => {
    // Simulate server delay
    await new Promise((resolve) => setTimeout(resolve, 500));

    // Simulate a successful sign-up by calling the loginRequest
    const res = await loginRequest(name, password);

    return {
        token: res.token,
        user: {
            name,
        },
    };
}
