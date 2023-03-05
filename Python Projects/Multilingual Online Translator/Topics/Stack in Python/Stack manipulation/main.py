x = int(input())
lst = []

for i in range(x):
    a = input().split()
    if a[0] == "PUSH":
        lst.append(a[1])

    elif a[0] == "POP":

        lst.pop()

for i in reversed(lst):
    print(i)
