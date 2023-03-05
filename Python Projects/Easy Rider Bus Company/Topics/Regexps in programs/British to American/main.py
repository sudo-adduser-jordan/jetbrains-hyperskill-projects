import re

string = input()
# string = 'I love this colour!'

pattern = re.compile(r'ou')

print(re.sub(pattern, 'o', string))
