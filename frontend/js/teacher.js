document.addEventListener('DOMContentLoaded', async () => {
    const user = checkAuth('TEACHER');
    if (!user) return;

    const nameEl = document.getElementById('user-name-banner');
    if (nameEl) nameEl.textContent = user.name || 'Teacher';
    const avatar = document.getElementById('profile-initial');
    if (avatar) avatar.textContent = (user.name || 'T').charAt(0).toUpperCase();

    const container = document.getElementById('recent-submissions-container');
    const data = await apiRequest('/teacher/dashboard');

    if (!data.success) {
        container.innerHTML = `<p style="color:#991B1B">${escapeHtml(data.message)}</p>`;
        return;
    }

    const d = data.data;
    document.getElementById('total-vivas').textContent = d.totalVivas;
    document.getElementById('published-vivas').textContent = d.publishedVivas;
    document.getElementById('total-submissions').textContent = d.totalSubmissions;
    document.getElementById('avg-score').textContent = Number(d.avgScore).toFixed(2) + '%';

    if (!d.recentSubmissions.length) {
        container.innerHTML = '<p style="color:#6B7280">No submissions yet.</p>';
        return;
    }

    container.innerHTML = d.recentSubmissions.map(s => `
        <div class="test-item">
            <div class="test-info">
                <h4>👤 ${escapeHtml(s.studentName)} — ${escapeHtml(s.vivaTitle)}</h4>
                <p>📅 ${s.submittedAt ? new Date(s.submittedAt).toLocaleString() : '-'} ⭐ ${escapeHtml(s.score)} (${escapeHtml(s.percentage)}%)</p>
            </div>
            <div class="test-status">✔ Submitted</div>
        </div>
    `).join('');
});
