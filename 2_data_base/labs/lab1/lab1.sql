CREATE TABLE character(
    character_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
CREATE TABLE location(
    location_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    landscape_type VARCHAR(100)
);
CREATE TABLE event(
    event_id SERIAL PRIMARY KEY,
    event_type VARCHAR(100),
    event_date TIMESTAMP,
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
    observed_at TIMESTAMP
);
CREATE TABLE expectee(
    character_id INTEGER PRIMARY KEY REFERENCES character(character_id),
    name VARCHAR(100) NOT NULL
);
CREATE TABLE expecter(
    character_id INTEGER PRIMARY KEY REFERENCES character(character_id),
    name VARCHAR(100) NOT NULL
);
CREATE TABLE expectation(
    expectation_id SERIAL PRIMARY KEY,
    observer_id INTEGER REFERENCES expecter(character_id),
    target_character INTEGER REFERENCES expectee(character_id),
    mindset_id INTEGER REFERENCES mindset(mindset_id)
);
