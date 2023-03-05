import re

string = input()
# string = '01/12/2018'

pattern = r'(((0[1-9])|([12][0-9])|(3[01]))\/((0[0-9])|(1[012]))\/((20[012]\d|19\d\d)|(1\d|2[0123])))'

if re.match(pattern, string):
    print(True)
else:
    print(False)
