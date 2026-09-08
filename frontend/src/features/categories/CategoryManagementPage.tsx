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
  deleteCategory,
  listCategories,
  registerCategory,
  updateCategory,
} from './categoryApi'
import type { CategoryResponse, RegisterCategoryRequest } from './types'

const emptyForm: RegisterCategoryRequest = {
  title: '',
  description: '',
}

export default function CategoryManagementPage() {
  const [categories, setCategories] = useState<CategoryResponse[]>([])
  const [form, setForm] = useState<RegisterCategoryRequest>(emptyForm)
  const [editingId, setEditingId] = useState<string | null>(null)
  const [loadingList, setLoadingList] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [deletingId, setDeletingId] = useState<string | null>(null)
  const [successMessage, setSuccessMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('')
  const [fieldError, setFieldError] = useState<{ field?: string; message: string } | null>(null)

  const loadCategories = useCallback(async () => {
    setLoadingList(true)
    setErrorMessage('')

    try {
      setCategories(await listCategories())
    } catch (requestError) {
      setErrorMessage(parseApiError(requestError).message)
    } finally {
      setLoadingList(false)
    }
  }, [])

  useEffect(() => {
    let active = true

    const initializeCategories = async () => {
      try {
        const availableCategories = await listCategories()
        if (active) setCategories(availableCategories)
      } catch (requestError) {
        if (active) setErrorMessage(parseApiError(requestError).message)
      } finally {
        if (active) setLoadingList(false)
      }
    }

    void initializeCategories()

    return () => {
      active = false
    }
  }, [])

  const resetForm = () => {
    setForm(emptyForm)
    setEditingId(null)
    setFieldError(null)
  }

  const updateField = (field: keyof RegisterCategoryRequest, value: string) => {
    setForm((current) => ({ ...current, [field]: value }))
    if (fieldError?.field === field) setFieldError(null)
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setSubmitting(true)
    setSuccessMessage('')
    setErrorMessage('')
    setFieldError(null)

    try {
      const category = editingId
        ? await updateCategory(editingId, form)
        : await registerCategory(form)
      setSuccessMessage(
        `Category “${category.title}” ${editingId ? 'updated' : 'created'} successfully.`,
      )
      resetForm()
      await loadCategories()
    } catch (requestError) {
      const parsedError = parseApiError(requestError)
      setFieldError(parsedError)
      if (!parsedError.field) setErrorMessage(parsedError.message)
    } finally {
      setSubmitting(false)
    }
  }

  const startEditing = (category: CategoryResponse) => {
    setEditingId(category.id)
    setForm({ title: category.title, description: category.description })
    setSuccessMessage('')
    setErrorMessage('')
    setFieldError(null)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleDelete = async (category: CategoryResponse) => {
    if (!window.confirm(`Delete category “${category.title}”?`)) return

    setDeletingId(category.id)
    setSuccessMessage('')
    setErrorMessage('')

    try {
      await deleteCategory(category.id)
      if (editingId === category.id) resetForm()
      setSuccessMessage(`Category “${category.title}” deleted successfully.`)
      await loadCategories()
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
            {editingId ? 'Edit Category' : 'Create Category'}
          </Typography>

          {successMessage && <Alert severity="success">{successMessage}</Alert>}
          {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
          {fieldError?.field && !Object.hasOwn(emptyForm, fieldError.field) && (
            <Alert severity="error">{fieldError.message}</Alert>
          )}

          <TextField
            label="Title"
            value={form.title}
            onChange={(event) => updateField('title', event.target.value)}
            error={fieldError?.field === 'title'}
            helperText={fieldError?.field === 'title' ? fieldError.message : undefined}
            required
          />
          <TextField
            label="Description"
            value={form.description}
            onChange={(event) => updateField('description', event.target.value)}
            error={fieldError?.field === 'description'}
            helperText={fieldError?.field === 'description' ? fieldError.message : undefined}
            required
            multiline
            minRows={3}
          />

          <Stack direction="row" spacing={1} sx={{ justifyContent: 'flex-end' }}>
            {editingId && (
              <Button onClick={resetForm} disabled={submitting}>
                Cancel
              </Button>
            )}
            <FormActions
              loading={submitting}
              submitLabel={editingId ? 'Save changes' : 'Create category'}
            />
          </Stack>
        </Stack>
      </Paper>

      <Paper variant="outlined">
        <Stack spacing={2} sx={{ p: 3, pb: 0 }}>
          <Typography variant="h5" component="h2">Categories</Typography>
        </Stack>
        {loadingList ? (
          <Stack sx={{ p: 4, alignItems: 'center' }}><CircularProgress /></Stack>
        ) : categories.length === 0 ? (
          <Typography color="text.secondary" sx={{ p: 3 }}>No categories registered.</Typography>
        ) : (
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Title</TableCell>
                  <TableCell>Description</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {categories.map((category) => (
                  <TableRow key={category.id} hover>
                    <TableCell>{category.title}</TableCell>
                    <TableCell>{category.description}</TableCell>
                    <TableCell align="right">
                      <Stack direction="row" spacing={1} sx={{ justifyContent: 'flex-end' }}>
                        <Button size="small" onClick={() => startEditing(category)}>Edit</Button>
                        <Button
                          size="small"
                          color="error"
                          onClick={() => void handleDelete(category)}
                          disabled={deletingId === category.id}
                        >
                          {deletingId === category.id ? 'Deleting…' : 'Delete'}
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
