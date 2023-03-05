def tagged(other_func):
    def wrapper(args_for_function):
        return f"<title>{other_func(args_for_function)}</title>"
    return wrapper