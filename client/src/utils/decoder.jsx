function decodeBase64Url(str) {
    str = str.replace(/-/g, '+').replace(/_/g, '/');
    while (str.length % 4) str += '=';
    return atob(str);
}

export function getUserIdFromToken(token) {
    if (!token) return null;
    const payload = token.split('.')[1];
    if (!payload) return null;
    try {
        const decoded = JSON.parse(decodeBase64Url(payload));
        return decoded.sub;
    } catch  (err) {
        console.error('Error decoding token:', err);
        return null;
    }
}
