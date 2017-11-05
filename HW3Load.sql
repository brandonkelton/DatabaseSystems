--Raw Data Load
--Automatically creates table public."rawAirlineData" based on column names and column count in the file referenced below
--An exact number of columns must be provided to the function
select load_csv_file('rawAirlineData', 'D:\CSDB\T100D\544193580_T_T100D_SEGMENT_US_CARRIER_ONLY_2017_6.csv', 45);

--Insert data into tables from raw data table

--INSERT: aircraft_config
insert into aircraft_config (select distinct aircraft_config::smallint from public."rawAirlineData");

--INSERT: aircraft_type
insert into aircraft_type (aircraft_type_id, aircraft_group) (select distinct aircraft_type::int, aircraft_group::smallint from public."rawAirlineData");

--INSERT: airline_group
insert into airline_group
select distinct airline_group::smallint from 
	(select carrier_group airline_group from public."rawAirlineData" union select carrier_group_new airline_group from public."rawAirlineData") g;

--INSERT: airline
insert into airline (airline_id, carrier, carrier_name, entity, region)
select distinct airline_id::int, carrier, carrier_name, unique_carrier_entity, region from public."rawAirlineData";

--INSERT: airline_airline_group
insert into airline_airline_group (airline_id, group_id, active_group)
select distinct airline_id::int, airline_group::smallint, active_group::boolean from
(
	select airline_id, carrier_group as airline_group, (case when carrier_group = carrier_group_new then true else false end) as active_group from public."rawAirlineData"
    union
    select airline_id, carrier_group_new as airline_group, true as active_group from public."rawAirlineData" where not carrier_group = carrier_group_new
) airline_groups order by airline_id;

--INSERT: airport_location
insert into airport_location (wac, state_name, state_abr, state_fips, city_name, city_market_id)
select distinct wac::smallint, state_name, state_abr, state_fips::smallint, city_name, city_market_id::int from
(
	select 
    	origin_wac as wac, 
    	origin_state_nm as state_name, 
    	origin_state_abr as state_abr, 
    	origin_state_fips as state_fips, 
    	split_part(origin_city_name, ', ', 1) as city_name, 
    	origin_city_market_id as city_market_id 
    from 
    	public."rawAirlineData"
    union
    select 
    	dest_wac as wac, 
    	dest_state_nm as state_name, 
    	dest_state_abr as state_abr, 
    	dest_state_fips as state_fips, 
    	split_part(dest_city_name, ', ', 1) as city_name, 
    	dest_city_market_id as city_market_id
    from 
    	public."rawAirlineData"
) data

--INSERT: airport
insert into airport (airport_id, airport_seq_id, airport_code, location_id)
select distinct airport_id::int, airport_seq_id::int, airport_code, location_id from
(
	select 
    	origin_airport_id as airport_id, 
    	origin_airport_seq_id as airport_seq_id, 
    	origin as airport_code, 
    	(select location_id from airport_location where city_name=split_part(r.origin_city_name, ', ', 1) and state_name=r.origin_state_nm limit 1) as location_id 
    from 
    	public."rawAirlineData" r
    union
    select 
    	dest_airport_id as airport_id, 
    	dest_airport_seq_id as airport_seq_id, 
    	dest as airport_code, 
    	(select location_id from airport_location where city_name=split_part(r.dest_city_name, ', ', 1) and state_name=r.dest_state_nm limit 1) as location_id 
    from 
    	public."rawAirlineData" r
) airports;

--INSERT: airline_airport_departures
insert into airline_airport_departures
(
	airport_id,
    airline_id,
    scheduled,
    performed
)
select
	origin_airport_id::int,
    airline_id::int,
    coalesce(sum(departures_scheduled::int), 0),
    coalesce(sum(departures_performed::int), 0)
from
	public."rawAirlineData"
group by origin_airport_id, airline_id

--INSERT: ramp_distance
insert into ramp_distance (select distinct ramp_to_ramp::int from public."rawAirlineData");

--INSERT: path
insert into "path" (origin_airport_id, destination_airport_id, distance)
select distinct origin_airport_id::int, dest_airport_id::int, distance::int from public."rawAirlineData";

--INSERT: path_data
insert into path_data
(
    path_id,
	airline_id,
    passengers,
    freight,
    mail,
    air_time,
    seats,
    payload,
    "class",
    ramp_distance_id,
    aircraft_type_id,
    aircraft_config_id
)
select distinct
	(select path_id from path where origin_airport_id=r.origin_airport_id::int and destination_airport_id=r.dest_airport_id::int and distance=r.distance::int limit 1) as path_id,
    r.airline_id::int,
    r.passengers::int,
    r.freight::int,
    r.mail::int,
    r.air_time::int,
    r.seats::int,
    r.payload::int,
    r."class",
    r.ramp_to_ramp::int,
    r.aircraft_type::int,
    r.aircraft_config::int
from
	public."rawAirlineData" r