DROP TABLE IF EXISTS test_tag;
DROP TABLE IF EXISTS patient_tag;
DROP TABLE IF EXISTS result;
DROP TABLE IF EXISTS test;
DROP TABLE IF EXISTS parameter;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS tag;

CREATE TABLE "patient" (
  "patient_id" serial PRIMARY KEY,
  "chart_number" varchar(6) unique,
  "name" varchar(50) NOT NULL,
  "sex" varchar(2) NOT NULL CHECK ("sex" IN ('F', 'M', 'SF', 'CM')),
  "species" varchar(20) NOT NULL,
  "birthday" date NOT NULL,
  "active" boolean default true
);

CREATE TABLE "test" (
  "test_id" serial PRIMARY KEY,
  "patient_id" int NOT NULL,
  "time_stamp" timestamp NOT NULL
);

CREATE TABLE "result" (
  "result_id" serial PRIMARY KEY,
  "test_id" int NOT NULL,
  "parameter_id" int NOT NULL,
  "result_value" numeric NOT NULL CHECK (result_value >= 0)
);

CREATE TABLE "parameter" (
  "parameter_id" serial PRIMARY KEY,
  "name" varchar(50) NOT NULL,
  "range_low" numeric NOT NULL CHECK (range_low >= 0),
  "range_high" numeric NOT NULL CHECK (range_high > range_low),
  "unit" varchar(50) NOT NULL
);

CREATE TABLE "tag" (
  "tag_id" serial PRIMARY KEY,
  "name" varchar(50) NOT NULL UNIQUE,
  "is_diagnosis" boolean DEFAULT false
);

CREATE TABLE "patient_tag" (
  "patient_id" int NOT NULL,
  "tag_id" int NOT NULL,
  CONSTRAINT PK_patient_tag PRIMARY KEY (patient_id, tag_id)
);

CREATE TABLE "test_tag" (
  "test_id" int NOT NULL,
  "tag_id" int NOT NULL,
  CONSTRAINT PK_test_tag PRIMARY KEY (test_id, tag_id)
);

ALTER TABLE "test" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

ALTER TABLE "result" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id") ON DELETE CASCADE;

ALTER TABLE "result" ADD FOREIGN KEY ("parameter_id") REFERENCES "parameter" ("parameter_id");

ALTER TABLE "patient_tag" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id") ON DELETE CASCADE;

ALTER TABLE "patient_tag" ADD FOREIGN KEY ("tag_id") REFERENCES "tag" ("tag_id");

ALTER TABLE "test_tag" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id") ON DELETE CASCADE;

ALTER TABLE "test_tag" ADD FOREIGN KEY ("tag_id") REFERENCES "tag" ("tag_id");

-- Filling Tables

INSERT INTO patient (chart_number, name, sex, species, birthday) values
  ('000000', 'Charlie Blevins', 'SF', 'Canine', '2013-03-14'),
  ('10175', 'Good Boy', 'CM', 'Canine', '2011-02-04'),
  ('115202', 'Killer Blevins', 'CM', 'Canine', '2012-07-02'),
  ('115654',  'Sick Girl', 'F', 'Canine', '2014-12-20');


INSERT INTO tag (name, is_diagnosis) values
	('healthy', false),
	('hypothyroidism', true),
	('proteinuria', true),
	('friendly', false),
	('periodontal disease', true),
	('skin mass', true),
	('hyporexia', true),
	('arthritis', true),
	('weight loss', true);
	
INSERT INTO parameter (name, range_low, range_high, unit) values
	('White Blood Cells', 4, 15.5, '10^3/mcL'),
	('Red Blood Cells', 4.8, 9.3, '10^6/mcL'),
	('Hemoglobin', 12.1, 20.3, 'g/dl'),
	('Hematocrit', 36, 60, '%'),
	('Mean Crepuscular Volume', 58, 79, 'fL'),
	('Platelets', 170, 400, '10^3/mcL');
	
INSERT INTO patient_tag (patient_id, tag_id) values
	(1, 1),
	(1, 4);
	
INSERT INTO test (patient_id, time_stamp) values 
	(1, '2024-03-14');
	
INSERT INTO result (test_id, parameter_id, result_value) values 
	(1, 1, 9.3),
	(1, 2, 8.0),
	(1, 3, 20.3),
	(1, 4, 54.0),
	(1, 5, 67.0),
	(1, 6, 330.0);
	