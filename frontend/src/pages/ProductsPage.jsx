import { useState, useEffect } from 'react'
import api from '../api/axios'

export default function ProductsPage() {
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(1)
  const [total, setTotal] = useState(0)
  const [category, setCategory] = useState('')
  const size = 10

  useEffect(() => {
    fetchProducts()
  }, [page, category])

  const fetchProducts = async () => {
    setLoading(true)
    try {
      const params = { page, size }
      if (category) params.category = category
      const res = await api.get('/products', { params })
      setProducts(res.data.data.items)
      setTotal(res.data.data.total)
    } catch (err) {
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const categories = ['팔시', '목걸이', '귀걸이', '반지', '기타']
  const totalPages = Math.ceil(total / size)

  return (
    <div className="p-8">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">상품 관리</h2>
          <p className="text-sm text-gray-500 mt-1">전체 {total}개</p>
        </div>
        <button className="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-blue-700 transition">
          + 상품 등록
        </button>
      </div>

      <div className="flex gap-2 mb-4 flex-wrap">
        <button
          onClick={() => { setCategory(''); setPage(1) }}
          className={`px-4 py-1.5 rounded-full text-sm font-medium transition ${
            !category ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border hover:bg-gray-50'
          }`}
        >
          전체
        </button>
        {categories.map(c => (
          <button
            key={c}
            onClick={() => { setCategory(c); setPage(1) }}
            className={`px-4 py-1.5 rounded-full text-sm font-medium transition ${
              category === c ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border hover:bg-gray-50'
            }`}
          >
            {c}
          </button>
        ))}
      </div>

      <div className="bg-white rounded-xl shadow-sm overflow-hidden border border-gray-100">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 text-gray-500 text-xs uppercase tracking-wide">
            <tr>
              <th className="text-left px-6 py-3">상품명</th>
              <th className="text-left px-6 py-3">카테고리</th>
              <th className="text-left px-6 py-3">브랜드</th>
              <th className="text-right px-6 py-3">원가</th>
              <th className="text-right px-6 py-3">판매가</th>
              <th className="text-center px-6 py-3">상태</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {loading ? (
              <tr><td colSpan={6} className="text-center py-12 text-gray-400">불러오는 중...</td></tr>
            ) : products.length === 0 ? (
              <tr><td colSpan={6} className="text-center py-12 text-gray-400">상품이 없습니다</td></tr>
            ) : products.map(p => (
              <tr key={p.productId} className="hover:bg-gray-50 cursor-pointer">
                <td className="px-6 py-4 font-medium text-gray-800">{p.name}</td>
                <td className="px-6 py-4">
                  <span className="bg-gray-100 text-gray-600 px-2 py-0.5 rounded text-xs">{p.category}</span>
                </td>
                <td className="px-6 py-4 text-gray-500">{p.brand || '-'}</td>
                <td className="px-6 py-4 text-right text-gray-500">{p.costPrice?.toLocaleString()}원</td>
                <td className="px-6 py-4 text-right font-semibold text-blue-600">{p.sellingPrice?.toLocaleString()}원</td>
                <td className="px-6 py-4 text-center">
                  <span className={`px-2.5 py-1 rounded-full text-xs font-medium ${
                    p.active ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500'
                  }`}>
                    {p.active ? '판매중' : '비활성'}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="flex justify-center items-center gap-3 mt-6">
          <button
            onClick={() => setPage(p => Math.max(1, p - 1))}
            disabled={page === 1}
            className="px-4 py-2 border rounded-lg text-sm disabled:opacity-40 hover:bg-gray-50"
          >
            이전
          </button>
          <span className="text-sm text-gray-600">{page} / {totalPages}</span>
          <button
            onClick={() => setPage(p => Math.min(totalPages, p + 1))}
            disabled={page === totalPages}
            className="px-4 py-2 border rounded-lg text-sm disabled:opacity-40 hover:bg-gray-50"
          >
            다음
          </button>
        </div>
      )}
    </div>
  )
}
