export interface SupplierRequest {
  socialName: string
  cnpj: string
  address: string
}

export interface SupplierResponse extends SupplierRequest {
  id: string
  createdAt: string
}

export interface SupplierPageResponse {
  content: SupplierResponse[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
}

export interface ViaCepResponse {
  cep: string
  logradouro: string
  complemento: string
  bairro: string
  localidade: string
  uf: string
  erro?: boolean
}
