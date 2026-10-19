// ---------------------------------------------------------------
// API configuration
// Local development  -> backend on http://localhost:8080
// GitHub Pages / any other host -> your deployed backend (Render)
// After deploying, set PROD_API_URL to your backend URL + "/api".
// ---------------------------------------------------------------
const LOCAL_API_URL = 'http://localhost:8080/api';
const PROD_API_URL = 'https://vivax-backend.onrender.com/api';
const API_BASE_URL = ['localhost', '127.0.0.1'].includes(window.location.hostname)
    ? LOCAL_API_URL
    : PROD_API_URL;

// Free hosts sleep when idle; the first request can take ~60s to wake the server.
const API_TIMEOUT_MS = 90000;

function escapeHtml(value) {
    return String(value ?? '').replace(/[&<>"']/g, ch => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    }[ch]));
}

async function apiRequest(endpoint, method = 'GET', body = null) {
    const token = localStorage.getItem('token');
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(), API_TIMEOUT_MS);

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method,
            headers,
            body: body ? JSON.stringify(body) : undefined,
            signal: controller.signal
        });

        let data = null;
        try { data = await response.json(); } catch (_) { /* non-JSON body */ }

        // Expired / invalid token on a protected call -> back to login
        if ((response.status === 401 || response.status === 403) && token && !endpoint.startsWith('/auth/')) {
            logout();
            return { success: false, message: 'Session expired. Please log in again.' };
        }

        if (!data) return { success: false, message: `Server error (HTTP ${response.status}).` };
        return data;
    } catch (err) {
        console.error(err);
        if (err.name === 'AbortError') {
            return { success: false, message: 'The server took too long to respond. Please try again.' };
        }
        return {
            success: false,
            message: `Cannot reach the server at ${API_BASE_URL}. Check that the backend is deployed and running.`
        };
    } finally {
        clearTimeout(timer);
    }
}
