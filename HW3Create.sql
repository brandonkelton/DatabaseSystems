CREATE TABLE aircraft_config
(
    aircraft_config_id smallint NOT NULL,
    CONSTRAINT aircraft_config_pkey PRIMARY KEY (aircraft_config_id)
);

CREATE TABLE aircraft_type
(
    aircraft_type_id integer NOT NULL,
    aircraft_group smallint NOT NULL,
    CONSTRAINT aircraft_type_pkey PRIMARY KEY (aircraft_type_id)
);

CREATE TABLE airline
(
    airline_id integer NOT NULL,
    carrier varchar(3) NOT NULL,
    carrier_name varchar(255) NOT NULL,
    entity varchar(50) NOT NULL,
    region varchar(1) NOT NULL,
    CONSTRAINT carrier_pkey PRIMARY KEY (airline_id)
);

CREATE TABLE airline_airline_group
(
    airline_id integer NOT NULL,
    group_id smallint NOT NULL,
    active_group boolean NOT NULL DEFAULT false,
    CONSTRAINT airline_carrier_group_pkey PRIMARY KEY (airline_id, group_id),
    CONSTRAINT fk_airline_airline_group_airline_id FOREIGN KEY (airline_id)
        REFERENCES public.airline (airline_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_airline_airline_group_group_id FOREIGN KEY (group_id)
        REFERENCES public.airline_group (group_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE airline_group
(
    group_id smallint NOT NULL,
    CONSTRAINT carrier_group_pkey PRIMARY KEY (group_id)
);

CREATE TABLE airport
(
    airport_id integer NOT NULL,
    airport_seq_id integer NOT NULL,
    airport_code varchar(5) NOT NULL,
    location_id integer,
    CONSTRAINT airport_pkey PRIMARY KEY (airport_id),
    CONSTRAINT fk_airport_location_id FOREIGN KEY (location_id)
        REFERENCES public.airport_location (location_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE public.airline_airport_departures
(
    airport_id integer NOT NULL,
    airline_id integer NOT NULL,
    scheduled integer NOT NULL DEFAULT 0,
    performed integer NOT NULL DEFAULT 0,
    CONSTRAINT airline_airport_departures_pkey PRIMARY KEY (airport_id, airline_id),
    CONSTRAINT fk_airport_departures_airport_id FOREIGN KEY (airport_id)
        REFERENCES public.airport (airport_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_airport_departures_airline_id FOREIGN KEY (airline_id)
        REFERENCES public.airline (airline_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT
)

CREATE TABLE airport_location
(
    location_id serial NOT NULL,
    wac smallint NOT NULL,
    state_name varchar(50) NOT NULL,
    state_abr varchar(2) NOT NULL,
    state_fips smallint NOT NULL,
    city_name varchar(50) NOT NULL,
    city_market_id integer NOT NULL,
    CONSTRAINT airport_location_pkey PRIMARY KEY (location_id),
    UNIQUE (city_name, state_name)
);

CREATE TABLE path
(
    path_id serial NOT NULL,
    origin_airport_id integer NOT NULL,
    destination_airport_id integer NOT NULL,
    distance integer NOT NULL,
    UNIQUE (origin_airport_id, destination_airport_id, distance),
    CONSTRAINT trip_pkey PRIMARY KEY (path_id),
    CONSTRAINT fk_path_destination_airport_id FOREIGN KEY (destination_airport_id)
        REFERENCES public.airport (airport_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_path_origin_airport_id FOREIGN KEY (origin_airport_id)
        REFERENCES public.airport (airport_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE path_data
(
    path_data_id serial NOT NULL,
    airline_id integer NOT NULL,
    passengers integer NOT NULL,
    freight integer NOT NULL,
    mail integer NOT NULL,
    air_time integer NOT NULL,
    seats integer NOT NULL,
    payload integer NOT NULL,
    "class" varchar(1),
    path_id integer,
    ramp_distance_id integer,
    aircraft_type_id integer,
    aircraft_config_id integer,
    CONSTRAINT flight_pkey PRIMARY KEY (path_data_id),
    CONSTRAINT fk_path_data_aircraft_config_id FOREIGN KEY (aircraft_config_id)
        REFERENCES public.aircraft_config (aircraft_config_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_path_data_aircraft_type_id FOREIGN KEY (aircraft_type_id)
        REFERENCES public.aircraft_type (aircraft_type_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_path_data_airline_id FOREIGN KEY (airline_id)
        REFERENCES public.airline (airline_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_path_data_path_id FOREIGN KEY (path_id)
        REFERENCES public.path (path_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_path_data_ramp_distance_id FOREIGN KEY (ramp_distance_id)
        REFERENCES public.ramp_distance (ramp_distance_id) 
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE ramp_distance
(
    ramp_distance_id integer NOT NULL,
    CONSTRAINT ramp_distance_pkey PRIMARY KEY (ramp_distance_id)
);

--Function that will be used to load data from CSV file
--NOTE: The below function is not my code

CREATE OR REPLACE FUNCTION public.load_csv_file(
	target_table text,
	csv_path text,
	col_count integer)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 0
AS $BODY$

declare

iter integer; -- dummy integer to iterate columns with
col text; -- variable to keep the column name at each iteration
col_first text; -- first column name, e.g., top left corner on a csv file or spreadsheet

begin
    set schema 'public';

    create table temp_table ();

    -- add just enough number of columns
    for iter in 1..col_count
    loop
        execute format('alter table temp_table add column col_%s text;', iter);
    end loop;

    -- copy the data from csv file
    execute format('copy temp_table from %L with delimiter '','' quote ''"'' csv ', csv_path);

    iter := 1;
    col_first := (select col_1 from temp_table limit 1);

    -- update the column names based on the first row which has the column names
    for col in execute format('select unnest(string_to_array(trim(temp_table::text, ''()''), '','')) from temp_table where col_1 = %L', col_first)
    loop
        execute format('alter table temp_table rename column col_%s to %s', iter, col);
        iter := iter + 1;
    end loop;

    -- delete the columns row
    execute format('delete from temp_table where %s = %L', col_first, col_first);

    -- change the temp table name to the name given as parameter, if not blank
    if length(target_table) > 0 then
        execute format('alter table temp_table rename to %I', target_table);
    end if;

end;

$BODY$;

ALTER FUNCTION public.load_csv_file(text, text, integer)
    OWNER TO postgres;


