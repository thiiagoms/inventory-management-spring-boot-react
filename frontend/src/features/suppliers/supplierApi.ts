import axios from 'axios'
import { httpClient } from '../../api/httpClient'
import type {
  SupplierPageResponse,
  SupplierRequest,
  SupplierResponse,
  ViaCepResponse,
} from './types'

const viaCepClient = axios.create({
  baseURL: 'https://viacep.com.br/ws',
  headers: { Accept: 'application/json' },
})

export async function registerSupplier(request: SupplierRequest): Promise<SupplierResponse> {
  const response = await httpClient.post<SupplierResponse>('/api/suppliers', request)
  return response.data
}

export async function listSuppliers(): Promise<SupplierResponse[]> {
  const response = await httpClient.get<SupplierPageResponse>('/api/suppliers', {
    params: { page: 0, size: 100 },
  })
  return response.data.content
}

export async function updateSupplier(
  id: string,
  request: SupplierRequest,
): Promise<SupplierResponse> {
  const response = await httpClient.patch<SupplierResponse>(`/api/suppliers/${id}`, request)
  return response.data
}

export async function deleteSupplier(id: string): Promise<void> {
  await httpClient.delete(`/api/suppliers/${id}`)
}

export async function findAddressByPostalCode(postalCode: string): Promise<ViaCepResponse> {
  const response = await viaCepClient.get<ViaCepResponse>(`/${postalCode}/json/`)
  return response.data
}
