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

CREATE TABLE article (
  article_id INT DEFAULT NEXTVAL('dbid') PRIMARY KEY,
  article_parentid INT NOT NULL DEFAULT CURRVAL('dbid'),
  article_sort INT NOT NULL DEFAULT CURRVAL('dbid'),
  article_tag VARCHAR(10) NOT NULL,
  article_class VARCHAR(100),
  article_text VARCHAR(1000),
  article_comment VARCHAR(200),
  UNIQUE (article_parentid, article_sort),
  CONSTRAINT article_child FOREIGN KEY (article_parentid) REFERENCES (article_id)
);


CREATE TABLE medicament (
	medicament_id INT PRIMARY KEY
	, medicament_name VARCHAR(100)
);

CREATE TABLE medicament_rest (
	medicament_id INT NOT NULL
	, medicament_rest_date DATE NOT NULL
	, medicament_rest int
	, UNIQUE (medicament_id, medicament_rest_date)
	, CONSTRAINT medicament_medicament_rest FOREIGN KEY (medicament_id) REFERENCES medicament (medicament_id)
);

