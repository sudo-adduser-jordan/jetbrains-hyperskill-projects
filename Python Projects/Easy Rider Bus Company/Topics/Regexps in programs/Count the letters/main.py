
string = input()
# string = 'AAGAAGTTCC'


def char_frequency(str1):
    x = {}
    for n in str1:
        keys = x.keys()
        if n in keys:
            x[n] += 1
        else:
            x[n] = 1
    return x


for key, value in sorted(char_frequency(string).items()):
    print(f'{key}: {value}')

