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
	dbPort := os.Getenv("DB_PORT")
	dbUser := os.Getenv("DB_USER")
	dbPassword := os.Getenv("DB_PASSWORD")
	dbName := os.Getenv("DB_NAME")

	connStr := fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		dbHost, dbPort, dbUser, dbPassword, dbName)

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

	tradingListener := service.NewTradingListener(connStr)
	go tradingListener.Start()

	scraper := service.NewScraperService(apiClient, dbStorage, "BTCUSDT", 5*time.Second)
	scraper.Start()
}
