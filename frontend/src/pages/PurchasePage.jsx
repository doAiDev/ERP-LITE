import { useEffect, useState } from 'react'
import api from '../api/axios'

const STATUS_LABEL = { draft: '대기', sent: '발주완료', confirmed: '확인', partial: '부분입고', completed: '완료', cancelled: '취소' }
const STATUS_STYLE = { draft: 'bg-gray-100 text-gray-600', sent: 'bg-blue-100 text-blue-700', confirmed: 'bg-indigo-100 text-indigo-700', partial: 'bg-amber-100 text-amber-700', completed: 'bg-green-100 text-green-700', cancelled: 'bg-red-100 text-red-600' }
const STATUSES = ['', 'draft', 'sent', 'confirmed', 'partial', 'completed', 'cancelled']
const STATUS_NEXT = { draft: ['sent', 'cancelled'], sent: ['confirmed', 'cancelled'], confirmed: ['cancelled'] }

export default function PurchasePage() {
  const [orders, setOrders] = useState([])
  const [total, setTotal] = useState(0)
  const [page, setPage] = useState(1)
  const [status, setStatus] = useState('')
  const [stores, setStores] = useState([])
  const [suppliers, setSuppliers] = useState([])
  const [products, setProducts] = useState([])
  const [variantMap, setVariantMap] = useState({})
  const [loading, setLoading] = useState(false)

  const [createModal, setCreateModal] = useState(false)
  const [receiveModal, setReceiveModal] = useState(null)
  const [orderDetail, setOrderDetail] = useState(null)
  const [form, setForm] = useState({ supplierId: '', storeId: '', expectedDate: '', notes: '' })
  const [orderItems, setOrderItems] = useState([{ variantId: '', productId: '', orderedQty: 1, unitCost: '' }])
  const [receiveItems, setReceiveItems] = useState([])
  const [receiveNotes, setReceiveNotes] = useState('')
  const [saving, setSaving] = useState(false)
  const SIZE = 20

  useEffect(() => {
    api.get('/api/stores').then(r => { const s = r.data.data||[]; setStores(s); if(s[0]) setForm(f=>({...f,storeId:String(s[0].storeId)})) })
    api.get('/api/purchase-orders/suppliers').then(r => setSuppliers(r.data.data||[]))
    api.get('/api/products', { params: { size: 200, active: true } }).then(r => setProducts(r.data.data?.items||[]))
  }, [])
  useEffect(() => { fetchOrders() }, [page, status])

  const fetchOrders = async () => {
    setLoading(true)
    try {
      const r = await api.get('/api/purchase-orders', { params: { status: status||undefined, page, size: SIZE } })
      setOrders(r.data.data?.items||[]); setTotal(r.data.data?.total||0)
    } finally { setLoading(false) }
  }

  const loadVariants = async (productId, idx, listSetter) => {
    if (!productId) return
    if (!variantMap[productId]) {
      const r = await api.get(`/api/products/${productId}`)
      const p = r.data.data
      setVariantMap(m => ({ ...m, [productId]: { variants: p.variants||[], costPrice: p.costPrice } }))
    }
    listSetter(prev => {
      const updated = [...prev]
      updated[idx] = { ...updated[idx], productId, variantId: '', unitCost: variantMap[productId]?.costPrice||'' }
      return updated
    })
  }

  const openReceive = async (order) => {
    const r = await api.get(`/api/purchase-orders/${order.orderId}`)
    const det = r.data.data
    setOrderDetail(det)
    setReceiveItems((det.items||[]).map(it => ({ variantId: it.variantId, productName: it.productName, sku: it.sku, orderedQty: it.orderedQty, receivedQty: it.receivedQty, inputQty: 0, condition: 'normal' })))
    setReceiveNotes('')
    setReceiveModal(order)
  }

  const handleCreate = async (e) => {
    e.preventDefault()
    const valid = orderItems.filter(it => it.variantId && it.orderedQty > 0 && it.unitCost)
    if (valid.length === 0) return alert('상품을 최소 1개 입력해주세요.')
    setSaving(true)
    try {
      await api.post('/api/purchase-orders', {
        supplierId: Number(form.supplierId), storeId: Number(form.storeId),
        expectedDate: form.expectedDate||undefined, notes: form.notes||undefined,
        items: valid.map(it => ({ variantId: Number(it.variantId), orderedQty: Number(it.orderedQty), unitCost: Number(it.unitCost) }))
      })
      setCreateModal(false); fetchOrders()
    } catch(e) { alert(e.response?.data?.message||'오류') }
    finally { setSaving(false) }
  }

  const handleReceive = async (e) => {
    e.preventDefault()
    const valid = receiveItems.filter(it => it.inputQty > 0)
    if (valid.length === 0) return alert('입고 수량을 입력해주세요.')
    setSaving(true)
    try {
      await api.post('/api/purchase-orders/receive', {
        orderId: receiveModal.orderId, notes: receiveNotes||undefined,
        items: valid.map(it => ({ variantId: it.variantId, receivedQty: Number(it.inputQty), condition: it.condition }))
      })
      setReceiveModal(null); fetchOrders()
    } catch(e) { alert(e.response?.data?.message||'오류') }
    finally { setSaving(false) }
  }

  const changeStatus = async (orderId, newStatus) => {
    try { await api.patch(`/api/purchase-orders/${orderId}/status`, null, { params: { status: newStatus } }); fetchOrders() }
    catch(e) { alert(e.response?.data?.message||'오류') }
  }

  const totalPages = Math.ceil(total / SIZE)
  const tabLabels = { '': '전체', draft: '대기', sent: '발주완료', confirmed: '확인', partial: '부분입고', completed: '완료', cancelled: '취소' }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-4">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">발주 / 입고</h2>
          <p className="text-gray-500 text-sm mt-1">공급업체 발주 및 입고 처리</p>
        </div>
        <button onClick={() => { setOrderItems([{variantId:'',productId:'',orderedQty:1,unitCost:''}]); setCreateModal(true) }}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm hover:bg-blue-700">+ 발주 등록</button>
      </div>

      <div className="flex gap-1 mb-4 border-b">
        {['','draft','sent','confirmed','partial','completed','cancelled'].map(s => (
          <button key={s} onClick={() => { setStatus(s); setPage(1) }}
            className={`px-4 py-2 text-sm font-medium border-b-2 transition ${status===s ? 'border-blue-600 text-blue-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
            {tabLabels[s]}
          </button>
        ))}
      </div>

      <div className="bg-white rounded-xl shadow overflow-x-auto">
        <table className="min-w-full text-sm">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="px-4 py-3 text-left font-medium text-gray-600">발주일</th>
              <th className="px-4 py-3 text-left font-medium text-gray-600">공급업체</th>
              <th className="px-4 py-3 text-left font-medium text-gray-600">매장</th>
              <th className="px-4 py-3 text-right font-medium text-gray-600">금액</th>
              <th className="px-4 py-3 text-center font-medium text-gray-600">상태</th>
              <th className="px-4 py-3 text-center font-medium text-gray-600">작업</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {loading ? <tr><td colSpan={6} className="text-center py-10 text-gray-400">불러오는 중...</td></tr>
            : orders.length === 0 ? <tr><td colSpan={6} className="text-center py-10 text-gray-400">데이터가 없습니다.</td></tr>
            : orders.map(o => (
              <tr key={o.orderId} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-gray-500 text-xs">{o.orderDate?.slice(0,10)}</td>
                <td className="px-4 py-3 text-gray-700">{o.supplierName}</td>
                <td className="px-4 py-3 text-gray-600">{o.storeName}</td>
                <td className="px-4 py-3 text-right font-semibold">{Number(o.totalAmount||0).toLocaleString()}원</td>
                <td className="px-4 py-3 text-center">
                  <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${STATUS_STYLE[o.status]||''}`}>{STATUS_LABEL[o.status]||o.status}</span>
                </td>
                <td className="px-4 py-3">
                  <div className="flex justify-center gap-2">
                    {(STATUS_NEXT[o.status]||[]).map(ns => (
                      <button key={ns} onClick={() => changeStatus(o.orderId, ns)}
                        className="text-xs bg-gray-100 text-gray-700 hover:bg-gray-200 px-2 py-1 rounded">{STATUS_LABEL[ns]}</button>
                    ))}
                    {['sent','confirmed','partial'].includes(o.status) && (
                      <button onClick={() => openReceive(o)} className="text-xs bg-amber-500 text-white hover:bg-amber-600 px-2 py-1 rounded">입고</button>
                    )}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-4">
          <button onClick={() => setPage(p=>Math.max(1,p-1))} disabled={page===1} className="px-3 py-1 text-sm border rounded disabled:opacity-40">이전</button>
          <span className="px-3 py-1 text-sm text-gray-600">{page} / {totalPages}</span>
          <button onClick={() => setPage(p=>Math.min(totalPages,p+1))} disabled={page===totalPages} className="px-3 py-1 text-sm border rounded disabled:opacity-40">다음</button>
        </div>
      )}

      {/* 발주 등록 모달 */}
      {createModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 overflow-y-auto py-6">
          <div className="bg-white rounded-xl shadow-lg w-full max-w-2xl mx-4 p-6">
            <h3 className="text-lg font-bold mb-4">발주 등록</h3>
            <form onSubmit={handleCreate} className="space-y-4">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">공급업체 *</label>
                  <select value={form.supplierId} onChange={e=>setForm(f=>({...f,supplierId:e.target.value}))} required className="w-full border rounded-lg px-3 py-2 text-sm">
                    <option value="">선택</option>
                    {suppliers.map(s=><option key={s.supplierId} value={s.supplierId}>{s.supplierName}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">매장 *</label>
                  <select value={form.storeId} onChange={e=>setForm(f=>({...f,storeId:e.target.value}))} required className="w-full border rounded-lg px-3 py-2 text-sm">
                    <option value="">선택</option>
                    {stores.map(s=><option key={s.storeId} value={s.storeId}>{s.name}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">입고 예정일</label>
                  <input type="date" value={form.expectedDate} onChange={e=>setForm(f=>({...f,expectedDate:e.target.value}))} className="w-full border rounded-lg px-3 py-2 text-sm" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">메모</label>
                  <input value={form.notes} onChange={e=>setForm(f=>({...f,notes:e.target.value}))} className="w-full border rounded-lg px-3 py-2 text-sm" />
                </div>
              </div>
              <div>
                <div className="flex items-center justify-between mb-2">
                  <label className="text-xs font-medium text-gray-700">발주 품목 *</label>
                  <button type="button" onClick={()=>setOrderItems(i=>[...i,{variantId:'',productId:'',orderedQty:1,unitCost:''}])} className="text-xs text-blue-600 hover:underline">+ 추가</button>
                </div>
                {orderItems.map((item,idx)=>(
                  <div key={idx} className="flex gap-2 items-center mb-2">
                    <select value={item.productId} onChange={e=>loadVariants(e.target.value,idx,setOrderItems)} className="flex-1 border rounded-lg px-2 py-1.5 text-sm">
                      <option value="">상품 선택</option>
                      {products.map(p=><option key={p.productId} value={p.productId}>{p.name}</option>)}
                    </select>
                    <select value={item.variantId} onChange={e=>{const u=[...orderItems];u[idx]={...u[idx],variantId:e.target.value};setOrderItems(u)}} className="flex-1 border rounded-lg px-2 py-1.5 text-sm">
                      <option value="">옵션</option>
                      {(variantMap[item.productId]?.variants||[]).map(v=><option key={v.variantId} value={v.variantId}>{v.sku}</option>)}
                    </select>
                    <input type="number" min="1" value={item.orderedQty} onChange={e=>{const u=[...orderItems];u[idx]={...u[idx],orderedQty:e.target.value};setOrderItems(u)}} className="w-16 border rounded-lg px-2 py-1.5 text-sm text-center" placeholder="수량" />
                    <input type="number" min="0" value={item.unitCost} onChange={e=>{const u=[...orderItems];u[idx]={...u[idx],unitCost:e.target.value};setOrderItems(u)}} className="w-28 border rounded-lg px-2 py-1.5 text-sm text-right" placeholder="단가" />
                    {orderItems.length>1&&<button type="button" onClick={()=>setOrderItems(i=>i.filter((_,j)=>j!==idx))} className="text-red-400 text-lg">×</button>}
                  </div>
                ))}
              </div>
              <div className="flex gap-3">
                <button type="button" onClick={()=>setCreateModal(false)} className="flex-1 border rounded-lg py-2 text-sm text-gray-600 hover:bg-gray-50">취소</button>
                <button type="submit" disabled={saving} className="flex-1 bg-blue-600 text-white rounded-lg py-2 text-sm hover:bg-blue-700 disabled:opacity-60">{saving?'저장 중...':'발주 등록'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* 입고 모달 */}
      {receiveModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 overflow-y-auto py-6">
          <div className="bg-white rounded-xl shadow-lg w-full max-w-xl mx-4 p-6">
            <h3 className="text-lg font-bold mb-1">입고 등록</h3>
            <p className="text-sm text-gray-500 mb-4">{receiveModal.supplierName} · {receiveModal.storeName}</p>
            <form onSubmit={handleReceive} className="space-y-4">
              <div className="space-y-2">
                {receiveItems.map((item,idx)=>(
                  <div key={idx} className="flex items-center gap-3 border rounded-lg px-3 py-2">
                    <div className="flex-1">
                      <p className="text-sm font-medium text-gray-800">{item.productName}</p>
                      <p className="text-xs text-gray-400">{item.sku} · 발주 {item.orderedQty} / 기입고 {item.receivedQty}</p>
                    </div>
                    <input type="number" min="0" max={item.orderedQty-item.receivedQty} value={item.inputQty}
                      onChange={e=>{const u=[...receiveItems];u[idx]={...u[idx],inputQty:e.target.value};setReceiveItems(u)}}
                      className="w-20 border rounded-lg px-2 py-1 text-sm text-center" placeholder="입고수량" />
                    <select value={item.condition} onChange={e=>{const u=[...receiveItems];u[idx]={...u[idx],condition:e.target.value};setReceiveItems(u)}} className="border rounded-lg px-2 py-1 text-sm">
                      <option value="normal">정상</option>
                      <option value="defective">불량</option>
                    </select>
                  </div>
                ))}
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">메모</label>
                <input value={receiveNotes} onChange={e=>setReceiveNotes(e.target.value)} className="w-full border rounded-lg px-3 py-2 text-sm" />
              </div>
              <div className="flex gap-3">
                <button type="button" onClick={()=>setReceiveModal(null)} className="flex-1 border rounded-lg py-2 text-sm text-gray-600 hover:bg-gray-50">취소</button>
                <button type="submit" disabled={saving} className="flex-1 bg-amber-500 text-white rounded-lg py-2 text-sm hover:bg-amber-600 disabled:opacity-60">{saving?'처리 중...':'입고 완료'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
