package client

import (
	"encoding/json"
	"net/http"
	"strconv"

	"scraper/internal/domain"
)

type BinanceClient struct {
	baseURL string
}

func NewBinanceClient() *BinanceClient {
	return &BinanceClient{
		baseURL: "https://api.binance.com",
	}
}

type binanceResponse struct {
	Symbol string `json:"symbol"`
	Price  string `json:"price"`
}

func (c *BinanceClient) FetchPrice(symbol string) (*domain.Ticker, error) {
	url := c.baseURL + "/api/v3/ticker/price?symbol=" + symbol

	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var apiResp binanceResponse
	if err := json.NewDecoder(resp.Body).Decode(&apiResp); err != nil {
		return nil, err
	}

	price, err := strconv.ParseFloat(apiResp.Price, 64)
	if err != nil {
		return nil, err
	}

	return &domain.Ticker{
		Symbol: apiResp.Symbol,
		Price:  price,
	}, nil
}
