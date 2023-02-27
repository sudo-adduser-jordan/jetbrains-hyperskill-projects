file = open('sums.txt', 'r')
for line in file:
    ints = [int(x) for x in line.split()]
    print(ints[0] + ints[1])
file.close()
