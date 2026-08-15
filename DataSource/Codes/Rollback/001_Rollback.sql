-- Rollback
DELETE from tb_codetype where cde_codetype = 'ROLE';
DELETE from tb_codetype where cde_codetype = 'ORDER_STATUS';
DELETE from tb_codetype where cde_codetype = 'PAYMENT_STATUS';

DELETE from tb_code where cde_code = 'ROLE_ADMIN';
DELETE from tb_code where cde_code = 'ROLE_EMPLOYEE';
DELETE from tb_code where cde_code = 'ROLE_MANAGER';