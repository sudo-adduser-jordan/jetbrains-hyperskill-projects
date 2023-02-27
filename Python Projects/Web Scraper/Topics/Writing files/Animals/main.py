old = open('animals.txt', 'r')
new = open('animals_new.txt', 'w')

for animal in old:
    new.write(animal.replace('\n', ' '))

old.close()
new.close()