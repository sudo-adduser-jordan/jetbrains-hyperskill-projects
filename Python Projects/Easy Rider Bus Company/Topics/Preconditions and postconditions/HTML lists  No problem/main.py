import re

string = input()
# string = '<li>Sister</li> <li>Father</li> <li>Mother-in-law</li>'
pattern = r'<li>(.*?)</li>'

result = re.findall(pattern, string)
for i in result:
    print(i)
