package hamming

import (
	"errors"
)

func DecodeHamming(encoded []int) ([]int, int, error) {
	n := len(encoded)
	if n == 0 {
		return nil, 0, errors.New("пустой ввод")
	}

	r := 0
	for (1<<r)-1 < n {
		r++
	}
	if (1<<r)-1 != n {
		return nil, 0, errors.New("длина не соответствует коду Хэмминга (ожидается 2^r - 1)")
	}

	for _, b := range encoded {
		if b != 0 && b != 1 {
			return nil, 0, errors.New("биты должны быть 0 или 1")
		}
	}

	bits := make([]int, n)
	copy(bits, encoded)

	syndrome := 0
	for i := 0; i < r; i++ {
		total := 0
		for pos := 1; pos <= n; pos++ {
			if pos&(1<<i) != 0 {
				total += bits[pos-1]
			}
		}
		if total%2 == 1 {
			syndrome |= (1 << i)
		}
	}

	if syndrome != 0 {
		if syndrome > n {
			return nil, 0, errors.New("обнаружена ошибка вне диапазона — возможно, более одной ошибки")
		}
		idx := syndrome - 1
		bits[idx] = 1 - bits[idx]
	}

	var data []int
	for pos := 1; pos <= n; pos++ {
		if !isPowerOfTwo(pos) {
			data = append(data, bits[pos-1])
		}
	}

	return data, syndrome, nil
}

func isPowerOfTwo(x int) bool {
	return x > 0 && (x&(x-1)) == 0
}
