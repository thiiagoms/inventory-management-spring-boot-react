BACKEND_DIR := backend
FRONTEND_DIR := frontend
MVNW := ./mvnw
PNPM ?= pnpm
MYSQL_DATA_DIR := .devops/backend/mysql/data
BACKEND_PORT ?= 8081
FRONTEND_PORT ?= 5173
COMPOSE_ENV = JWT_SECRET="$${JWT_SECRET:-$$(openssl rand -base64 32)}" JWT_TTL_MINUTES="$${JWT_TTL_MINUTES:-60}" BACKEND_PORT="$(BACKEND_PORT)" FRONTEND_PORT="$(FRONTEND_PORT)"

.PHONY: help up down logs frontend-install clean test format format-check checkstyle spotbugs architecture quality verify

help:
	@echo "Available commands:"
	@echo "  make up            Start the database, backend, and frontend"
	@echo "  make down          Stop the database and backend containers"
	@echo "  make logs          Follow backend and database logs"
	@echo "  make frontend-install  Install frontend dependencies"
	@echo "  make clean         Remove local MySQL database files while keeping .gitignore"
	@echo "  make test          Run backend tests and generate the JaCoCo coverage report"
	@echo "  make format        Apply automatic Java formatting"
	@echo "  make format-check  Verify Java formatting"
	@echo "  make checkstyle    Verify coding standards"
	@echo "  make spotbugs      Run potential-bug analysis"
	@echo "  make architecture  Run Clean Architecture and DDD dependency tests"
	@echo "  make quality       Run every quality gate without the full test suite"
	@echo "  make verify        Run tests and every Maven quality gate"

up:
	@$(COMPOSE_ENV) podman compose up --build --wait --detach
	@echo "Application is ready at http://127.0.0.1:$(FRONTEND_PORT)"
	@echo "Backend is ready at http://localhost:$(BACKEND_PORT)"

down:
	@$(COMPOSE_ENV) podman compose down

logs:
	@$(COMPOSE_ENV) podman compose logs --follow frontend backend database

frontend-install:
	@command -v $(PNPM) >/dev/null 2>&1 || { echo "pnpm is required. Install it before running make up."; exit 1; }
	@cd $(FRONTEND_DIR) && $(PNPM) install

clean:
	find $(MYSQL_DATA_DIR) -mindepth 1 ! -path '$(MYSQL_DATA_DIR)/.gitignore' -delete

test:
	cd $(BACKEND_DIR) && $(MVNW) clean test

format:
	cd $(BACKEND_DIR) && $(MVNW) spotless:apply

format-check:
	cd $(BACKEND_DIR) && $(MVNW) spotless:check

checkstyle:
	cd $(BACKEND_DIR) && $(MVNW) checkstyle:check

spotbugs:
	cd $(BACKEND_DIR) && $(MVNW) -DskipTests compile spotbugs:check

architecture:
	cd $(BACKEND_DIR) && $(MVNW) -Djacoco.skip=true -Dtest='io.thiiagoms.ims.ArchitectureTest' test

quality:
	@$(MAKE) --keep-going format-check checkstyle spotbugs architecture

verify:
	cd $(BACKEND_DIR) && $(MVNW) verify
