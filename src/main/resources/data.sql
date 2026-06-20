INSERT INTO app_users (username, password, role, enabled)
SELECT
    'admin',
    '$2a$10$TlvgzQFP3Y6NGea48CmuSOth0lOaiU6lh2LADpuRGGQV6YG1Ka0Ea',
    'ADMIN',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM app_users WHERE username = 'admin'
);

INSERT INTO expense_categories (name, monthly_budget, display_order)
SELECT '食費', 50000, 10
WHERE NOT EXISTS (SELECT 1 FROM expense_categories WHERE name = '食費');

INSERT INTO expense_categories (name, monthly_budget, display_order)
SELECT '交通費', 15000, 20
WHERE NOT EXISTS (SELECT 1 FROM expense_categories WHERE name = '交通費');

INSERT INTO expense_categories (name, monthly_budget, display_order)
SELECT '日用品', 20000, 30
WHERE NOT EXISTS (SELECT 1 FROM expense_categories WHERE name = '日用品');

INSERT INTO expenses (category_id, expense_date, amount, description, payment_method, memo)
SELECT c.id, CURRENT_DATE - INTERVAL '3 days', 1280, '昼食', 'クレジットカード', 'サンプル支出'
FROM expense_categories c
WHERE c.name = '食費'
  AND NOT EXISTS (SELECT 1 FROM expenses WHERE description = '昼食');

INSERT INTO expenses (category_id, expense_date, amount, description, payment_method, memo)
SELECT c.id, CURRENT_DATE - INTERVAL '2 days', 640, '電車代', '交通系IC', 'サンプル支出'
FROM expense_categories c
WHERE c.name = '交通費'
  AND NOT EXISTS (SELECT 1 FROM expenses WHERE description = '電車代');

INSERT INTO expenses (category_id, expense_date, amount, description, payment_method, memo)
SELECT c.id, CURRENT_DATE - INTERVAL '1 day', 3280, '洗剤と消耗品', '現金', 'サンプル支出'
FROM expense_categories c
WHERE c.name = '日用品'
  AND NOT EXISTS (SELECT 1 FROM expenses WHERE description = '洗剤と消耗品');
