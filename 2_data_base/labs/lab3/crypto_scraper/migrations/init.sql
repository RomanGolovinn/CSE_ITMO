CREATE TABLE IF NOT EXISTS crypto_prices (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    price NUMERIC(15, 2) NOT NULL,
    fetched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS price_alerts (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    price NUMERIC(15, 2) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION check_btc_moon()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.price > 80000.00 THEN
        INSERT INTO price_alerts (symbol, price, message)
        VALUES (NEW.symbol, NEW.price, 'ВНИМАНИЕ! Биткоин пробил 80k!');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS btc_alert_trigger ON crypto_prices;

CREATE TRIGGER btc_alert_trigger
AFTER INSERT ON crypto_prices
FOR EACH ROW EXECUTE FUNCTION check_btc_moon();