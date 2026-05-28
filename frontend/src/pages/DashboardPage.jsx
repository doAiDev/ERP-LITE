export default function DashboardPage() {
  const cards = [
    { label: '오늘 매출', value: '-', color: 'text-blue-600', bg: 'bg-blue-50', icon: '💰' },
    { label: '부족 재고', value: '-', color: 'text-red-500', bg: 'bg-red-50', icon: '⚠️' },
    { label: '이번달 고객', value: '-', color: 'text-green-600', bg: 'bg-green-50', icon: '👥' },
  ]

  return (
    <div className="p-8">
      <h2 className="text-2xl font-bold text-gray-800 mb-2">대시보드</h2>
      <p className="text-sm text-gray-500 mb-6">점포 현황을 한눈에 확인하세요</p>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {cards.map(card => (
          <div key={card.label} className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-4">
              <p className="text-sm text-gray-500">{card.label}</p>
              <span className={`text-2xl p-2 rounded-lg ${card.bg}`}>{card.icon}</span>
            </div>
            <p className={`text-3xl font-bold ${card.color}`}>{card.value}</p>
            <p className="text-xs text-gray-400 mt-2">화면 구현 예정</p>
          </div>
        ))}
      </div>
      <div className="mt-8 bg-white rounded-xl shadow-sm p-6 border border-gray-100">
        <h3 className="font-semibold text-gray-700 mb-3">안내</h3>
        <p className="text-sm text-gray-500">좌측 메뉴에서 상품 관리를 클릭하여 상품 목록을 확인하세요.</p>
      </div>
    </div>
  )
}
