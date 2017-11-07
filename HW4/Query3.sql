--How many passengers arrived in Colorado from outside Colorado? Display the following information in
--this format:
--Origin city name & state, destination city in colorado, airline name

select 
	originAl.city_name as origin_city, 
    originAl.state_name as origin_state, 
    destAl.city_name as colorado_dest_city, 
    a.carrier_name as airline,
    sum(d.passengers) as passengers
from path p
join airport originA on p.origin_airport_id=originA.airport_id
join airport destA on p.destination_airport_id=destA.airport_id
join airport_location originAl on originA.location_id=originAl.location_id
join airport_location destAl on destA.location_id=destAl.location_id
join path_data d on p.path_id=d.path_id
join airline a on d.airline_id=a.airline_id
where destAl.state_name='Colorado' and not originAl.state_name='Colorado'
group by originAl.city_name, originAl.state_name, destAl.city_name, a.carrier_name


