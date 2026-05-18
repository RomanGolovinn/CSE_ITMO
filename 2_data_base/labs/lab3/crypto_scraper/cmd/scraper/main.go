package main

import (
	"database/sql"
	"fmt"
	"log"
	"os"
	"time"

	_ "github.com/lib/pq"

	"scraper/internal/client"
	"scraper/internal/service"
	"scraper/internal/storage"
)

func main() {
	dbHost := os.Getenv("DB_HOST")
	if dbHost == "" {
		dbHost = "localhost"
	}

	connStr := fmt.Sprintf("host=%s port=5432 user=s484969 password=secret dbname=ucheb sslmode=disable", dbHost)

	var db *sql.DB
	var err error

	for i := 0; i < 5; i++ {
		db, err = sql.Open("postgres", connStr)
		if err == nil {
			err = db.Ping()
			if err == nil {
				break
			}
		}
		log.Println("Ожидание запуска базы данных...")
		time.Sleep(3 * time.Second)
	}

	if err != nil {
		log.Fatal("Не удалось подключиться к БД:", err)
	}
	defer db.Close()

	log.Println("Успешное подключение к PostgreSQL")

	apiClient := client.NewBinanceClient()
	dbStorage := storage.NewPostgresStorage(db)

	scraper := service.NewScraperService(apiClient, dbStorage, "BTCUSDT", 5*time.Second)
	scraper.Start()
}
