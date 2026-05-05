DROP MATERIALIZED VIEW IF EXISTS mv_character_stats;

CREATE MATERIALIZED VIEW mv_character_stats AS
SELECT 
    c.character_id, 
    c.name, 
    COUNT(exr.character_id) AS total_expecters,
    LENGTH(m.description) / COUNT(m.description) AS av_len 
FROM character c
JOIN event_participant ep ON c.character_id = ep.character_id
JOIN expectee exe ON c.character_id = exe.character_id
JOIN expectation e ON e.expectation_id = exe.expectation_id
JOIN expecter exr ON exr.expectation_id = e.expectation_id
JOIN mindset m ON m.mindset_id = e.mindset_id
GROUP BY c.character_id, c.name
HAVING COUNT(ep.character_id) > 3;

CREATE OR REPLACE FUNCTION prevent_past_event_participation()
RETURNS TRIGGER AS $$
DECLARE
    e_date DATE;
BEGIN
    SELECT event_date INTO e_date FROM event WHERE event_id = NEW.event_id;

    IF e_date < CURRENT_DATE THEN
        RAISE EXCEPTION 'Нельзя добавить персонажа: событие % уже прошло!', NEW.event_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_event_date_trigger
BEFORE INSERT ON event_participant
FOR EACH ROW EXECUTE FUNCTION prevent_past_event_participation();