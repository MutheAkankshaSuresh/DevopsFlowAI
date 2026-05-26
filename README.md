# DevOpsFlow AI

DevOpsFlow AI is an enterprise-style DevOps workflow platform for tracking engineering tasks, deployments, API health, alerts, audit history, and delivery analytics in one place.

The project is built as a full-stack portfolio application with a Spring Boot backend, React dashboard, JWT security, MySQL persistence, Docker Compose support, Swagger API documentation, and seeded demo data.

## Features

- JWT-based authentication with role-ready demo users
- Task management with status lanes, priorities, deadlines, and AI priority suggestions
- Deployment tracking with version history, deployment status, risk scoring, and failure alerts
- API monitoring with endpoint registry, response-time checks, uptime history, and generated alerts
- Dashboard analytics for tasks, deployments, uptime, active alerts, and delivery insights
- Audit logging for important platform actions
- Swagger/OpenAPI documentation for backend API testing
- Docker Compose setup for MySQL, Spring Boot, and React/Nginx
- Seeded demo data so the app is useful immediately after startup

## Tech Stack

| Layer | Tools |
| --- | --- |
| Frontend | React 18, Vite, Recharts, Lucide React |
| Backend | Java 17, Spring Boot 3, Spring Security, Spring Data JPA |
| Database | MySQL 8.4, H2 fallback for local backend-only runs |
| API Docs | Springdoc OpenAPI / Swagger UI |
| DevOps | Docker, Docker Compose, Nginx |

## Demo Login

| Role | Email | Password |
| --- | --- | --- |
| Admin | `admin@devopsflow.ai` | `admin123` |
| Team Lead | `lead@devopsflow.ai` | `lead123` |
| Developer | `dev@devopsflow.ai` | `dev123` |

## Run With Docker

From the project root:

```bash
docker compose up --build
```

Then open:

- Frontend: `http://localhost:5173`
- Backend API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Run Locally Without Docker

### Backend

The backend can start with H2 when MySQL environment variables are not supplied.

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The Vite development server runs on `http://localhost:5173`.

## Important API Endpoints

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/api/auth/login` | Authenticate a user |
| `POST` | `/api/auth/register` | Register a user |
| `GET` | `/api/dashboard` | Dashboard metrics and insights |
| `GET` | `/api/tasks` | List tasks |
| `POST` | `/api/tasks` | Create a task |
| `POST` | `/api/tasks/suggest-priority` | Suggest task priority |
| `GET` | `/api/deployments` | List deployments |
| `POST` | `/api/deployments` | Create deployment record |
| `GET` | `/api/monitoring/endpoints` | List monitored APIs |
| `POST` | `/api/monitoring/endpoints/{id}/check` | Run API health check |
| `GET` | `/api/alerts` | List alerts |
| `GET` | `/api/audit` | View audit log |

## Project Structure

```text
.
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/devopsflowai/
│       ├── config/        Security, CORS, Swagger, seed data
│       ├── controller/    REST controllers
│       ├── dto/           Request and response models
│       ├── entity/        JPA entities and enums
│       ├── exception/     Global exception handling
│       ├── repository/    Spring Data repositories
│       ├── scheduler/     Background monitoring scheduler
│       ├── security/      JWT filter and token service
│       └── service/       Business logic and AI insights
├── frontend/
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── package.json
│   └── src/
│       ├── api/client.js
│       ├── main.jsx
│       └── styles.css
├── postman/
│   └── devopsflow-ai.postman_collection.json
└── docker-compose.yml
```

## Portfolio Pitch

DevOpsFlow AI solves a common software delivery problem: engineering teams often track tasks, deployments, incidents, API health, and operational history in separate tools. This project brings those workflows together and adds rule-based AI-style insights such as deployment risk scoring, unstable API alerts, and delivery recommendations.

## Future Enhancements

- Refresh token rotation
- Real email or Slack alerts
- WebSocket live monitoring updates
- GitHub Actions CI/CD pipeline
- Fine-grained endpoint permissions by role
- Exportable deployment and uptime reports
