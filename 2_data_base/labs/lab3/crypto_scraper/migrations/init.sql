CREATE TABLE IF NOT EXISTS crypto_prices (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    price NUMERIC(15, 2) NOT NULL,
    percentage_change NUMERIC(5, 2) DEFAULT 0.00,
    fetched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trading_rules (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) UNIQUE NOT NULL,
    buy_price NUMERIC(15, 2) NOT NULL,
    sell_price NUMERIC(15, 2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

INSERT INTO trading_rules (symbol, buy_price, sell_price)
VALUES ('BTCUSDT', 75000.00, 76900.00)
ON CONFLICT (symbol) DO NOTHING;

CREATE OR REPLACE FUNCTION check_trading_rules()
RETURNS TRIGGER AS $$
DECLARE
    rule RECORD;
    prev_price NUMERIC(15, 2);
    calc_change NUMERIC(5, 2) := 0.00;
    payload TEXT;
BEGIN
    SELECT price INTO prev_price FROM crypto_prices 
    WHERE symbol = NEW.symbol AND id < NEW.id 
    ORDER BY id DESC LIMIT 1;

    IF prev_price IS NOT NULL AND prev_price > 0 THEN
        calc_change := ((NEW.price - prev_price) / prev_price) * 100;
    END IF;

    NEW.percentage_change := calc_change;

    IF ABS(calc_change) >= 0.01 THEN
        PERFORM pg_notify('sound_channel', 'true');
    END IF;

    SELECT * INTO rule FROM trading_rules WHERE symbol = NEW.symbol AND is_active = TRUE LIMIT 1;
    IF FOUND THEN
        IF NEW.price <= rule.buy_price THEN
            payload := '{"action": "BUY", "symbol": "' || NEW.symbol || '", "price": ' || NEW.price || '}';
            PERFORM pg_notify('trading_channel', payload);
        ELSIF NEW.price >= rule.sell_price THEN
            payload := '{"action": "SELL", "symbol": "' || NEW.symbol || '", "price": ' || NEW.price || '}';
            PERFORM pg_notify('trading_channel', payload);
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS btc_trade_trigger ON crypto_prices;

CREATE TRIGGER btc_trade_trigger
BEFORE INSERT ON crypto_prices
FOR EACH ROW EXECUTE FUNCTION check_trading_rules();