def check_name(string):
    if string == 'l':
        print("Never use the characters 'l', 'O', or 'I' as single-character variable names")

    elif string == 'O':
        print("Never use the characters 'l', 'O', or 'I' as single-character variable names")

    elif string == 'I':
        print("Never use the characters 'l', 'O', or 'I' as single-character variable names")

    elif string == string.lower():
        print("It is a common variable")

    elif string == string.upper():
        print("It is a constant")
    else:
        print("You shouldn't use mixedCase")


# check_name(input())
