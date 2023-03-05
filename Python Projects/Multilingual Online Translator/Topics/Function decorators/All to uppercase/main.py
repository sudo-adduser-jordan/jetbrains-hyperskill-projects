def to_upper(function):
    def wrapper(args_for_function):
        return function(args_for_function).upper()
    return wrapper
