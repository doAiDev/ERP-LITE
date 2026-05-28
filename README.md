# 악세사리 ERP 시스템

멀티 매장 악세사리 쇼핑몰 운영을 위한 ERP 웹 시스템

## 기술 스택

| 구분 | 기술 |
|------|------|
| Backend | Spring Boot 2.7.18, Java 11, MyBatis, MSSQL |
| Frontend | React 18, Vite, Tailwind CSS, Axios |
| 인증 | JWT (Access Token + Refresh Token) |

---

## 데이터베이스 테이블 정의

### 1. `stores` — 매장
| 컬럼 | 타입 | 설명 |
|------|------|------|
| store_id | INT PK | 매장 ID |
| name | NVARCHAR(100) | 매장명 |
| address | NVARCHAR(255) | 주소 |
| phone | VARCHAR(20) | 전화번호 |
| opened_at | DATE | 개점일 |
| is_active | BIT | 활성 여부 |
| created_at | DATETIME2 | 등록일시 |

---

### 2. `staff` — 직원
| 컬럼 | 타입 | 설명 |
|------|------|------|
| staff_id | INT PK | 직원 ID |
| store_id | INT FK | 소속 매장 |
| name | NVARCHAR(50) | 이름 |
| phone | VARCHAR(20) | 전화번호 |
| role | VARCHAR(10) | 권한 (admin / manager / staff) |
| is_active | BIT | 활성 여부 |
| created_at | DATETIME2 | 등록일시 |

---

### 3. `auth_users` — 인증 계정
| 컬럼 | 타입 | 설명 |
|------|------|------|
| user_id | INT PK | 계정 ID |
| staff_id | INT FK | 연결된 직원 |
| username | VARCHAR(50) | 로그인 아이디 (UNIQUE) |
| hashed_password | VARCHAR(255) | 암호화된 비밀번호 |
| last_login | DATETIME2 | 마지막 로그인 |
| is_active | BIT | 승인 여부 (0=대기, 1=승인) |

---

### 4. `products` — 상품
| 컬럼 | 타입 | 설명 |
|------|------|------|
| product_id | INT PK | 상품 ID |
| name | NVARCHAR(100) | 상품명 |
| category | NVARCHAR(50) | 카테고리 |
| brand | NVARCHAR(50) | 브랜드 |
| cost_price | DECIMAL(10,2) | 원가 |
| selling_price | DECIMAL(10,2) | 판매가 |
| is_active | BIT | 판매 여부 |
| created_at | DATETIME2 | 등록일시 |
| updated_at | DATETIME2 | 수정일시 |

---

### 5. `product_variants` — 상품 옵션(SKU)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| variant_id | INT PK | 옵션 ID |
| product_id | INT FK | 상품 |
| color | NVARCHAR(30) | 색상 |
| size | NVARCHAR(10) | 사이즈 |
| material | NVARCHAR(50) | 소재 |
| sku | VARCHAR(50) | SKU 코드 (UNIQUE) |
| is_active | BIT | 활성 여부 |

---

### 6. `store_inventory` — 매장별 재고
| 컬럼 | 타입 | 설명 |
|------|------|------|
| inventory_id | INT PK | 재고 ID |
| store_id | INT FK | 매장 |
| variant_id | INT FK | 상품 옵션 |
| quantity | INT | 현재 재고 수량 |
| min_quantity | INT | 최소 재고 수량 (부족 알림 기준) |
| updated_at | DATETIME2 | 수정일시 |

> UNIQUE 제약: (store_id, variant_id)

---

### 7. `inventory_transfers` — 재고 이동 이력
| 컬럼 | 타입 | 설명 |
|------|------|------|
| transfer_id | INT PK | 이동 ID |
| from_store_id | INT FK | 출발 매장 |
| to_store_id | INT FK | 도착 매장 |
| variant_id | INT FK | 상품 옵션 |
| quantity | INT | 이동 수량 |
| transferred_by | INT FK | 처리 직원 |
| transferred_at | DATETIME2 | 이동 일시 |
| notes | NVARCHAR(200) | 메모 |

---

### 8. `customers` — 고객
| 컬럼 | 타입 | 설명 |
|------|------|------|
| customer_id | INT PK | 고객 ID |
| name | NVARCHAR(50) | 이름 |
| phone | VARCHAR(20) | 전화번호 (UNIQUE) |
| email | VARCHAR(100) | 이메일 |
| birth_date | DATE | 생년월일 |
| grade | VARCHAR(10) | 등급 (bronze/silver/gold/vip) |
| total_purchase_amount | DECIMAL(12,2) | 누적 구매금액 |
| visit_count | INT | 방문 횟수 |
| point_balance | INT | 포인트 잔액 |
| created_at | DATETIME2 | 등록일시 |
| created_store_id | INT FK | 최초 등록 매장 |

---

### 9. `point_transactions` — 포인트 거래 내역
| 컬럼 | 타입 | 설명 |
|------|------|------|
| tx_id | INT PK | 거래 ID |
| customer_id | INT FK | 고객 |
| type | VARCHAR(15) | 유형 (earn/use/expire/admin_adjust) |
| amount | INT | 포인트 변동량 |
| balance_after | INT | 거래 후 잔액 |
| description | NVARCHAR(200) | 설명 |
| sale_id | INT | 연결 매출 ID |
| created_at | DATETIME2 | 일시 |

---

### 10. `grade_policies` — 등급 정책
| 컬럼 | 타입 | 설명 |
|------|------|------|
| policy_id | INT PK | 정책 ID |
| grade | VARCHAR(10) | 등급명 |
| min_amount | DECIMAL(12,2) | 해당 등급 최소 누적 구매금액 |
| point_rate | DECIMAL(5,2) | 포인트 적립률 (%) |
| created_at | DATETIME2 | 등록일시 |

---

### 11. `sales` — 매출
| 컬럼 | 타입 | 설명 |
|------|------|------|
| sale_id | INT PK | 매출 ID |
| store_id | INT FK | 매장 |
| staff_id | INT FK | 담당 직원 |
| customer_id | INT FK | 고객 (NULL 가능) |
| sale_date | DATETIME2 | 매출 일시 |
| total_amount | DECIMAL(12,2) | 최종 결제금액 |
| discount_amount | DECIMAL(12,2) | 할인금액 |
| payment_method | VARCHAR(15) | 결제수단 (cash/card/kakao_pay/naver_pay) |
| source | VARCHAR(10) | 입력 경로 (manual/pos) |
| is_refunded | BIT | 환불 여부 |

---

### 12. `sale_items` — 매출 품목
| 컬럼 | 타입 | 설명 |
|------|------|------|
| item_id | INT PK | 품목 ID |
| sale_id | INT FK | 매출 |
| variant_id | INT FK | 상품 옵션 |
| quantity | INT | 수량 |
| unit_price | DECIMAL(10,2) | 판매 단가 |
| discount_rate | DECIMAL(5,2) | 품목 할인율 (%) |

---

### 13. `daily_settlements` — 일별 정산
| 컬럼 | 타입 | 설명 |
|------|------|------|
| settlement_id | INT PK | 정산 ID |
| store_id | INT FK | 매장 |
| settle_date | DATE | 정산일 |
| total_sales | DECIMAL(12,2) | 총 매출액 |
| total_refunds | DECIMAL(12,2) | 총 환불액 |
| net_sales | DECIMAL(12,2) | 순 매출액 |
| cash_amount | DECIMAL(12,2) | 현금 매출 |
| card_amount | DECIMAL(12,2) | 카드 매출 |
| mobile_amount | DECIMAL(12,2) | 모바일 매출 |

> UNIQUE 제약: (store_id, settle_date)

---

### 14. `suppliers` — 공급업체
| 컬럼 | 타입 | 설명 |
|------|------|------|
| supplier_id | INT PK | 업체 ID |
| name | NVARCHAR(100) | 업체명 |
| contact_name | NVARCHAR(50) | 담당자명 |
| phone | VARCHAR(20) | 전화번호 |
| email | VARCHAR(100) | 이메일 |
| address | NVARCHAR(255) | 주소 |
| payment_terms | NVARCHAR(100) | 결제 조건 |
| lead_time_days | INT | 리드타임 (일) |
| is_active | BIT | 활성 여부 |

---

### 15. `purchase_orders` — 발주서
| 컬럼 | 타입 | 설명 |
|------|------|------|
| order_id | INT PK | 발주 ID |
| supplier_id | INT FK | 공급업체 |
| store_id | INT FK | 입고 대상 매장 |
| order_date | DATETIME2 | 발주 일시 |
| expected_date | DATE | 입고 예정일 |
| status | VARCHAR(15) | 상태 (draft→sent→confirmed→partial/completed / cancelled) |
| total_amount | DECIMAL(12,2) | 발주 총액 |
| notes | NVARCHAR(500) | 메모 |
| created_by | INT FK | 발주 담당 직원 |

---

### 16. `purchase_order_items` — 발주 품목
| 컬럼 | 타입 | 설명 |
|------|------|------|
| order_item_id | INT PK | 품목 ID |
| order_id | INT FK | 발주서 |
| variant_id | INT FK | 상품 옵션 |
| ordered_qty | INT | 발주 수량 |
| received_qty | INT | 입고 완료 수량 |
| unit_cost | DECIMAL(10,2) | 발주 단가 |

---

### 17. `goods_receipts` — 입고
| 컬럼 | 타입 | 설명 |
|------|------|------|
| receipt_id | INT PK | 입고 ID |
| order_id | INT FK | 발주서 |
| received_date | DATETIME2 | 입고 일시 |
| received_by | INT FK | 입고 처리 직원 |
| notes | NVARCHAR(500) | 메모 |

---

### 18. `goods_receipt_items` — 입고 품목
| 컬럼 | 타입 | 설명 |
|------|------|------|
| receipt_item_id | INT PK | 입고 품목 ID |
| receipt_id | INT FK | 입고 |
| variant_id | INT FK | 상품 옵션 |
| received_qty | INT | 실제 입고 수량 |
| condition | VARCHAR(10) | 상태 (normal / defective) |

> 입고 완료 시 `store_inventory` 수량 자동 증가

---

### 19. `audit_logs` — 감사 로그
| 컬럼 | 타입 | 설명 |
|------|------|------|
| log_id | BIGINT PK | 로그 ID |
| staff_id | INT FK | 작업 직원 |
| action | VARCHAR(20) | 작업 유형 (CREATE/UPDATE/DELETE 등) |
| table_name | VARCHAR(50) | 대상 테이블 |
| record_id | INT | 대상 레코드 ID |
| old_value | NVARCHAR(MAX) | 변경 전 값 (JSON) |
| new_value | NVARCHAR(MAX) | 변경 후 값 (JSON) |
| created_at | DATETIME2 | 작업 일시 |

---

## API 엔드포인트 요약

| 모듈 | 메서드 | 경로 | 설명 |
|------|--------|------|------|
| 인증 | POST | /api/auth/login | 로그인 |
| 인증 | POST | /api/auth/register | 가입 신청 |
| 매장 | GET | /api/stores | 매장 목록 |
| 상품 | GET/POST | /api/products | 상품 목록/등록 |
| 상품 | GET/PUT | /api/products/{id} | 상품 상세/수정 |
| 재고 | GET | /api/inventory | 재고 목록 |
| 재고 | PATCH | /api/inventory/adjust | 재고 조정 |
| 재고 | POST | /api/inventory/transfer | 재고 이동 |
| 고객 | GET/POST | /api/customers | 고객 목록/등록 |
| 고객 | GET/PUT | /api/customers/{id} | 고객 상세/수정 |
| 매출 | GET/POST | /api/sales | 매출 목록/등록 |
| 매출 | POST | /api/sales/{id}/refund | 환불 |
| 발주 | GET/POST | /api/purchase-orders | 발주 목록/등록 |
| 발주 | POST | /api/purchase-orders/receive | 입고 처리 |
| 관리자 | GET | /api/admin/users/pending | 가입 대기 목록 |
| 관리자 | POST | /api/admin/users/{id}/approve | 가입 승인 |
