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
        return decoded.userId;
    } catch {
        return null;
    }
}

export function isTokenExpired(token) {
    if (!token) return true;
    const payload = token.split('.')[1];
    if (!payload) return true;
    try {
        const decoded = JSON.parse(decodeBase64Url(payload));
        if (!decoded.exp) return true;
        // exp is in seconds since epoch
        const now = Math.floor(Date.now() / 1000);
        return decoded.exp < now;
    } catch {
        return true;
    }
}
