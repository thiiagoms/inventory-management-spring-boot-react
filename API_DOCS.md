# Inventory Management API

This document summarizes the backend HTTP contract implemented by the Spring Boot controllers.
For the generated OpenAPI definition, use Swagger UI or the raw JSON endpoint listed below.

## Base URLs

| Resource     | Local URL                                     |
| ------------ | --------------------------------------------- |
| API base URL | `http://127.0.0.1:8081`                       |
| Swagger UI   | `http://127.0.0.1:8081/swagger-ui/index.html` |
| OpenAPI JSON | `http://127.0.0.1:8081/v3/api-docs`           |
| Health check | `http://127.0.0.1:8081/actuator/health`       |

Swagger and OpenAPI use the backend port. The port can be changed with `BACKEND_PORT` in the root
`.env` file.

## Authentication

The API uses stateless JWT bearer authentication. Only these endpoints are public:

- `POST /api/users`
- `POST /api/users/authenticate`
- `GET /actuator/health`

All other endpoints, including the Swagger/OpenAPI routes, require this header:

```http
Authorization: Bearer <token>
```

Authenticate to obtain a token:

```http
POST /api/users/authenticate
Content-Type: application/json

{
  "email": "john.doe@gmail.com",
  "password": "Strong@123"
}
```

Successful response (`200 OK`):

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresAt": "2026-09-09T02:00:00Z"
}
```

## Endpoint overview

| Method   | Endpoint                  | Authentication | Successful status | Purpose                                 |
| -------- | ------------------------- | -------------- | ----------------: | --------------------------------------- |
| `POST`   | `/api/users`              | Public         |             `201` | Register a user                         |
| `POST`   | `/api/users/authenticate` | Public         |             `200` | Authenticate and obtain a JWT           |
| `PATCH`  | `/api/users/{id}`         | Owner          |             `200` | Update the authenticated user's profile |
| `DELETE` | `/api/users/{id}`         | Owner          |             `204` | Delete the authenticated user's profile |
| `POST`   | `/api/categories`         | Required       |             `201` | Create a category                       |
| `GET`    | `/api/categories`         | Required       |             `200` | List categories                         |
| `GET`    | `/api/categories/{id}`    | Required       |             `200` | Retrieve a category                     |
| `PATCH`  | `/api/categories/{id}`    | Required       |             `200` | Update a category                       |
| `DELETE` | `/api/categories/{id}`    | Required       |             `204` | Delete a category                       |
| `POST`   | `/api/suppliers`          | Required       |             `201` | Create a supplier                       |
| `GET`    | `/api/suppliers`          | Required       |             `200` | List suppliers                          |
| `GET`    | `/api/suppliers/{id}`     | Required       |             `200` | Retrieve a supplier                     |
| `PATCH`  | `/api/suppliers/{id}`     | Required       |             `200` | Update a supplier                       |
| `DELETE` | `/api/suppliers/{id}`     | Required       |             `204` | Delete a supplier                       |
| `POST`   | `/api/products`           | Required       |             `201` | Create a product                        |
| `GET`    | `/api/products`           | Required       |             `200` | List products                           |
| `GET`    | `/api/products/{id}`      | Required       |             `200` | Retrieve a product                      |
| `PATCH`  | `/api/products/{id}`      | Required       |             `200` | Update a product                        |
| `DELETE` | `/api/products/{id}`      | Required       |             `204` | Delete a product                        |
| `GET`    | `/actuator/health`        | Public         |             `200` | Read application health                 |

`{id}` values are UUID strings.

## Users

### Register a user

```http
POST /api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@gmail.com",
  "password": "Strong@123",
  "phone": "11999999999"
}
```

E-mail and phone must be unique. Brazilian phone formatting is accepted and normalized by the
backend. Password policy and all other domain validation are enforced by the backend.

Response (`201 Created`):

```json
{
  "id": "2f6d26c2-4c34-4fa2-a90e-469e8b594ae9",
  "name": "John Doe",
  "email": "john.doe@gmail.com",
  "phone": "11999999999"
}
```

### Update a user

```http
PATCH /api/users/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane.doe@gmail.com",
  "password": "NewStrong@123",
  "phone": "11988887777"
}
```

Every field is optional. The authenticated user ID in the JWT must match `{id}`; otherwise the API
returns `403 Forbidden`.

## Categories

### Create a category

```http
POST /api/categories
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Electronics",
  "description": "Electronic devices and accessories"
}
```

Category titles must be unique.

Category response:

```json
{
  "id": "c83f2ce6-f9ce-44b1-9da0-e22d1a786e60",
  "title": "Electronics",
  "description": "Electronic devices and accessories"
}
```

### Update a category

```json
{
  "title": "Consumer Electronics",
  "description": "Updated description"
}
```

Both fields are optional for `PATCH /api/categories/{id}`.

## Suppliers

### Create a supplier

```http
POST /api/suppliers
Authorization: Bearer <token>
Content-Type: application/json

{
  "socialName": "Acme Supplies Ltda",
  "cnpj": "11.222.333/0001-81",
  "address": "Praça da Sé, São Paulo - SP, 01001-000"
}
```

Social Name and CNPJ must be unique. The backend validates and stores CNPJ as 14 digits. CNPJ is
immutable after registration.

Supplier response:

```json
{
  "id": "8e658419-4718-4270-ad5f-d5b11d0dff30",
  "socialName": "Acme Supplies Ltda",
  "cnpj": "11222333000181",
  "address": "Praça da Sé, São Paulo - SP, 01001-000",
  "createdAt": "2026-09-09T01:13:37.440566Z"
}
```

### Update a supplier

```http
PATCH /api/suppliers/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "socialName": "Acme Distribution Ltda",
  "address": "Avenida Paulista, São Paulo - SP, 01310-100"
}
```

Both fields are optional. CNPJ is deliberately absent from the update contract.

ViaCEP lookup is a frontend integration and is not proxied through a backend endpoint.

## Products

### Create a product

```http
POST /api/products
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Wireless Keyboard",
  "description": "Compact Bluetooth keyboard",
  "imageUrl": "https://example.com/keyboard.jpg",
  "price": 199.90,
  "stockQuantity": 25,
  "categoryIds": ["c83f2ce6-f9ce-44b1-9da0-e22d1a786e60"],
  "supplierId": "8e658419-4718-4270-ad5f-d5b11d0dff30",
  "expiryDate": "2027-12-31T23:59:59"
}
```

Price and stock quantity must be positive. Category and supplier IDs must reference existing
resources. Every product belongs to one supplier and can belong to multiple categories. SKU is
generated by the backend.

Product response:

```json
{
  "id": "31dbca57-77a1-4336-bd50-b9b9da8a73e8",
  "title": "Wireless Keyboard",
  "description": "Compact Bluetooth keyboard",
  "sku": "wireless-keyboard-550e8400-e29b-41d4-a716-446655440000-1788912000000",
  "imageUrl": "https://example.com/keyboard.jpg",
  "price": 199.90,
  "stockQuantity": 25,
  "categoryIds": ["c83f2ce6-f9ce-44b1-9da0-e22d1a786e60"],
  "supplierId": "8e658419-4718-4270-ad5f-d5b11d0dff30",
  "expiryDate": "2027-12-31T23:59:59"
}
```

### Update a product

`PATCH /api/products/{id}` accepts any subset of:

```json
{
  "title": "Wireless Keyboard Pro",
  "description": "Updated description",
  "imageUrl": "https://example.com/keyboard-pro.jpg",
  "price": 249.90,
  "stockQuantity": 30
}
```

SKU, categories, supplier, and expiry date are not part of the current update contract.

## Pagination

Category, supplier, and product list endpoints accept:

- `page`: zero-based page index; default `0`
- `size`: number of items per page; default `20`

Example:

```http
GET /api/products?page=0&size=20
Authorization: Bearer <token>
```

Paginated response shape:

```json
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true
}
```

## Errors and status codes

The API uses this structured error response when an application or validation error is available:

```json
{
  "timestamp": "2026-09-09T01:00:00Z",
  "status": 409,
  "error": "resource_already_exists",
  "field": "socialName",
  "message": "A supplier with this Social Name already exists."
}
```

|                      Status | Meaning                                                                          |
| --------------------------: | -------------------------------------------------------------------------------- |
|           `400 Bad Request` | Invalid request field or domain value                                            |
|          `401 Unauthorized` | Missing, invalid, or expired JWT; or invalid login credentials                   |
|             `403 Forbidden` | Authenticated user does not own the requested user profile                       |
|             `404 Not Found` | Requested or associated resource does not exist                                  |
|              `409 Conflict` | A unique value such as e-mail, phone, title, Social Name, or CNPJ already exists |
| `422 Unprocessable Content` | A PATCH request did not produce any changes                                      |
| `500 Internal Server Error` | Unexpected backend failure                                                       |

Clients should display `message` and may associate it with the supplied `field`. Backend stack
traces are not part of the HTTP response contract.
