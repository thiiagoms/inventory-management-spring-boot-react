export interface RegisterCategoryRequest {
  title: string
  description: string
}

export interface UpdateCategoryRequest {
  title?: string
  description?: string
}

export interface CategoryResponse {
  id: string
  title: string
  description: string
}

export interface CategoryPageResponse {
  content: CategoryResponse[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
}
