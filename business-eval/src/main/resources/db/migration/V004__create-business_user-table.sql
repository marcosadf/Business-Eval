CREATE TABLE business_user (
	id SERIAL CONSTRAINT pk_id_business_user PRIMARY KEY,
	user_id bigint NOT NULL,
	business_id bigint NOT NULL,
	FOREIGN KEY (user_id) REFERENCES sys_user (id),
	FOREIGN KEY (business_id) REFERENCES business (id)
);