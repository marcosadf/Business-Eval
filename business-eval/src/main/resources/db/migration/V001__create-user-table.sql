CREATE TYPE authorities AS ENUM('DEFAULT', 'ROOT', 'MANAGER');
CREATE TABLE manager (
	id SERIAL CONSTRAINT pk_id_user PRIMARY KEY,
	name varchar(80), 
	email varchar(60) NOT NULL UNIQUE,
	login_code varchar(255) NOT NULL,
	expiration_code datetime,
	authority authorities NOT NULL DEFAULT 'DEFAULT'
);