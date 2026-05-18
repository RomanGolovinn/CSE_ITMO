package client

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"strconv"

	"scraper/internal/domain"
)

type BinanceClient struct {
	baseURL string
}

func NewBinanceClient() *BinanceClient {
	return &BinanceClient{
		baseURL: "https://api.mexc.com",
	}
}

type binanceResponse struct {
	Symbol string `json:"symbol"`
	Price  string `json:"price"`
}

func (c *BinanceClient) FetchPrice(symbol string) (*domain.Ticker, error) {
	url := c.baseURL + "/api/v3/ticker/price?symbol=" + symbol

	req, err := http.NewRequest(http.MethodGet, url, nil)
	if err != nil {
		return nil, err
	}

	req.Header.Set("User-Agent", "Mozilla/5.0")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		bodyBytes, _ := io.ReadAll(resp.Body)
		return nil, fmt.Errorf("bad status: %d, body: %s", resp.StatusCode, string(bodyBytes))
	}

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
