import requests
from bs4 import BeautifulSoup

sTitles = []

url = input()
# url = "http://web.archive.org/web/20201201053628/https://www.who.int/health-topics"
r = requests.get(url)
soup = BeautifulSoup(r.content, 'html.parser')

a = soup.find_all('a')
for i in a:
    link = str(i.get("href"))
    if link.__contains__("topics") or link.__contains__("entity"):
        if i.text.startswith("S") and len(i.text) > 1:
            sTitles.append(i.text)
print(sTitles)
