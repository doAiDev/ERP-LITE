-- ============================================================
-- 다점포 악세사리 ERP 시스템 - MSSQL DDL 스크립트
-- ============================================================

USE accessory_erp_dev;
GO

-- ============================================================
-- 1. 점포(stores) 테이블
-- ============================================================
IF OBJECT_ID('stores', 'U') IS NULL
CREATE TABLE stores (
    store_id     INT IDENTITY(1,1) PRIMARY KEY,
    store_name   NVARCHAR(100) NOT NULL,              -- 점포명
    address      NVARCHAR(200),                        -- 주소
    phone        NVARCHAR(20),                         -- 전화번호
    manager_name NVARCHAR(50),                         -- 담당 매니저명
    is_active    BIT DEFAULT 1,                        -- 활성화 여부
    created_at   DATETIME2(0) DEFAULT GETDATE(),
    updated_at   DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 2. 직원(staff) 테이블
-- ============================================================
IF OBJECT_ID('staff', 'U') IS NULL
CREATE TABLE staff (
    staff_id   INT IDENTITY(1,1) PRIMARY KEY,
    store_id   INT NOT NULL REFERENCES stores(store_id),  -- 소속 점포
    name       NVARCHAR(50) NOT NULL,                     -- 직원명
    position   NVARCHAR(50),                              -- 직위 (점장/직원 등)
    phone      NVARCHAR(20),                              -- 연락처
    email      NVARCHAR(100),                             -- 이메일
    role       NVARCHAR(20) DEFAULT 'STAFF',              -- 권한 (ADMIN/MANAGER/STAFF)
    hire_date  DATE DEFAULT CAST(GETDATE() AS DATE),      -- 입사일
    is_active  BIT DEFAULT 1,                             -- 재직 여부
    created_at DATETIME2(0) DEFAULT GETDATE(),
    updated_at DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 3. 인증 사용자(auth_users) 테이블
-- ============================================================
IF OBJECT_ID('auth_users', 'U') IS NULL
CREATE TABLE auth_users (
    user_id        INT IDENTITY(1,1) PRIMARY KEY,
    staff_id       INT UNIQUE REFERENCES staff(staff_id),   -- 직원 매핑 (1:1)
    username       NVARCHAR(50) UNIQUE NOT NULL,            -- 로그인 아이디
    password_hash  NVARCHAR(200) NOT NULL,                  -- BCrypt 해시
    is_active      BIT DEFAULT 1,
    last_login_at  DATETIME2(0),                            -- 마지막 로그인 시각
    created_at     DATETIME2(0) DEFAULT GETDATE(),
    updated_at     DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 4. 상품(products) 테이블
-- ============================================================
IF OBJECT_ID('products', 'U') IS NULL
CREATE TABLE products (
    product_id   INT IDENTITY(1,1) PRIMARY KEY,
    product_name NVARCHAR(100) NOT NULL,     -- 상품명
    category     NVARCHAR(50),               -- 카테고리 (팔찌/목걸이/귀걸이/반지/기타)
    brand        NVARCHAR(50),               -- 브랜드
    description  NVARCHAR(500),              -- 상품 설명
    cost_price   DECIMAL(12,2) DEFAULT 0,    -- 원가
    sale_price   DECIMAL(12,2) DEFAULT 0,    -- 판매가
    is_active    BIT DEFAULT 1,
    created_at   DATETIME2(0) DEFAULT GETDATE(),
    updated_at   DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 5. 상품 옵션(product_variants) 테이블
-- ============================================================
IF OBJECT_ID('product_variants', 'U') IS NULL
CREATE TABLE product_variants (
    variant_id   INT IDENTITY(1,1) PRIMARY KEY,
    product_id   INT NOT NULL REFERENCES products(product_id),
    sku          NVARCHAR(50) UNIQUE NOT NULL,   -- 재고 관리 코드
    color        NVARCHAR(50),                   -- 색상
    size_info    NVARCHAR(50),                   -- 사이즈 정보
    extra_price  DECIMAL(10,2) DEFAULT 0,        -- 추가 금액
    is_active    BIT DEFAULT 1,
    created_at   DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 6. 점포별 재고(store_inventory) 테이블
-- ============================================================
IF OBJECT_ID('store_inventory', 'U') IS NULL
CREATE TABLE store_inventory (
    inventory_id    INT IDENTITY(1,1) PRIMARY KEY,
    store_id        INT NOT NULL REFERENCES stores(store_id),
    variant_id      INT NOT NULL REFERENCES product_variants(variant_id),
    quantity        INT DEFAULT 0,              -- 현재 재고 수량
    low_stock_alert INT DEFAULT 10,             -- 재고 부족 알림 기준
    updated_at      DATETIME2(0) DEFAULT GETDATE(),
    CONSTRAINT UQ_store_variant UNIQUE (store_id, variant_id)
);
GO

-- ============================================================
-- 7. 재고 이동(inventory_transfers) 테이블
-- ============================================================
IF OBJECT_ID('inventory_transfers', 'U') IS NULL
CREATE TABLE inventory_transfers (
    transfer_id    INT IDENTITY(1,1) PRIMARY KEY,
    from_store_id  INT NOT NULL REFERENCES stores(store_id),   -- 출발 점포
    to_store_id    INT NOT NULL REFERENCES stores(store_id),   -- 도착 점포
    variant_id     INT NOT NULL REFERENCES product_variants(variant_id),
    quantity       INT NOT NULL,               -- 이동 수량
    transferred_by INT REFERENCES staff(staff_id),  -- 처리 직원
    transferred_at DATETIME2(0) DEFAULT GETDATE(),
    notes          NVARCHAR(200)               -- 비고
);
GO

-- ============================================================
-- 8. 고객(customers) 테이블
-- ============================================================
IF OBJECT_ID('customers', 'U') IS NULL
CREATE TABLE customers (
    customer_id   INT IDENTITY(1,1) PRIMARY KEY,
    name          NVARCHAR(50) NOT NULL,        -- 고객명
    phone         NVARCHAR(20) UNIQUE,          -- 전화번호 (유일)
    email         NVARCHAR(100),                -- 이메일
    birth_date    DATE,                         -- 생년월일 (생일 마케팅)
    gender        NVARCHAR(10),                 -- 성별
    grade         NVARCHAR(20) DEFAULT 'bronze', -- 등급 (bronze/silver/gold/vip)
    point_balance INT DEFAULT 0,                -- 포인트 잔액
    total_purchase DECIMAL(15,2) DEFAULT 0,     -- 누적 구매금액
    memo          NVARCHAR(300),                -- 메모
    is_active     BIT DEFAULT 1,
    joined_at     DATETIME2(0) DEFAULT GETDATE(),
    updated_at    DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 9. 포인트 내역(point_transactions) 테이블
-- ============================================================
IF OBJECT_ID('point_transactions', 'U') IS NULL
CREATE TABLE point_transactions (
    tx_id         INT IDENTITY(1,1) PRIMARY KEY,
    customer_id   INT NOT NULL REFERENCES customers(customer_id),
    amount        INT NOT NULL,                -- 포인트 변동량 (양수: 적립, 음수: 사용)
    balance_after INT NOT NULL,               -- 변동 후 잔액
    tx_type       NVARCHAR(20) NOT NULL,      -- 유형 (EARN/USE/EXPIRE/MANUAL)
    description   NVARCHAR(200),              -- 설명
    sale_id       INT,                        -- 관련 판매 ID (nullable)
    created_by    INT REFERENCES staff(staff_id),
    created_at    DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 10. 등급 정책(grade_policies) 테이블
-- ============================================================
IF OBJECT_ID('grade_policies', 'U') IS NULL
CREATE TABLE grade_policies (
    policy_id        INT IDENTITY(1,1) PRIMARY KEY,
    grade_name       NVARCHAR(20) UNIQUE NOT NULL,  -- bronze/silver/gold/vip
    min_purchase     DECIMAL(15,2) DEFAULT 0,       -- 최소 누적 구매금액
    point_rate       DECIMAL(5,2) DEFAULT 1.0,      -- 포인트 적립률 (%)
    discount_rate    DECIMAL(5,2) DEFAULT 0,        -- 할인율 (%)
    description      NVARCHAR(200),
    updated_at       DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 11. 판매(sales) 테이블
-- ============================================================
IF OBJECT_ID('sales', 'U') IS NULL
CREATE TABLE sales (
    sale_id        INT IDENTITY(1,1) PRIMARY KEY,
    store_id       INT NOT NULL REFERENCES stores(store_id),
    staff_id       INT REFERENCES staff(staff_id),
    customer_id    INT REFERENCES customers(customer_id),  -- nullable (비회원 가능)
    total_amount   DECIMAL(12,2) DEFAULT 0,   -- 합계 금액
    discount_amount DECIMAL(12,2) DEFAULT 0,  -- 할인 금액
    point_used     INT DEFAULT 0,             -- 사용 포인트
    point_earned   INT DEFAULT 0,             -- 적립 포인트
    final_amount   DECIMAL(12,2) DEFAULT 0,   -- 최종 결제금액
    payment_method NVARCHAR(20),              -- 결제수단 (card/cash/transfer)
    source         NVARCHAR(20) DEFAULT 'manual', -- 입력 경로 (manual/pos)
    status         NVARCHAR(20) DEFAULT 'COMPLETED', -- 상태 (COMPLETED/REFUNDED/PARTIAL_REFUND)
    notes          NVARCHAR(300),
    sold_at        DATETIME2(0) DEFAULT GETDATE(),
    created_at     DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 12. 판매 항목(sale_items) 테이블
-- ============================================================
IF OBJECT_ID('sale_items', 'U') IS NULL
CREATE TABLE sale_items (
    item_id       INT IDENTITY(1,1) PRIMARY KEY,
    sale_id       INT NOT NULL REFERENCES sales(sale_id),
    variant_id    INT NOT NULL REFERENCES product_variants(variant_id),
    quantity      INT NOT NULL,
    unit_price    DECIMAL(12,2) NOT NULL,     -- 판매 단가
    discount      DECIMAL(12,2) DEFAULT 0,    -- 항목 할인
    subtotal      DECIMAL(12,2) NOT NULL,     -- 소계
    is_refunded   BIT DEFAULT 0              -- 환불 여부
);
GO

-- ============================================================
-- 13. 일일 정산(daily_settlements) 테이블
-- ============================================================
IF OBJECT_ID('daily_settlements', 'U') IS NULL
CREATE TABLE daily_settlements (
    settlement_id     INT IDENTITY(1,1) PRIMARY KEY,
    store_id          INT NOT NULL REFERENCES stores(store_id),
    settlement_date   DATE NOT NULL,          -- 정산일
    total_sales_count INT DEFAULT 0,          -- 판매 건수
    total_amount      DECIMAL(15,2) DEFAULT 0, -- 총 매출
    cash_amount       DECIMAL(15,2) DEFAULT 0, -- 현금 매출
    card_amount       DECIMAL(15,2) DEFAULT 0, -- 카드 매출
    transfer_amount   DECIMAL(15,2) DEFAULT 0, -- 계좌이체 매출
    refund_amount     DECIMAL(15,2) DEFAULT 0, -- 환불 금액
    net_amount        DECIMAL(15,2) DEFAULT 0, -- 순매출 (total - refund)
    created_at        DATETIME2(0) DEFAULT GETDATE(),
    CONSTRAINT UQ_settlement UNIQUE (store_id, settlement_date)
);
GO

-- ============================================================
-- 14. 공급업체(suppliers) 테이블
-- ============================================================
IF OBJECT_ID('suppliers', 'U') IS NULL
CREATE TABLE suppliers (
    supplier_id   INT IDENTITY(1,1) PRIMARY KEY,
    supplier_name NVARCHAR(100) NOT NULL,     -- 업체명
    contact_name  NVARCHAR(50),               -- 담당자명
    phone         NVARCHAR(20),               -- 전화번호
    email         NVARCHAR(100),              -- 이메일
    address       NVARCHAR(200),              -- 주소
    notes         NVARCHAR(300),              -- 비고
    is_active     BIT DEFAULT 1,
    created_at    DATETIME2(0) DEFAULT GETDATE(),
    updated_at    DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 15. 발주(purchase_orders) 테이블
-- ============================================================
IF OBJECT_ID('purchase_orders', 'U') IS NULL
CREATE TABLE purchase_orders (
    order_id      INT IDENTITY(1,1) PRIMARY KEY,
    supplier_id   INT NOT NULL REFERENCES suppliers(supplier_id),
    store_id      INT NOT NULL REFERENCES stores(store_id),    -- 입고 대상 점포
    ordered_by    INT REFERENCES staff(staff_id),              -- 발주 담당자
    order_date    DATETIME2(0) DEFAULT GETDATE(),              -- 발주일시
    expected_date DATE,                                        -- 입고 예정일
    status        NVARCHAR(20) DEFAULT 'PENDING',              -- 상태 (PENDING/PARTIAL/RECEIVED/CANCELLED)
    total_amount  DECIMAL(15,2) DEFAULT 0,                     -- 발주 금액
    notes         NVARCHAR(300),
    created_at    DATETIME2(0) DEFAULT GETDATE(),
    updated_at    DATETIME2(0) DEFAULT GETDATE()
);
GO

-- ============================================================
-- 16. 발주 항목(purchase_order_items) 테이블
-- ============================================================
IF OBJECT_ID('purchase_order_items', 'U') IS NULL
CREATE TABLE purchase_order_items (
    order_item_id  INT IDENTITY(1,1) PRIMARY KEY,
    order_id       INT NOT NULL REFERENCES purchase_orders(order_id),
    variant_id     INT NOT NULL REFERENCES product_variants(variant_id),
    ordered_qty    INT NOT NULL,                -- 발주 수량
    received_qty   INT DEFAULT 0,               -- 입고 완료 수량
    unit_cost      DECIMAL(12,2) NOT NULL,       -- 발주 단가
    subtotal       DECIMAL(12,2) NOT NULL        -- 소계
);
GO

-- ============================================================
-- 17. 입고(goods_receipts) 테이블
-- ============================================================
IF OBJECT_ID('goods_receipts', 'U') IS NULL
CREATE TABLE goods_receipts (
    receipt_id    INT IDENTITY(1,1) PRIMARY KEY,
    order_id      INT NOT NULL REFERENCES purchase_orders(order_id),
    received_by   INT REFERENCES staff(staff_id),
    received_at   DATETIME2(0) DEFAULT GETDATE(),
    notes         NVARCHAR(300)
);
GO

-- ============================================================
-- 18. 입고 항목(goods_receipt_items) 테이블
-- ============================================================
IF OBJECT_ID('goods_receipt_items', 'U') IS NULL
CREATE TABLE goods_receipt_items (
    receipt_item_id INT IDENTITY(1,1) PRIMARY KEY,
    receipt_id      INT NOT NULL REFERENCES goods_receipts(receipt_id),
    order_item_id   INT NOT NULL REFERENCES purchase_order_items(order_item_id),
    variant_id      INT NOT NULL REFERENCES product_variants(variant_id),
    received_qty    INT NOT NULL,              -- 실제 입고 수량
    condition       NVARCHAR(20) DEFAULT 'GOOD', -- 상태 (GOOD/DAMAGED/MISSING)
    notes           NVARCHAR(200)
);
GO

-- ============================================================
-- 19. 감사 로그(audit_logs) 테이블
-- ============================================================
IF OBJECT_ID('audit_logs', 'U') IS NULL
CREATE TABLE audit_logs (
    log_id       INT IDENTITY(1,1) PRIMARY KEY,
    user_id      INT REFERENCES auth_users(user_id),
    staff_id     INT REFERENCES staff(staff_id),
    action       NVARCHAR(20) NOT NULL,       -- 행위 (CREATE/UPDATE/DELETE)
    target_table NVARCHAR(50),               -- 대상 테이블
    target_id    NVARCHAR(50),               -- 대상 레코드 ID
    old_value    NVARCHAR(MAX),              -- 변경 전 값 (JSON)
    new_value    NVARCHAR(MAX),              -- 변경 후 값 (JSON)
    ip_address   NVARCHAR(50),              -- 클라이언트 IP
    user_agent   NVARCHAR(500),             -- 브라우저 정보
    created_at   DATETIME2(0) DEFAULT GETDATE()
);
GO

-- 인덱스 생성
CREATE INDEX IX_sales_store_date ON sales(store_id, sold_at);
CREATE INDEX IX_sales_customer ON sales(customer_id);
CREATE INDEX IX_inventory_store ON store_inventory(store_id);
CREATE INDEX IX_customers_phone ON customers(phone);
CREATE INDEX IX_customers_grade ON customers(grade);
CREATE INDEX IX_audit_created ON audit_logs(created_at);
CREATE INDEX IX_transfers_store ON inventory_transfers(from_store_id, to_store_id);
GO
