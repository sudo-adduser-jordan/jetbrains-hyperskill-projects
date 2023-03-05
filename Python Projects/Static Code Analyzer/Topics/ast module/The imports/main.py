import ast

tree = ast.parse(code)

# put your code here
nodes = ast.walk(tree)
for node in node
