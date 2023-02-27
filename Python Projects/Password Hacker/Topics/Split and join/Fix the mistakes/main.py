text = input()
words = text.split()
for word in words:
    # finish the code here
    if word.lower().__contains__('www.'):
        print(word)
    if word.lower().__contains__('https://'):
        print(word)
    if word.lower().__contains__('http://'):
        print(word)

