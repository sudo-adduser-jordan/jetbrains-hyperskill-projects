from lxml import etree


def find_password(xml_string):
    root = etree.fromstring(xml_string)
    for element in root.iter():
        if element.tag == "account" or element.tag == "user":
            return element.get('password')

