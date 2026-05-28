import { useEffect, useState } from 'react'
import api from '../api/axios'

export default function InventoryPage() {
  const [inventory, setInventory] = useState([])
  const [stores, setStores] = useState([])
  const [total, setTotal] = useState(0)
  const [page, setPage] = useState(1)
  const [storeId, setStoreId] = useState('')
  const [lowStock, setLowStock] = useState(false)
  const [loading, setLoading] = useState(false)

  const [adjustModal, setAdjustModal] = useState(null)
  const [transferModal, setTransferModal] = useState(null)
  const [adjustForm, setAdjustForm] = useState({ quantity: '', reason: '' })
  const [transferForm, setTransferForm] = useState({ toStoreId: '', quantity: '', notes: '' })
  const [saving, setSaving] = useState(false)

  const SIZE = 20

  useEffect(() => {
    api.get('/api/stores').then(r => setStores(r.data.data || []))
  }, [])

  useEffect(() => {
    fetchInventory()
  }, [page, storeId, lowStock])

  const fetchInventory = async () => {
    setLoading(true)
    try {
      const params = { page, size: SIZE }
      if (storeId) params.storeId = storeId
      if (lowStock) params.lowStock = true
      const r = await api.get('/api/inventory', { params })
      setInventory(r.data.data?.items || [])
      setTotal(r.data.data?.total || 0)
    } catch (e) {
      console.error(e)
    } finally {
      setLoading(false)
    }
  }

  const openAdjust = (item) => {
    setAdjustModal(item)
    setAdjustForm({ quantity: item.quantity, reason: '' })
  }

  const openTransfer = (item) => {
    setTransferModal(item)
    setTransferForm({ toStoreId: '', quantity: '', notes: '' })
  }

  const handleAdjust = async () => {
    if (!adjustForm.reason.trim()) return alert('사유를 입력해주세요.')
    setSaving(true)
    try {
      await api.patch('/api/inventory/adjust', {
        storeId: adjustModal.storeId,
        variantId: adjustModal.variantId,
        quantity: Number(adjustForm.quantity),
        reason: adjustForm.reason,
      })
      setAdjustModal(null)
      fetchInventory()
    } catch (e) {
      alert(e.response?.data?.message || '오류가 발생했습니다.')
    } finally {
      setSaving(false)
    }
  }

  const handleTransfer = async () => {
    if (!transferForm.toStoreId) return alert('이동할 매장을 선택해주세요.')
    if (!transferForm.quantity || Number(transferForm.quantity) <= 0) return alert('수량을 입력해주세요.')
    setSaving(true)
    try {
      await api.post('/api/inventory/transfer', {
        fromStoreId: transferModal.storeId,
        toStoreId: Number(transferForm.toStoreId),
        variantId: transferModal.variantId,
        quantity: Number(transferForm.quantity),
        notes: transferForm.notes,
      })
      setTransferModal(null)
      fetchInventory()
    } catch (e) {
      alert(e.response?.data?.message || '오류가 발생했습니다.')
    } finally {
      setSaving(false)
    }
  }

  const totalPages = Math.ceil(total / SIZE)

  return (
    <div className="p-6">
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-gray-800">재고 관리</h2>
        <p className="text-gray-500 text-sm mt-1">매장별 상품 재고 현황</p>
      </div>

      {/* 필터 */}
      <div className="flex gap-3 mb-4 flex-wrap">
        <select
          value={storeId}
          onChange={e => { setStoreId(e.target.value); setPage(1) }}
          className="border rounded-lg px-3 py-2 text-sm"
        >
          <option value="">전체 매장</option>
          {stores.map(s => <option key={s.storeId} value={s.storeId}>{s.name}</option>)}
        </select>
        <label className="flex items-center gap-2 text-sm text-gray-600 cursor-pointer">
          <input
            type="checkbox"
            checked={lowStock}
            onChange={e => { setLowStock(e.target.checked); setPage(1) }}
            className="w-4 h-4"
          />
          부족 재고만 보기
        </label>
        <span className="ml-auto text-sm text-gray-500">총 {total}건</span>
      </div>

      {/* 테이블 */}
      <div className="bg-white rounded-xl shadow overflow-x-auto">
        <table className="min-w-full text-sm">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="px-4 py-3 text-left font-medium text-gray-600">매장</th>
              <th className="px-4 py-3 text-left font-medium text-gray-600">상품명</th>
              <th className="px-4 py-3 text-left font-medium text-gray-600">SKU</th>
              <th className="px-4 py-3 text-left font-medium text-gray-600">색상</th>
              <th className="px-4 py-3 text-left font-medium text-gray-600">사이즈</th>
              <th className="px-4 py-3 text-right font-medium text-gray-600">현재고</th>
              <th className="px-4 py-3 text-right font-medium text-gray-600">최소수량</th>
              <th className="px-4 py-3 text-center font-medium text-gray-600">상태</th>
              <th className="px-4 py-3 text-center font-medium text-gray-600">작업</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {loading ? (
              <tr><td colSpan={9} className="text-center py-10 text-gray-400">불러오는 중...</td></tr>
            ) : inventory.length === 0 ? (
              <tr><td colSpan={9} className="text-center py-10 text-gray-400">데이터가 없습니다.</td></tr>
            ) : inventory.map((item, i) => (
              <tr key={i} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-gray-700">{item.storeName}</td>
                <td className="px-4 py-3 font-medium text-gray-800">{item.productName}</td>
                <td className="px-4 py-3 text-gray-500 font-mono text-xs">{item.sku}</td>
                <td className="px-4 py-3 text-gray-600">{item.color || '-'}</td>
                <td className="px-4 py-3 text-gray-600">{item.size || '-'}</td>
                <td className="px-4 py-3 text-right font-semibold text-gray-800">{item.quantity}</td>
                <td className="px-4 py-3 text-right text-gray-500">{item.minQuantity}</td>
                <td className="px-4 py-3 text-center">
                  {item.lowStock ? (
                    <span className="bg-red-100 text-red-700 text-xs font-medium px-2 py-0.5 rounded-full">부족</span>
                  ) : (
                    <span className="bg-green-100 text-green-700 text-xs font-medium px-2 py-0.5 rounded-full">정상</span>
                  )}
                </td>
                <td className="px-4 py-3 text-center">
                  <div className="flex justify-center gap-2">
                    <button
                      onClick={() => openAdjust(item)}
                      className="text-xs bg-blue-50 text-blue-600 hover:bg-blue-100 px-2 py-1 rounded"
                    >조정</button>
                    <button
                      onClick={() => openTransfer(item)}
                      className="text-xs bg-amber-50 text-amber-600 hover:bg-amber-100 px-2 py-1 rounded"
                    >이동</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* 페이지네이션 */}
      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-4">
          <button onClick={() => setPage(p => Math.max(1, p - 1))} disabled={page === 1}
            className="px-3 py-1 text-sm border rounded disabled:opacity-40">이전</button>
          <span className="px-3 py-1 text-sm text-gray-600">{page} / {totalPages}</span>
          <button onClick={() => setPage(p => Math.min(totalPages, p + 1))} disabled={page === totalPages}
            className="px-3 py-1 text-sm border rounded disabled:opacity-40">다음</button>
        </div>
      )}

      {/* 재고 조정 모달 */}
      {adjustModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl shadow-lg w-full max-w-sm p-6">
            <h3 className="text-lg font-bold mb-1">재고 조정</h3>
            <p className="text-sm text-gray-500 mb-4">{adjustModal.storeName} · {adjustModal.productName} ({adjustModal.sku})</p>
            <label className="block text-sm font-medium text-gray-700 mb-1">수량 (절대값)</label>
            <input
              type="number" min="0"
              value={adjustForm.quantity}
              onChange={e => setAdjustForm(f => ({ ...f, quantity: e.target.value }))}
              className="w-full border rounded-lg px-3 py-2 text-sm mb-3"
            />
            <label className="block text-sm font-medium text-gray-700 mb-1">사유 *</label>
            <input
              type="text" placeholder="예: 실사 결과 반영"
              value={adjustForm.reason}
              onChange={e => setAdjustForm(f => ({ ...f, reason: e.target.value }))}
              className="w-full border rounded-lg px-3 py-2 text-sm mb-4"
            />
            <div className="flex gap-3">
              <button onClick={() => setAdjustModal(null)}
                className="flex-1 border rounded-lg py-2 text-sm text-gray-600 hover:bg-gray-50">취소</button>
              <button onClick={handleAdjust} disabled={saving}
                className="flex-1 bg-blue-600 text-white rounded-lg py-2 text-sm hover:bg-blue-700 disabled:opacity-60">
                {saving ? '저장 중...' : '저장'}</button>
            </div>
          </div>
        </div>
      )}

      {/* 재고 이동 모달 */}
      {transferModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl shadow-lg w-full max-w-sm p-6">
            <h3 className="text-lg font-bold mb-1">재고 이동</h3>
            <p className="text-sm text-gray-500 mb-4">
              {transferModal.storeName} → ? · {transferModal.productName} ({transferModal.sku})
            </p>
            <label className="block text-sm font-medium text-gray-700 mb-1">이동할 매장 *</label>
            <select
              value={transferForm.toStoreId}
              onChange={e => setTransferForm(f => ({ ...f, toStoreId: e.target.value }))}
              className="w-full border rounded-lg px-3 py-2 text-sm mb-3"
            >
              <option value="">매장 선택</option>
              {stores.filter(s => s.storeId !== transferModal.storeId)
                .map(s => <option key={s.storeId} value={s.storeId}>{s.name}</option>)}
            </select>
            <label className="block text-sm font-medium text-gray-700 mb-1">수량 * (현재: {transferModal.quantity})</label>
            <input
              type="number" min="1" max={transferModal.quantity}
              value={transferForm.quantity}
              onChange={e => setTransferForm(f => ({ ...f, quantity: e.target.value }))}
              className="w-full border rounded-lg px-3 py-2 text-sm mb-3"
            />
            <label className="block text-sm font-medium text-gray-700 mb-1">메모</label>
            <input
              type="text" placeholder="선택 사항"
              value={transferForm.notes}
              onChange={e => setTransferForm(f => ({ ...f, notes: e.target.value }))}
              className="w-full border rounded-lg px-3 py-2 text-sm mb-4"
            />
            <div className="flex gap-3">
              <button onClick={() => setTransferModal(null)}
                className="flex-1 border rounded-lg py-2 text-sm text-gray-600 hover:bg-gray-50">취소</button>
              <button onClick={handleTransfer} disabled={saving}
                className="flex-1 bg-amber-500 text-white rounded-lg py-2 text-sm hover:bg-amber-600 disabled:opacity-60">
                {saving ? '처리 중...' : '이동'}</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
