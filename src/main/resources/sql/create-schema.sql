drop table if exists tbl_performance;
drop table if exists tbl_tour_city;
drop table if exists tbl_assignment;
drop table if exists tbl_employee;
drop table if exists tbl_todos;

create table tbl_todos
(
    id        long primary key auto_increment,
    task      varchar(32) not null,
    completed boolean default false
);

CREATE TABLE IF NOT EXISTS tbl_employee
(
    id         uuid default random_uuid() NOT NULL PRIMARY KEY,
    first_name varchar(20)                not null,
    last_name  varchar(20)                not null,
    email_addr varchar(50) unique         not null,
    manager    uuid references tbl_employee (id)
);

CREATE TABLE IF NOT EXISTS tbl_assignment
(
    employee_id   uuid not null references tbl_employee,
    task_id       long not null references tbl_todos,
    date_assigned datetime default now()
);

CREATE TABLE IF NOT EXISTS tbl_tour_city
(
    location VARCHAR(64),
    city     VARCHAR(32) not null,
    state    VARCHAR(32) not null,
    capacity int default 0,
    constraint pk_city primary key (city, state)
);

CREATE TABLE IF NOT EXISTS tbl_performance
(
    tour_id        uuid default random_uuid() NOT NULL PRIMARY KEY,
    artist         VARCHAR(128)               not null,
    city           VARCHAR(32)                not null,
    state          VARCHAR(32)                not null,
    scheduled_date datetime,
    constraint fk_tour_city foreign key (city, state) references tbl_tour_city (city, state),
    constraint uniq_performer unique (artist, city, state, scheduled_date)
);
