--How many passengers departed Colorado on all airlines? Display the following information in this format:
--Complete city name, complete state name (not the 2 character abbreviation), Airport name, Airport code (the 3
--character code), the complete airline name, and the number of passengers.

select l.city_name, l.state_name, a.airport_code, al.carrier_name, sum(d.passengers) as passengers
from path p
join airport a on p.origin_airport_id = a.airport_id
join airport_location l on a.location_id=l.location_id
join path_data d on p.path_id=d.path_id
join airline al on d.airline_id=al.airline_id
where l.city_name='Denver' and l.state_name='Colorado'
group by l.city_name, l.state_name, a.airport_code, al.carrier_name


