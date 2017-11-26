create schema AirReserve;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table AirReserve.customer
(
	customer_id uuid not null primary key default(uuid_generate_v1()),
    first_name varchar(50),
    last_name varchar(50),
    street varchar(100),
    city varchar(100),
    state_province varchar(100),
    country varchar(100),
    postal_code varchar(50)
);

create table AirReserve.customer_phone
(
	customer_id uuid not null references AirReserve.customer (customer_id),
    country_code varchar(5),
    area_code varchar(5),
    phone varchar(20),
    phone_type varchar(25),
    primary key (customer_id, country_code, area_code, phone)
);

create table AirReserve.customer_email
(
	customer_id uuid not null references AirReserve.customer (customer_id),
    email varchar(100) unique,
    preferred boolean,
    primary key (customer_id, email)
);

create table AirReserve.airline
(
	airline_code varchar(10) not null primary key,
    airline_name varchar(50) not null unique
);

create table AirReserve.city
(
	city_code varchar(10) not null primary key,
    city varchar(100) not null,
    state_province varchar(100) not null,
    country varchar(100) not null,
    unique (city, state_province, country)
);

--NOTE: Flight numbers are tied to the origin/destination in my configuration
create table AirReserve.flight
(
	flight_number varchar(10) not null primary key,
    origin_city_code varchar(10) not null,
    dest_city_code varchar(10) not null,
    length_hours int not null,
    length_minutes int not null,
    unique (flight_number, origin_city_code, dest_city_code)
);

create table AirReserve.flight_instance
(
    flight_instance_id uuid not null primary key default(uuid_generate_v1()),
	airline_code varchar(10) not null references AirReserve.airline (airline_code),
    flight_number varchar(10) not null references AirReserve.flight (flight_number),
    departure_date date not null,
    departure_hour int not null,
    departure_minute int not null,
    arrival_date date not null,
    arrival_hour int not null,
    arrival_minute int not null
);

create table AirReserve.booking
(
	booking_id uuid not null primary key default(uuid_generate_v1()),
    customer_id uuid not null references AirReserve.customer (customer_id),
    city_code varchar(10) not null references AirReserve.city (city_code),
    booking_date timestamp not null default(current_timestamp)
);

create table AirReserve.booking_flight_instance
(
	booking_id uuid not null references AirReserve.booking (booking_id),
    flight_instance_id uuid not null references AirReserve.flight_instance (flight_instance_id),
    primary key (booking_id, flight_instance_id)
);

create table AirReserve.booking_passenger
(
	booking_id uuid not null references AirReserve.booking (booking_id),
    first_name varchar(50),
    middle_name varchar(50),
    last_name varchar(50),
    primary key (booking_id, first_name, middle_name, last_name)
);
