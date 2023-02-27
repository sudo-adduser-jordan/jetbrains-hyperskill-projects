with open('name.txt') as f1:
    name = f1.read()
with open('surname.txt') as f2:
    surname = f2.read()
with open('full_name.txt', 'w') as f3:
    full_name = name + ' ' + surname
    f3.write(full_name)
