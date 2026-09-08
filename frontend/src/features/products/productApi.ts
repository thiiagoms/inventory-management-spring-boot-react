import { httpClient } from '../../api/httpClient'
import type {
  ProductPageResponse,
  ProductResponse,
  RegisterProductRequest,
  UpdateProductRequest,
} from './types'

export async function registerProduct(
  request: RegisterProductRequest,
): Promise<ProductResponse> {
  const response = await httpClient.post<ProductResponse>('/api/products', request)
  return response.data
}

export async function listProducts(): Promise<ProductResponse[]> {
  const response = await httpClient.get<ProductPageResponse>('/api/products', {
    params: { page: 0, size: 100 },
  })
  return response.data.content
}

export async function updateProduct(
  id: string,
  request: UpdateProductRequest,
): Promise<ProductResponse> {
  const response = await httpClient.patch<ProductResponse>(`/api/products/${id}`, request)
  return response.data
}

export async function deleteProduct(id: string): Promise<void> {
  await httpClient.delete(`/api/products/${id}`)
}
