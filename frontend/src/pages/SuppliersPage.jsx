import { useState, useEffect } from 'react'
import axios from '../api/axios'

const PAYMENT_TERMS = ['선결제', '현금', '30일', '60일', '90일', '어음']

export default function SuppliersPage() {
  const [suppliers, setSuppliers] = useState([])
  const [total, setTotal] = useState(0)
  const [page, setPage] = useState(1)
  const [search, setSearch] = useState('')
  const [filterActive, setFilterActive] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editTarget, setEditTarget] = useState(null)
  const [form, setForm] = useState(defaultForm())
  const [loading, setLoading] = useState(false)

  function defaultForm() {
    return {
      supplierName: '', contactName: '', phone: '', email: '',
      address: '', paymentTerms: '', leadTimeDays: '', isActive: true
    }
  }

  const fetchSuppliers = async () => {
    setLoading(true)
    try {
      const params = { page, size: 20 }
      if (search) params.search = search
      if (filterActive !== '') params.isActive = filterActive
      const res = await axios.get('/api/suppliers', { params })
      setSuppliers(res.data.data.items)
      setTotal(res.data.data.total)
    } catch (e) {
      console.error(e)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchSuppliers() }, [page, search, filterActive])

  const openCreate = () => {
    setEditTarget(null)
    setForm(defaultForm())
    setShowModal(true)
  }

  const openEdit = (s) => {
    setEditTarget(s)
    setForm({
      supplierName: s.supplierName || '',
      contactName:  s.contactName  || '',
      phone:        s.phone        || '',
      email:        s.email        || '',
      address:      s.address      || '',
      paymentTerms: s.paymentTerms || '',
      leadTimeDays: s.leadTimeDays ?? '',
      isActive:     s.isActive ?? true
    })
    setShowModal(true)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      const body = {
        ...form,
        leadTimeDays: form.leadTimeDays !== '' ? Number(form.leadTimeDays) : null
      }
      if (editTarget) {
        await axios.put(`/api/suppliers/${editTarget.supplierId}`, body)
      } else {
        await axios.post('/api/suppliers', body)
      }
      setShowModal(false)
      fetchSuppliers()
    } catch (e) {
      alert(e.response?.data?.message || '저장 중 오류가 발생했습니다.')
    }
  }

  const totalPages = Math.ceil(total / 20)

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">공급업체 관리</h1>
          <p className="text-sm text-gray-500 mt-0.5">총 {total}개 업체</p>
        </div>
        <button onClick={openCreate}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 text-sm font-medium">
          + 업체 등록
        </button>
      </div>

      {/* 검색 / 필터 */}
      <div className="flex gap-3 mb-4">
        <input
          className="border rounded-lg px-3 py-2 text-sm w-72"
          placeholder="업체명 / 담당자 / 전화번호 검색"
          value={search}
          onChange={e => { setSearch(e.target.value); setPage(1) }}
        />
        <select
          className="border rounded-lg px-3 py-2 text-sm"
          value={filterActive}
          onChange={e => { setFilterActive(e.target.value); setPage(1) }}>
          <option value="">전체 상태</option>
          <option value="true">활성</option>
          <option value="false">비활성</option>
        </select>
      </div>

      {/* 목록 */}
      <div className="bg-white rounded-xl shadow overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 text-gray-500 uppercase text-xs">
            <tr>
              <th className="px-4 py-3 text-left">업체명</th>
              <th className="px-4 py-3 text-left">담당자</th>
              <th className="px-4 py-3 text-left">전화번호</th>
              <th className="px-4 py-3 text-left">이메일</th>
              <th className="px-4 py-3 text-left">결제조건</th>
              <th className="px-4 py-3 text-center">리드타임</th>
              <th className="px-4 py-3 text-center">상태</th>
              <th className="px-4 py-3 text-center">관리</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {loading ? (
              <tr><td colSpan={8} className="text-center py-12 text-gray-400">불러오는 중...</td></tr>
            ) : suppliers.length === 0 ? (
              <tr><td colSpan={8} className="text-center py-12 text-gray-400">등록된 업체가 없습니다</td></tr>
            ) : suppliers.map(s => (
              <tr key={s.supplierId} className="hover:bg-gray-50 transition">
                <td className="px-4 py-3 font-medium text-gray-800">{s.supplierName}</td>
                <td className="px-4 py-3 text-gray-600">{s.contactName || '-'}</td>
                <td className="px-4 py-3 text-gray-600">{s.phone || '-'}</td>
                <td className="px-4 py-3 text-gray-500 text-xs">{s.email || '-'}</td>
                <td className="px-4 py-3 text-gray-600">{s.paymentTerms || '-'}</td>
                <td className="px-4 py-3 text-center text-gray-600">
                  {s.leadTimeDays != null ? `${s.leadTimeDays}일` : '-'}
                </td>
                <td className="px-4 py-3 text-center">
                  <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${
                    s.isActive ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500'
                  }`}>
                    {s.isActive ? '활성' : '비활성'}
                  </span>
                </td>
                <td className="px-4 py-3 text-center">
                  <button onClick={() => openEdit(s)}
                    className="text-blue-600 hover:text-blue-800 text-xs font-medium">
                    수정
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* 페이지네이션 */}
      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-4">
          <button disabled={page === 1} onClick={() => setPage(p => p - 1)}
            className="px-3 py-1 rounded border text-sm disabled:opacity-40 hover:bg-gray-50">이전</button>
          <span className="px-3 py-1 text-sm text-gray-600">{page} / {totalPages}</span>
          <button disabled={page === totalPages} onClick={() => setPage(p => p + 1)}
            className="px-3 py-1 rounded border text-sm disabled:opacity-40 hover:bg-gray-50">다음</button>
        </div>
      )}

      {/* 등록 / 수정 모달 */}
      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 w-[520px] max-h-[90vh] overflow-y-auto shadow-xl">
            <h2 className="text-lg font-bold mb-4">{editTarget ? '업체 수정' : '업체 등록'}</h2>
            <form onSubmit={handleSubmit} className="space-y-3">
              <div>
                <label className="block text-xs text-gray-500 mb-1">업체명 *</label>
                <input required
                  className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-300"
                  placeholder="예: (주)아름다운악세사리"
                  value={form.supplierName}
                  onChange={e => setForm(f => ({ ...f, supplierName: e.target.value }))} />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs text-gray-500 mb-1">담당자명</label>
                  <input
                    className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-300"
                    value={form.contactName}
                    onChange={e => setForm(f => ({ ...f, contactName: e.target.value }))} />
                </div>
                <div>
                  <label className="block text-xs text-gray-500 mb-1">전화번호</label>
                  <input
                    className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-300"
                    placeholder="02-0000-0000"
                    value={form.phone}
                    onChange={e => setForm(f => ({ ...f, phone: e.target.value }))} />
                </div>
              </div>

              <div>
                <label className="block text-xs text-gray-500 mb-1">이메일</label>
                <input type="email"
                  className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-300"
                  value={form.email}
                  onChange={e => setForm(f => ({ ...f, email: e.target.value }))} />
              </div>

              <div>
                <label className="block text-xs text-gray-500 mb-1">주소</label>
                <input
                  className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-300"
                  value={form.address}
                  onChange={e => setForm(f => ({ ...f, address: e.target.value }))} />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs text-gray-500 mb-1">결제조건</label>
                  <select
                    className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-300"
                    value={form.paymentTerms}
                    onChange={e => setForm(f => ({ ...f, paymentTerms: e.target.value }))}>
                    <option value="">선택</option>
                    {PAYMENT_TERMS.map(t => <option key={t} value={t}>{t}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-xs text-gray-500 mb-1">리드타임 (일)</label>
                  <input type="number" min="0"
                    className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-300"
                    placeholder="예: 7"
                    value={form.leadTimeDays}
                    onChange={e => setForm(f => ({ ...f, leadTimeDays: e.target.value }))} />
                </div>
              </div>

              <div className="flex items-center gap-2 pt-1">
                <input type="checkbox" id="isActive"
                  checked={form.isActive}
                  onChange={e => setForm(f => ({ ...f, isActive: e.target.checked }))} />
                <label htmlFor="isActive" className="text-sm text-gray-700 cursor-pointer">활성 업체</label>
              </div>

              <div className="flex gap-3 pt-2">
                <button type="button" onClick={() => setShowModal(false)}
                  className="flex-1 border rounded-lg py-2 text-sm text-gray-600 hover:bg-gray-50">
                  취소
                </button>
                <button type="submit"
                  className="flex-1 bg-blue-600 text-white rounded-lg py-2 text-sm font-medium hover:bg-blue-700">
                  {editTarget ? '수정 저장' : '등록'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
