// Works on localhost AND on GitHub Pages project sites (/AI-Lab-Viva-Assistant/frontend/...)
// FRONTEND_ROOT = URL of the "frontend/" folder, derived from where this script was loaded.
const FRONTEND_ROOT = new URL('../', document.currentScript.src).href;

function goTo(path) {
    window.location.href = FRONTEND_ROOT + path;
}

function getStoredUser() {
    try {
        return JSON.parse(localStorage.getItem('user'));
    } catch (_) {
        return null;
    }
}

function dashboardPathFor(role) {
    if (role === 'STUDENT') return 'student/dashboard.html';
    if (role === 'TEACHER') return 'teacher/dashboard.html';
    return null;
}

async function login(email, password) {
    const data = await apiRequest('/auth/login', 'POST', { email, password });
    if (!data.success) return data;

    const user = data.data.user;
    const target = dashboardPathFor(user.role);
    if (!target) {
        return { success: false, message: `No dashboard available for role ${user.role}.` };
    }

    localStorage.setItem('token', data.data.token);
    localStorage.setItem('user', JSON.stringify(user));
    goTo(target);
    return data;
}

// Call on protected pages. Pass the role the page is meant for.
function checkAuth(expectedRole) {
    const token = localStorage.getItem('token');
    const user = getStoredUser();
    if (!token || !user) {
        goTo('login.html');
        return null;
    }
    if (expectedRole && user.role !== expectedRole) {
        const target = dashboardPathFor(user.role);
        goTo(target || 'login.html');
        return null;
    }
    return user;
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    goTo('login.html');
}
