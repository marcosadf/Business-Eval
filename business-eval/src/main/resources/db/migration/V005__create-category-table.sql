CREATE TABLE category (
	id SERIAL CONSTRAINT pk_id_category PRIMARY KEY,
	name varchar(45),
	position bigint
);