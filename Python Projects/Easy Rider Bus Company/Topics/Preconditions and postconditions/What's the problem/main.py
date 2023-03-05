import re

string = input()
pattern = "(?<=@)\w+"
results = re.search(pattern, string)
print(results.group())