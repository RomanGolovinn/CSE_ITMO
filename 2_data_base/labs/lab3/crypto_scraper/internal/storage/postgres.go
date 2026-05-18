package storage

import (
	"database/sql"
	"fmt"
	"time"

	"scraper/internal/domain"
)

type PostgresStorage struct {
	db *sql.DB
}

func NewPostgresStorage(db *sql.DB) *PostgresStorage {
	return &PostgresStorage{
		db: db,
	}
}

func (s *PostgresStorage) SavePrice(ticker *domain.Ticker) error {
	query := `INSERT INTO crypto_prices (symbol, price) VALUES ($1, $2)`

	_, err := s.db.Exec(query, ticker.Symbol, ticker.Price)

	return err
}

func (s *PostgresStorage) SavePriceDistributed(ticker *domain.Ticker) error {
	tx, err := s.db.Begin()
	if err != nil {
		return err
	}

	query := `INSERT INTO crypto_prices (symbol, price) VALUES ($1, $2)`
	if _, err := tx.Exec(query, ticker.Symbol, ticker.Price); err != nil {
		tx.Rollback()
		return err
	}

	txID := fmt.Sprintf("tx_crypto_%d", time.Now().UnixNano())

	prepareQuery := fmt.Sprintf("PREPARE TRANSACTION '%s'", txID)
	if _, err := tx.Exec(prepareQuery); err != nil {
		return err
	}

	commitQuery := fmt.Sprintf("COMMIT PREPARED '%s'", txID)
	if _, err := s.db.Exec(commitQuery); err != nil {
		return err
	}

	return nil
}
