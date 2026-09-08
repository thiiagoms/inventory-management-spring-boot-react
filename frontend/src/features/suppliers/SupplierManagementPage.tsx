import { useCallback, useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import {
  Alert,
  Button,
  CircularProgress,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from '@mui/material'
import { parseApiError } from '../../api/errors'
import FormActions from '../../components/FormActions'
import {
  deleteSupplier,
  findAddressByPostalCode,
  listSuppliers,
  registerSupplier,
  updateSupplier,
} from './supplierApi'
import type { SupplierRequest, SupplierResponse, ViaCepResponse } from './types'

const emptyForm: SupplierRequest = {
  socialName: '',
  cnpj: '',
  address: '',
}

function formatCnpj(value: string): string {
  const digits = value.replace(/\D/g, '').slice(0, 14)
  return digits
    .replace(/^(\d{2})(\d)/, '$1.$2')
    .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/\.(\d{3})(\d)/, '.$1/$2')
    .replace(/(\d{4})(\d)/, '$1-$2')
}

function formatPostalCode(value: string): string {
  return value.replace(/\D/g, '').slice(0, 8).replace(/^(\d{5})(\d)/, '$1-$2')
}

function composeAddress(result: ViaCepResponse): string {
  return [
    result.logradouro,
    result.complemento,
    result.bairro,
    [result.localidade, result.uf].filter(Boolean).join(' - '),
    result.cep,
  ]
    .filter(Boolean)
    .join(', ')
}

export default function SupplierManagementPage() {
  const [suppliers, setSuppliers] = useState<SupplierResponse[]>([])
  const [form, setForm] = useState<SupplierRequest>(emptyForm)
  const [postalCode, setPostalCode] = useState('')
  const [editingId, setEditingId] = useState<string | null>(null)
  const [loadingList, setLoadingList] = useState(true)
  const [searchingAddress, setSearchingAddress] = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [deletingId, setDeletingId] = useState<string | null>(null)
  const [successMessage, setSuccessMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('')
  const [fieldError, setFieldError] = useState<{ field?: string; message: string } | null>(null)

  const loadSuppliers = useCallback(async () => {
    setLoadingList(true)
    setErrorMessage('')
    try {
      setSuppliers(await listSuppliers())
    } catch (requestError) {
      setErrorMessage(parseApiError(requestError).message)
    } finally {
      setLoadingList(false)
    }
  }, [])

  useEffect(() => {
    let active = true
    const initialize = async () => {
      try {
        const availableSuppliers = await listSuppliers()
        if (active) setSuppliers(availableSuppliers)
      } catch (requestError) {
        if (active) setErrorMessage(parseApiError(requestError).message)
      } finally {
        if (active) setLoadingList(false)
      }
    }
    void initialize()
    return () => {
      active = false
    }
  }, [])

  const resetForm = () => {
    setForm(emptyForm)
    setPostalCode('')
    setEditingId(null)
    setFieldError(null)
  }

  const updateField = (field: keyof SupplierRequest, value: string) => {
    setForm((current) => ({ ...current, [field]: value }))
    if (fieldError?.field === field) setFieldError(null)
  }

  const searchAddress = async () => {
    const digits = postalCode.replace(/\D/g, '')
    if (digits.length !== 8) {
      setErrorMessage('Enter an 8-digit CEP before searching.')
      return
    }

    setSearchingAddress(true)
    setErrorMessage('')
    try {
      const result = await findAddressByPostalCode(digits)
      if (result.erro) {
        setErrorMessage('CEP not found.')
        return
      }
      updateField('address', composeAddress(result))
    } catch {
      setErrorMessage('Could not search ViaCEP. Try again.')
    } finally {
      setSearchingAddress(false)
    }
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setSubmitting(true)
    setSuccessMessage('')
    setErrorMessage('')
    setFieldError(null)
    try {
      const supplier = editingId
        ? await updateSupplier(editingId, form)
        : await registerSupplier(form)
      setSuccessMessage(
        `Supplier “${supplier.socialName}” ${editingId ? 'updated' : 'created'} successfully.`,
      )
      resetForm()
      await loadSuppliers()
    } catch (requestError) {
      const parsedError = parseApiError(requestError)
      setFieldError(parsedError)
      if (!parsedError.field) setErrorMessage(parsedError.message)
    } finally {
      setSubmitting(false)
    }
  }

  const startEditing = (supplier: SupplierResponse) => {
    setEditingId(supplier.id)
    setForm({ socialName: supplier.socialName, cnpj: supplier.cnpj, address: supplier.address })
    setPostalCode('')
    setSuccessMessage('')
    setErrorMessage('')
    setFieldError(null)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleDelete = async (supplier: SupplierResponse) => {
    if (!window.confirm(`Delete supplier “${supplier.socialName}”?`)) return
    setDeletingId(supplier.id)
    setSuccessMessage('')
    setErrorMessage('')
    try {
      await deleteSupplier(supplier.id)
      if (editingId === supplier.id) resetForm()
      setSuccessMessage(`Supplier “${supplier.socialName}” deleted successfully.`)
      await loadSuppliers()
    } catch (requestError) {
      setErrorMessage(parseApiError(requestError).message)
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <Stack spacing={3}>
      <Paper variant="outlined" sx={{ p: { xs: 3, sm: 4 } }}>
        <Stack component="form" spacing={3} onSubmit={handleSubmit}>
          <Typography variant="h4" component="h1">
            {editingId ? 'Edit Supplier' : 'Create Supplier'}
          </Typography>
          {successMessage && <Alert severity="success">{successMessage}</Alert>}
          {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
          {fieldError?.field && !Object.hasOwn(emptyForm, fieldError.field) && (
            <Alert severity="error">{fieldError.message}</Alert>
          )}

          <TextField
            label="Social name"
            value={form.socialName}
            onChange={(event) => updateField('socialName', event.target.value)}
            error={fieldError?.field === 'socialName'}
            helperText={fieldError?.field === 'socialName' ? fieldError.message : undefined}
            required
          />
          <TextField
            label="CNPJ"
            value={formatCnpj(form.cnpj)}
            onChange={(event) => updateField('cnpj', event.target.value.replace(/\D/g, '').slice(0, 14))}
            error={fieldError?.field === 'cnpj'}
            helperText={fieldError?.field === 'cnpj' ? fieldError.message : undefined}
            placeholder="12.345.678/0001-90"
            slotProps={{ htmlInput: { inputMode: 'numeric' } }}
            required
          />

          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
            <TextField
              label="CEP"
              value={formatPostalCode(postalCode)}
              onChange={(event) => setPostalCode(event.target.value.replace(/\D/g, '').slice(0, 8))}
              placeholder="01001-000"
              slotProps={{ htmlInput: { inputMode: 'numeric' } }}
              fullWidth
            />
            <Button
              type="button"
              variant="outlined"
              onClick={() => void searchAddress()}
              disabled={searchingAddress || postalCode.length !== 8}
              sx={{ minWidth: 150 }}
            >
              {searchingAddress ? 'Searching…' : 'Search ViaCEP'}
            </Button>
          </Stack>
          <TextField
            label="Address"
            value={form.address}
            onChange={(event) => updateField('address', event.target.value)}
            error={fieldError?.field === 'address'}
            helperText={
              fieldError?.field === 'address'
                ? fieldError.message
                : 'Search by CEP, then add a number or complement when needed.'
            }
            required
            multiline
            minRows={3}
          />

          <Stack direction="row" spacing={1} sx={{ justifyContent: 'flex-end' }}>
            {editingId && <Button onClick={resetForm} disabled={submitting}>Cancel</Button>}
            <FormActions
              loading={submitting}
              submitLabel={editingId ? 'Save changes' : 'Create supplier'}
            />
          </Stack>
        </Stack>
      </Paper>

      <Paper variant="outlined">
        <Typography variant="h5" component="h2" sx={{ p: 3, pb: 0 }}>Suppliers</Typography>
        {loadingList ? (
          <Stack sx={{ p: 4, alignItems: 'center' }}><CircularProgress /></Stack>
        ) : suppliers.length === 0 ? (
          <Typography color="text.secondary" sx={{ p: 3 }}>No suppliers registered.</Typography>
        ) : (
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Social name</TableCell>
                  <TableCell>CNPJ</TableCell>
                  <TableCell>Address</TableCell>
                  <TableCell>Created at</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {suppliers.map((supplier) => (
                  <TableRow key={supplier.id} hover>
                    <TableCell>{supplier.socialName}</TableCell>
                    <TableCell>{formatCnpj(supplier.cnpj)}</TableCell>
                    <TableCell>{supplier.address}</TableCell>
                    <TableCell>{new Date(supplier.createdAt).toLocaleString()}</TableCell>
                    <TableCell align="right">
                      <Stack direction="row" spacing={1} sx={{ justifyContent: 'flex-end' }}>
                        <Button size="small" onClick={() => startEditing(supplier)}>Edit</Button>
                        <Button
                          size="small"
                          color="error"
                          onClick={() => void handleDelete(supplier)}
                          disabled={deletingId === supplier.id}
                        >
                          {deletingId === supplier.id ? 'Deleting…' : 'Delete'}
                        </Button>
                      </Stack>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>
    </Stack>
  )
}
