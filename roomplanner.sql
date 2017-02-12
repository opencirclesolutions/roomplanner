CREATE TABLE organisations (
	id                   SERIAL NOT NULL,
	name		     VARCHAR(100) NOT NULL,
	version              INTEGER NOT NULL,		
	PRIMARY KEY (id)	
);

CREATE TABLE locations
(
	id                   SERIAL NOT NULL,
	organisation_id      INTEGER NOT NULL,
	code	             VARCHAR(20) NOT NULL,
	name		     VARCHAR(100) NOT NULL,
	street	             VARCHAR(100) NOT NULL,
	house_number         VARCHAR(20) NOT NULL,
	city                 VARCHAR(50) NOT NULL,
	version              INTEGER NOT NULL,		
	PRIMARY KEY (id)
);

ALTER TABLE locations
ADD CONSTRAINT fk_locations_organisations FOREIGN KEY (organisation_id) REFERENCES organisations(id)
		ON DELETE CASCADE;

CREATE TABLE rooms
(
	id                   SERIAL NOT NULL,
        organisation_id      INTEGER NOT NULL,
	code	             VARCHAR(20) NOT NULL,
	name		     VARCHAR(100) NOT NULL,
	capacity             INTEGER NOT NULL,
	cost_per_hour        INTEGER,
	whiteboard           BOOLEAN NOT NULL,
	video_conferencing   BOOLEAN NOT NULL,
	phone_conferencing   BOOLEAN NOT NULL,
	confort_level        INTEGER NOT NULL,
	location_id          INTEGER NOT NULL,	
	version              INTEGER NOT NULL,	
	PRIMARY KEY (id)
);

ALTER TABLE rooms
ADD CONSTRAINT fk_rooms_organisations FOREIGN KEY (organisation_id) REFERENCES organisations(id)
		ON DELETE CASCADE;

ALTER TABLE rooms
ADD CONSTRAINT fk_rooms_locations FOREIGN KEY (location_id) REFERENCES Locations(id)
		ON DELETE CASCADE;

CREATE TABLE employees
(
	id                   SERIAL NOT NULL,
	first_name	     VARCHAR(50) NOT NULL,
	last_name	     VARCHAR(50) NOT NULL,	
	email                VARCHAR(100) NOT NULL,
	version              INTEGER NOT NULL,	
	PRIMARY KEY (id)
);


CREATE TABLE meetings
(
	id                   SERIAL NOT NULL,
        organisation_id      INTEGER NOT NULL,
	description          VARCHAR(200) NULL,
	desired_location_id  INTEGER NULL,
	assigned_location_id INTEGER NULL,		     	
        whiteboard           BOOLEAN NOT NULL,
	video_conferencing   BOOLEAN NOT NULL,
	phone_conferencing   BOOLEAN NOT NULL,
	priority	     INTEGER NOT NULL,
	meeting_date         DATE NOT NULL,
	start_time	     TIME,
	end_time             TIME,	
	assigned             BOOLEAN NOT NULL,
	room_id   	     INTEGER NULL,	
	exchange_item_id     VARCHAR(500) NULL,
	exchange_change_key  VARCHAR(500) NULL,
	confirmed            BOOLEAN,
	version              INTEGER NOT NULL,	
	PRIMARY KEY (id)
);

ALTER TABLE meetings
ADD CONSTRAINT fk_meetings_rooms FOREIGN KEY (room_id) REFERENCES rooms(id)
		ON DELETE CASCADE;

ALTER TABLE meetings
ADD CONSTRAINT fk_meetings_organisations FOREIGN KEY (organisation_id) REFERENCES organisations(id)
		ON DELETE CASCADE;

CREATE TABLE meetings_employees (
	id                   SERIAL NOT NULL,
	employee_id          INTEGER NOT NULL,
	meeting_id	     INTEGER NOT NULL
);

ALTER TABLE meetings_employees
ADD CONSTRAINT fk_me_employees FOREIGN KEY (employee_id) REFERENCES employees(id)
		ON DELETE CASCADE;

ALTER TABLE meetings_employees
ADD CONSTRAINT fk_me_meetings FOREIGN KEY (meeting_id) REFERENCES meetings(id)
		ON DELETE CASCADE;

INSERT INTO ORGANISATIONS(name,version) VALUES ('OCS', 0);
INSERT INTO ORGANISATIONS(name,version) VALUES ('NotEnoughRooms', 0);
INSERT INTO ORGANISATIONS(name,version) VALUES ('CheapAss.com', 0);
INSERT INTO ORGANISATIONS(name,version) VALUES ('Evil Corp', 0);


