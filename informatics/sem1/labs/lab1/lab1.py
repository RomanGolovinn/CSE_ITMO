import math

def to_base_neg10_int(n: int) -> str:
    if n == 0:
        return "0"
    base = -10
    res = ""
    while n != 0:
        remainder = n % base
        n //= base
        if remainder < 0:
            remainder += -base
            n += 1
        res = str(remainder) + res
    return res

def to_base_neg10_frac(frac: float, precision: int = 5) -> str:
    res = ""
    for _ in range(precision):
        frac *= -10
        digit = math.floor(frac)
        if digit < 0:
            digit += 10
            frac += 1
        res += str(digit)
        frac -= digit
    return res.rstrip("0")

def to_base_neg10(num: float, precision: int = 5) -> str:
    int_part = math.floor(num)
    frac_part = num - int_part
    int_str = to_base_neg10_int(int_part)
    if frac_part > 0:
        frac_str = to_base_neg10_frac(frac_part, precision)
        if frac_str:
            return f"{int_str}.{frac_str}"
    return int_str

print(to_base_neg10(float(input())))