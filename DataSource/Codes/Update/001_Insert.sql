--Update
INSERT INTO tb_codetype (
    cde_codetype,
    txt_shortdesc,
    txt_desc,
    dte_created
) VALUES (
             'ROLE',
             'Role',
             'Code type for Roles',
             CURRENT_TIMESTAMP
         );
INSERT INTO tb_codetype (
    cde_codetype,
    txt_shortdesc,
    txt_desc,
    dte_created
) VALUES (
             'ORDER_STATUS',
             'Order status',
             'Code type for Order Statuses',
             CURRENT_TIMESTAMP
         );
INSERT INTO tb_codetype (
    cde_codetype,
    txt_shortdesc,
    txt_desc,
    dte_created
) VALUES (
             'PAYMENT_STATUS',
             'Payment Status',
             'Code type for Payment statuses',
             CURRENT_TIMESTAMP
);

INSERT INTO tb_code (
    cde_code,
    num_codetype,
    txt_shortdesc,
    txt_desc,
    dte_created
) VALUES (
             'ROLE_ADMIN',
             (select num_codetype from tb_codetype where cde_codetype = 'ROLE'),
             'Admin role',
             'Admin role for user to access the Application',
             CURRENT_TIMESTAMP
         );

INSERT INTO tb_code (
    cde_code,
    num_codetype,
    txt_shortdesc,
    txt_desc,
    dte_created
) VALUES (
             'ROLE_EMPLOYEE',
             (select num_codetype from tb_codetype where cde_codetype = 'ROLE'),
             'Employee role',
             'Employee role for user to access the Application',
             CURRENT_TIMESTAMP
         );

INSERT INTO tb_code (
    cde_code,
    num_codetype,
    txt_shortdesc,
    txt_desc,
    dte_created
) VALUES (
             'ROLE_MANAGER',
             (select num_codetype from tb_codetype where cde_codetype = 'ROLE'),
             'Manager role',
             'Manager role for user to access the Application',
             CURRENT_TIMESTAMP
         );

