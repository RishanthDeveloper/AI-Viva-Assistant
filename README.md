# VivaX - AI-Powered Laboratory Viva Examination Platform

Full-stack viva examination platform: Spring Boot 3 (Java 17) + JWT security backend, vanilla HTML/CSS/JS frontend.

## Architecture
* **Frontend** (`/frontend`) - static files, hosted on GitHub Pages
* **Backend** (`/backend`) - Spring Boot REST API, must be hosted somewhere public (e.g. Render) for the GitHub Pages site to work
* **Database** - in-memory H2 by default (zero config, demo data re-seeded on each start); MySQL / PostgreSQL via env vars

> A page served from `https://...github.io` can NOT call `http://localhost:8080` for other visitors
> (Chrome shows "access other apps and services on this device" and the request fails with
> "Network Error"). The backend has to be deployed to a public HTTPS URL.

## Run locally
```bash
# backend (needs Java 17 + Maven)
cd backend
mvn spring-boot:run          # http://localhost:8080/api/health

# frontend (new terminal)
cd frontend
python -m http.server 8000   # http://localhost:8000/login.html
```
Frontend auto-uses `http://localhost:8080/api` when opened from `localhost` / `127.0.0.1`.

## Deploy the backend (Render, free)
1. Push this repo to GitHub.
2. Render -> **New +** -> **Blueprint** -> select this repo (it reads `render.yaml`), or **New Web Service** -> Runtime **Docker**, Dockerfile path `./backend/Dockerfile`, Docker context `./backend`.
3. Environment variables:
   * `JWT_SECRET` - long random string (32+ chars)
   * `CORS_ORIGINS` - `https://rishanthdeveloper.github.io`
   * optional persistence: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (JDBC URL for MySQL or PostgreSQL)
4. After deploy, open `https://<your-service>.onrender.com/api/health` - it must return `{"status":"UP",...}`.
5. In `frontend/js/api.js` set `PROD_API_URL = 'https://<your-service>.onrender.com/api'`, commit and push; GitHub Pages redeploys.

Free Render services sleep after ~15 min idle; the first login after that can take up to a minute (the login page shows a "Connecting to server" message).

## Demo credentials
* Student: `student@test.com` / `password`
* Teacher: `teacher@test.com` / `password`

## Use Supabase (PostgreSQL) as the database
1. Supabase -> your project -> **Connect** -> **Session pooler** (IPv4 compatible, port 5432). Copy host, user and region.
2. Set these env vars on the backend (Render -> Environment, or your shell locally):
   * `DB_URL` = `jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres?sslmode=require`
   * `DB_USERNAME` = `postgres.<project-ref>`
   * `DB_PASSWORD` = your database password
3. Start the backend. Hibernate creates the tables and `DataSeeder` inserts the demo users once; data now persists across restarts.
4. Run `database/supabase-rls.sql` once in the Supabase SQL Editor to lock the tables against the public anon key.

If you use the Transaction pooler (port 6543) instead, append `&prepareThreshold=0` to `DB_URL`.
The browser never talks to Supabase directly - only the backend does, so no Supabase keys go in the frontend.
