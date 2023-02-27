from time import time


def catalan(n):
    if n <= 1:
        return 1
    res = 0
    for i in range(n):
        res += catalan(i) * catalan(n-i-1)
    return res


start = str(time())
for i in range(16):
    catalan(i)
end = str(time())

message = f'It took {end} - {start} seconds!'
