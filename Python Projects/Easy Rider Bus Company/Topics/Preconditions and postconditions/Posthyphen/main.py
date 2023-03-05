import re

string = input()

pattern = r'(?<=-)\w+'

result = re.search(pattern, string)

print(result.group(0))


