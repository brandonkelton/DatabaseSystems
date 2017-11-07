--Same information and format but freight, not passengers

select l.city_name, l.state_name, a.airport_code, al.carrier_name, sum(d.freight) as freight
from path p
join airport a on p.origin_airport_id = a.airport_id
join airport_location l on a.location_id=l.location_id
join path_data d on p.path_id=d.path_id
join airline al on d.airline_id=al.airline_id
where l.city_name='Denver' and l.state_name='Colorado'
group by l.city_name, l.state_name, a.airport_code, al.carrier_name


