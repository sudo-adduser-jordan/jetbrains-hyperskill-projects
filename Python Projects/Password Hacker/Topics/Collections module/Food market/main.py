from collections import ChainMap


food_types = {'Vegetables': 15, 'Dairy': 20, 'Meat': 3, 'Cereals': 9, 'Fruits': 11, 'Fish': 7}
countries = {'USA': 25, 'Australia': 15, 'Canada': 15, 'France': 6, 'India': 4}
discount = {'gold': 20, 'regular': 10}

chain = ChainMap(food_types, countries)
food_types['Sweets'] = 10

# some missing lines
countries['USA'] = 35
new_chain = chain.new_child(discount)

print(new_chain)
