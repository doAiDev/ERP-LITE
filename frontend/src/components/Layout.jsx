import { Outlet, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Layout() {
  const { logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const navItems = [
    { path: '/', label: '대시보드', icon: '📊' },
    { path: '/products', label: '상품 관리', icon: '📦' },
  ]

  return (
    <div className="flex h-screen bg-gray-100">
      <aside className="w-60 bg-gray-900 text-white flex flex-col">
        <div className="p-5 border-b border-gray-700">
          <h1 className="text-lg font-bold">악세사리 ERP</h1>
          <p className="text-xs text-gray-400 mt-0.5">점포 관리 시스템</p>
        </div>
        <nav className="flex-1 p-4 space-y-1">
          {navItems.map(item => (
            <NavLink
              key={item.path}
              to={item.path}
              end={item.path === '/'}
              className={({ isActive }) =>
                `flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm transition ${
                  isActive ? 'bg-blue-600 text-white' : 'text-gray-300 hover:bg-gray-800'
                }`
              }
            >
              <span>{item.icon}</span>
              {item.label}
            </NavLink>
          ))}
        </nav>
        <div className="p-4 border-t border-gray-700">
          <button
            onClick={handleLogout}
            className="w-full text-left text-sm text-gray-400 hover:text-white px-4 py-2 rounded-lg hover:bg-gray-800 transition"
          >
            🚶 로그아웃
          </button>
        </div>
      </aside>
      <main className="flex-1 overflow-auto">
        <Outlet />
      </main>
    </div>
  )
}
