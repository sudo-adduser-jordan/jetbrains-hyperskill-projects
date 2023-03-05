from bs4 import BeautifulSoup

families = '<?xml version="1.0" encoding="UTF-8"?> <families><family name="Swan-Dwyer"><parent ' \
           'name="Charlie"/><parent name="Renee"/><daughter name="Isabella"/></family><family ' \
           'name="Cullen-Hale"><parent name="Carlisle"/><parent name="Esme"/><daughter name="Alice"/><son ' \
           'name="Emmet"/><daughter name="Rosalie"/><son name="Jasper"/><son name="Edward"/></family></families>'

soup = BeautifulSoup(families, "xml")
son_tag = soup.find("son")
# print(soup.prettify())
print(son_tag.parent)
