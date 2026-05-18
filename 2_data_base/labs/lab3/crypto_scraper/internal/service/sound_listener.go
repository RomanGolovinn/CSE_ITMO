package service

import (
	"log"
	"os/exec"
	"time"

	"github.com/lib/pq"
)

type SoundListener struct {
	connStr string
}

func NewSoundListener(connStr string) *SoundListener {
	return &SoundListener{
		connStr: connStr,
	}
}

func (l *SoundListener) Start() {
	listener := pq.NewListener(l.connStr, 10*time.Second, time.Minute, nil)
	err := listener.Listen("sound_channel")
	if err != nil {
		panic(err)
	}

	for {
		select {
		case n := <-listener.Notify:
			if n.Extra == "true" {
				log.Println("Высокая волатильность! Запуск звукового оповещения...")
				cmd := exec.Command("mpg123", "sound.mp3")
				err := cmd.Start()
				if err != nil {
					log.Println("Не удалось воспроизвести звук:", err)
				}
			}
		case <-time.After(90 * time.Second):
			go listener.Ping()
		}
	}
}
