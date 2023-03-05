import re

user_name = input()
template = r'\d'

match = re.match(template, user_name)

if match:
    print('Oops! The username has to start with a letter.')
else:
    print('Thank you!')
