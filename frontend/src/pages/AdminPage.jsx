import { useEffect, useState } from 'react'
import api from '../api/axios'
import { useAuth } from '../context/AuthContext'
import { useNavigate } from 'react-router-dom'

export default function AdminPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [pending, setPending] = useState([])
  const [loading, setLoading] = useState(false)
  const [processing, setProcessing] = useState(null)

  useEffect(() => {
    if (user?.role !== 'admin') { navigate('/'); return }
    fetchPending()
  }, [])

  const fetchPending = async () => {
    setLoading(true)
    try { const r = await api.get('/api/admin/users/pending'); setPending(r.data.data || []) }
    catch (e) { console.error(e) }
    finally { setLoading(false) }
  }

  const handleApprove = async (userId) => {
    setProcessing(userId)
    try { await api.post(`/api/admin/users/${userId}/approve`); setPending(p => p.filter(u => u.userId !== userId)) }
    catch (e) { alert(e.response?.data?.message || '오류') }
    finally { setProcessing(null) }
  }

  const handleReject = async (userId, username) => {
    if (!confirm(`'${username}' 계정을 삭제하시겠습니까?`)) return
    setProcessing(userId)
    try { await api.delete(`/api/admin/users/${userId}`); setPending(p => p.filter(u => u.userId !== userId)) }
    catch (e) { alert(e.response?.data?.message || '오류') }
    finally { setProcessing(null) }
  }

  return (
    <div className="p-6">
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-gray-800">가입 승인 관리</h2>
        <p className="text-gray-500 text-sm mt-1">승인 대기 중인 계정을 관리합니다.</p>
      </div>
      <div className="bg-white rounded-xl shadow">
        <div className="px-6 py-4 border-b flex items-center justify-between">
          <h3 className="font-semibold text-gray-700">승인 대기 목록</h3>
          <span className="bg-orange-100 text-orange-700 text-xs font-bold px-2.5 py-1 rounded-full">{pending.length}명</span>
        </div>
        {loading ? <div className="text-center py-12 text-gray-400">불러오는 중...</div>
        : pending.length === 0 ? (
          <div className="text-center py-12">
            <div className="text-4xl mb-3">✅</div>
            <p className="text-gray-400 text-sm">승인 대기 중인 계정이 없습니다.</p>
          </div>
        ) : (
          <table className="min-w-full text-sm">
            <thead className="bg-gray-50 border-b">
              <tr>
                <th className="px-6 py-3 text-left font-medium text-gray-600">이름</th>
                <th className="px-6 py-3 text-left font-medium text-gray-600">아이디</th>
                <th className="px-6 py-3 text-left font-medium text-gray-600">소속 매장</th>
                <th className="px-6 py-3 text-left font-medium text-gray-600">직급</th>
                <th className="px-6 py-3 text-center font-medium text-gray-600">처리</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {pending.map(u => (
                <tr key={u.userId} className="hover:bg-gray-50">
                  <td className="px-6 py-4 font-medium text-gray-800">{u.staffName || u.name || '-'}</td>
                  <td className="px-6 py-4 text-gray-600 font-mono">{u.username}</td>
                  <td className="px-6 py-4 text-gray-600">{u.storeName || '-'}</td>
                  <td className="px-6 py-4">
                    <span className="bg-gray-100 text-gray-600 text-xs px-2 py-0.5 rounded">{u.role === 'admin' ? '관리자' : u.role === 'manager' ? '매니저' : '직원'}</span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex justify-center gap-2">
                      <button onClick={() => handleApprove(u.userId)} disabled={processing===u.userId} className="bg-green-600 text-white text-xs px-3 py-1.5 rounded-lg hover:bg-green-700 disabled:opacity-60">{processing===u.userId?'...':'승인'}</button>
                      <button onClick={() => handleReject(u.userId, u.username)} disabled={processing===u.userId} className="bg-red-100 text-red-600 text-xs px-3 py-1.5 rounded-lg hover:bg-red-200 disabled:opacity-60">거절</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  )
}
