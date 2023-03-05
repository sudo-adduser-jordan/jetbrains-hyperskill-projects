import re 

pets = input()
# pets = 'I love Dogs and cats!'

template = r'dog|cat|parrot|hamster'

match = re.findall(template, pets, flags=re.IGNORECASE)

print(match)


