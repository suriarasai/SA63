-- Sample data. Runs after Hibernate creates the tables (see application.properties).
INSERT INTO cubicle (cubicle_id, name, location, description) VALUES
    (1, 'C-101', 'Level 1, Block A', 'Window seat next to the pantry'),
    (2, 'C-102', 'Level 1, Block A', 'Corner desk with a whiteboard'),
    (3, 'C-201', 'Level 2, Block B', 'Quiet zone, far from meetings'),
    (4, 'C-202', 'Level 2, Block B', 'Near the HR office'),
    (5, 'C-301', 'Level 3, Block C', 'New hot desk, not yet assigned');

INSERT INTO project (project_id, name, department, budget) VALUES
    (1, 'Gruntmaster 6000',     'PRODUCT', 250000.00),
    (2, 'Payroll Migration',    'FINANCE', 120000.00),
    (3, 'Hiring Freeze Portal', 'HR',       45000.00),
    (4, 'Cloud Cost Cutter',    'IT',       80000.00);

INSERT INTO employee (emp_id, name, doj, pay, title, emp_type, department, cubicle_id) VALUES
    (1, 'Dilbert', '1990-01-20', 5000.00, 'software engineer', 0, 'PRODUCT', 1),
    (2, 'Alice',   '1989-01-01', 5500.00, 'software engineer', 0, 'PRODUCT', 2),
    (3, 'Wally',   '1985-06-15', 4800.00, 'software engineer', 0, 'PRODUCT', 3),
    (4, 'Asok',    '2024-07-01', 1500.00, 'intern',            1, 'IT',      NULL),
    (5, 'Catbert', '1999-12-02', 7000.00, 'HR director',       0, 'HR',      4),
    (6, 'Dogbert', '2010-03-03', 9000.00, 'consultant',        2, 'FINANCE', NULL);

INSERT INTO gadget (gadget_id, name, description, emp_id) VALUES
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

INSERT INTO employee_project (emp_id, project_id) VALUES
    (1, 1), (1, 2),
    (2, 1),
    (4, 1), (4, 3),
    (5, 3),
    (6, 2);
