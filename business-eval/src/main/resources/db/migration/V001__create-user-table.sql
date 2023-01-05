CREATE TYPE authorities AS ENUM('DEFAULT', 'ROOT');
CREATE TABLE sys_user (
	id SERIAL CONSTRAINT pk_id_user PRIMARY KEY,
	name varchar(80), 
	email varchar(60) NOT NULL UNIQUE,
	login_code varchar(255),
	expiration_code TIMESTAMP WITH TIME ZONE,
	authority authorities NOT NULL DEFAULT 'DEFAULT'
);
CREATE CAST (CHARACTER VARYING as authorities) WITH INOUT AS IMPLICIT;