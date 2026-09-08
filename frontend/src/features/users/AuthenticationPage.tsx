import { useState } from 'react'
import type { FormEvent } from 'react'
import { Alert, Link, Paper, Stack, TextField, Typography } from '@mui/material'
import { Link as RouterLink, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { parseApiError } from '../../api/errors'
import { hasAuthenticationToken } from '../../api/httpClient'
import FormActions from '../../components/FormActions'
import { authenticateUser } from './userApi'
import type { AuthenticateRequest } from './types'

const emptyForm: AuthenticateRequest = {
  email: '',
  password: '',
}

interface AuthenticationLocationState {
  authenticationRequired?: boolean
  registrationSucceeded?: boolean
}

export default function AuthenticationPage() {
  const location = useLocation()
  const navigate = useNavigate()
  const [form, setForm] = useState<AuthenticateRequest>(emptyForm)
  const [loading, setLoading] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const [fieldError, setFieldError] = useState<{ field?: string; message: string } | null>(null)
  const locationState = location.state as AuthenticationLocationState | null

  if (hasAuthenticationToken()) {
    return <Navigate to="/categories" replace />
  }

  const updateField = (field: keyof AuthenticateRequest, value: string) => {
    setForm((current) => ({ ...current, [field]: value }))
    if (fieldError?.field === field) setFieldError(null)
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setLoading(true)
    setErrorMessage('')
    setFieldError(null)

    try {
      await authenticateUser(form)
      navigate('/categories', { replace: true })
    } catch (requestError) {
      const parsedError = parseApiError(requestError)
      setFieldError(parsedError)

      if (!Object.hasOwn(emptyForm, parsedError.field ?? '')) {
        setErrorMessage(parsedError.message)
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <Paper variant="outlined" sx={{ p: { xs: 3, sm: 4 }, maxWidth: 560, mx: 'auto' }}>
      <Stack component="form" spacing={3} onSubmit={handleSubmit}>
        <Typography variant="h4" component="h1">
          Sign In
        </Typography>

        {locationState?.authenticationRequired && (
          <Alert severity="info">Sign in before creating categories or products.</Alert>
        )}
        {locationState?.registrationSucceeded && (
          <Alert severity="success">Your account was created successfully. You can now sign in.</Alert>
        )}
        {errorMessage && <Alert severity="error">{errorMessage}</Alert>}

        <TextField
          label="Email"
          type="email"
          value={form.email}
          onChange={(event) => updateField('email', event.target.value)}
          error={fieldError?.field === 'email'}
          helperText={fieldError?.field === 'email' ? fieldError.message : undefined}
          required
          autoComplete="email"
          autoFocus
        />
        <TextField
          label="Password"
          type="password"
          value={form.password}
          onChange={(event) => updateField('password', event.target.value)}
          error={fieldError?.field === 'password'}
          helperText={fieldError?.field === 'password' ? fieldError.message : undefined}
          required
          autoComplete="current-password"
        />

        <FormActions loading={loading} submitLabel="Sign in" />

        <Link component={RouterLink} to="/users/new" sx={{ alignSelf: 'center' }}>
          Don’t Have An Account? Sign Up
        </Link>
      </Stack>
    </Paper>
  )
}
