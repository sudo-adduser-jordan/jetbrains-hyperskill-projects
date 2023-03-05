from lxml import etree

PATH = 'C:\\Users\\profile1\\PycharmProjects\\Multilingual Online Translator\\Topics\\XML in ' \
       'Python\\Teams\\data\\dataset\\input.txt'

xml_file = PATH
root = etree.parse(xml_file).getroot()

names = root[0]
for name in names:
    print(name.get('name'), end=" ", flush=True)

