import ast

# put your code here
user_input = input()
check_user_input = ast.literal_eval(user_input)
print(type(check_user_input))
