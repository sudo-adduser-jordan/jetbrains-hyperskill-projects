def compare(numerator, denominator):
    return denominator and numerator / denominator == 0.5


a = int(input())
b = int(input())

print(bool(compare(a, b)))
