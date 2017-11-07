--List all the flights into Denver that the distance was between 500 and 1200 miles. Display the following
--information:
--Origin city and state, airline name

select originAl.city_name as origin_city, originAl.state_name as origin_state, a.carrier_name as airline_name
from path p
join path_data d on p.path_id=d.path_id
join airport destA on p.destination_airport_id=destA.airport_id
join airport_location destAl on destA.location_id=destAl.location_id
join airport originA on p.origin_airport_id=originA.airport_id
join airport_location originAl on originA.location_id=originAl.location_id
join airline a on d.airline_id=a.airline_id
where destAl.city_name='Denver' and destAl.state_name='Colorado' and p.distance > 500 and p.distance < 1200
group by originAl.city_name, originAl.state_name, a.carrier_name