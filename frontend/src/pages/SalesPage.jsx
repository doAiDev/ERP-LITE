import { useEffect, useState } from 'react'
import api from '../api/axios'

const PAY_LABEL = { cash: '현금', card: '카드', kakao_pay: '카카오페이', naver_pay: '네이버페이' }
const PAY_STYLE = { cash: 'bg-green-100 text-green-700', card: 'bg-blue-100 text-blue-700', kakao_pay: 'bg-yellow-100 text-yellow-700', naver_pay: 'bg-emerald-100 text-emerald-700' }

export default function SalesPage() {
  const [sales, setSales] = useState([])
  const [total, setTotal] = useState(0)
  const [page, setPage] = useState(1)
  const [storeId, setStoreId] = useState('')
  const [startDate, setStartDate] = useState('')
  const [endDate, setEndDate] = useState('')
  const [stores, setStores] = useState([])
  const [stats, setStats] = useState(null)
  const [loading, setLoading] = useState(false)

  // create modal
  const [modal, setModal] = useState(false)
  const [products, setProducts] = useState([])
  const [variantMap, setVariantMap] = useState({})
  const [items, setItems] = useState([{ variantId: '', productId: '', quantity: 1, unitPrice: '' }])
  const [payMethod, setPayMethod] = useState('card')
  const [discountAmount, setDiscountAmount] = useState(0)
  const [custPhone, setCustPhone] = useState('')
  const [customer, setCustomer] = useState(null)
  const [custSearching, setCustSearching] = useState(false)
  const [saleStoreId, setSaleStoreId] = useState('')
  const [saving, setSaving] = useState(false)

  const SIZE = 20

  useEffect(() => {
    api.get('/api/stores').then(r => { const s = r.data.data || []; setStores(s); if (s[0]) setSaleStoreId(String(s[0].storeId)) })
    fetchStats()
  }, [])
  useEffect(() => { fetchSales() }, [page, storeId, startDate, endDate])

  const fetchSales = async () => {
    setLoading(true)
    try {
      const params = { page, size: SIZE }
      if (storeId) params.storeId = storeId
      if (startDate) params.startDate = startDate
      if (endDate) params.endDate = endDate
      const r = await api.get('/api/sales', { params })
      setSales(r.data.data?.items || [])
      setTotal(r.data.data?.total || 0)
    } finally { setLoading(false) }
  }

  const fetchStats = async () => {
    try { const r = await api.get('/api/sales/today-stats'); setStats(r.data.data) } catch {}
  }

  const openModal = async () => {
    const r = await api.get('/api/products', { params: { size: 200, active: true } })
    setProducts(r.data.data?.items || [])
    setItems([{ variantId: '', productId: '', quantity: 1, unitPrice: '' }])
    setPayMethod('card'); setDiscountAmount(0); setCustPhone(''); setCustomer(null)
    setModal(true)
  }

  const loadVariants = async (productId, idx) => {
    if (!productId) return
    if (!variantMap[productId]) {
      const r = await api.get(`/api/products/${productId}`)
      const p = r.data.data
      setVariantMap(m => ({ ...m, [productId]: { variants: p.variants || [], sellingPrice: p.sellingPrice } }))
    }
    const updated = [...items]
    updated[idx] = { ...updated[idx], productId, variantId: '', unitPrice: variantMap[productId]?.sellingPrice || '' }
    setItems(updated)
  }

  const setItemField = (idx, field, val) => {
    const updated = [...items]
    updated[idx] = { ...updated[idx], [field]: val }
    if (field === 'variantId' && val) {
      const prod = variantMap[updated[idx].productId]
      if (prod) updated[idx].unitPrice = prod.sellingPrice || ''
    }
    setItems(updated)
  }

  const addItem = () => setItems(i => [...i, { variantId: '', productId: '', quantity: 1, unitPrice: '' }])
  const removeItem = (idx) => setItems(i => i.filter((_, j) => j !== idx))

  const searchCustomer = async () => {
    if (!custPhone.trim()) return
    setCustSearching(true)
    try {
      const r = await api.get('/api/customers', { params: { search: custPhone, size: 1 } })
      const list = r.data.data?.items || []
      setCustomer(list.length > 0 ? list[0] : null)
      if (list.length === 0) alert('고객을 찾을 수 없습니다.')
    } finally { setCustSearching(false) }
  }

  const subTotal = items.reduce((sum, it) => sum + (Number(it.unitPrice) * Number(it.quantity) || 0), 0)
  const finalTotal = Math.max(0, subTotal - Number(discountAmount || 0))

  const handleCreate = async (e) => {
    e.preventDefault()
    if (!saleStoreId) return alert('매장을 선택해주세요.')
    const validItems = items.filter(it => it.variantId && it.quantity > 0 && it.unitPrice)
    if (validItems.length === 0) return alert('상품을 최소 1개 입력해주세요.')
    setSaving(true)
    try {
      await api.post('/api/sales', {
        storeId: Number(saleStoreId),
        customerId: customer?.customerId || null,
        paymentMethod: payMethod,
        discountAmount: Number(discountAmount || 0),
        items: validItems.map(it => ({ variantId: Number(it.variantId), quantity: Number(it.quantity), unitPrice: Number(it.unitPrice), discountRate: 0 }))
      })
      setModal(false); fetchSales(); fetchStats()
    } catch (e) { alert(e.response?.data?.message || '오류가 발생했습니다.') }
    finally { setSaving(false) }
  }

  const handleRefund = async (saleId) => {
    if (!confirm('이 매출을 환불 처리하시겠습니까?')) return
    try { await api.post(`/api/sales/${saleId}/refund`); fetchSales() }
    catch (e) { alert(e.response?.data?.message || '오류') }
  }

  const totalPages = Math.ceil(total / SIZE)

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-4">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">매출 관리</h2>
          <p className="text-gray-500 text-sm mt-1">매출 등록 및 현황</p>
        </div>
        <button onClick={openModal} className="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm hover:bg-blue-700">+ 매출 등록</button>
      </div>

      {stats && (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-5">
          {[['오늘 매출 건수', stats.sale_count + '건'], ['오늘 매출액', Number(stats.total_sales||0).toLocaleString()+'원'], ['현금', Number(stats.cash_amount||0).toLocaleString()+'원'], ['카드', Number(stats.card_amount||0).toLocaleString()+'원']].map(([k,v]) => (
            <div key={k} className="bg-white rounded-xl shadow p-4">
              <p className="text-xs text-gray-500">{k}</p>
              <p className="text-xl font-bold text-gray-800 mt-1">{v}</p>
            </div>
          ))}
        </div>
      )}

      <div className="flex gap-3 mb-4 flex-wrap">
        <select value={storeId} onChange={e => { setStoreId(e.target.value); setPage(1) }} className="border rounded-lg px-3 py-2 text-sm">
          <option value="">전체 매장</option>
          {stores.map(s => <option key={s.storeId} value={s.storeId}>{s.name}</option>)}
        </select>
        <input type="date" value={startDate} onChange={e => { setStartDate(e.target.value); setPage(1) }} className="border rounded-lg px-3 py-2 text-sm" />
        <span className="self-center text-gray-400">~</span>
        <input type="date" value={endDate} onChange={e => { setEndDate(e.target.value); setPage(1) }} className="border rounded-lg px-3 py-2 text-sm" />
        <span className="ml-auto self-center text-sm text-gray-500">총 {total}건</span>
      </div>

      <div className="bg-white rounded-xl shadow overflow-x-auto">
        <table className="min-w-full text-sm">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="px-4 py-3 text-left font-medium text-gray-600">일시</th>
              <th className="px-4 py-3 text-left font-medium text-gray-600">매장</th>
              <th className="px-4 py-3 text-left font-medium text-gray-600">고객</th>
              <th className="px-4 py-3 text-right font-medium text-gray-600">금액</th>
              <th className="px-4 py-3 text-center font-medium text-gray-600">결제</th>
              <th className="px-4 py-3 text-center font-medium text-gray-600">상태</th>
              <th className="px-4 py-3 text-center font-medium text-gray-600">작업</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {loading ? <tr><td colSpan={7} className="text-center py-10 text-gray-400">불러오는 중...</td></tr>
            : sales.length === 0 ? <tr><td colSpan={7} className="text-center py-10 text-gray-400">데이터가 없습니다.</td></tr>
            : sales.map(s => (
              <tr key={s.saleId} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-gray-500 text-xs">{s.saleDate?.replace('T',' ').slice(0,16)}</td>
                <td className="px-4 py-3 text-gray-700">{s.storeName}</td>
                <td className="px-4 py-3 text-gray-600">{s.customerName || '-'}</td>
                <td className="px-4 py-3 text-right font-semibold text-gray-800">{Number(s.totalAmount).toLocaleString()}원</td>
                <td className="px-4 py-3 text-center">
                  <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${PAY_STYLE[s.paymentMethod] || 'bg-gray-100 text-gray-600'}`}>{PAY_LABEL[s.paymentMethod] || s.paymentMethod}</span>
                </td>
                <td className="px-4 py-3 text-center">
                  {s.refunded ? <span className="text-xs bg-red-100 text-red-600 px-2 py-0.5 rounded-full">환불</span>
                  : <span className="text-xs bg-green-100 text-green-600 px-2 py-0.5 rounded-full">정상</span>}
                </td>
                <td className="px-4 py-3 text-center">
                  {!s.refunded && <button onClick={() => handleRefund(s.saleId)} className="text-xs text-red-500 hover:underline">환불</button>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-4">
          <button onClick={() => setPage(p => Math.max(1,p-1))} disabled={page===1} className="px-3 py-1 text-sm border rounded disabled:opacity-40">이전</button>
          <span className="px-3 py-1 text-sm text-gray-600">{page} / {totalPages}</span>
          <button onClick={() => setPage(p => Math.min(totalPages,p+1))} disabled={page===totalPages} className="px-3 py-1 text-sm border rounded disabled:opacity-40">다음</button>
        </div>
      )}

      {modal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 overflow-y-auto py-6">
          <div className="bg-white rounded-xl shadow-lg w-full max-w-2xl mx-4 p-6">
            <h3 className="text-lg font-bold mb-4">매출 등록</h3>
            <form onSubmit={handleCreate} className="space-y-4">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">매장 *</label>
                  <select value={saleStoreId} onChange={e => setSaleStoreId(e.target.value)} required className="w-full border rounded-lg px-3 py-2 text-sm">
                    <option value="">선택</option>
                    {stores.map(s => <option key={s.storeId} value={s.storeId}>{s.name}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">결제 방법 *</label>
                  <select value={payMethod} onChange={e => setPayMethod(e.target.value)} className="w-full border rounded-lg px-3 py-2 text-sm">
                    <option value="card">카드</option>
                    <option value="cash">현금</option>
                    <option value="kakao_pay">카카오페이</option>
                    <option value="naver_pay">네이버페이</option>
                  </select>
                </div>
              </div>

              <div className="flex gap-2">
                <div className="flex-1">
                  <label className="block text-xs font-medium text-gray-700 mb-1">고객 전화번호 (선택)</label>
                  <div className="flex gap-2">
                    <input value={custPhone} onChange={e => setCustPhone(e.target.value)} placeholder="010-0000-0000" className="flex-1 border rounded-lg px-3 py-2 text-sm" />
                    <button type="button" onClick={searchCustomer} disabled={custSearching} className="bg-gray-600 text-white px-3 py-2 rounded-lg text-sm">검색</button>
                  </div>
                </div>
                {customer && (
                  <div className="flex items-end">
                    <div className="bg-blue-50 border border-blue-200 rounded-lg px-3 py-2 text-sm">
                      <span className="font-medium text-blue-700">{customer.name}</span>
                      <span className="text-blue-500 ml-2">{(customer.pointBalance||0).toLocaleString()}P</span>
                    </div>
                  </div>
                )}
              </div>

              <div>
                <div className="flex items-center justify-between mb-2">
                  <label className="text-xs font-medium text-gray-700">상품 목록 *</label>
                  <button type="button" onClick={addItem} className="text-xs text-blue-600 hover:underline">+ 상품 추가</button>
                </div>
                <div className="space-y-2">
                  {items.map((item, idx) => (
                    <div key={idx} className="flex gap-2 items-center">
                      <select value={item.productId} onChange={e => loadVariants(e.target.value, idx)} className="flex-1 border rounded-lg px-2 py-1.5 text-sm">
                        <option value="">상품 선택</option>
                        {products.map(p => <option key={p.productId} value={p.productId}>{p.name}</option>)}
                      </select>
                      <select value={item.variantId} onChange={e => setItemField(idx, 'variantId', e.target.value)} className="flex-1 border rounded-lg px-2 py-1.5 text-sm">
                        <option value="">옵션 선택</option>
                        {(variantMap[item.productId]?.variants || []).map(v => <option key={v.variantId} value={v.variantId}>{v.sku} {v.color && `(${v.color})`}</option>)}
                      </select>
                      <input type="number" min="1" value={item.quantity} onChange={e => setItemField(idx, 'quantity', e.target.value)} className="w-16 border rounded-lg px-2 py-1.5 text-sm text-center" placeholder="수량" />
                      <input type="number" min="0" value={item.unitPrice} onChange={e => setItemField(idx, 'unitPrice', e.target.value)} className="w-28 border rounded-lg px-2 py-1.5 text-sm text-right" placeholder="단가" />
                      {items.length > 1 && <button type="button" onClick={() => removeItem(idx)} className="text-red-400 hover:text-red-600 text-lg">×</button>}
                    </div>
                  ))}
                </div>
              </div>

              <div className="flex items-center justify-between bg-gray-50 rounded-lg p-3">
                <div className="flex items-center gap-3">
                  <span className="text-sm text-gray-600">할인금액</span>
                  <input type="number" min="0" value={discountAmount} onChange={e => setDiscountAmount(e.target.value)} className="w-28 border rounded-lg px-2 py-1 text-sm text-right" />
                  <span className="text-sm text-gray-500">원</span>
                </div>
                <div className="text-right">
                  <p className="text-xs text-gray-500">소계: {subTotal.toLocaleString()}원</p>
                  <p className="text-lg font-bold text-gray-800">합계: {finalTotal.toLocaleString()}원</p>
                </div>
              </div>

              <div className="flex gap-3">
                <button type="button" onClick={() => setModal(false)} className="flex-1 border rounded-lg py-2 text-sm text-gray-600 hover:bg-gray-50">취소</button>
                <button type="submit" disabled={saving} className="flex-1 bg-blue-600 text-white rounded-lg py-2 text-sm hover:bg-blue-700 disabled:opacity-60">{saving ? '처리 중...' : '매출 등록'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
