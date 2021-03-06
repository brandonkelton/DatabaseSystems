
--Create Countries Table
CREATE TABLE Countries (
    countryId INT,
    name VARCHAR(20),
    latitude INT,
    longitude INT,
    area INT,
    population INT,
    gdp INT,
    gdpYear INT
);

--Create Borders Table
CREATE TABLE Borders (
    country1Id INT,
    country2Id INT
);

--Insert Country Records
INSERT INTO Countries
(countryId, name, latitude, longitude, area, population, gdp, gdpYear)
VALUES
(1, 'Germany', 51, 9, 357022, 80594017, 48200, 2016),
(2, 'Netherlands', 52, 5, 41543, 17084719, 50800, 2016),
(3, 'Belgium', 50, 4, 30528, 11491346, 44900, 2016),
(4, 'Luxembourg', 49, 6, 2586, 594130, 102000, 2016),
(5, 'Poland', 52, 20, 312685, 38476269, 27700, 2016),
(6, 'Czech Republic', 49, 15, 78867, 10674723, 33200, 2016),
(7, 'Austria', 47, 13, 83871, 8754413, 47900, 2016),
(8, 'France', 46, 2, 643801, 67106161, 42400, 2016),
(9, 'Switzerland', 47, 8, 41277, 8236303, 59400, 2016);

--Insert Border Records
INSERT INTO Borders 
(country1Id, country2Id)
VALUES
(1, 7),
(1, 3),
(1, 6),
(1, 8),
(1, 4),
(1, 2),
(1, 5),
(1, 9),
(2, 3),
(2, 1),
(3, 8),
(3, 1),
(3, 4),
(3, 2),
(4, 3),
(4, 8),
(4, 1),
(5, 6),
(5, 1),
(6, 7),
(6, 1),
(6, 5),
(7, 6),
(7, 1),
(7, 9),
(8, 3),
(8, 1),
(8, 4),
(8, 9),
(9, 7),
(9, 8),
(9, 1);

--Select all countries bordering Germany
SELECT
    C.name "CountryGermanBorder"
FROM
    Borders B
JOIN
    Countries C ON B.country2Id=C.countryId
WHERE
    B.country1Id=1;

--Select all countries with population over 35M
SELECT
    C.name "CountryOver35M", 
    C.population Population
FROM
    Countries C
WHERE
    C.population > 35000000;

--Select all countries with pop. > 35M and border Germany
SELECT
    C.name "CountryGermanBorderOver35M",
    C.population Population
FROM 
    Borders B
JOIN
    Countries C ON B.country2Id=C.countryId
WHERE
    B.country1Id=1
    AND C.population > 35000000;

DROP TABLE Borders;
DROP TABLE Countries;