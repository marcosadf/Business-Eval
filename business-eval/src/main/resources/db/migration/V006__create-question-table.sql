CREATE TABLE question (
	id SERIAL CONSTRAINT pk_id_question PRIMARY KEY,
	description varchar(500),
	position bigint,
	category_id bigint NOT NULL,
	FOREIGN KEY (category_id) REFERENCES category (id)
);