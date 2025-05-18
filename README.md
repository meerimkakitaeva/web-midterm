# 📝 Notes App — Spring Boot Project

This is a backend application developed with Spring Boot as part of the Web Development Final Exam. It allows users to register, authenticate (with JWT and optional OAuth2), and manage notes securely.

## 🔐 Features

### ✅ User Authentication
- Traditional email/password login
- Secure password hashing with BCrypt
- JWT token-based authentication:
  - Access token (1 hour)
  - Refresh token (7 days)
- Refresh tokens are stored in the database
- Token validation and renewal endpoint

### 🧾 User Registration
- User registration with username, email, password
- Input validation
- Password encryption
- (Optional) Email verification (structure ready)

### 🌐 OAuth2 (Optional)
- Integration with Google login (in progress)
- OAuth2 client setup via Spring Security

## Protected API Access

- Endpoints like `/api/notes/**` require authentication
- Access is managed via `Bearer` JWT tokens in the `Authorization` header
- Spring Security is configured with custom filters for token validation

## 🔄 Token Flow

1. User logs in via `/api/auth/login`
2. Receives an `accessToken` and `refreshToken`
3. Access token is used for secured API calls
4. When it expires, user sends `refreshToken` to get a new access token

## 🧪 API Documentation

- Swagger UI available at:  
  `http://localhost:8080/swagger-ui.html`

- API Endpoints:
  - `POST /api/auth/register`
  - `POST /api/auth/login`
  - `POST /api/auth/refresh-token`
  - `GET /api/notes`
  - etc.
