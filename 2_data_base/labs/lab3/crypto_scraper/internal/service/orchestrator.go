package service

import (
	"fmt"
	"log"
	"time"

	"scraper/internal/client"
	"scraper/internal/storage"
)

type ScraperService struct {
	apiClient *client.BinanceClient
	dbStorage *storage.PostgresStorage
	symbol    string
	interval  time.Duration
}

func NewScraperService(api *client.BinanceClient, db *storage.PostgresStorage, sym string, intv time.Duration) *ScraperService {
	return &ScraperService{
		apiClient: api,
		dbStorage: db,
		symbol:    sym,
		interval:  intv,
	}
}

func (s *ScraperService) Start() {
	log.Printf("Запуск скрапера для %s. Интервал: %v\n", s.symbol, s.interval)
	t := time.NewTicker(s.interval)
	defer t.Stop()

	for {
		s.processTick()
		<-t.C
	}
}

func (s *ScraperService) processTick() {
	priceData, err := s.apiClient.FetchPrice(s.symbol)
	if err != nil {
		log.Println("Ошибка получения данных от API:", err)
		return
	}

	fmt.Printf("[%s] Получена цена: %.2f$\n", priceData.Symbol, priceData.Price)

	err = s.dbStorage.SavePrice(priceData)
	if err != nil {
		log.Println("Ошибка сохранения:", err)
		return
	}
}
