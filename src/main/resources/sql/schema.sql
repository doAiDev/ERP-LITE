-- 악세사리 ERP 데이터베이스 스키마 (MSSQL)

CREATE TABLE stores (
    store_id   INT IDENTITY(1,1) PRIMARY KEY,
    name       NVARCHAR(100) NOT NULL,
    address    NVARCHAR(255),
    phone      VARCHAR(20),
    opened_at  DATE,
    is_active  BIT DEFAULT 1,
    created_at DATETIME2(0) DEFAULT GETDATE()
);

CREATE TABLE staff (
    staff_id   INT IDENTITY(1,1) PRIMARY KEY,
    store_id   INT NOT NULL REFERENCES stores(store_id),
    name       NVARCHAR(50) NOT NULL,
    phone      VARCHAR(20),
    role       VARCHAR(10) NOT NULL CHECK (role IN ('admin','manager','staff')),
    is_active  BIT DEFAULT 1,
    created_at DATETIME2(0) DEFAULT GETDATE()
);

CREATE TABLE auth_users (
    user_id         INT IDENTITY(1,1) PRIMARY KEY,
    staff_id        INT NOT NULL REFERENCES staff(staff_id),
    username        VARCHAR(50) NOT NULL UNIQUE,
    hashed_password VARCHAR(255) NOT NULL,
    last_login      DATETIME2(0),
    is_active       BIT DEFAULT 1
);

CREATE TABLE products (
    product_id    INT IDENTITY(1,1) PRIMARY KEY,
    name          NVARCHAR(100) NOT NULL,
    category      NVARCHAR(50),
    brand         NVARCHAR(50),
    cost_price    DECIMAL(10,2) NOT NULL DEFAULT 0,
    selling_price DECIMAL(10,2) NOT NULL DEFAULT 0,
    is_active     BIT DEFAULT 1,
    created_at    DATETIME2(0) DEFAULT GETDATE(),
    updated_at    DATETIME2(0) DEFAULT GETDATE()
);

CREATE TABLE product_variants (
    variant_id INT IDENTITY(1,1) PRIMARY KEY,
    product_id INT NOT NULL REFERENCES products(product_id),
    color      NVARCHAR(30),
    size       NVARCHAR(10),
    material   NVARCHAR(50),
    sku        VARCHAR(50) UNIQUE,
    is_active  BIT DEFAULT 1
);

CREATE TABLE store_inventory (
    inventory_id INT IDENTITY(1,1) PRIMARY KEY,
    store_id     INT NOT NULL REFERENCES stores(store_id),
    variant_id   INT NOT NULL REFERENCES product_variants(variant_id),
    quantity     INT NOT NULL DEFAULT 0,
    min_quantity INT NOT NULL DEFAULT 0,
    updated_at   DATETIME2(0) DEFAULT GETDATE(),
    CONSTRAINT uq_store_variant UNIQUE (store_id, variant_id)
);

CREATE TABLE inventory_transfers (
    transfer_id    INT IDENTITY(1,1) PRIMARY KEY,
    from_store_id  INT NOT NULL REFERENCES stores(store_id),
    to_store_id    INT NOT NULL REFERENCES stores(store_id),
    variant_id     INT NOT NULL REFERENCES product_variants(variant_id),
    quantity       INT NOT NULL,
    transferred_by INT REFERENCES staff(staff_id),
    transferred_at DATETIME2(0) DEFAULT GETDATE(),
    notes          NVARCHAR(200)
);

CREATE TABLE customers (
    customer_id           INT IDENTITY(1,1) PRIMARY KEY,
    name                  NVARCHAR(50) NOT NULL,
    phone                 VARCHAR(20) NOT NULL UNIQUE,
    email                 VARCHAR(100),
    birth_date            DATE,
    grade                 VARCHAR(10) DEFAULT 'bronze' CHECK (grade IN ('bronze','silver','gold','vip')),
    total_purchase_amount DECIMAL(12,2) DEFAULT 0,
    visit_count           INT DEFAULT 0,
    point_balance         INT DEFAULT 0,
    created_at            DATETIME2(0) DEFAULT GETDATE(),
    created_store_id      INT REFERENCES stores(store_id)
);

CREATE TABLE point_transactions (
    tx_id         INT IDENTITY(1,1) PRIMARY KEY,
    customer_id   INT NOT NULL REFERENCES customers(customer_id),
    type          VARCHAR(15) NOT NULL CHECK (type IN ('earn','use','expire','admin_adjust')),
    amount        INT NOT NULL,
    balance_after INT NOT NULL,
    description   NVARCHAR(200),
    sale_id       INT,
    created_at    DATETIME2(0) DEFAULT GETDATE()
);

CREATE TABLE grade_policies (
    policy_id  INT IDENTITY(1,1) PRIMARY KEY,
    grade      VARCHAR(10) NOT NULL UNIQUE CHECK (grade IN ('bronze','silver','gold','vip')),
    min_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    point_rate DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    created_at DATETIME2(0) DEFAULT GETDATE()
);

CREATE TABLE sales (
    sale_id         INT IDENTITY(1,1) PRIMARY KEY,
    store_id        INT NOT NULL REFERENCES stores(store_id),
    staff_id        INT REFERENCES staff(staff_id),
    customer_id     INT REFERENCES customers(customer_id),
    sale_date       DATETIME2(0) DEFAULT GETDATE(),
    total_amount    DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) DEFAULT 0,
    payment_method  VARCHAR(15) NOT NULL CHECK (payment_method IN ('cash','card','kakao_pay','naver_pay')),
    source          VARCHAR(10) DEFAULT 'manual' CHECK (source IN ('manual','pos')),
    is_refunded     BIT DEFAULT 0
);

CREATE TABLE sale_items (
    item_id       INT IDENTITY(1,1) PRIMARY KEY,
    sale_id       INT NOT NULL REFERENCES sales(sale_id),
    variant_id    INT NOT NULL REFERENCES product_variants(variant_id),
    quantity      INT NOT NULL,
    unit_price    DECIMAL(10,2) NOT NULL,
    discount_rate DECIMAL(5,2) DEFAULT 0
);

CREATE TABLE daily_settlements (
    settlement_id INT IDENTITY(1,1) PRIMARY KEY,
    store_id      INT NOT NULL REFERENCES stores(store_id),
    settle_date   DATE NOT NULL,
    total_sales   DECIMAL(12,2) DEFAULT 0,
    total_refunds DECIMAL(12,2) DEFAULT 0,
    net_sales     DECIMAL(12,2) DEFAULT 0,
    cash_amount   DECIMAL(12,2) DEFAULT 0,
    card_amount   DECIMAL(12,2) DEFAULT 0,
    mobile_amount DECIMAL(12,2) DEFAULT 0,
    CONSTRAINT uq_store_date UNIQUE (store_id, settle_date)
);

CREATE TABLE suppliers (
    supplier_id    INT IDENTITY(1,1) PRIMARY KEY,
    name           NVARCHAR(100) NOT NULL,
    contact_name   NVARCHAR(50),
    phone          VARCHAR(20),
    email          VARCHAR(100),
    address        NVARCHAR(255),
    payment_terms  NVARCHAR(100),
    lead_time_days INT DEFAULT 7,
    is_active      BIT DEFAULT 1
);

CREATE TABLE purchase_orders (
    order_id      INT IDENTITY(1,1) PRIMARY KEY,
    supplier_id   INT NOT NULL REFERENCES suppliers(supplier_id),
    store_id      INT NOT NULL REFERENCES stores(store_id),
    order_date    DATETIME2(0) DEFAULT GETDATE(),
    expected_date DATE,
    status        VARCHAR(15) NOT NULL DEFAULT 'draft'
                  CHECK (status IN ('draft','sent','confirmed','partial','completed','cancelled')),
    total_amount  DECIMAL(12,2) DEFAULT 0,
    notes         NVARCHAR(500),
    created_by    INT REFERENCES staff(staff_id)
);

CREATE TABLE purchase_order_items (
    order_item_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id      INT NOT NULL REFERENCES purchase_orders(order_id),
    variant_id    INT NOT NULL REFERENCES product_variants(variant_id),
    ordered_qty   INT NOT NULL,
    received_qty  INT DEFAULT 0,
    unit_cost     DECIMAL(10,2) NOT NULL
);

CREATE TABLE goods_receipts (
    receipt_id    INT IDENTITY(1,1) PRIMARY KEY,
    order_id      INT NOT NULL REFERENCES purchase_orders(order_id),
    received_date DATETIME2(0) DEFAULT GETDATE(),
    received_by   INT REFERENCES staff(staff_id),
    notes         NVARCHAR(500)
);

CREATE TABLE goods_receipt_items (
    receipt_item_id INT IDENTITY(1,1) PRIMARY KEY,
    receipt_id      INT NOT NULL REFERENCES goods_receipts(receipt_id),
    variant_id      INT NOT NULL REFERENCES product_variants(variant_id),
    received_qty    INT NOT NULL,
    condition       VARCHAR(10) NOT NULL DEFAULT 'normal' CHECK (condition IN ('normal','defective'))
);

CREATE TABLE audit_logs (
    log_id     BIGINT IDENTITY(1,1) PRIMARY KEY,
    staff_id   INT REFERENCES staff(staff_id),
    action     VARCHAR(20) NOT NULL,
    table_name VARCHAR(50) NOT NULL,
    record_id  INT,
    old_value  NVARCHAR(MAX),
    new_value  NVARCHAR(MAX),
    created_at DATETIME2(0) DEFAULT GETDATE()
);
