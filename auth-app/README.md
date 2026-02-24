# Auth App (React + Express + Quarkus + Postgres)

This repo contains:
- **Frontend**: React (Vite) on `http://localhost:5173`
- **Backend #1**: Express + Prisma on `http://localhost:5000`
- **Backend #2**: Quarkus + Postgres on `http://localhost:8080`
- **Database**: Postgres (Docker) on `localhost:5432`

The main idea:
✅ The frontend reads one variable `VITE_API_URL`  
➡️ You switch the backend by changing this value to **5000** (Express) or **8080** (Quarkus) id docker-compose.yml file in the root of the project.

---

Project Ports
| Service | Port |
|--------|------|
| React (Vite) | 5173 |
| Express API | 5000 |
| Quarkus API | 8080 |
| Postgres | 5432 |

---

