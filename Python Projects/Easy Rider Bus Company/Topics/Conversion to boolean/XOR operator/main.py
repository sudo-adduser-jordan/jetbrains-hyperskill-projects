def xor(a, b):
    # Write your code here
    if bool(a) == bool(b):
        return False
    else:
        if bool(a):
            return a
        else:
            return b
