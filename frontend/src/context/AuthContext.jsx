import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const token = localStorage.getItem('accessToken')
    const role = localStorage.getItem('role')
    return token ? { token, role } : null
  })

  const login = (data) => {
    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('role', data.role)
    setUser({ token: data.accessToken, role: data.role })
  }

  const logout = () => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('role')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
