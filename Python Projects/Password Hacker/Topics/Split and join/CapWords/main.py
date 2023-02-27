string_one = input()
string_two = ''
if not string_one.__contains__('_'):
    print(string_one.capitalize())
else:
    words = string_one.split('_')
    for word in words:
        word = word.capitalize()
        string_two += word
print(string_two)