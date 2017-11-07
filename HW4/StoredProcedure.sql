--Write a PL/pgSQL stored procedure that analyzes the various flights of the airlines. 
--The report should display:
--Airline name, total number of flights, average distance, ratio of passengers to distance, 
--ratio of freight to distance, correlation coefficient for passengers to freight, 
--correlation coefficient for passengers to distance.

CREATE OR REPLACE FUNCTION analyzeData() 
RETURNS TABLE 
(
    airline varchar(50), 
    total_flights int, 
    average_distance int, 
    ratio_passengers_distance real,
    ratio_freight_distance real,
    corr_passengers_freight real,
    corr_passengers_distance real
) AS $$

BEGIN

return query select 
	flights_performed.airline::varchar(50), 
    total_flights::int,
    average_distance::int,
    ratio_passengers_distance::real,
    ratio_freight_distance::real,
    corr_passengers_freight::real,
    corr_passengers_distance::real
from
(
	select 
    	airline.carrier_name as airline, 
        sum(depart.performed) as total_flights
    from
    	airline
    join 
    	airline_airport_departures depart on airline.airline_id=depart.airline_id
    group by
    	airline.carrier_name
) flights_performed

join

(
	select 
    	airline.carrier_name as airline,
        round(avg(p.distance), 0) as average_distance
    from
    	airline
    join
    	path_data pd on airline.airline_id=pd.airline_id
    join
    	path p on pd.path_id = p.path_id
    group by
    	airline.carrier_name
) average_distance on flights_performed.airline=average_distance.airline
	
join

(
	select 
    	airline.carrier_name as airline, 
        (sum(pd.passengers) / sum(p.distance)) ratio_passengers_distance
    from
    	airline
    join
    	path_data pd on pd.airline_id=airline.airline_id
    join
    	path p on pd.path_id=p.path_id
    group by
    	airline.carrier_name
) passengers_to_distance on average_distance.airline=passengers_to_distance.airline
    
join

(
	select 
    	airline.carrier_name as airline, 
        (sum(pd.freight) / sum(p.distance)) ratio_freight_distance
    from
    	airline
    join
    	path_data pd on pd.airline_id=airline.airline_id
    join
    	path p on pd.path_id=p.path_id
    group by
    	airline.carrier_name
) freight_to_distance on passengers_to_distance.airline=freight_to_distance.airline
    
join

(
	select 
    	airline.carrier_name as airline, 
        round(corr(pd.passengers, pd.freight)::decimal, 2) corr_passengers_freight
    from
    	airline
    join
    	path_data pd on pd.airline_id=airline.airline_id
    group by
    	airline.carrier_name
) corr_passengers_to_freight on freight_to_distance.airline=corr_passengers_to_freight.airline
    
join

(
	select 
    	airline.carrier_name as airline, 
        round(corr(pd.passengers, p.distance)::decimal, 2) corr_passengers_distance
    from
    	airline
    join
    	path_data pd on pd.airline_id=airline.airline_id
    join
    	path p on pd.path_id=p.path_id
    group by
    	airline.carrier_name
) corr_passengers_to_distance on corr_passengers_to_freight.airline=corr_passengers_to_distance.airline;

    
END; $$

LANGUAGE plpgsql;