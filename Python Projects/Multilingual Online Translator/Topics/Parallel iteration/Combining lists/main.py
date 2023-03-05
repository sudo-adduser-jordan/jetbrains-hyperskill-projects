# please do not modify the following code
numbers, word = input().split()
numbers = list(numbers)
word = list(word)

zip_iterator = zip(numbers, word)
print(list(zip_iterator))
