package service

import (
	"fmt"
	"time"

	"github.com/lib/pq"
)

type TradingListener struct {
	connStr string
}

func NewTradingListener(connStr string) *TradingListener {
	return &TradingListener{
		connStr: connStr,
	}
}

func (l *TradingListener) Start() {
	listener := pq.NewListener(l.connStr, 10*time.Second, time.Minute, nil)
	err := listener.Listen("trading_channel")
	if err != nil {
		panic(err)
	}

	for {
		select {
		case n := <-listener.Notify:
			fmt.Printf("Сигнал от БД: Канал=%s, Данные=%s\n", n.Channel, n.Extra)
		case <-time.After(90 * time.Second):
			go listener.Ping()
		}
	}
}
