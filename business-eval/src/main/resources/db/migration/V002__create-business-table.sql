CREATE TABLE business (
	id SERIAL CONSTRAINT pk_id_business PRIMARY KEY,
	name varchar(130), 
	cnpj_cpf varchar(18) NOT NULL UNIQUE,
	manager_id bigint NOT NULL,
	FOREIGN KEY (manager_id) REFERENCES manager (id)
);