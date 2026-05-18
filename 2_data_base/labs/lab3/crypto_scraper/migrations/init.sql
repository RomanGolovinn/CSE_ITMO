CREATE TABLE IF NOT EXISTS crypto_prices (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    price NUMERIC(15, 2) NOT NULL,
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
    payload TEXT;
BEGIN
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
AFTER INSERT ON crypto_prices
FOR EACH ROW EXECUTE FUNCTION check_trading_rules();