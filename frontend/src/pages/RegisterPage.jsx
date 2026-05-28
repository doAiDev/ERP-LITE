import { useEffect, useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from '../api/axios'

export default function RegisterPage() {
  const navigate = useNavigate()
  const [stores, setStores] = useState([])
  const [form, setForm] = useState({
    name: '', phone: '', storeId: '', username: '', password: '', confirmPassword: ''
  })
  const [loading, setLoading] = useState(false)
  const [done, setDone] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    api.get('/api/stores').then(r => setStores(r.data.data || [])).catch(() => {})
  }, [])

  const set = (key) => (e) => setForm(f => ({ ...f, [key]: e.target.value }))

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    if (form.password !== form.confirmPassword) {
      setError('비밀번호가 일치하지 않습니다.')
      return
    }
    if (!form.storeId) {
      setError('소속 매장을 선택해주세요.')
      return
    }
    setLoading(true)
    try {
      await api.post('/api/auth/register', {
        name: form.name,
        phone: form.phone,
        storeId: Number(form.storeId),
        username: form.username,
        password: form.password,
      })
      setDone(true)
    } catch (e) {
      setError(e.response?.data?.message || '회원가입 중 오류가 발생했습니다.')
    } finally {
      setLoading(false)
    }
  }

  if (done) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="bg-white rounded-2xl shadow p-8 w-full max-w-md text-center">
          <div className="text-5xl mb-4">✅</div>
          <h2 className="text-xl font-bold text-gray-800 mb-2">가입 신청 완료</h2>
          <p className="text-gray-500 text-sm mb-6">
            관리자 승인 후 로그인하실 수 있습니다.<br />
            승인까지 시간이 소요될 수 있습니다.
          </p>
          <button
            onClick={() => navigate('/login')}
            className="w-full bg-blue-600 text-white py-2.5 rounded-lg text-sm font-medium hover:bg-blue-700"
          >
            로그인 페이지로
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="bg-white rounded-2xl shadow p-8 w-full max-w-md">
        <div className="mb-6">
          <h1 className="text-2xl font-bold text-gray-800">직원 가입 신청</h1>
          <p className="text-gray-500 text-sm mt-1">악세사리 ERP 시스템</p>
        </div>

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-lg mb-4">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">이름 *</label>
              <input
                type="text" required
                value={form.name} onChange={set('name')}
                placeholder="홍길동"
                className="w-full border rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">연락처</label>
              <input
                type="tel"
                value={form.phone} onChange={set('phone')}
                placeholder="010-0000-0000"
                className="w-full border rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">소속 매장 *</label>
            <select
              required
              value={form.storeId} onChange={set('storeId')}
              className="w-full border rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">매장을 선택하세요</option>
              {stores.map(s => <option key={s.storeId} value={s.storeId}>{s.name}</option>)}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">아이디 *</label>
            <input
              type="text" required minLength={4} maxLength={50}
              value={form.username} onChange={set('username')}
              placeholder="4자 이상"
              className="w-full border rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">비밀번호 *</label>
            <input
              type="password" required minLength={6} maxLength={50}
              value={form.password} onChange={set('password')}
              placeholder="6자 이상"
              className="w-full border rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">비밀번호 확인 *</label>
            <input
              type="password" required
              value={form.confirmPassword} onChange={set('confirmPassword')}
              placeholder="비밀번호 재입력"
              className="w-full border rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <button
            type="submit" disabled={loading}
            className="w-full bg-blue-600 text-white py-2.5 rounded-lg text-sm font-medium hover:bg-blue-700 disabled:opacity-60 mt-2"
          >
            {loading ? '신청 중...' : '가입 신청'}
          </button>
        </form>

        <p className="text-center text-sm text-gray-500 mt-5">
          이미 계정이 있으신가요?{' '}
          <Link to="/login" className="text-blue-600 hover:underline">로그인</Link>
        </p>
      </div>
    </div>
  )
}
