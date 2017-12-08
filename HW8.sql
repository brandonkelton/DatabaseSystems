--Follow directions from site prior to this point
copy usa.census2010 from 'D:\CSDB\HW8\Lesson3Data\census2010.csv' 
with (format csv, header true, quote '"');

--set search_path so we don't have to type the schema name all the time  :)
set search_path to usa, public;

--first training query in site instructions
select name, sub_region from states where sub_region = 'Soda';

--Select the states with a 2008 population over 10 million
select name, pop2008 from states where pop2008 > 10000000;

--Select the state capitals
select name, stateabb 
from cities 
where capital = 1;

--Select the states whose names begin with the word "New"
select name from states where left(name, 3) = 'New';

--Select the cities whose names contain the letter "z"
select name from cities where name like '%z%';

--Sort the states by their 2008 population from high to low
select name, pop2008 from states order by pop2008 desc;

--Sort the states first by soft drink name then by state name
select name, sub_region from states order by sub_region, name;

--Select the states with a 2008 population over 10 million and where the 
--majority of population refers to soft drinks as pop
select name, pop2008, sub_region 
from states
where pop2008 > 10000000 and sub_region = 'Pop';

--Select cities in the states of NY, NJ and PA (using the stateabb column)
select name, stateabb from cities where stateabb in ('US-NY', 'US-NJ', 'US-PA');

--For each state compute the percentage of the 2010 population that is white. 
select 
	s.name, 
	(c.white / c.total::double precision) as pctwhite
from states s
join census2010 c on s.state=c.state;

--Sum the 2008 state populations across the soft drink categories (i.e., What is 
--the population of the 'soda' states? Of the 'pop' states? Of the 'coke' states?)
select s.sub_region, sum(s.pop2008) as popTotal
from states s
group by s.sub_region;

--Bring together data from the states and census2010 tables, outputting the name 
--from the states table, total population from the census2010 table and geom from 
--the states table
select s.name, c.total, s.geom
from states s
join census2010 c on s.state=c.state;

--Calculate the average 2010 male population across the soft drink categories
select 
	s.sub_region, 
    avg((c.male / c.total::double precision)) as pctmale
from states s
join census2010 c on s.state=c.state
group by s.sub_region;

--*****--New schema set up and practive queries from lession--*****--

SELECT name, ST_Centroid(geom) AS centroid
FROM states
WHERE sub_region = 'Soda';

SELECT name, ST_AsText(ST_Centroid(geom)) AS centroid
FROM states
WHERE sub_region = 'Soda';

SELECT name, ST_AsText(ST_Centroid(geom)) AS centroid, ST_Area(ST_Transform(geom,2163)) AS area
FROM states
WHERE sub_region = 'Soda';

set search_path to nyc_poi, public;

CREATE TABLE nyc_poi.pts
(
    gid serial NOT NULL,
    name character varying(50),
    geom geometry(Point,4269),
    CONSTRAINT pts_pkey PRIMARY KEY (gid)
);

SELECT AddGeometryColumn('nyc_poi','pts','geom',4269,'POINT',2);

--Populate data

INSERT INTO pts (name, geom)
VALUES ('Empire State Building', ST_GeomFromText('POINT(-73.985744 40.748549)',4269));

INSERT INTO pts (name, geom)
VALUES ('Statue of Liberty', ST_GeomFromText('POINT(-74.044508 40.689229)',4269));

INSERT INTO pts (name, geom)
VALUES ('World Trade Center', ST_GeomFromText('POINT(-74.013371 40.711549)',4269));

INSERT INTO pts (name, geom)
VALUES ('Radio City Music Hall', ST_GeomFromText('POINT(-73.97988 40.760171)',4269)),
('Madison Square Garden', ST_GeomFromText('POINT(-73.993544 40.750541)',4269));

------Work------

--Repeat the steps (in Part A above) to create a new table within the nyc_poi schema
CREATE TABLE nyc_poi.lines
(
    gid serial NOT NULL,
    name character varying(50),
    CONSTRAINT lines_pkey PRIMARY KEY (gid)
);

SELECT AddGeometryColumn('nyc_poi','lines','geom',4269,'LINESTRING',2);

----Set up

INSERT INTO lines (name, geom)
VALUES ('Holland Tunnel',ST_GeomFromText('LINESTRING(
-74.036486 40.730121,
-74.03125 40.72882,
-74.011123 40.725958)',4269)),
('Lincoln Tunnel',ST_GeomFromText('LINESTRING(
-74.019921 40.767119,
-74.002841 40.759773)',4269)),
('Brooklyn Bridge',ST_GeomFromText('LINESTRING(
-73.99945 40.708231,
-73.9937 40.703676)',4269));

--Repeat the steps (in Part A above) to create a new table within the nyc_poi schema
CREATE TABLE nyc_poi.polys
(
    gid serial NOT NULL,
    name character varying(50),
    CONSTRAINT polys_pkey PRIMARY KEY (gid)
);

SELECT AddGeometryColumn('nyc_poi','polys','geom',4269,'POLYGON',2);

---Insert data

INSERT INTO polys (name, geom)
VALUES ('Central Park',ST_GeomFromText('POLYGON((
-73.973057 40.764356,
-73.981898 40.768094,
-73.958209 40.800621,
-73.949282 40.796853,
-73.973057 40.764356))',4269));

truncate table polys;

INSERT INTO polys (name, geom)
VALUES ('Central Park',ST_GeomFromText('POLYGON((
-73.973057 40.764356,
-73.981898 40.768094,
-73.958209 40.800621,
-73.949282 40.796853,
-73.973057 40.764356),
(-73.966681 40.785221,
-73.966058 40.787674,
-73.965586 40.788064,
-73.9649 40.788291,
-73.963913 40.788194,
-73.963333 40.788291,
-73.962539 40.788259,
-73.962153 40.788389,
-73.96181 40.788714,
-73.961359 40.788909,
-73.960887 40.788925,
-73.959986 40.788649,
-73.959492 40.788649,
-73.958913 40.78873,
-73.958269 40.788974,
-73.957797 40.788844,
-73.957497 40.788568,
-73.957497 40.788259,
-73.957776 40.787739,
-73.95784 40.787057,
-73.957819 40.786569,
-73.960801 40.782394,
-73.961145 40.78215,
-73.961638 40.782036,
-73.962518 40.782199,
-73.963076 40.78267,
-73.963677 40.783661,
-73.965694 40.784457,
-73.966681 40.785221)
)',4269));

----Create "mixed" table
CREATE TABLE nyc_poi.mixed
(
    gid serial NOT NULL,
    name character varying(50),
    CONSTRAINT mixed_pkey PRIMARY KEY (gid)
);

SELECT AddGeometryColumn('nyc_poi','mixed','geom',4269,'GEOMETRY',2);

INSERT INTO mixed (name, geom)
VALUES ('Empire State Building', ST_GeomFromText('POINT(-73.985744 40.748549)',4269)),
('Statue of Liberty', ST_GeomFromText('POINT(-74.044508 40.689229)',4269)),
('World Trade Center', ST_GeomFromText('POINT(-74.013371 40.711549)',4269)),
('Radio City Music Hall', ST_GeomFromText('POINT(-73.97988 40.760171)',4269)),
('Madison Square Garden', ST_GeomFromText('POINT(-73.993544 40.750541)',4269)),
('Holland Tunnel',ST_GeomFromText('LINESTRING(
-74.036486 40.730121,
-74.03125 40.72882,
-74.011123 40.725958)',4269)),
('Lincoln Tunnel',ST_GeomFromText('LINESTRING(
-74.019921 40.767119,
-74.002841 40.759773)',4269)),
('Brooklyn Bridge',ST_GeomFromText('LINESTRING(
-73.99945 40.708231,
-73.9937 40.703676)',4269)),
('Central Park',ST_GeomFromText('POLYGON((
-73.973057 40.764356,
-73.981898 40.768094,
-73.958209 40.800621,
-73.949282 40.796853,
-73.973057 40.764356))',4269));

