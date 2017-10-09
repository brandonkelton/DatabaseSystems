
create table Carrier
(
	airline_id int primary key not null,
    carrier varchar(3) not null,
    carrier_name varchar(255) not null,
    carrier_group smallint not null,
    carrier_group_new smallint not null
);

create table Airport
(
	airport_id int primary key not null,
    airport_seq_id int not null,
    city_market_id int not null,
    airport_code varchar(5) not null,
    city_name varchar(255) not null,
    state_nm varchar(50) not null,
    state_abr varchar(2) not null,
    state_fips smallint not null,
    wac smallint not null
);

create table Flight
(
    flight_id serial primary key not null,
    origin_airport_id int not null,
    destination_airport_id int not null,
    airline_id int not null,
    passengers int not null,
    freight int not null,
    mail int not null,
    distance int not null,
    ramp_to_ramp int not null,
    air_time int not null,
    seats int not null,
    payload int not null,
    aircraft_group smallint not null,
    aircraft_type int not null,
    aircraft_config smallint not null,
    class varchar(1)
);