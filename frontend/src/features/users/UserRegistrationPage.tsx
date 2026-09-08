import { useState } from 'react'
import type { FormEvent } from 'react'
import { Alert, Paper, Stack, TextField, Typography } from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { parseApiError } from '../../api/errors'
import FormActions from '../../components/FormActions'
import { registerUser } from './userApi'
import type { RegisterUserRequest } from './types'

const emptyForm: RegisterUserRequest = {
  name: '',
  email: '',
  password: '',
  phone: '',
}

function formatBrazilianCellphone(value: string): string {
  const digits = value.replace(/\D/g, '').slice(0, 11)

  if (digits.length <= 2) return digits

  const areaCode = digits.slice(0, 2)
  const ninthDigit = digits.slice(2, 3)
  const firstBlock = digits.slice(3, 7)
  const secondBlock = digits.slice(7, 11)

  return [
    areaCode,
    ninthDigit,
    firstBlock,
  ].filter(Boolean).join(' ') + (secondBlock ? `-${secondBlock}` : '')
}

export default function UserRegistrationPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState<RegisterUserRequest>(emptyForm)
  const [confirmPassword, setConfirmPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const [confirmPasswordError, setConfirmPasswordError] = useState('')
  const [fieldError, setFieldError] = useState<{ field?: string; message: string } | null>(null)

  const updateField = (field: keyof RegisterUserRequest, value: string) => {
    setForm((current) => ({ ...current, [field]: value }))
    if (fieldError?.field === field) setFieldError(null)
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setErrorMessage('')
    setConfirmPasswordError('')
    setFieldError(null)

    if (form.password !== confirmPassword) {
      setConfirmPasswordError('Passwords do not match.')
      return
    }

    setLoading(true)

    try {
      const status = await registerUser(form)

      if (status === 201) {
        navigate('/', {
          replace: true,
          state: { registrationSucceeded: true },
        })
        return
      }

      setErrorMessage(`User registration returned an unexpected status (${status}).`)
    } catch (registrationError) {
      const parsedError = parseApiError(registrationError)
      setFieldError(parsedError)
      if (!parsedError.field) setErrorMessage(parsedError.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Paper variant="outlined" sx={{ p: { xs: 3, sm: 4 } }}>
      <Stack component="form" spacing={3} onSubmit={handleSubmit}>
        <Typography variant="h4" component="h1">
          Register User
        </Typography>

        {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
        {fieldError?.field && !Object.hasOwn(emptyForm, fieldError.field) && (
          <Alert severity="error">{fieldError.message}</Alert>
        )}

        <TextField
          label="Name"
          value={form.name}
          onChange={(event) => updateField('name', event.target.value)}
          error={fieldError?.field === 'name'}
          helperText={fieldError?.field === 'name' ? fieldError.message : undefined}
          required
          autoComplete="name"
        />
        <TextField
          label="Email"
          type="email"
          value={form.email}
          onChange={(event) => updateField('email', event.target.value)}
          error={fieldError?.field === 'email'}
          helperText={fieldError?.field === 'email' ? fieldError.message : undefined}
          required
          autoComplete="email"
        />
        <TextField
          label="Password"
          type="password"
          value={form.password}
          onChange={(event) => updateField('password', event.target.value)}
          error={fieldError?.field === 'password'}
          helperText={fieldError?.field === 'password' ? fieldError.message : undefined}
          required
          autoComplete="new-password"
        />
        <TextField
          label="Confirm password"
          type="password"
          value={confirmPassword}
          onChange={(event) => {
            setConfirmPassword(event.target.value)
            setConfirmPasswordError('')
          }}
          error={Boolean(confirmPasswordError)}
          helperText={confirmPasswordError || undefined}
          required
          autoComplete="new-password"
        />
        <TextField
          label="Phone"
          value={formatBrazilianCellphone(form.phone)}
          onChange={(event) => updateField('phone', event.target.value.replace(/\D/g, '').slice(0, 11))}
          error={fieldError?.field === 'phone'}
          helperText={fieldError?.field === 'phone' ? fieldError.message : undefined}
          required
          autoComplete="tel"
          placeholder="31 9 9456-4567"
          slotProps={{ htmlInput: { inputMode: 'numeric' } }}
        />

        <FormActions loading={loading} submitLabel="Register user" />
      </Stack>
    </Paper>
  )
}
