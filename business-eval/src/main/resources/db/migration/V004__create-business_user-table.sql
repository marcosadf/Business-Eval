CREATE TABLE business_user (
	id SERIAL CONSTRAINT pk_id_business_user PRIMARY KEY,
	user_id bigint NOT NULL,
	invitation_accepted BOOLEAN NOT NULL DEFAULT FALSE,
	business_id bigint NOT NULL,
	FOREIGN KEY (user_id) REFERENCES sys_user (id),
	FOREIGN KEY (business_id) REFERENCES business (id)
);