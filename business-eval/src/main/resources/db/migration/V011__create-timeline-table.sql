CREATE TABLE timeline (
	id SERIAL CONSTRAINT pk_id_timeline PRIMARY KEY,
	description varchar(255) NOT NULL,
	datetime_edition TIMESTAMP WITH TIME ZONE
);