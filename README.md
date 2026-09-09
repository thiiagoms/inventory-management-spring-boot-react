# 📦 Inventory Management

A full-stack inventory management application with authenticated management flows for users,
categories, suppliers, and products. The React frontend is intentionally thin and delegates
validation and business rules to the Spring Boot API.

## ✨ Features

- User registration and JWT authentication
- Category CRUD
- Supplier CRUD with unique Social Name and CNPJ
- Address lookup through ViaCEP
- Product CRUD with category and supplier associations
- OpenAPI documentation, database migrations, and automated architecture checks

## 🧰 Technology stack

### ☕ Backend

- Java 21
- Spring Boot 4
- Spring Web MVC, Spring Data JPA, Spring Security, and Bean Validation
- JWT authentication with JJWT
- MySQL 8.4
- Flyway database migrations
- SpringDoc OpenAPI and Swagger UI
- JUnit, MockMvc, ArchUnit, JaCoCo, Checkstyle, Spotless, and SpotBugs

### ⚛️ Frontend

- React 19 and TypeScript 6
- Vite 8
- Material UI
- React Router
- Axios
- Oxlint

### 🐳 Development environment

- Podman Compose
- GNU Make entrypoints
- One shared `.env` file at the repository root

## 🏗️ Architecture notes

The backend is organized by feature (`user`, `category`, `supplier`, and `product`). Each feature
follows inward dependencies across `domain`, `application`, `infrastructure`, and `presentation`
packages. Shared identity, pagination, persistence, security, and HTTP error concerns live under
`shared`. ArchUnit tests enforce that domain and application code do not depend on outer layers.

The frontend uses a small feature-oriented structure. Pages own their local React state, feature
API modules call one centralized Axios client, React Router controls public and protected routes,
and Material UI supplies the layout and form components. The frontend does not duplicate backend
business rules.

## 🚀 Run the complete application

### Prerequisites

- Podman with Compose support
- Make
- OpenSSL (used by the Makefile when a JWT secret is not already configured)

Create the single root environment file and start all services:

```bash
cp .env.example .env
make up
```

The command builds and starts MySQL, the Spring Boot backend, and the Vite frontend. Container
health checks complete before the command reports that the application is ready.

## 🌐 Local URLs and ports

| Service      | Default port | URL                                           |
| ------------ | -----------: | --------------------------------------------- |
| Frontend     |       `5173` | <http://127.0.0.1:5173>                       |
| Backend API  |       `8081` | <http://127.0.0.1:8081>                       |
| Swagger UI   |       `8081` | <http://127.0.0.1:8081/swagger-ui/index.html> |
| OpenAPI JSON |       `8081` | <http://127.0.0.1:8081/v3/api-docs>           |
| Health check |       `8081` | <http://127.0.0.1:8081/actuator/health>       |
| MySQL        |       `3306` | `127.0.0.1:3306`                              |

Swagger does not use a separate port; it is exposed by the backend. The current security policy
requires authentication for Swagger and OpenAPI routes. Public access is limited to user
registration, authentication, and the health endpoint.

Ports can be changed in the root `.env` file. `VITE_API_URL` must match the backend address used
by a locally running frontend.

## 🔐 First use

1. Open the frontend.
2. Select **Don’t Have An Account? Sign Up** and register a user.
3. Sign in with the registered e-mail and password.
4. Create a category and supplier.
5. Create a product and associate it with the category and supplier.

The frontend stores the JWT in browser session storage and attaches it to authenticated API
requests.

## 📖 API documentation

See [API_DOCS.md](API_DOCS.md) for authentication, endpoint mappings, payload examples,
pagination, status codes, and the common error response.

## 🛠️ Useful commands

```bash
make help              # List all available commands
make up                # Build and start the complete application
make logs              # Follow frontend, backend, and database logs
make down              # Stop the containers
make frontend-install  # Install frontend dependencies
make test              # Run backend tests and generate JaCoCo coverage
make quality           # Run formatting, Checkstyle, SpotBugs, and architecture checks
make verify            # Run the complete Maven verification lifecycle
```

MySQL data persists under `.devops/backend/mysql/data`. After stopping the application,
`make clean` removes that local database data and cannot be undone.

## ⚙️ Environment configuration

The root [.env.example](.env.example) documents all supported settings:

- `FRONTEND_PORT` and `BACKEND_PORT`
- `VITE_API_URL` and `VITE_API_DOCKER_URL`
- MySQL port, database, and credentials
- JVM container options
- Base64-encoded JWT secret and token lifetime

Do not commit the generated `.env` file or real credentials.
