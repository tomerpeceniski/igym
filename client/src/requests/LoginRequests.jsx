import fakeID from "../data/fakeID.js";

// Simulate a base64url encode for JWT
function base64url(source) {
    let encoded = btoa(JSON.stringify(source));
    return encoded.replace(/=+$/, '').replace(/\+/g, '-').replace(/\//g, '_');
}

export const loginRequest = async (name, password) => {
    // Simulate server delay
    await new Promise((resolve) => setTimeout(resolve, 500));

    // JWT header and payload
    const now = Math.floor(Date.now() / 1000);
    const header = { alg: "HS256", typ: "JWT" };
    const payload = {
        userId: fakeID,
        name,
        iat: now,
        exp: now + 30, // expires in 30 seconds
    };

    const encodedHeader = base64url(header);
    const encodedPayload = base64url(payload);
    const signature = "simulatedsignature";
    const token = `${encodedHeader}.${encodedPayload}.${signature}`;

    return {
        token,
        user: {
            name,
        },
    };
};