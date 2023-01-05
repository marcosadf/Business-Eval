CREATE TABLE answer (
	id SERIAL CONSTRAINT pk_id_answer PRIMARY KEY,
	value_answer integer DEFAULT 0,
	question_id bigint NOT NULL,
	business_user_id bigint NOT NULL,
	FOREIGN KEY (question_id) REFERENCES question (id),
	FOREIGN KEY (business_user_id) REFERENCES business_user (id)
);