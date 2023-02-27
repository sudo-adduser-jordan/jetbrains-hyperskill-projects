# work with a list from this variable
numbers = [int(n) for n in input()]

# change the next two lines
less_than_5 = [x for x in numbers if x < 5]
greater_than_5 = [x for x in numbers if x > 5]

# printing your results
print(less_than_5)
print(greater_than_5)