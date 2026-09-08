import { httpClient, saveAuthenticationToken } from '../../api/httpClient'
import type {
  AuthenticateRequest,
  AuthenticationResponse,
  RegisterUserRequest,
  UserResponse,
} from './types'

export async function registerUser(request: RegisterUserRequest): Promise<number> {
  const response = await httpClient.post<UserResponse>('/api/users', request)
  return response.status
}

export async function authenticateUser(request: AuthenticateRequest): Promise<void> {
  const response = await httpClient.post<AuthenticationResponse>(
    '/api/users/authenticate',
    request,
  )
  saveAuthenticationToken(response.data.token)
}
