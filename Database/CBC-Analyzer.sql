DROP TABLE IF EXISTS test_tag;
DROP TABLE IF EXISTS patient_tag;
DROP TABLE IF EXISTS parent_patient;
DROP TABLE IF EXISTS "result";
DROP TABLE IF EXISTS test;
DROP TABLE IF EXISTS "parameter";
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS "user";


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

CREATE TABLE "user" (
    "user_id" serial PRIMARY KEY,
    "first_name" varchar(50) NOT NULL,
    "last_name" varchar(50) NOT NULL,
    "is_doctor" boolean DEFAULT false,
    "username" varchar(15) UNIQUE NOT NULL,
    "password" varchar(20) NOT NULL
);

CREATE TABLE "parent_patient" (
    "parent_id" int NOT NULL,
    "patient_id" int NOT NULL,
    CONSTRAINT PK_parent_patient PRIMARY KEY (parent_id, patient_id)
);

ALTER TABLE "test" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

ALTER TABLE "result" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id") ON DELETE CASCADE;

ALTER TABLE "result" ADD FOREIGN KEY ("parameter_id") REFERENCES "parameter" ("parameter_id");

ALTER TABLE "patient_tag" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id") ON DELETE CASCADE;

ALTER TABLE "patient_tag" ADD FOREIGN KEY ("tag_id") REFERENCES "tag" ("tag_id");

ALTER TABLE "test_tag" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id") ON DELETE CASCADE;

ALTER TABLE "test_tag" ADD FOREIGN KEY ("tag_id") REFERENCES "tag" ("tag_id");

ALTER TABLE "parent_patient" ADD FOREIGN KEY ("parent_id") REFERENCES "user" ("user_id") ON DELETE CASCADE;

ALTER TABLE "parent_patient" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id") ON DELETE CASCADE;



-- Filling Tables

INSERT INTO patient (chart_number, name, sex, species, birthday) values
    ('000000', 'Charlie Blevins', 'SF', 'Canine', '2013-03-14'),
    ('000000', 'Charlie Blevins', 'SF', 'Canine', '2013-03-14', true),
    ('10175', 'Good Boy', 'CM', 'Canine', '2011-02-04', true),
    ('115202', 'Killer Blevins', 'CM', 'Canine', '2012-07-02', true),
    ('115654', 'Sick Girl', 'F', 'Canine', '2014-12-20', true),
    ('6709', 'Max Remke', 'M', 'Canine', '2023-12-02', true);


INSERT INTO tag (name, is_diagnosis) values
	('healthy', false),
    ('hypothyroidism', true),
    ('proteinuria', true),
    ('friendly', false)
    ('periodontal disease', true),
    ('skin mass', true),
    ('hyporexia', true),
    ('arthritis', true),
    ('weight loss', true),
    ( 'vomiting', false),
    ( 'obese', true),
    ( 'Renal insufficiency', true);
	
INSERT INTO parameter (name, range_low, range_high, unit) values
	('White Blood Cells', 4, 15.5, '10^3/mcL'),
	('Red Blood Cells', 4.8, 9.3, '10^6/mcL'),
	('Hemoglobin', 12.1, 20.3, 'g/dl'),
	('Hematocrit', 36, 60, '%'),
	('Mean Crepuscular Volume', 58, 79, 'fL'),
	('Platelets', 170, 400, '10^3/mcL');
	
INSERT INTO patient_tag (patient_id, tag_id) values
	(1, 1),
    (1, 4),
    (2, 11),
    (4, 12);
	
INSERT INTO test (patient_id, time_stamp) values 
	(1, 1, '2024-03-14 00:00:00'),
    (2, 1, '2024-04-19 18:53:41.8161'),
    (3, 1, '2024-04-19 18:54:23.247954'),
    (4, 1, '2024-04-19 18:55:24.242228'),
    (5, 5, '2024-04-19 18:58:47.958267'),
    (6, 2, '2024-04-19 18:59:27.933445'),
    (7, 2, '2024-04-19 18:59:38.036401'),
    (8, 2, '2024-04-19 19:01:59.641406');
	
INSERT INTO result (test_id, parameter_id, result_value) values 
	(1, 1, 9.3);
    (1, 2, 8.0);
    (1, 3, 20.3);
    (1, 4, 54.0);
    (1, 5, 67.0);
    (1, 6, 330.0);
    (2, 1, 12.8);
    (2, 2, 7.72);
    (2, 3, 18.8);
    (2, 4, 53);
    (2, 5, 61);
    (2, 6, 279);
    (3, 1, 10.7);
    (3, 2, 7.86);
    (3, 3, 15.7);
    (3, 4, 65);
    (3, 5, 75);
    (3, 6, 203);
    (4, 1, 6.3);
    (4, 2, 6.84);
    (4, 3, 14.9);
    (4, 4, 50);
    (4, 5, 76);
    (4, 6, 532);
    (5, 1, 11.5);
    (5, 2, 5.55);
    (5, 3, 12.1);
    (5, 4, 43);
    (5, 5, 66);
    (5, 6, 692);
    (6, 1, 12.2);
    (6, 2, 7.12);
    (6, 3, 14.6);
    (6, 4, 26);
    (6, 5, 84);
    (6, 6, 351);
    (7, 1, 14.8);
    (7, 2, 7.6);
    (7, 3, 17.3);
    (7, 4, 47);
    (7, 5, 72);
    (7, 6, 435);
    (8, 1, 8.4);
    (8, 2, 5.34);
    (8, 3, 18.3);
    (8, 4, 27);
    (8, 5, 83);
    (8, 6, 696);

INSERT INTO "user" (first_name, last_name, is_doctor, username, password) VALUES
    ('Beau', 'Blevins', false, 'testparent', '1234'),
    ('Chris', 'Kelly', true, 'testdoctor', '1234');

INSERT INTO "parent_patient" (parent_id, patient_id) VALUES
    (1, 1),
    (1, 3);

INSERT INTO public.test_tag (test_id, tag_id) VALUES
    (2, 7);
    (4, 7);
    (4, 10);
    (7, 11);
    (8, 11);