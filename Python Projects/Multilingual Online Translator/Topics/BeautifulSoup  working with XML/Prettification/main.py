from bs4 import BeautifulSoup

contacts = '<?xml version="1.0" encoding="UTF-8"?><contacts><contact><name>Tyrion ' \
           'Lannister</name><prefix>Work</prefix></contact><contact><name>Jon ' \
           'Snow</name><prefix>Personal</prefix></contact><contact><name>Arya ' \
           'Stark</name><prefix>Personal</prefix></contact><contact><name>Daenerys ' \
           'Targaryen</name><prefix>Work</prefix></contact></contacts>'

soup = BeautifulSoup(contacts, "xml")

print(soup.prettify())


