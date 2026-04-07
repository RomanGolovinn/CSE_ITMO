package main

import (
	"database/sql"
	"fmt"
	"log"
	"os"
	"sync"
	"time"

	_ "github.com/lib/pq"
)

func main() {
	connStr := "host=localhost port=5432 user=s484969 password=secret dbname=ucheb sslmode=disable"

	db, err := sql.Open("postgres", connStr)
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	var wg sync.WaitGroup
	wg.Add(1)

	go func(wg *sync.WaitGroup) {
		defer wg.Done()
		content, err := os.ReadFile("mat_create.sql")
		if err != nil {
			log.Fatal("Ошибка чтения файла:", err)
		}
		_, err = db.Exec(string(content))
		if err != nil {
			log.Fatal(err)
		}

		content, err = os.ReadFile("addit_mat.sql")
		if err != nil {
			log.Fatal("Ошибка чтения файла:", err)
		}

		start := time.Now()
		for i := 0; i < 100; i++ {
			_, err = db.Exec(string(content))
			if err != nil {
				log.Fatal(err)
			}
		}
		end := time.Now()
		delta := end.Sub(start)
		fmt.Println("materialized: ", delta)
	}(&wg)

	content, err := os.ReadFile("addit_def.sql")
	if err != nil {
		log.Fatal("Ошибка чтения файла:", err)
	}

	start := time.Now()
	for i := 0; i < 100; i++ {
		_, err = db.Exec(string(content))
		if err != nil {
			log.Fatal(err)
		}
	}
	end := time.Now()
	delta := end.Sub(start)
	fmt.Println("default: ", delta)

	wg.Wait()
}
