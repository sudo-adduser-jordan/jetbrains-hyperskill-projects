n = int(input())

my_stack = []

for i in range(n):
    my_stack.append(input())

for i in range(n):
    print(my_stack.pop())
