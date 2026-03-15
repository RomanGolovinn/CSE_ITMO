CREATE TABLE character(
    character_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
CREATE TABLE location(
    location_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    landscape_type VARCHAR(100),
    x DOUBLE PRECISION NOT NULL,
    y DOUBLE PRECISION NOT NULL,
    z DOUBLE PRECISION,
    planet VARCHAR(100)
);
CREATE TABLE event(
    event_id SERIAL PRIMARY KEY,
    event_type VARCHAR(100),
    event_date DATE,
    location_id INTEGER REFERENCES location(location_id)
);
CREATE TABLE event_participant(
    event_id INTEGER REFERENCES event(event_id),
    character_id INTEGER REFERENCES character(character_id)
);
CREATE TABLE mindset(
    mindset_id SERIAL PRIMARY KEY,
    description TEXT,
    is_common BOOLEAN
);
CREATE TABLE character_state(
    state_id SERIAL PRIMARY KEY,
    character_id INTEGER REFERENCES character(character_id),
    mindset_id INTEGER REFERENCES mindset(mindset_id),
    observed_at INTERVAL
);
CREATE TABLE expectation(
    expectation_id SERIAL PRIMARY KEY,
    mindset_id INTEGER REFERENCES mindset(mindset_id)
);
CREATE TABLE expectee(
    expectee_id SERIAL PRIMARY KEY,
    character_id INTEGER REFERENCES character(character_id),
    expectation_id INTEGER REFERENCES expectation(expectation_id)
);
CREATE TABLE expecter(
    expecter_id SERIAL PRIMARY KEY,
    character_id INTEGER REFERENCES character(character_id),
    expectation_id INTEGER REFERENCES expectation(expectation_id)
);


INSERT INTO character (name) VALUES ('Alvin'), ('Jeserac'), ('Khedron');

INSERT INTO location (name, landscape_type, x, y, z, planet) VALUES ('Castle', 'desert', 300.005, 50, -456.789, 'Earth');

INSERT INTO event (event_type, event_date, location_id) VALUES ('meeting', '220025-01-01', 1);

INSERT INTO mindset (description, is_common) VALUES ('он сильно переменился со времени их последней встречи в башне Лоранна', true);

INSERT INTO expectation (mindset_id) VALUES (1);


INSERT INTO event_participant (event_id, character_id) VALUES (1, 1), (1, 2), (1, 3);

INSERT INTO character_state (character_id, mindset_id, observed_at) VALUES
(1, NULL, NULL), (2, 1, '2 months'), (3, NULL, NULL);

INSERT INTO expectee (character_id, expectation_id) VALUES (2, 1), (3, 1);

INSERT INTO expecter (character_id, expectation_id) VALUES (1, 1);


select c.character_id, c.name, count(exr.character_id) as total_expecters,
lenght(m.description)/count(m.dcription) as av_len from character c
join event_participant ep on c.character_id = ep.character_id
join expectee exe on c.character_id = exe.character_id
join expectation e on e.expectation_id = exe.expectation_id
join expecter exr on exr.expectation_id = e.expectation_id
join mindset m on m.mindset_id = e.mindset_id
group by c.character_id
having count(ep.character_id) > 3;

