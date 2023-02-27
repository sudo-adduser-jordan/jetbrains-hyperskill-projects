string = "no clouds here to spy on pets"


def get_5th_letters(text):
    return text[::5]


string_one = get_5th_letters(string)

string_two = string_one[::-1]

print(string_two)
