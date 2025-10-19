package main

import (
	"fmt"
	"errors"
	"math"
)

const (
	maxFracDigits = 10
)

func intToNeg10(n int) string{
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

func FractionToNeg10(frac float64) (string, error) {
	if frac <= 0 {
		return "", nil
	}
	if frac >= 1 {
		return "", errors.New("fraction must be in [0, 1)")
	}

	digits := ""
	for i := 0; i < maxFracDigits; i++{
		frac *= -10
		digit := int(math.Floor(frac))

		if digit < 0{
			digit += 10
			frac += 10
		}

		digits += fmt.Sprintf("%d", digit)
		frac -= float64(digit)

		if math.Abs(frac) < 1e-12 {
			break
		}
	}

	return digits, nil
}

func FloatToNeg10(x float64) (string, error) {
	if x < 0 {
		return "", errors.New("only non-negative numbers supported")
	}

	intPart := int(math.Floor(x))
	fracPart := x - float64(intPart)

	intStr := IntToNeg10(intPart)

	if fracPart < 1e-12 {
		return intStr, nil
	}

	fracStr, err := FractionToNeg10(fracPart)
	if err != nil {
		return "", err
	}

	return intStr + "." + fracStr, nil
}

func main() {
	x := 123.987
	result, err := FloatToNeg10(x)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return
	}
	fmt.Printf("%.3f (base 10) = %s (base -10)\n", x, result)
}