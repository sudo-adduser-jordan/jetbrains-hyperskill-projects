def divide_by(number):
    try:
        if number > 0:
            print(100 / number)
        else:
            print(100 / (number / 2))
    except ZeroDivisionError:
        print("Please enter a number that is not equal to zero")
