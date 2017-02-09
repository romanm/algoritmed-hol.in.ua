CREATE SEQUENCE dbid;

CREATE TABLE test1 (
	test1_id INT DEFAULT NEXTVAL('dbid') PRIMARY KEY
	,test1_name VARCHAR(100)
);

CREATE TABLE users (
  username VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  enabled boolean NOT NULL DEFAULT true,
  PRIMARY KEY (username)
);

CREATE TABLE user_roles (
  user_role_id int DEFAULT NEXTVAL('dbid') PRIMARY KEY,
  username VARCHAR(45) NOT NULL,
  role VARCHAR(45) NOT NULL,
  UNIQUE (role,username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username)
);

INSERT INTO users(username,password) VALUES ('ruh','ruh');
INSERT INTO user_roles (username, role) VALUES ('ruh', 'ROLE_RUH');


