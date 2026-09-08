# Inventory Management

## Run the complete application

The backend, database, and frontend share the single `.env` file in the repository root.

```bash
cp .env.example .env
make up
```

Open `http://127.0.0.1:5173` by default. To follow container output or stop the application:

```bash
make logs
make down
```

The exposed ports can be changed in the root `.env` file.
