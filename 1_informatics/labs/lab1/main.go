package main

import (
	"fmt"
	"math"
	"strconv"
	"bufio"
	"os"
)

const (
	maxFracDigits = 10
)

func IntToNeg10(n int) string{
	if n == 0{
		return "0"
	}
	digits := ""
	for n != 0 {
		remainder := n % (-10)
		n = n / (-10)
		if remainder < 0 {
			remainder += 10
			n++
		}
		digits = fmt.Sprintf("%d", remainder) + digits
	}
	return digits
}

func FractionToNeg10(frac float64) (float64, error) {
	var digits float64 = 0.0
	fracStr := fmt.Sprintf("%.10f", frac)

	if len(fracStr) <= 2 {
		return 0, nil
	}
	fracDigits := fracStr[2:]

	for i := 0; i < len(fracDigits); i++{
		digit := int(fracDigits[i] - '0')
		if i%2 == 0{
			digits += (10 - float64(digit)) * math.Pow(10, float64((-1)*i - 1))
			digits += 1 * math.Pow(10, float64((-1)*i))
		}else{
			digits += float64(digit) * math.Pow(10, float64((-1)*i - 1))
		}
	}
	digits = math.Round(digits*1e5)/1e5
	return digits, nil
}

func FloatToNeg10(x float64) (float64, error) {

	intPart := int(math.Floor(x))
	intNeg10, err := strconv.ParseFloat(IntToNeg10(intPart), 64)
	
	fracNeg10, err := FractionToNeg10(x - float64(intPart))

	return intNeg10 + fracNeg10, err
}

func main() {
	fmt.Println(
		"Из-за специфики вычислений и системы счисления числа с плавающей точкой",
		"округляются до 5 знаков после запятой")
	fmt.Println("При вычисленияе чисел с плавающей точкой результат может быть не точный")
	fmt.Print("Введите число в 10 системе счисления: ")
	
	scanner := bufio.NewScanner(os.Stdin)
	if scanner.Scan() {
		input := scanner.Text()
		x, err := strconv.ParseFloat(input, 64)
		if err != nil {
			fmt.Println("Ошибка: введено не число")
			return
		}

		result, err := FloatToNeg10(x)
		if err != nil {
			fmt.Printf("Error: %v\n", err)
			return
		}
		fmt.Println("Число в -10 системе счисления: ", result)
	} else {
		fmt.Println("Не удалось прочитать ввод")
	}
}