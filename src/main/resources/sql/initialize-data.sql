insert into tbl_todos (task)
values ('make breakfast');
insert into tbl_todos (task)
values ('run 5k');
insert into tbl_todos (task, COMPLETED)
values ('read novel', true);
insert into tbl_todos (task, COMPLETED)
values ('visit museum', true);
insert into tbl_todos (task)
values ('fix window');
insert into tbl_todos (task)
values ('bake cookies');
insert into tbl_todos (task)
values ('Walk the dog');
insert into tbl_todos (task, COMPLETED)
values ('feed the cat', true);

insert into TBL_EMPLOYEE (first_name, last_name, email_addr, manager)
VALUES ('Abel', 'Chuck', 'abel.chuck@email.com', null);
insert into TBL_EMPLOYEE (first_name, last_name, email_addr, manager)
VALUES ('Brian', 'Kent', 'brian.kent@email.com',
        (select id from TBL_EMPLOYEE where EMAIL_ADDR = 'abel.chuck@email.com'));
insert into TBL_EMPLOYEE (first_name, last_name, email_addr, manager)
VALUES ('Camile', 'Walsh', 'camile.walsh@email.com',
        (select id from TBL_EMPLOYEE where EMAIL_ADDR = 'abel.chuck@email.com'));
insert into TBL_EMPLOYEE (first_name, last_name, email_addr, manager)
VALUES ('Donni', 'Torn', 'donni.torn@email.com',
        (select id from TBL_EMPLOYEE where EMAIL_ADDR = 'brian.kent@email.com'));
insert into TBL_EMPLOYEE (first_name, last_name, email_addr, manager)
VALUES ('Emily', 'Kline', 'emily.kline@email.com',
        (select id from TBL_EMPLOYEE where EMAIL_ADDR = 'camile.walsh@email.com'));
insert into TBL_EMPLOYEE (first_name, last_name, email_addr, manager)
VALUES ('Festus', 'Mare', 'festus.mare@email.com',
        (select id from TBL_EMPLOYEE where EMAIL_ADDR = 'brian.kent@email.com'));
insert into TBL_EMPLOYEE (first_name, last_name, email_addr, manager)
VALUES ('Gina', 'Blake', 'gina.blake@email.com',
        (select id from TBL_EMPLOYEE where EMAIL_ADDR = 'camile.walsh@email.com'));
insert into TBL_EMPLOYEE (first_name, last_name, email_addr, manager)
VALUES ('Henry', 'Kim', 'henry.kim@email.com', (select id from TBL_EMPLOYEE where EMAIL_ADDR = 'donni.torn@email.com'));
insert into TBL_EMPLOYEE (first_name, last_name, email_addr, manager)
VALUES ('Justin', 'Pace', 'justin.pace@email.com',
        (select id from TBL_EMPLOYEE where EMAIL_ADDR = 'donni.torn@email.com'));

insert into TBL_ASSIGNMENT (EMPLOYEE_ID, TASK_ID)
values ((select id from TBL_EMPLOYEE where EMAIL_ADDR = 'donni.torn@email.com'),
        (select id from TBL_TODOS where TASK = 'make breakfast'));
insert into TBL_ASSIGNMENT (EMPLOYEE_ID, TASK_ID)
values ((select id from TBL_EMPLOYEE where EMAIL_ADDR = 'henry.kim@email.com'),
        (select id from TBL_TODOS where TASK = 'run 5k'));
insert into TBL_ASSIGNMENT (EMPLOYEE_ID, TASK_ID)
values ((select id from TBL_EMPLOYEE where EMAIL_ADDR = 'justin.pace@email.com'),
        (select id from TBL_TODOS where TASK = 'read novel'));
insert into TBL_ASSIGNMENT (EMPLOYEE_ID, TASK_ID)
values ((select id from TBL_EMPLOYEE where EMAIL_ADDR = 'gina.blake@email.com'),
        (select id from TBL_TODOS where TASK = 'fix window'));
insert into TBL_ASSIGNMENT (EMPLOYEE_ID, TASK_ID)
values ((select id from TBL_EMPLOYEE where EMAIL_ADDR = 'justin.pace@email.com'),
        (select id from TBL_TODOS where TASK = 'fix window'));

select t.task, e.FIRST_NAME, e.LAST_NAME
from TBL_ASSIGNMENT a
         inner join TBL_TODOS t on a.TASK_ID = t.id
         inner join TBL_EMPLOYEE e on e.ID = a.EMPLOYEE_ID;

select *
from TBL_ASSIGNMENT;

-- tree of managers from top to bottom
WITH recursive org_structure(id, first_name, last_name, email_addr, manager, level) AS
                   (
                       --anchor query
                       SELECT m.*, 1
                       FROM tbl_employee m
                       where m.manager is null
                       UNION ALL
                       --recursive query
                       SELECT e.*, prior.level + 1
                       FROM org_structure prior
                                INNER JOIN tbl_employee e
                                           on e.manager = prior.id)
SELECT o.*
FROM org_structure o;

-- tree of managed from bottom to top

MERGE INTO tbl_tour_city KEY (city, state) VALUES (null, 'Madison', 'WI', null);
MERGE INTO tbl_tour_city KEY (city, state) VALUES (null, 'Chicago', 'IL', null);
MERGE INTO tbl_tour_city KEY (city, state) VALUES (null, 'Boston', 'MA', null);
MERGE INTO tbl_tour_city KEY (city, state) VALUES (null, 'Cincinnati', 'OH', null);
MERGE INTO tbl_tour_city KEY (city, state) VALUES (null, 'Nashville', 'TN', null);

insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('Jason Alden', 'Chicago', 'IL');
insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('Taylor Swift', 'Cincinnati', 'OH');
insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('Jason Alden', 'Boston', 'MA');
insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('Katty Perry', 'Nashville', 'TN');
insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('Luke Bryant', 'Madison', 'WI');
insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('Blake Shelton', 'Madison', 'WI');
insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('Megan Stallion', 'Nashville', 'TN');
insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('Lil Wayne', 'Boston', 'MA');
insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('John Legend', 'Chicago', 'IL');
insert into TBL_PERFORMANCE (ARTIST, CITY, STATE)
VALUES ('Kane Brown', 'Cincinnati', 'OH');

select *
from TBL_PERFORMANCE p
         inner join tbl_tour_city t on t.CITY = p.CITY and t.state = p.state;

select fk.*, ref.*
from TBL_PERFORMANCE fk
         inner join TBL_TOUR_CITY ref on fk.CITY = ref.CITY and fk.STATE = ref.STATE
where ref.CITY = ?
  and ref.STATE = ?;

select fk.*, ref.*
from tbl_employee fk
         inner join tbl_employee ref on fk.manager = ref.id
where ref.id =
      (select e.id from tbl_employee e where e.email_addr = ?);

select fk.*, ref.*
from TBL_EMPLOYEE ref
         inner join TBL_EMPLOYEE fk on fk.MANAGER = ref.ID
where ref.ID = ?;

//78451f91-af64-47a4-aa1d-0326e7e6357c