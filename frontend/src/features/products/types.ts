export interface RegisterProductRequest {
  title: string
  description: string
  imageUrl: string
  price: number
  stockQuantity: number
  categoryIds: string[]
  supplierId: string
  expiryDate: string
}

export interface ProductResponse extends RegisterProductRequest {
  id: string
  sku: string
}

export interface ProductPageResponse {
  content: ProductResponse[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
}

export interface UpdateProductRequest {
  title?: string
  description?: string
  imageUrl?: string
  price?: number
  stockQuantity?: number
}
