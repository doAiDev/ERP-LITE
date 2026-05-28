import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import LoginPage from './pages/LoginPage'
import DashboardPage from './pages/DashboardPage'
import ProductsPage from './pages/ProductsPage'
import Layout from './components/Layout'

function PrivateRoute({ children }) {
  const { user } = useAuth()
  return user ? children : <Navigate to="/login" replace />
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/" element={
            <PrivateRoute>
              <Layout />
            </PrivateRoute>
          }>
            <Route index element={<DashboardPage />} />
            <Route path="products" element={<ProductsPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
