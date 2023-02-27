import requests
from bs4 import BeautifulSoup

url = input()
r = requests.get(url)
soup = BeautifulSoup(r.content, 'html.parser')

h1 = soup.find('h1')
print(h1.text)
