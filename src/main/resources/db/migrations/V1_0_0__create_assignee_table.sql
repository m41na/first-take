CREATE TABLE IF NOT EXISTS tbl_emp_recognition
(
    id        UUID  default random_uuid() primary key,
    employee  UUID         not null references tbl_employee (id),
    prize     varchar(264) not null,
    est_value float default 0,
    constraint uniq_prize unique (id, employee)
);