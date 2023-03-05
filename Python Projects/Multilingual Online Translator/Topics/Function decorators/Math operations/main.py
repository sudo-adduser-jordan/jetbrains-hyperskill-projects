def print_info(func):
    def wrapper(arg1, arg2):
        print("The arguments of the function are:", arg1, arg2)
        return func(arg1, arg2)

    return wrapper


@print_info
def addition(arg1, arg2):
    return arg1 + arg2
