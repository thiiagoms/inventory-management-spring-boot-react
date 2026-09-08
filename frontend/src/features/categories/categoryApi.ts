import { httpClient } from '../../api/httpClient'
import type {
  CategoryPageResponse,
  CategoryResponse,
  RegisterCategoryRequest,
  UpdateCategoryRequest,
} from './types'

export async function registerCategory(
  request: RegisterCategoryRequest,
): Promise<CategoryResponse> {
  const response = await httpClient.post<CategoryResponse>('/api/categories', request)
  return response.data
}

export async function listCategories(): Promise<CategoryResponse[]> {
  const response = await httpClient.get<CategoryPageResponse>('/api/categories', {
    params: { page: 0, size: 100 },
  })
  return response.data.content
}

export async function updateCategory(
  id: string,
  request: UpdateCategoryRequest,
): Promise<CategoryResponse> {
  const response = await httpClient.patch<CategoryResponse>(`/api/categories/${id}`, request)
  return response.data
}

export async function deleteCategory(id: string): Promise<void> {
  await httpClient.delete(`/api/categories/${id}`)
}
