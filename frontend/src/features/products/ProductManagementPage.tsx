import { useCallback, useEffect, useMemo, useState } from 'react'
import type { FormEvent } from 'react'
import {
  Alert,
  Button,
  Checkbox,
  CircularProgress,
  FormControl,
  FormHelperText,
  InputLabel,
  ListItemText,
  MenuItem,
  Paper,
  Select,
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
import { listCategories } from '../categories/categoryApi'
import type { CategoryResponse } from '../categories/types'
import { listSuppliers } from '../suppliers/supplierApi'
import type { SupplierResponse } from '../suppliers/types'
import {
  deleteProduct,
  listProducts,
  registerProduct,
  updateProduct,
} from './productApi'
import type { ProductResponse } from './types'

interface ProductFormState {
  title: string
  description: string
  imageUrl: string
  price: string
  stockQuantity: string
  categoryIds: string[]
  supplierId: string
  expiryDate: string
}

const emptyForm: ProductFormState = {
  title: '',
  description: '',
  imageUrl: '',
  price: '',
  stockQuantity: '',
  categoryIds: [],
  supplierId: '',
  expiryDate: '',
}

export default function ProductManagementPage() {
  const [products, setProducts] = useState<ProductResponse[]>([])
  const [categories, setCategories] = useState<CategoryResponse[]>([])
  const [suppliers, setSuppliers] = useState<SupplierResponse[]>([])
  const [form, setForm] = useState<ProductFormState>(emptyForm)
  const [editingId, setEditingId] = useState<string | null>(null)
  const [loadingList, setLoadingList] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [deletingId, setDeletingId] = useState<string | null>(null)
  const [successMessage, setSuccessMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('')
  const [fieldError, setFieldError] = useState<{ field?: string; message: string } | null>(null)

  const categoryTitles = useMemo(
    () => new Map(categories.map((category) => [category.id, category.title])),
    [categories],
  )

  const supplierNames = useMemo(
    () => new Map(suppliers.map((supplier) => [supplier.id, supplier.socialName])),
    [suppliers],
  )

  const loadData = useCallback(async () => {
    setLoadingList(true)
    setErrorMessage('')

    try {
      const [availableProducts, availableCategories, availableSuppliers] = await Promise.all([
        listProducts(),
        listCategories(),
        listSuppliers(),
      ])
      setProducts(availableProducts)
      setCategories(availableCategories)
      setSuppliers(availableSuppliers)
    } catch (requestError) {
      setErrorMessage(parseApiError(requestError).message)
    } finally {
      setLoadingList(false)
    }
  }, [])

  useEffect(() => {
    let active = true

    const initializeData = async () => {
      try {
        const [availableProducts, availableCategories, availableSuppliers] = await Promise.all([
          listProducts(),
          listCategories(),
          listSuppliers(),
        ])

        if (active) {
          setProducts(availableProducts)
          setCategories(availableCategories)
          setSuppliers(availableSuppliers)
        }
      } catch (requestError) {
        if (active) setErrorMessage(parseApiError(requestError).message)
      } finally {
        if (active) setLoadingList(false)
      }
    }

    void initializeData()

    return () => {
      active = false
    }
  }, [])

  const resetForm = () => {
    setForm(emptyForm)
    setEditingId(null)
    setFieldError(null)
  }

  const updateField = (field: keyof ProductFormState, value: string | string[]) => {
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
      const product = editingId
        ? await updateProduct(editingId, {
            title: form.title,
            description: form.description,
            imageUrl: form.imageUrl,
            price: Number(form.price),
            stockQuantity: Number(form.stockQuantity),
          })
        : await registerProduct({
            ...form,
            price: Number(form.price),
            stockQuantity: Number(form.stockQuantity),
          })
      setSuccessMessage(
        `Product “${product.title}” ${editingId ? 'updated' : 'created'} successfully.`,
      )
      resetForm()
      await loadData()
    } catch (requestError) {
      const parsedError = parseApiError(requestError)
      setFieldError(parsedError)
      if (!parsedError.field) setErrorMessage(parsedError.message)
    } finally {
      setSubmitting(false)
    }
  }

  const startEditing = (product: ProductResponse) => {
    setEditingId(product.id)
    setForm({
      title: product.title,
      description: product.description,
      imageUrl: product.imageUrl,
      price: String(product.price),
      stockQuantity: String(product.stockQuantity),
      categoryIds: product.categoryIds,
      supplierId: product.supplierId,
      expiryDate: product.expiryDate,
    })
    setSuccessMessage('')
    setErrorMessage('')
    setFieldError(null)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleDelete = async (product: ProductResponse) => {
    if (!window.confirm(`Delete product “${product.title}”?`)) return

    setDeletingId(product.id)
    setSuccessMessage('')
    setErrorMessage('')

    try {
      await deleteProduct(product.id)
      if (editingId === product.id) resetForm()
      setSuccessMessage(`Product “${product.title}” deleted successfully.`)
      await loadData()
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
            {editingId ? 'Edit Product' : 'Create Product'}
          </Typography>

          {editingId && (
            <Alert severity="info">
              Supplier, categories, and expiry date cannot be changed by the product update API.
            </Alert>
          )}
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
          <TextField
            label="Image URL"
            type="url"
            value={form.imageUrl}
            onChange={(event) => updateField('imageUrl', event.target.value)}
            error={fieldError?.field === 'imageUrl'}
            helperText={fieldError?.field === 'imageUrl' ? fieldError.message : undefined}
            required
          />

          {!editingId && (
            <FormControl required error={fieldError?.field === 'supplierId'}>
              <InputLabel id="supplier-label">Supplier</InputLabel>
              <Select
                labelId="supplier-label"
                label="Supplier"
                value={form.supplierId}
                onChange={(event) => updateField('supplierId', event.target.value)}
                disabled={suppliers.length === 0}
              >
                {suppliers.map((supplier) => (
                  <MenuItem key={supplier.id} value={supplier.id}>
                    {supplier.socialName}
                  </MenuItem>
                ))}
              </Select>
              <FormHelperText>
                {fieldError?.field === 'supplierId'
                  ? fieldError.message
                  : suppliers.length === 0
                    ? 'Create a supplier before creating a product.'
                    : 'Select the supplier for this product.'}
              </FormHelperText>
            </FormControl>
          )}

          {!editingId && (
            <FormControl required error={fieldError?.field === 'categoryIds'}>
              <InputLabel id="categories-label">Categories</InputLabel>
              <Select
                labelId="categories-label"
                label="Categories"
                multiple
                value={form.categoryIds}
                onChange={(event) => {
                  const value = event.target.value
                  updateField('categoryIds', typeof value === 'string' ? value.split(',') : value)
                }}
                renderValue={(selected) =>
                  selected
                    .map((categoryId) => categoryTitles.get(categoryId) ?? categoryId)
                    .join(', ')
                }
                disabled={categories.length === 0}
              >
                {categories.map((category) => (
                  <MenuItem key={category.id} value={category.id}>
                    <Checkbox checked={form.categoryIds.includes(category.id)} />
                    <ListItemText primary={category.title} secondary={category.description} />
                  </MenuItem>
                ))}
              </Select>
              <FormHelperText>
                {fieldError?.field === 'categoryIds'
                  ? fieldError.message
                  : categories.length === 0
                    ? 'Create a category before creating a product.'
                    : 'Select one or more categories.'}
              </FormHelperText>
            </FormControl>
          )}

          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
            <TextField
              label="Price"
              type="number"
              value={form.price}
              onChange={(event) => updateField('price', event.target.value)}
              error={fieldError?.field === 'price'}
              helperText={fieldError?.field === 'price' ? fieldError.message : undefined}
              slotProps={{ htmlInput: { min: 0, step: '0.01' } }}
              required
              fullWidth
            />
            <TextField
              label="Stock quantity"
              type="number"
              value={form.stockQuantity}
              onChange={(event) => updateField('stockQuantity', event.target.value)}
              error={fieldError?.field === 'stockQuantity'}
              helperText={fieldError?.field === 'stockQuantity' ? fieldError.message : undefined}
              slotProps={{ htmlInput: { min: 0, step: 1 } }}
              required
              fullWidth
            />
          </Stack>

          {!editingId && (
            <TextField
              label="Expiry date"
              type="datetime-local"
              value={form.expiryDate}
              onChange={(event) => updateField('expiryDate', event.target.value)}
              error={fieldError?.field === 'expiryDate'}
              helperText={fieldError?.field === 'expiryDate' ? fieldError.message : undefined}
              slotProps={{ inputLabel: { shrink: true } }}
              required
            />
          )}

          <Stack direction="row" spacing={1} sx={{ justifyContent: 'flex-end' }}>
            {editingId && (
              <Button onClick={resetForm} disabled={submitting}>Cancel</Button>
            )}
            <FormActions
              loading={submitting}
              submitLabel={editingId ? 'Save changes' : 'Create product'}
            />
          </Stack>
        </Stack>
      </Paper>

      <Paper variant="outlined">
        <Typography variant="h5" component="h2" sx={{ p: 3, pb: 0 }}>Products</Typography>
        {loadingList ? (
          <Stack sx={{ p: 4, alignItems: 'center' }}><CircularProgress /></Stack>
        ) : products.length === 0 ? (
          <Typography color="text.secondary" sx={{ p: 3 }}>No products registered.</Typography>
        ) : (
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Product</TableCell>
                  <TableCell>SKU</TableCell>
                  <TableCell>Price</TableCell>
                  <TableCell>Stock</TableCell>
                  <TableCell>Categories</TableCell>
                  <TableCell>Supplier</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {products.map((product) => (
                  <TableRow key={product.id} hover>
                    <TableCell>
                      <Typography sx={{ fontWeight: 600 }}>{product.title}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        {product.description}
                      </Typography>
                    </TableCell>
                    <TableCell>{product.sku}</TableCell>
                    <TableCell>{product.price.toFixed(2)}</TableCell>
                    <TableCell>{product.stockQuantity}</TableCell>
                    <TableCell>
                      {product.categoryIds
                        .map((categoryId) => categoryTitles.get(categoryId) ?? categoryId)
                        .join(', ')}
                    </TableCell>
                    <TableCell>
                      {supplierNames.get(product.supplierId) ?? product.supplierId}
                    </TableCell>
                    <TableCell align="right">
                      <Stack direction="row" spacing={1} sx={{ justifyContent: 'flex-end' }}>
                        <Button size="small" onClick={() => startEditing(product)}>Edit</Button>
                        <Button
                          size="small"
                          color="error"
                          onClick={() => void handleDelete(product)}
                          disabled={deletingId === product.id}
                        >
                          {deletingId === product.id ? 'Deleting…' : 'Delete'}
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
