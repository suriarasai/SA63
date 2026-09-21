-- =====================================================================
-- Association mapping demo .

-- Enum storage:
--   Employee.empType    -> ORDINAL : 0 = PERMEMPLOYEE, 1 = TEMPEMPLOYEE, 2 = CONTRACTOR
--   Employee.department -> STRING  : 'PRODUCT', 'HR', ...
--   Project.department  -> ORDINAL (no @Enumerated, so Hibernate defaults to ordinal):
--       0 = HR, 1 = FINANCE, 2 = ADMIN, 3 = BI, 4 = SALES, 5 = PRODUCT, 6 = IT, 7 = MARCOMM
--
-- NOTE: the old "employee" table insert was removed; there is no Employee entity any more.
-- =====================================================================


 
-- =====================================================================
-- UNIDIRECTIONAL  (UniEmployee owns every association)
--   @OneToOne  uniCubicle       -> FK column uni_employee.uni_cubicle_cubicle_id
--   @OneToMany uniGadgetsList   -> join table uni_employee_uni_gadgets_list
--                                  (no @JoinColumn, so gadgets have NO employee column)
--   @ManyToMany uniProjectsList -> join table uni_employee_uni_projects_list
-- =====================================================================
 
INSERT INTO uni_cubicle (cubicle_id, name, location, description) VALUES
    (1, 'C-101', 'Level 1, Block A', 'Window seat next to the pantry'),
    (2, 'C-102', 'Level 1, Block A', 'Corner desk with a whiteboard'),
    (3, 'C-201', 'Level 2, Block B', 'Quiet zone, far from meetings'),
    (4, 'C-202', 'Level 2, Block B', 'Near the HR office'),
    (5, 'C-301', 'Level 3, Block C', 'New hot desk, not yet assigned');
 
INSERT INTO uni_project (project_id, name, department, budjet) VALUES
    (1, 'Gruntmaster 6000',     5, 250000.00),   -- PRODUCT
    (2, 'Payroll Migration',    1, 120000.00),   -- FINANCE
    (3, 'Hiring Freeze Portal', 0,  45000.00),   -- HR
    (4, 'Cloud Cost Cutter',    6,  80000.00);   -- IT (nobody has joined yet)
 
INSERT INTO uni_gadget (gadget_id, name, description) VALUES
    (1,  'ThinkPad X1',              'Development laptop'),
    (2,  'Oscilloscope',             'Hardware debugging'),
    (3,  'Pocket Protector',         'Standard engineering issue'),
    (4,  'MacBook Pro 14',           'Development laptop'),
    (5,  'Dell 27in Monitor',        'External display'),
    (6,  'Noise-cancelling Headset', 'Essential for naps'),
    (7,  'Pixel Test Phone',         'Android device for testing'),
    (8,  'Surface Laptop',           'HR workstation'),
    (9,  'ThinkPad T14',             'Contractor laptop'),
    (10, 'YubiKey',                  'Hardware security key');
 
INSERT INTO uni_employee (emp_id, name, doj, pay, title, emp_type, department, uni_cubicle_cubicle_id) VALUES
    (1, 'Dilbert', '1990-01-20', 5000.00, 'software engineer', 0, 'PRODUCT', 1),
    (2, 'Alice',   '1989-01-01', 5500.00, 'software engineer', 0, 'PRODUCT', 2),
    (3, 'Wally',   '1985-06-15', 4800.00, 'software engineer', 0, 'PRODUCT', 3),
    (4, 'Asok',    '2024-07-01', 1500.00, 'intern',            1, 'IT',      NULL),
    (5, 'Catbert', '1999-12-02', 7000.00, 'HR director',       0, 'HR',      4),
    (6, 'Dogbert', '2010-03-03', 9000.00, 'consultant',        2, 'FINANCE', NULL);
 
-- Employee -> gadgets (each gadget appears once: unique constraint on the gadget column)
INSERT INTO uni_employee_uni_gadgets_list (uni_employee_emp_id, uni_gadgets_list_gadget_id) VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 4), (2, 5),
    (3, 6),
    (4, 7),
    (5, 8),
    (6, 9), (6, 10);
 
-- Employee -> projects
INSERT INTO uni_employee_uni_projects_list (uni_employee_emp_id, uni_projects_list_project_id) VALUES
    (1, 1), (1, 2),
    (2, 1),
    (4, 1), (4, 3),
    (5, 3),
    (6, 2);
 
 
-- =====================================================================
-- BIDIRECTIONAL  (with mappedBy on the inverse side of each association)
--   BiEmployee.uniCubicle   owner   -> FK column bi_employee.uni_cubicle_cubicle_id
--   BiCubicle.biEmployee    inverse (mappedBy = "uniCubicle")      -> no column
--   BiGadget.biemployee     owner   -> FK column bi_gadget.biemployee_emp_id
--   BiEmployee.uniGadgetsList inverse (mappedBy = "biemployee")    -> no join table
--   BiEmployee.uniProjectsList owner -> join table bi_employee_uni_projects_list
--   BiProject.biEmployees   inverse (mappedBy = "uniProjectsList") -> no join table
-- Only the owning sides are stored; Hibernate fills the inverse sides when loading.
-- =====================================================================
 
INSERT INTO bi_cubicle (cubicle_id, name, location, description) VALUES
    (1, 'C-101', 'Level 1, Block A', 'Window seat next to the pantry'),
    (2, 'C-102', 'Level 1, Block A', 'Corner desk with a whiteboard'),
    (3, 'C-201', 'Level 2, Block B', 'Quiet zone, far from meetings'),
    (4, 'C-202', 'Level 2, Block B', 'Near the HR office'),
    (5, 'C-301', 'Level 3, Block C', 'New hot desk, not yet assigned');
 
INSERT INTO bi_project (project_id, name, department, budjet) VALUES
    (1, 'Gruntmaster 6000',     5, 250000.00),   -- PRODUCT
    (2, 'Payroll Migration',    1, 120000.00),   -- FINANCE
    (3, 'Hiring Freeze Portal', 0,  45000.00),   -- HR
    (4, 'Cloud Cost Cutter',    6,  80000.00);   -- IT (nobody has joined yet)
 
INSERT INTO bi_employee (emp_id, name, doj, pay, title, emp_type, department, uni_cubicle_cubicle_id) VALUES
    (1, 'Dilbert', '1990-01-20', 5000.00, 'software engineer', 0, 'PRODUCT', 1),
    (2, 'Alice',   '1989-01-01', 5500.00, 'software engineer', 0, 'PRODUCT', 2),
    (3, 'Wally',   '1985-06-15', 4800.00, 'software engineer', 0, 'PRODUCT', 3),
    (4, 'Asok',    '2024-07-01', 1500.00, 'intern',            1, 'IT',      NULL),
    (5, 'Catbert', '1999-12-02', 7000.00, 'HR director',       0, 'HR',      4),
    (6, 'Dogbert', '2010-03-03', 9000.00, 'consultant',        2, 'FINANCE', NULL);
 
-- Gadget -> employee: the FK now lives directly on the gadget (no join table)
INSERT INTO bi_gadget (gadget_id, name, description, biemployee_emp_id) VALUES
    (1,  'ThinkPad X1',              'Development laptop',         1),
    (2,  'Oscilloscope',             'Hardware debugging',         1),
    (3,  'Pocket Protector',         'Standard engineering issue', 1),
    (4,  'MacBook Pro 14',           'Development laptop',         2),
    (5,  'Dell 27in Monitor',        'External display',           2),
    (6,  'Noise-cancelling Headset', 'Essential for naps',         3),
    (7,  'Pixel Test Phone',         'Android device for testing', 4),
    (8,  'Surface Laptop',           'HR workstation',             5),
    (9,  'ThinkPad T14',             'Contractor laptop',          6),
    (10, 'YubiKey',                  'Hardware security key',      6);
 
-- Employee <-> projects: single join table owned by BiEmployee.
-- Because the association is now bidirectional, the employee column is named after
-- the INVERSE field (BiProject.biEmployees) -> bi_employees_emp_id, not bi_employee_emp_id.
INSERT INTO bi_employee_uni_projects_list (bi_employees_emp_id, uni_projects_list_project_id) VALUES
    (1, 1), (1, 2),
    (2, 1),
    (4, 1), (4, 3),
    (5, 3),
    (6, 2);