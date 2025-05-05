CREATE TABLE seller_account (
	id serial4 NOT NULL,
	"name" varchar(255) NULL,
	"owner" varchar(255) NULL,
	is_test bool NULL,
	"type" varchar(255) NULL,
	status int4 NULL,
	CONSTRAINT seller_account_pkey PRIMARY KEY (id)
);