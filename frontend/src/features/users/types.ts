export interface RegisterUserRequest {
  name: string
  email: string
  password: string
  phone: string
}

export interface UserResponse {
  id: string
  name: string
  email: string
  phone: string
}

export interface AuthenticateRequest {
  email: string
  password: string
}

export interface AuthenticationResponse {
  token: string
  expiresAt: string
}
