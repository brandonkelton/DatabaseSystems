--Raw Data Load
select load_csv_file('rawAirlineData', 'D:\CSDB\T100D\544193580_T_T100D_SEGMENT_US_CARRIER_ONLY_2017_6.csv', 45);

--Origin Airports
insert into airport (airport_id, airport_seq_id, city_market_id, airport_code, city_name, state_nm, state_abr, state_fips, wac)
select distinct origin_airport_id::int, origin_airport_seq_id::int, origin_city_market_id::int, origin, origin_city_name, origin_state_nm, origin_state_abr, origin_state_fips::smallint, origin_wac::int from public."rawAirlineData"

--Destination Airports minus those already existing
insert into airport (airport_id, airport_seq_id, city_market_id, airport_code, city_name, state_nm, state_abr, state_fips, wac)
select distinct dest_airport_id::int, dest_airport_seq_id::int, dest_city_market_id::int, dest, dest_city_name, dest_state_nm, dest_state_abr, dest_state_fips::smallint, dest_wac::int from public."rawAirlineData" where dest_airport_id::int not in (select airport_id from airport)

--Carriers
insert into carrier (airline_id, carrier, carrier_name, carrier_group, carrier_group_new)
select distinct airline_id::int, carrier, carrier_name, carrier_group::smallint, carrier_group_new::smallint from public."rawAirlineData"

--Flights
insert into flight (origin_airport_id, destination_airport_id, airline_id, passengers, freight, mail, distance, ramp_to_ramp, air_time, seats, payload, aircraft_group, aircraft_type, aircraft_config, class)
select distinct origin_airport_id::int, dest_airport_id::int, airline_id::int, passengers::int, freight::int, mail::int, distance::int, ramp_to_ramp::int, air_time::int, seats::int, payload::int, aircraft_group::int, aircraft_type::int, aircraft_config::smallint, class from public."rawAirlineData"
