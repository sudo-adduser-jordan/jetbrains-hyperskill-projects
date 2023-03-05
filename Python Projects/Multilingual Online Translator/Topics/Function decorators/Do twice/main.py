def do_twice(function):
    def wrapper(args):
        function(args)
        function(args)
    return wrapper