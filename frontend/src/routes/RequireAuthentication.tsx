import { Navigate, Outlet } from 'react-router-dom'
import { hasAuthenticationToken } from '../api/httpClient'

export default function RequireAuthentication() {
  if (!hasAuthenticationToken()) {
    return (
      <Navigate
        to="/"
        replace
        state={{ authenticationRequired: true }}
      />
    )
  }

  return <Outlet />
}
