document.addEventListener('DOMContentLoaded', async () => {
    const user = checkAuth('STUDENT');
    if (!user) return;

    const nameEl = document.getElementById('user-name-banner');
    if (nameEl) nameEl.textContent = user.name || 'Student';
    const avatar = document.getElementById('profile-initial');
    if (avatar) avatar.textContent = (user.name || 'S').charAt(0).toUpperCase();

    const container = document.getElementById('recent-tests-container');
    const data = await apiRequest('/student/dashboard');

    if (!data.success) {
        container.innerHTML = `<p style="color:#991B1B">${escapeHtml(data.message)}</p>`;
        return;
    }

    const d = data.data;
    document.getElementById('total-tests').textContent = d.totalTests;
    document.getElementById('completed-tests').textContent = d.completed;
    document.getElementById('upcoming-tests').textContent = d.upcoming;
    document.getElementById('avg-score').textContent = Number(d.avgScore).toFixed(2) + '%';

    if (!d.recentTests.length) {
        container.innerHTML = '<p style="color:#6B7280">No completed tests yet.</p>';
        return;
    }

    container.innerHTML = d.recentTests.map(test => `
        <div class="test-item">
            <div class="test-info">
                <h4>📄 ${escapeHtml(test.vivaTitle)}</h4>
                <p>📅 ${test.submittedAt ? new Date(test.submittedAt).toLocaleString() : '-'} ⭐ ${escapeHtml(test.score)} (${escapeHtml(test.percentage)}%)</p>
            </div>
            <div class="test-status">✔ Completed</div>
        </div>
    `).join('');
});
