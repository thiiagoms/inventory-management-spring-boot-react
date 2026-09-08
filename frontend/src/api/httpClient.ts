import axios from 'axios'

const AUTH_TOKEN_KEY = 'inventory-api-token'
export const AUTHENTICATION_CHANGED_EVENT = 'inventory-authentication-changed'
const configuredApiUrl = import.meta.env.VITE_API_URL?.trim()

if (!configuredApiUrl) {
  throw new Error('VITE_API_URL is required in the repository root .env file.')
}

export const httpClient = axios.create({
  baseURL: import.meta.env.DEV ? '/backend' : configuredApiUrl,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
})

httpClient.interceptors.request.use((config) => {
  const token = sessionStorage.getItem(AUTH_TOKEN_KEY)

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

export function saveAuthenticationToken(token: string) {
  sessionStorage.setItem(AUTH_TOKEN_KEY, token)
  window.dispatchEvent(new Event(AUTHENTICATION_CHANGED_EVENT))
}

export function hasAuthenticationToken(): boolean {
  return Boolean(sessionStorage.getItem(AUTH_TOKEN_KEY))
}
