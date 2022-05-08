create table books
(
    isbn   varchar(255) not null,
    author varchar(255),
    title  varchar(255),
    primary key (isbn)
);

create table users
(
    id       int primary key auto_increment,
    login    varchar(30) not null,
    password varchar(40) not null,
    unique uniq_login (login)
);

create table permissions
(
    id         int primary key auto_increment,
    permission varchar(30) not null,
    unique uniq_permission (permission)
);

create table user_to_permissions
(
    user_id       int not null,
    permission_id int not null,
    constraint fk_user_to_permission_user foreign key (user_id) references users (id),
    constraint fk_user_to_permission_permission foreign key (permission_id) references permissions (id)
);

create table user_to_favourites
(
    user_id int          not null,
    isbn_id varchar(255) not null,
    constraint fk_user_to_favourites_user foreign key (user_id) references users (id),
    constraint fk_user_to_favourites_isbn foreign key (isbn_id) references books (isbn)
);

insert into users (login, password)
values ('admin', '5F4DCC3B5AA765D61D8327DEB882CF99'),
       ('user', '5F4DCC3B5AA765D61D8327DEB882CF99');

insert into permissions (permission)
values ('ADMIN');

insert into user_to_permissions (user_id, permission_id)
values ((select id from users where login = 'admin'), (select id from permissions where permission = 'ADMIN'));

insert into books (isbn, author, title)
values ('9780747532743', 'J. K. Rowling', 'Harry Potter And The Philosopher''s Stone'),
       ('9780449212608', 'Margaret Atwood', 'The Handmaid''s Tale'),
       ('9780486283265', 'Alexandre Dumas', 'The Three Musketeers'),
       ('9788129140432', 'SUN TZU', 'THE ART OF WAR'),
       ('9780062422750', 'Michael Bond', 'A Bear Called Paddington'),
       ('9780975361511', 'Mayne Reid', 'The Headless Horseman'),
       ('9798662731349', 'Christopher Buehlman', 'Between Two Fires'),
       ('9780671683900', 'Larry McMurtry', 'Lonesome Dove'),
       ('9781481418614', 'Lynn Weingarten', 'Bad Girls with Perfect Faces'),
       ('9781442421097', 'Kathi Appelt', 'Angel Thieves'),
       ('9780545424936', 'Maggie Stiefvater', 'The Raven Boys'),
       ('9780545582933', 'J. K. Rowling', 'Harry Potter and the Prisoner of Azkaban'),
       ('9780141191447', 'Shirley Jackson', 'The Haunting Of Hill House');