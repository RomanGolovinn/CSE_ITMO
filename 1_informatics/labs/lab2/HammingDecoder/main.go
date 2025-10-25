package main

import (
	"bufio"
	"fmt"
	"os"
	"HammingDecode/hamming"
)

func stringToBits(s string) ([]int, error) {
	bits := make([]int, len(s))
	for i, r := range s {
		switch r {
		case '0':
			bits[i] = 0
		case '1':
			bits[i] = 1
		default:
			return nil, fmt.Errorf("недопустимый символ: %c", r)
		}
	}
	return bits, nil
}

func bitsToString(bits []int) string {
	res := make([]byte, len(bits))
	for i, b := range bits {
		res[i] = byte('0' + b)
	}
	return string(res)
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	if scanner.Scan() {
		input := scanner.Text()
		if input == "" {
			fmt.Fprintln(os.Stderr, "пустой ввод")
			os.Exit(1)
		}

		encoded, err := stringToBits(input)
		if err != nil {
			fmt.Fprintf(os.Stderr, "ошибка парсинга: %v\n", err)
			os.Exit(1)
		}

		decoded, syndrome, err := hamming.DecodeHamming(encoded)
		if err != nil {
			fmt.Fprintf(os.Stderr, "ошибка декодирования: %v\n", err)
			os.Exit(1)
		}

		fmt.Println(bitsToString(decoded))
		fmt.Printf("Ошибка в бите №%d\n", syndrome)
	}
}