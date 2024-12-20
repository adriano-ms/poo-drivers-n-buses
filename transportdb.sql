CREATE DATABASE IF NOT EXISTS transportdb;

USE transportdb;

CREATE TABLE IF NOT EXISTS buses(
	id INT PRIMARY KEY AUTO_INCREMENT,
	license_plate CHAR(7) NOT NULL UNIQUE,
	brand VARCHAR(32) NOT NULL,
	seats_number INT(3) NOT NULL,
	is_eletric BIT DEFAULT 0 NOT NULL,
	line VARCHAR(16) NOT NULL
);

CREATE TABLE IF NOT EXISTS drivers(
	id INT PRIMARY KEY AUTO_INCREMENT,
	driver_license CHAR(11) NOT NULL UNIQUE,
	name VARCHAR(64) NOT NULL,
	admission_date DATE NOT NULL,
	shift VARCHAR(16) NOT NULL,
	phone VARCHAR(11) NOT NULL,
	bus_id INT,
	FOREIGN KEY(bus_id) REFERENCES buses(id)
);

