import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import AppLayout from '../components/AppLayout'
import CategoryManagementPage from '../features/categories/CategoryManagementPage'
import ProductManagementPage from '../features/products/ProductManagementPage'
import SupplierManagementPage from '../features/suppliers/SupplierManagementPage'
import AuthenticationPage from '../features/users/AuthenticationPage'
import UserRegistrationPage from '../features/users/UserRegistrationPage'
import RequireAuthentication from './RequireAuthentication'

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AppLayout />}>
          <Route index element={<AuthenticationPage />} />
          <Route path="users/new" element={<UserRegistrationPage />} />
          <Route element={<RequireAuthentication />}>
            <Route path="categories" element={<CategoryManagementPage />} />
            <Route path="suppliers" element={<SupplierManagementPage />} />
            <Route path="products" element={<ProductManagementPage />} />
            <Route path="categories/new" element={<Navigate to="/categories" replace />} />
            <Route path="products/new" element={<Navigate to="/products" replace />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}
