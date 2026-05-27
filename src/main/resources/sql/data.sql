-- ============================================================
-- 다점포 악세사리 ERP 시스템 - 시드 데이터
-- ============================================================

USE accessory_erp_dev;
GO

-- ============================================================
-- 1. 등급 정책 (grade_policies)
-- ============================================================
INSERT INTO grade_policies (grade_name, min_purchase, point_rate, discount_rate, description) VALUES
('bronze', 0,       1.0,  0,   N'기본 등급 - 구매금액 1% 적립'),
('silver', 300000,  1.5,  3,   N'실버 등급 - 누적 30만원 이상, 1.5% 적립, 3% 할인'),
('gold',   700000,  2.0,  5,   N'골드 등급 - 누적 70만원 이상, 2% 적립, 5% 할인'),
('vip',    1500000, 3.0,  10,  N'VIP 등급 - 누적 150만원 이상, 3% 적립, 10% 할인');
GO

-- ============================================================
-- 2. 점포 (stores)
-- ============================================================
INSERT INTO stores (store_name, address, phone, manager_name) VALUES
(N'강남점', N'서울 강남구 테헤란로 123', '02-1234-5678', N'김점장'),
(N'홍대점', N'서울 마포구 홍대입구로 45', '02-2345-6789', N'이점장'),
(N'잠실점', N'서울 송파구 잠실대로 200', '02-3456-7890', N'박점장');
GO

-- ============================================================
-- 3. 직원 (staff) - 10명
-- ============================================================
INSERT INTO staff (store_id, name, position, phone, email, role) VALUES
(1, N'김관리자', N'시스템관리자', '010-1111-0001', 'admin@accessory.com', 'ADMIN'),
(1, N'이강남', N'점장', '010-1111-0002', 'gangnam.mgr@accessory.com', 'MANAGER'),
(1, N'박강남', N'직원', '010-1111-0003', 'gangnam1@accessory.com', 'STAFF'),
(1, N'최강남', N'직원', '010-1111-0004', 'gangnam2@accessory.com', 'STAFF'),
(2, N'정홍대', N'점장', '010-2222-0001', 'hongdae.mgr@accessory.com', 'MANAGER'),
(2, N'강홍대', N'직원', '010-2222-0002', 'hongdae1@accessory.com', 'STAFF'),
(2, N'조홍대', N'직원', '010-2222-0003', 'hongdae2@accessory.com', 'STAFF'),
(3, N'윤잠실', N'점장', '010-3333-0001', 'jamsil.mgr@accessory.com', 'MANAGER'),
(3, N'장잠실', N'직원', '010-3333-0002', 'jamsil1@accessory.com', 'STAFF'),
(3, N'임잠실', N'직원', '010-3333-0003', 'jamsil2@accessory.com', 'STAFF');
GO

-- ============================================================
-- 4. 인증 사용자 (auth_users) - BCrypt 해시: admin1234
-- ============================================================
INSERT INTO auth_users (staff_id, username, password_hash) VALUES
(1,  'admin',     '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
(2,  'gangnam_m', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
(3,  'gangnam_1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
(4,  'gangnam_2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
(5,  'hongdae_m', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
(6,  'hongdae_1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
(7,  'hongdae_2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
(8,  'jamsil_m',  '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
(9,  'jamsil_1',  '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
(10, 'jamsil_2',  '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi');
GO

-- ============================================================
-- 5. 상품 (products) - 50개 (카테고리별 10개)
-- ============================================================
-- 팔찌 10개
INSERT INTO products (product_name, category, brand, cost_price, sale_price) VALUES
(N'실버 체인 팔찌', N'팔찌', N'LuvJoy', 8000, 19800),
(N'골드 뱅글 팔찌', N'팔찌', N'LuvJoy', 12000, 29800),
(N'크리스탈 비즈 팔찌', N'팔찌', N'Sparkle', 5000, 14900),
(N'레더 미니 팔찌', N'팔찌', N'Urban', 3000, 9900),
(N'진주 팔찌', N'팔찌', N'Elegance', 15000, 39800),
(N'로즈골드 체인 팔찌', N'팔찌', N'LuvJoy', 10000, 24900),
(N'클로버 참 팔찌', N'팔찌', N'Clover', 7000, 18900),
(N'이니셜 팔찌', N'팔찌', N'MyStyle', 6000, 16800),
(N'무지개 비즈 팔찌', N'팔찌', N'Sparkle', 4000, 12900),
(N'블랙 꼬임 팔찌', N'팔찌', N'Urban', 3500, 10900);

-- 목걸이 10개
INSERT INTO products (product_name, category, brand, cost_price, sale_price) VALUES
(N'실버 하트 목걸이', N'목걸이', N'LuvJoy', 10000, 24900),
(N'골드 크로스 목걸이', N'목걸이', N'Elegance', 18000, 44900),
(N'진주 펜던트 목걸이', N'목걸이', N'Elegance', 20000, 49800),
(N'레인보우 크리스탈 목걸이', N'목걸이', N'Sparkle', 8000, 19900),
(N'이니셜 레터 목걸이', N'목걸이', N'MyStyle', 9000, 22900),
(N'달 별 목걸이 세트', N'목걸이', N'Cosmic', 12000, 29900),
(N'티어드롭 목걸이', N'목걸이', N'Sparkle', 7000, 17900),
(N'베이직 체인 목걸이', N'목걸이', N'Urban', 5000, 14900),
(N'로즈 목걸이', N'목걸이', N'Clover', 11000, 27900),
(N'나비 펜던트 목걸이', N'목걸이', N'Clover', 9000, 23900);

-- 귀걸이 10개
INSERT INTO products (product_name, category, brand, cost_price, sale_price) VALUES
(N'실버 링 귀걸이', N'귀걸이', N'LuvJoy', 6000, 15900),
(N'골드 드롭 귀걸이', N'귀걸이', N'Elegance', 12000, 29900),
(N'크리스탈 스터드 귀걸이', N'귀걸이', N'Sparkle', 5000, 13900),
(N'진주 이어링', N'귀걸이', N'Elegance', 14000, 34900),
(N'하트 귀걸이', N'귀걸이', N'LuvJoy', 5500, 14900),
(N'별모양 이어링', N'귀걸이', N'Cosmic', 4500, 12900),
(N'후프 귀걸이 (대)', N'귀걸이', N'Urban', 7000, 17900),
(N'후프 귀걸이 (소)', N'귀걸이', N'Urban', 5000, 13900),
(N'깃털 드롭 귀걸이', N'귀걸이', N'Bohemian', 6500, 16900),
(N'미니 다이아 스터드', N'귀걸이', N'Elegance', 8000, 19900);

-- 반지 10개
INSERT INTO products (product_name, category, brand, cost_price, sale_price) VALUES
(N'실버 밴드 반지', N'반지', N'LuvJoy', 7000, 17900),
(N'골드 트위스트 반지', N'반지', N'Elegance', 13000, 32900),
(N'크리스탈 반지', N'반지', N'Sparkle', 8000, 20900),
(N'진주 반지', N'반지', N'Elegance', 16000, 39900),
(N'이니셜 반지', N'반지', N'MyStyle', 9000, 23900),
(N'클로버 반지', N'반지', N'Clover', 8500, 21900),
(N'스택 반지 세트 (3종)', N'반지', N'LuvJoy', 10000, 26900),
(N'미디 반지', N'반지', N'Urban', 4000, 11900),
(N'별자리 반지', N'반지', N'Cosmic', 9500, 24900),
(N'나비 반지', N'반지', N'Clover', 7500, 19900);

-- 기타 10개
INSERT INTO products (product_name, category, brand, cost_price, sale_price) VALUES
(N'헤어핀 세트', N'기타', N'Clover', 3000, 8900),
(N'머리띠 (크리스탈)', N'기타', N'Sparkle', 5000, 14900),
(N'브로치 (장미)', N'기타', N'Clover', 6000, 15900),
(N'발찌 (실버)', N'기타', N'LuvJoy', 7000, 18900),
(N'발찌 (골드)', N'기타', N'Elegance', 10000, 24900),
(N'앙클렛 세트', N'기타', N'LuvJoy', 8000, 21900),
(N'커프 (실버 와이드)', N'기타', N'Urban', 9000, 23900),
(N'커프 (골드 슬림)', N'기타', N'Urban', 8000, 20900),
(N'바디 체인', N'기타', N'Bohemian', 12000, 30900),
(N'귀 커프 세트', N'기타', N'Urban', 5500, 14900);
GO

-- ============================================================
-- 6. 상품 옵션 (product_variants) - 각 상품 2개씩 (100개)
-- ============================================================
INSERT INTO product_variants (product_id, sku, color, size_info, extra_price) VALUES
(1, 'BRC-001-SIL-F', N'실버', N'Free', 0),
(1, 'BRC-001-SIL-S', N'실버', N'S', -1000),
(2, 'BRC-002-GLD-F', N'골드', N'Free', 0),
(2, 'BRC-002-GLD-L', N'골드', N'L', 1000),
(3, 'BRC-003-CLR-F', N'클리어', N'Free', 0),
(3, 'BRC-003-PNK-F', N'핑크', N'Free', 0),
(4, 'BRC-004-BRN-F', N'브라운', N'Free', 0),
(4, 'BRC-004-BLK-F', N'블랙', N'Free', 0),
(5, 'BRC-005-WHT-F', N'화이트', N'Free', 0),
(5, 'BRC-005-CRM-F', N'크림', N'Free', 0),
(6, 'BRC-006-RGD-F', N'로즈골드', N'Free', 0),
(6, 'BRC-006-RGD-S', N'로즈골드', N'S', -1000),
(7, 'BRC-007-GLD-F', N'골드', N'Free', 0),
(7, 'BRC-007-SIL-F', N'실버', N'Free', 0),
(8, 'BRC-008-A-F', N'A', N'Free', 0),
(8, 'BRC-008-B-F', N'B', N'Free', 0),
(9, 'BRC-009-MUL-F', N'멀티', N'Free', 0),
(9, 'BRC-009-PST-F', N'파스텔', N'Free', 0),
(10, 'BRC-010-BLK-F', N'블랙', N'Free', 0),
(10, 'BRC-010-NVY-F', N'네이비', N'Free', 0),
-- 목걸이 variants
(11, 'NCK-011-SIL-F', N'실버', N'Free', 0),
(11, 'NCK-011-SIL-L', N'실버', N'Long', 2000),
(12, 'NCK-012-GLD-F', N'골드', N'Free', 0),
(12, 'NCK-012-GLD-S', N'골드', N'Short', -2000),
(13, 'NCK-013-WHT-F', N'화이트', N'Free', 0),
(13, 'NCK-013-PNK-F', N'핑크', N'Free', 1000),
(14, 'NCK-014-MUL-F', N'멀티', N'Free', 0),
(14, 'NCK-014-CLR-F', N'클리어', N'Free', 0),
(15, 'NCK-015-A-F', N'A', N'Free', 0),
(15, 'NCK-015-B-F', N'B', N'Free', 0),
(16, 'NCK-016-GLD-F', N'골드', N'Free', 0),
(16, 'NCK-016-SIL-F', N'실버', N'Free', 0),
(17, 'NCK-017-CLR-F', N'클리어', N'Free', 0),
(17, 'NCK-017-PNK-F', N'핑크', N'Free', 0),
(18, 'NCK-018-GLD-F', N'골드', N'Free', 0),
(18, 'NCK-018-SIL-F', N'실버', N'Free', 0),
(19, 'NCK-019-PNK-F', N'핑크', N'Free', 0),
(19, 'NCK-019-RED-F', N'레드', N'Free', 0),
(20, 'NCK-020-WHT-F', N'화이트', N'Free', 0),
(20, 'NCK-020-PNK-F', N'핑크', N'Free', 0),
-- 귀걸이 variants
(21, 'EAR-021-SIL-F', N'실버', N'Free', 0),
(21, 'EAR-021-GLD-F', N'골드', N'Free', 1000),
(22, 'EAR-022-GLD-F', N'골드', N'Free', 0),
(22, 'EAR-022-RGD-F', N'로즈골드', N'Free', 0),
(23, 'EAR-023-CLR-F', N'클리어', N'Free', 0),
(23, 'EAR-023-PNK-F', N'핑크', N'Free', 0),
(24, 'EAR-024-WHT-F', N'화이트', N'Free', 0),
(24, 'EAR-024-CRM-F', N'크림', N'Free', 0),
(25, 'EAR-025-SIL-F', N'실버', N'Free', 0),
(25, 'EAR-025-GLD-F', N'골드', N'Free', 0),
(26, 'EAR-026-GLD-F', N'골드', N'Free', 0),
(26, 'EAR-026-SIL-F', N'실버', N'Free', 0),
(27, 'EAR-027-GLD-F', N'골드', N'Large', 0),
(27, 'EAR-027-SIL-F', N'실버', N'Large', 0),
(28, 'EAR-028-GLD-F', N'골드', N'Small', 0),
(28, 'EAR-028-SIL-F', N'실버', N'Small', 0),
(29, 'EAR-029-BRN-F', N'브라운', N'Free', 0),
(29, 'EAR-029-BLK-F', N'블랙', N'Free', 0),
(30, 'EAR-030-SIL-F', N'실버', N'Free', 0),
(30, 'EAR-030-GLD-F', N'골드', N'Free', 1000),
-- 반지 variants
(31, 'RNG-031-SIL-5', N'실버', N'5호', 0),
(31, 'RNG-031-SIL-7', N'실버', N'7호', 0),
(32, 'RNG-032-GLD-5', N'골드', N'5호', 0),
(32, 'RNG-032-GLD-7', N'골드', N'7호', 0),
(33, 'RNG-033-CLR-5', N'클리어', N'5호', 0),
(33, 'RNG-033-PNK-7', N'핑크', N'7호', 0),
(34, 'RNG-034-WHT-5', N'화이트', N'5호', 0),
(34, 'RNG-034-WHT-7', N'화이트', N'7호', 0),
(35, 'RNG-035-A-F', N'A', N'Free', 0),
(35, 'RNG-035-B-F', N'B', N'Free', 0),
(36, 'RNG-036-GLD-F', N'골드', N'Free', 0),
(36, 'RNG-036-SIL-F', N'실버', N'Free', 0),
(37, 'RNG-037-SIL-SET', N'실버', N'세트', 0),
(37, 'RNG-037-GLD-SET', N'골드', N'세트', 2000),
(38, 'RNG-038-SIL-F', N'실버', N'Free', 0),
(38, 'RNG-038-GLD-F', N'골드', N'Free', 1000),
(39, 'RNG-039-CON-F', N'별자리', N'Free', 0),
(39, 'RNG-039-CON-S', N'별자리', N'Small', -1000),
(40, 'RNG-040-WHT-F', N'화이트', N'Free', 0),
(40, 'RNG-040-PNK-F', N'핑크', N'Free', 0),
-- 기타 variants
(41, 'ETC-041-PNK-F', N'핑크', N'Free', 0),
(41, 'ETC-041-WHT-F', N'화이트', N'Free', 0),
(42, 'ETC-042-CLR-F', N'클리어', N'Free', 0),
(42, 'ETC-042-PNK-F', N'핑크', N'Free', 0),
(43, 'ETC-043-RED-F', N'레드', N'Free', 0),
(43, 'ETC-043-PNK-F', N'핑크', N'Free', 0),
(44, 'ETC-044-SIL-F', N'실버', N'Free', 0),
(44, 'ETC-044-GLD-F', N'골드', N'Free', 2000),
(45, 'ETC-045-GLD-F', N'골드', N'Free', 0),
(45, 'ETC-045-SIL-F', N'실버', N'Free', 0),
(46, 'ETC-046-SIL-F', N'실버', N'Free', 0),
(46, 'ETC-046-GLD-F', N'골드', N'Free', 0),
(47, 'ETC-047-SIL-F', N'실버', N'Wide', 0),
(47, 'ETC-047-GLD-F', N'골드', N'Wide', 2000),
(48, 'ETC-048-SIL-F', N'실버', N'Slim', 0),
(48, 'ETC-048-GLD-F', N'골드', N'Slim', 1000),
(49, 'ETC-049-GLD-F', N'골드', N'Free', 0),
(49, 'ETC-049-SIL-F', N'실버', N'Free', 0),
(50, 'ETC-050-SIL-F', N'실버', N'Free', 0),
(50, 'ETC-050-GLD-F', N'골드', N'Free', 0);
GO

-- ============================================================
-- 7. 점포별 재고 (store_inventory)
-- ============================================================
-- 강남점 (store_id=1) 재고
INSERT INTO store_inventory (store_id, variant_id, quantity, low_stock_alert)
SELECT 1, variant_id, ABS(CHECKSUM(NEWID())) % 50 + 5, 10
FROM product_variants;

-- 홍대점 (store_id=2) 재고
INSERT INTO store_inventory (store_id, variant_id, quantity, low_stock_alert)
SELECT 2, variant_id, ABS(CHECKSUM(NEWID())) % 50 + 5, 10
FROM product_variants;

-- 잠실점 (store_id=3) 재고
INSERT INTO store_inventory (store_id, variant_id, quantity, low_stock_alert)
SELECT 3, variant_id, ABS(CHECKSUM(NEWID())) % 50 + 5, 10
FROM product_variants;
GO

-- ============================================================
-- 8. 고객 (customers) - 100명
-- ============================================================
INSERT INTO customers (name, phone, email, birth_date, gender, grade, point_balance, total_purchase) VALUES
(N'김민지', '010-1001-0001', 'mj.kim@email.com', '1995-03-15', N'여', 'silver', 1500, 320000),
(N'이서연', '010-1001-0002', 'sy.lee@email.com', '1992-07-22', N'여', 'gold', 3200, 750000),
(N'박준호', '010-1001-0003', 'jh.park@email.com', '1988-11-08', N'남', 'bronze', 500, 85000),
(N'최유진', '010-1001-0004', 'yj.choi@email.com', '1998-02-14', N'여', 'silver', 2100, 450000),
(N'정다은', '010-1001-0005', 'de.jung@email.com', '1996-09-30', N'여', 'gold', 4500, 820000),
(N'강현우', '010-1001-0006', 'hw.kang@email.com', '1991-05-17', N'남', 'bronze', 200, 45000),
(N'조은혜', '010-1001-0007', 'eh.jo@email.com', '1997-12-03', N'여', 'vip', 8900, 1650000),
(N'윤지호', '010-1001-0008', 'jh.yun@email.com', '1993-08-25', N'남', 'silver', 1800, 380000),
(N'장수빈', '010-1001-0009', 'sb.jang@email.com', '1999-04-11', N'여', 'bronze', 300, 62000),
(N'임태양', '010-1001-0010', 'ty.lim@email.com', '1990-01-28', N'남', 'gold', 2800, 710000),
(N'한지수', '010-1001-0011', 'js.han@email.com', '1994-06-19', N'여', 'silver', 1200, 310000),
(N'오민준', '010-1001-0012', 'mj.oh@email.com', '1987-10-07', N'남', 'bronze', 100, 28000),
(N'서예린', '010-1001-0013', 'yr.seo@email.com', '2000-03-22', N'여', 'silver', 1600, 350000),
(N'권나영', '010-1001-0014', 'ny.kwon@email.com', '1995-08-14', N'여', 'gold', 3900, 780000),
(N'신동현', '010-1001-0015', 'dh.shin@email.com', '1989-12-05', N'남', 'bronze', 400, 70000),
(N'홍수아', '010-1001-0016', 'sa.hong@email.com', '1998-07-31', N'여', 'silver', 2300, 470000),
(N'문지은', '010-1001-0017', 'je.moon@email.com', '1996-02-18', N'여', 'vip', 7600, 1580000),
(N'노준서', '010-1001-0018', 'js.noh@email.com', '1992-09-09', N'남', 'gold', 2600, 720000),
(N'유하은', '010-1001-0019', 'he.yu@email.com', '1997-05-27', N'여', 'silver', 1900, 420000),
(N'배성민', '010-1001-0020', 'sm.bae@email.com', '1985-11-13', N'남', 'bronze', 600, 92000),
(N'전소희', '010-1001-0021', 'sh.jeon@email.com', '1999-04-02', N'여', 'silver', 1100, 305000),
(N'류지원', '010-1001-0022', 'jw.ryu@email.com', '1993-08-16', N'남', 'bronze', 250, 55000),
(N'허민아', '010-1001-0023', 'ma.heo@email.com', '2001-01-10', N'여', 'bronze', 180, 38000),
(N'남기태', '010-1001-0024', 'kt.nam@email.com', '1988-06-24', N'남', 'gold', 3100, 740000),
(N'안지현', '010-1001-0025', 'jh.an@email.com', '1995-10-08', N'여', 'silver', 2000, 410000),
(N'구현석', '010-1001-0026', 'hs.koo@email.com', '1991-03-19', N'남', 'bronze', 350, 68000),
(N'엄채원', '010-1001-0027', 'cw.um@email.com', '1997-07-05', N'여', 'vip', 9200, 1720000),
(N'변성훈', '010-1001-0028', 'sh.byun@email.com', '1986-12-30', N'남', 'gold', 2400, 695000),
(N'채지민', '010-1001-0029', 'jm.chae@email.com', '1999-09-14', N'여', 'silver', 1700, 365000),
(N'탁민재', '010-1001-0030', 'mj.tak@email.com', '1994-04-25', N'남', 'bronze', 450, 78000),
(N'도하린', '010-1001-0031', 'hr.do@email.com', '2000-02-07', N'여', 'silver', 1300, 320000),
(N'모지훈', '010-1001-0032', 'jh.mo@email.com', '1992-06-12', N'남', 'gold', 2900, 730000),
(N'편소연', '010-1001-0033', 'sy.pyeon@email.com', '1996-10-28', N'여', 'bronze', 150, 32000),
(N'황재원', '010-1001-0034', 'jw.hwang@email.com', '1989-03-03', N'남', 'bronze', 280, 58000),
(N'심나리', '010-1001-0035', 'nr.shim@email.com', '1998-08-17', N'여', 'silver', 2200, 440000),
(N'곽태준', '010-1001-0036', 'tj.kwak@email.com', '1990-01-21', N'남', 'gold', 3400, 760000),
(N'봉수정', '010-1001-0037', 'sj.bong@email.com', '1995-05-06', N'여', 'silver', 1500, 330000),
(N'마현진', '010-1001-0038', 'hj.ma@email.com', '1987-09-29', N'남', 'vip', 11000, 1900000),
(N'표지영', '010-1001-0039', 'jy.pyo@email.com', '1997-12-15', N'여', 'bronze', 220, 48000),
(N'소민수', '010-1001-0040', 'ms.so@email.com', '1993-04-01', N'남', 'gold', 2700, 705000),
(N'명아름', '010-1001-0041', 'ar.myung@email.com', '1999-07-20', N'여', 'silver', 1400, 340000),
(N'사준영', '010-1001-0042', 'jy.sa@email.com', '1991-11-11', N'남', 'bronze', 320, 65000),
(N'하유나', '010-1001-0043', 'yn.ha@email.com', '2001-03-25', N'여', 'bronze', 90, 22000),
(N'어승우', '010-1001-0044', 'sw.uh@email.com', '1986-08-08', N'남', 'gold', 2500, 700000),
(N'반지혜', '010-1001-0045', 'jh.ban@email.com', '1994-12-19', N'여', 'silver', 1800, 385000),
(N'성도현', '010-1001-0046', 'dh.sung@email.com', '1990-06-14', N'남', 'bronze', 400, 73000),
(N'가은비', '010-1001-0047', 'eb.ga@email.com', '1997-02-28', N'여', 'vip', 8200, 1600000),
(N'나현호', '010-1001-0048', 'hh.na@email.com', '1992-10-03', N'남', 'gold', 3000, 715000),
(N'다솜이', '010-1001-0049', 'si.da@email.com', '1998-05-09', N'여', 'silver', 1600, 355000),
(N'라민철', '010-1001-0050', 'mc.ra@email.com', '1985-01-16', N'남', 'bronze', 500, 88000),
(N'마지희', '010-1001-0051', 'jh.ma2@email.com', '1999-09-21', N'여', 'silver', 1250, 315000),
(N'바성준', '010-1001-0052', 'sj.ba@email.com', '1993-03-07', N'남', 'bronze', 180, 40000),
(N'사아린', '010-1001-0053', 'ar.sa@email.com', '2000-07-13', N'여', 'bronze', 130, 29000),
(N'아준혁', '010-1001-0054', 'jh.a@email.com', '1988-11-27', N'남', 'gold', 3300, 755000),
(N'자유진', '010-1001-0055', 'yj.ja@email.com', '1995-04-04', N'여', 'silver', 1900, 395000),
(N'차민혁', '010-1001-0056', 'mh.cha@email.com', '1991-08-18', N'남', 'bronze', 270, 57000),
(N'카나래', '010-1001-0057', 'nr.ka@email.com', '1997-12-02', N'여', 'bronze', 200, 44000),
(N'타수현', '010-1001-0058', 'sh.ta@email.com', '1986-05-09', N'남', 'gold', 2800, 720000),
(N'파지민', '010-1001-0059', 'jm.pa@email.com', '1994-09-23', N'여', 'silver', 2100, 430000),
(N'하동욱', '010-1001-0060', 'dw.ha@email.com', '1990-02-14', N'남', 'vip', 12000, 2100000),
(N'갈지선', '010-1001-0061', 'js.gal@email.com', '1999-06-30', N'여', 'silver', 1350, 325000),
(N'공태민', '010-1001-0062', 'tm.gong@email.com', '1992-10-16', N'남', 'bronze', 230, 50000),
(N'낙지현', '010-1001-0063', 'jh.nak@email.com', '2001-02-05', N'여', 'bronze', 110, 25000),
(N'당혜미', '010-1001-0064', 'hm.dang@email.com', '1987-07-19', N'남', 'gold', 3600, 770000),
(N'랑수아', '010-1001-0065', 'sa.rang@email.com', '1995-11-08', N'여', 'silver', 1700, 360000),
(N'망민준', '010-1001-0066', 'mj.mang@email.com', '1991-04-22', N'남', 'bronze', 360, 69000),
(N'방하나', '010-1001-0067', 'hn.bang@email.com', '1997-08-06', N'여', 'silver', 1450, 335000),
(N'상지욱', '010-1001-0068', 'jw.sang@email.com', '1989-12-20', N'남', 'gold', 2600, 698000),
(N'앙미소', '010-1001-0069', 'ms.ang@email.com', '1996-03-11', N'여', 'vip', 9800, 1750000),
(N'왕철수', '010-1001-0070', 'cs.wang@email.com', '1984-09-24', N'남', 'bronze', 550, 95000),
(N'잠지원', '010-1001-0071', 'jw.jam@email.com', '1998-01-15', N'여', 'silver', 2000, 415000),
(N'창민수', '010-1001-0072', 'ms.chang@email.com', '1993-05-29', N'남', 'bronze', 300, 63000),
(N'탕나리', '010-1001-0073', 'nr.tang@email.com', '2000-10-10', N'여', 'bronze', 160, 35000),
(N'팡주혁', '010-1001-0074', 'jh.pang@email.com', '1988-03-17', N'남', 'gold', 3200, 745000),
(N'항소연', '010-1001-0075', 'sy.hang@email.com', '1994-07-31', N'여', 'silver', 1850, 390000),
(N'갱태호', '010-1001-0076', 'th.gaeng@email.com', '1990-11-14', N'남', 'bronze', 420, 76000),
(N'냉수진', '010-1001-0077', 'sj.naeng@email.com', '1997-04-28', N'여', 'silver', 1550, 345000),
(N'댕민아', '010-1001-0078', 'ma.daeng@email.com', '1986-09-04', N'남', 'gold', 2400, 690000),
(N'랭지현', '010-1001-0079', 'jh.raeng@email.com', '1995-01-18', N'여', 'vip', 10500, 1850000),
(N'맹서준', '010-1001-0080', 'sj.maeng@email.com', '1992-06-03', N'남', 'bronze', 240, 52000),
(N'뱅나은', '010-1001-0081', 'ne.baeng@email.com', '1999-10-17', N'여', 'silver', 1300, 318000),
(N'샹민준', '010-1001-0082', 'mj.shang@email.com', '1991-02-28', N'남', 'bronze', 200, 46000),
(N'앵지영', '010-1001-0083', 'jy.aeng@email.com', '1996-07-12', N'여', 'bronze', 140, 31000),
(N'쟁성호', '010-1001-0084', 'sh.jaeng@email.com', '1987-12-26', N'남', 'gold', 3500, 765000),
(N'챙하늘', '010-1001-0085', 'hn.chaeng@email.com', '1994-05-11', N'여', 'silver', 1700, 370000),
(N'캥도윤', '010-1001-0086', 'dy.kaeng@email.com', '1990-09-25', N'남', 'bronze', 380, 71000),
(N'탱아연', '010-1001-0087', 'ay.taeng@email.com', '1998-03-09', N'여', 'silver', 1950, 405000),
(N'팽지수', '010-1001-0088', 'js.paeng@email.com', '1985-08-22', N'남', 'gold', 2700, 708000),
(N'행소미', '010-1001-0089', 'sm.haeng@email.com', '1997-11-06', N'여', 'vip', 8500, 1620000),
(N'갱철호', '010-1001-0090', 'ch.gaeng2@email.com', '1993-04-20', N'남', 'bronze', 260, 56000),
(N'냉수연', '010-1001-0091', 'sy.naeng2@email.com', '2000-08-04', N'여', 'bronze', 120, 27000),
(N'댕진혁', '010-1001-0092', 'jh.daeng2@email.com', '1989-01-18', N'남', 'gold', 3100, 738000),
(N'랭미래', '010-1001-0093', 'mr.raeng2@email.com', '1995-06-01', N'여', 'silver', 1600, 348000),
(N'맹성준', '010-1001-0094', 'sj.maeng2@email.com', '1991-10-15', N'남', 'bronze', 310, 64000),
(N'뱅하은', '010-1001-0095', 'he.baeng2@email.com', '1998-02-27', N'여', 'silver', 2050, 420000),
(N'샹지훈', '010-1001-0096', 'jh.shang2@email.com', '1986-07-11', N'남', 'gold', 2600, 702000),
(N'앵민선', '010-1001-0097', 'ms.aeng2@email.com', '1996-11-24', N'여', 'vip', 9500, 1700000),
(N'쟁태준', '010-1001-0098', 'tj.jaeng2@email.com', '1992-04-08', N'남', 'bronze', 170, 37000),
(N'챙수빈', '010-1001-0099', 'sb.chaeng2@email.com', '1999-09-21', N'여', 'silver', 1400, 330000),
(N'탱민호', '010-1001-0100', 'mh.taeng2@email.com', '1988-02-05', N'남', 'bronze', 290, 61000);
GO

-- ============================================================
-- 9. 공급업체 (suppliers) - 3개
-- ============================================================
INSERT INTO suppliers (supplier_name, contact_name, phone, email, address) VALUES
(N'(주)동양악세사리', N'이공급', '02-5678-1234', 'supply@dongyang.com', N'서울 중구 을지로 100'),
(N'홍콩주얼리코리아', N'박수입', '02-8765-4321', 'hk@hkjewelry.co.kr', N'서울 종로구 종로 200'),
(N'트렌드메이크', N'최트렌드', '02-3344-5566', 'trend@trendmake.com', N'경기 성남시 분당구 판교로 300');
GO
