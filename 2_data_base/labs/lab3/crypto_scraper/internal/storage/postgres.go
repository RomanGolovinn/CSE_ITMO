package storage

import (
	"database/sql"

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
