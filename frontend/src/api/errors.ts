import axios from 'axios'

interface ApiErrorResponse {
  timestamp: string
  status: number
  error: string
  field: string | null
  message: string
}

export interface ParsedApiError {
  field?: string
  message: string
}

const fallbackMessages: Record<number, string> = {
  400: 'The request was rejected. Check the form fields and try again.',
  401: 'Authentication is required. Register a user again to start a new session.',
  404: 'The referenced resource was not found.',
  409: 'A resource with these details already exists.',
  422: 'The request could not be processed.',
}

export function parseApiError(error: unknown): ParsedApiError {
  if (!axios.isAxiosError<ApiErrorResponse>(error)) {
    return { message: 'An unexpected error occurred. Please try again.' }
  }

  const status = error.response?.status
  const responseData = error.response?.data

  if (status !== undefined && status >= 500) {
    return { message: 'The server encountered an unexpected error.' }
  }

  const message =
    typeof responseData?.message === 'string' && responseData.message.trim()
      ? responseData.message
      : status !== undefined
        ? fallbackMessages[status]
        : undefined

  return {
    field:
      typeof responseData?.field === 'string' && responseData.field.trim()
        ? responseData.field
        : undefined,
    message: message ?? 'Could not reach the backend. Check that it is running and try again.',
  }
}
