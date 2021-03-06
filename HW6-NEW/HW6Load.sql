insert into airreserve.airline (airline_code, airline_name) values
('AC', 'Air Canada'),
('UA', 'United'),
('BA', 'British Airways')

insert into airreserve.city (city_code, city, state_province, country) values
('TOR', 'Toronto', 'Ontario', 'Canada'),
('MON', 'Montreal', 'Quebec', 'Canada'),
('JFK', 'New York', 'New York', 'USA'),
('ORD', 'Chicago', 'Illinois', 'USA'),
('LCY', 'London', 'England', 'United Kingdom'),
('EDI', 'Edinburgh', 'England', 'United Kingdom'),
('PAR', 'Paris', 'Île-de-France', 'France'),
('NCE', 'Nice', 'Cote D''Azur', 'France')

insert into airreserve.flight (flight_number, origin_city_code, dest_city_code, length_hours, length_minutes) values
('1111','TOR','MON',10,0),
('1112','TOR','JFK',2,14),
('1113','TOR','ORD',9,8),
('1114','TOR','LCY',18,4),
('1115','TOR','EDI',19,21),
('1116','MON','TOR',14,3),
('1117','MON','EDI',18,55),
('1118','JFK','TOR',3,32),
('1119','JFK','ORD',8,20),
('1120','ORD','TOR',7,53),
('1121','ORD','JFK',6,42),
('1122','LCY','TOR',16,23),
('1123','LCY','MON',23,2),
('1124','LCY','EDI',0,57),
('1125','LCY','PAR',18,3),
('1126','LCY','NCE',17,42),
('1127','EDI','TOR',17,5),
('1128','EDI','MON',24,5),
('1129','EDI','LCY',0,42),
('1130','EDI','PAR',14,5),
('1131','EDI','NCE',0,42),
('1132','PAR','LCY',14,3),
('1133','PAR','EDI',12,33),
('1134','PAR','NCE',0,45),
('1135','NCE','LCY',15,2),
('1136','NCE','EDI',14,43),
('1137','NCE','PAR',0,47)

insert into airreserve.flight_instance
(airline_code, flight_number, departure_date, departure_hour, departure_minute, arrival_date, arrival_hour, arrival_minute) values
('AC', '1111', '12/11/2017', 8, 0, '12/11/2017', 10, 0),
('UA', '1112', '12/12/2017', 11, 30, '12/12/2017', 14, 0),
('UA', '1113', '12/11/2017', 10, 44, '12/11/2017', 17, 23),
('UA', '1114', '12/14/2017', 15, 30, '12/14/2017', 19, 54),
('BA', '1114', '12/23/2017', 8, 0, '12/23/2017', 10, 0),
('BA', '1115', '12/17/2017', 20, 55, '12/18/2017', 4, 33),
('AC', '1116', '12/11/2017', 9, 45, '12/11/2017', 19, 50),
('BA', '1117', '12/15/2017', 18, 30, '12/16/2017', 2, 45),
('UA', '1118', '12/16/2017', 4, 5, '12/16/2017', 12, 32),
('UA', '1119', '12/17/2017', 8, 0, '12/17/2017', 10, 0),
('UA', '1120', '12/18/2017', 8, 0, '12/18/2017', 10, 0),
('UA', '1121', '12/19/2017', 12, 32, '12/19/2017', 14, 35),
('UA', '1122', '12/20/2017', 11, 55, '12/20/2017', 18, 50),
('AC', '1123', '12/20/2017', 13, 50, '12/20/2017', 22, 40),
('BA', '1124', '12/18/2017', 10, 0, '12/18/2017', 12, 0),
('BA', '1125', '12/15/2017', 14, 20, '12/15/2017', 19, 0),
('BA', '1126', '12/22/2017', 7, 0, '12/22/2017', 9, 14),
('AC', '1127', '12/31/2017', 17, 15, '12/31/2017', 23, 10),
('AC', '1128', '12/8/2017', 12, 30, '12/8/2017', 17, 30),
('BA', '1129', '12/21/2017', 8, 30, '12/21/2017', 10, 45),
('BA', '1130', '12/24/2017', 15, 0, '12/24/2017', 19, 0),
('BA', '1131', '12/28/2017', 19, 0, '12/28/2017', 21, 15),
('BA', '1132', '12/7/2017', 8, 0, '12/7/2017', 10, 0),
('BA', '1133', '12/18/2017', 12, 0, '12/18/2017', 15, 0),
('BA', '1134', '12/21/2017', 15, 40, '12/22/2017', 2, 30),
('BA', '1135', '12/2/2017', 8, 0, '12/2/2017', 10, 0),
('BA', '1136', '12/3/2017', 8, 0, '12/3/2017', 10, 0),
('BA', '1137', '12/4/2017', 8, 0, '12/4/2017', 10, 0)