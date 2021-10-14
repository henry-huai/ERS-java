DROP TABLE IF EXISTS requests cascade;
DROP TABLE IF EXISTS users cascade;

create table users(
	user_id serial primary key,
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	pass_word varchar(64) not null,
	ismanager boolean default false,
	email varchar(100) unique not null
);


create table requests(
	request_id serial primary key,
	image text,
	status int default 0,
	description varchar(100) not null,
	transaction_date Date not null default current_date,
	user_id int references users(user_id)
);

insert into users (first_name, last_name, pass_word, email) values ('henry', 'huai', 1234, 'henry8florida@gmail.com');