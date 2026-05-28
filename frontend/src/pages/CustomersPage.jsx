import { useEffect, useState } from 'react'
import api from '../api/axios'

const GRADE_STYLE = {
  bronze: 'bg-orange-100 text-orange-700',
  silver: 'bg-gray-200 text-gray-700',
  gold:   'bg-yellow-100 text-yellow-700',
  vip:    'bg-purple-100 text-purple-700',
}

export default function CustomersPage() {
  const [customers, setCustomers] = useState([])
  const [total, setTotal] = useState(0)
  const [search, setSearch] = useState('')
  const [grade, setGrade] = useState('')
  const [page, setPage] = useState(1)
  const [loading, setLoading] = useState(false)
  const [modal, setModal] = useState(false)
  const [stores, setStores] = useState([])
  const [form, setForm] = useState({ name: '', phone: '', email: '', birthDate: '', createdStoreId: '' })
  const [saving, setSaving] = useState(false)
  const SIZE = 20

  useEffect(() => { api.get('/api/stores').then(r => setStores(r.data.data || [])) }, [])
  useEffect(() => { fetchCustomers() }, [page, grade])

  const fetchCustomers = async () => {
    setLoading(true)
    try {
      const r = await api.get('/api/customers', { params: { search: search || undefined, grade: grade || undefined, page, size: SIZE } })
      setCustomers(r.data.data?.items || [])
      setTotal(r.data.data?.total || 0)
    } finally { setLoading(false) }
  }

  const handleSearch = (e) => { e.preventDefault(); setPage(1); fetchCustomers() }

  const handleCreate = async (e) => {
    e.preventDefault(); setSaving(true)
    try {
      await api.post('/api/customers', {
        ...form,
        birthDate: form.birthDate || undefined,
        createdStoreId: form.createdStoreId ? Number(form.createdStoreId) : undefined,
      })
      setModal(false)
      setForm({ name: '', phone: '', email: '', birthDate: '', createdStoreId: '' })
      fetchCustomers()
    } catch (e) { alert(e.response?.data?.message || '오류가 발생했습니다.') }
    finally { setSaving(false) }
  }

  const totalPages = Math.ceil(total / SIZE)

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">고객 관리</h2>
          <p className="text-gray-500 text-sm mt-1">회원 정보 및 포인트 관리</p>
        </div>
        <button onClick={() => setModal(true)} className="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm hover:bg-blue-700">
          + 고객 등록
        </button>
      </div>

      <div className="flex gap-3 mb-4 flex-wrap">
        <form onSubmit={handleSearch} className="flex gap-2">
          <input value={search} onChange={e => setSearch(e.target.value)} placeholder="이름 또는 전화번호"
            className="border rounded-lg px-3 py-2 text-sm w-52" />
          <button type="submit" className="bg-gray-700 text-white px-3 py-2 rounded-lg text-sm">검색</button>
        </form>
        <select value={grade} onChange={e => { setGrade(e.target.value); setPage(1) }} className="border rounded-lg px-3 py-2 text-sm">
          <option value="">전체 등급</option>
          <option value="bronze">Bronze</option>
          <option value="silver">Silver</option>
          <option value="gold">Gold</option>
          <option value="vip">VIP</option>
        </select>
        <span className="ml-auto text-sm text-gray-500 self-center">총 {total}명</span>
      </div>

      <div className="bg-white rounded-xl shadow overflow-x-auto">
        <table className="min-w-full text-sm">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="px-5 py-3 text-left font-medium text-gray-600">이름</th>
              <th className="px-5 py-3 text-left font-medium text-gray-600">전화번호</th>
              <th className="px-5 py-3 text-left font-medium text-gray-600">이메일</th>
              <th className="px-5 py-3 text-center font-medium text-gray-600">등급</th>
              <th className="px-5 py-3 text-right font-medium text-gray-600">누적구매</th>
              <th className="px-5 py-3 text-right font-medium text-gray-600">방문횟수</th>
              <th className="px-5 py-3 text-right font-medium text-gray-600">포인트</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {loading ? (
              <tr><td colSpan={7} className="text-center py-10 text-gray-400">불러오는 중...</td></tr>
            ) : customers.length === 0 ? (
              <tr><td colSpan={7} className="text-center py-10 text-gray-400">데이터가 없습니다.</td></tr>
            ) : customers.map(c => (
              <tr key={c.customerId} className="hover:bg-gray-50">
                <td className="px-5 py-3 font-medium text-gray-800">{c.name}</td>
                <td className="px-5 py-3 text-gray-600">{c.phone}</td>
                <td className="px-5 py-3 text-gray-500">{c.email || '-'}</td>
                <td className="px-5 py-3 text-center">
                  <span className={`text-xs font-bold px-2 py-0.5 rounded-full ${GRADE_STYLE[c.grade] || 'bg-gray-100 text-gray-600'}`}>
                    {c.grade?.toUpperCase()}
                  </span>
                </td>
                <td className="px-5 py-3 text-right text-gray-700">{Number(c.totalPurchaseAmount || 0).toLocaleString()}원</td>
                <td className="px-5 py-3 text-right text-gray-600">{c.visitCount}회</td>
                <td className="px-5 py-3 text-right font-semibold text-blue-600">{(c.pointBalance || 0).toLocaleString()}P</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-4">
          <button onClick={() => setPage(p => Math.max(1, p-1))} disabled={page===1} className="px-3 py-1 text-sm border rounded disabled:opacity-40">이전</button>
          <span className="px-3 py-1 text-sm text-gray-600">{page} / {totalPages}</span>
          <button onClick={() => setPage(p => Math.min(totalPages, p+1))} disabled={page===totalPages} className="px-3 py-1 text-sm border rounded disabled:opacity-40">다음</button>
        </div>
      )}

      {modal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl shadow-lg w-full max-w-md p-6">
            <h3 className="text-lg font-bold mb-4">고객 등록</h3>
            <form onSubmit={handleCreate} className="space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">이름 *</label>
                  <input required value={form.name} onChange={e => setForm(f=>({...f,name:e.target.value}))} className="w-full border rounded-lg px-3 py-2 text-sm" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">전화번호 *</label>
                  <input required value={form.phone} onChange={e => setForm(f=>({...f,phone:e.target.value}))} placeholder="010-0000-0000" className="w-full border rounded-lg px-3 py-2 text-sm" />
                </div>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">이메일</label>
                <input type="email" value={form.email} onChange={e => setForm(f=>({...f,email:e.target.value}))} className="w-full border rounded-lg px-3 py-2 text-sm" />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">생년월일</label>
                  <input type="date" value={form.birthDate} onChange={e => setForm(f=>({...f,birthDate:e.target.value}))} className="w-full border rounded-lg px-3 py-2 text-sm" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">소속 매장</label>
                  <select value={form.createdStoreId} onChange={e => setForm(f=>({...f,createdStoreId:e.target.value}))} className="w-full border rounded-lg px-3 py-2 text-sm">
                    <option value="">선택</option>
                    {stores.map(s => <option key={s.storeId} value={s.storeId}>{s.name}</option>)}
                  </select>
                </div>
              </div>
              <div className="flex gap-3 pt-2">
                <button type="button" onClick={() => setModal(false)} className="flex-1 border rounded-lg py-2 text-sm text-gray-600 hover:bg-gray-50">취소</button>
                <button type="submit" disabled={saving} className="flex-1 bg-blue-600 text-white rounded-lg py-2 text-sm hover:bg-blue-700 disabled:opacity-60">{saving ? '저장 중...' : '등록'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
