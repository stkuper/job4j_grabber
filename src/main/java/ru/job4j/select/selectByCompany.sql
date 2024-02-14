CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

INSERT INTO company(id, name) VALUES
(1, 'Daz'),
(2, 'Gaz'),
(3, 'Vaz'),
(4, 'Maz'),
(5, 'Raz');

INSERT INTO person(id, name, company_id) VALUES
(1, 'Misha', 5),
(2, 'Sasha', 4),
(3, 'Lesha', 3),
(4, 'Masha', 2),
(5, 'Kostya', 1),
(6, 'Sveta', 5),
(7, 'Elena', 5),
(8, 'Petr', 3),
(9, 'Stas', 3),
(10, 'Valya', 1);

SELECT * FROM company;
SELECT * FROM person;

SELECT p.name as person_name,
	   c.name as company_name
FROM company c
JOIN person p ON p.company_id = c.id
WHERE c.id != 5;

SELECT c.name as company_name,
	   COUNT(p.id) as person_count
FROM company c
JOIN person p ON p.company_id = c.id
GROUP BY c.name
ORDER BY person_count DESC, c.name;

SELECT c.name as company_name,
	   COUNT(p.id) as person_count
FROM company c
JOIN person p ON p.company_id = c.id
GROUP BY c.name
HAVING COUNT(p.id) = (SELECT COUNT(id)
					  FROM person
					  GROUP BY company_id
					  ORDER BY COUNT(id) DESC
					  LIMIT 1);