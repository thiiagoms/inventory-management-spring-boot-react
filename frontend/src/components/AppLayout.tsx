import { AppBar, Box, Button, Container, Paper, Stack, Toolbar, Typography } from '@mui/material'
import { useEffect, useState } from 'react'
import { NavLink, Outlet } from 'react-router-dom'
import {
  AUTHENTICATION_CHANGED_EVENT,
  hasAuthenticationToken,
} from '../api/httpClient'

const publicNavigationItems = [
  { label: 'Sign in', path: '/' },
]

const authenticatedNavigationItems = [
  { label: 'Categories', path: '/categories' },
  { label: 'Products', path: '/products' },
]

export default function AppLayout() {
  const [authenticated, setAuthenticated] = useState(hasAuthenticationToken)

  useEffect(() => {
    const syncAuthenticationState = () => {
      setAuthenticated(hasAuthenticationToken())
    }

    window.addEventListener(AUTHENTICATION_CHANGED_EVENT, syncAuthenticationState)

    return () => {
      window.removeEventListener(AUTHENTICATION_CHANGED_EVENT, syncAuthenticationState)
    }
  }, [])

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'grey.50' }}>
      <AppBar position="static" elevation={0}>
        <Toolbar sx={{ gap: { xs: 1, sm: 3 }, flexWrap: 'wrap', py: 1 }}>
          <Typography
            variant="h6"
            component="div"
            sx={{ flexGrow: 1, minWidth: '12rem', fontWeight: 700 }}
          >
            Inventory API Tester
          </Typography>
          {!authenticated && (
            <Box component="nav" aria-label="Main navigation" sx={{ display: 'flex', gap: 0.5 }}>
              {publicNavigationItems.map((item) => (
                <Button
                  key={item.path}
                  component={NavLink}
                  to={item.path}
                  end
                  color="inherit"
                  sx={{ '&.active': { bgcolor: 'rgba(255, 255, 255, 0.18)' } }}
                >
                  {item.label}
                </Button>
              ))}
            </Box>
          )}
        </Toolbar>
      </AppBar>
      <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' } }}>
        {authenticated && (
          <Paper
            component="aside"
            square
            variant="outlined"
            sx={{
              width: { xs: '100%', sm: 220 },
              minHeight: { sm: 'calc(100vh - 72px)' },
              flexShrink: 0,
              borderTop: 0,
              borderLeft: 0,
              borderBottom: { xs: 1, sm: 0 },
              p: 2,
            }}
          >
            <Typography variant="overline" color="text.secondary" sx={{ px: 1 }}>
              Management
            </Typography>
            <Stack component="nav" aria-label="Management navigation" spacing={0.5} sx={{ mt: 1 }}>
              {authenticatedNavigationItems.map((item) => (
                <Button
                  key={item.path}
                  component={NavLink}
                  to={item.path}
                  sx={{
                    justifyContent: 'flex-start',
                    color: 'text.primary',
                    '&.active': { bgcolor: 'primary.main', color: 'primary.contrastText' },
                  }}
                >
                  {item.label}
                </Button>
              ))}
            </Stack>
          </Paper>
        )}
        <Container
          component="main"
          maxWidth="lg"
          sx={{ py: { xs: 3, sm: 4 }, minWidth: 0 }}
        >
          <Outlet />
        </Container>
      </Box>
    </Box>
  )
}
