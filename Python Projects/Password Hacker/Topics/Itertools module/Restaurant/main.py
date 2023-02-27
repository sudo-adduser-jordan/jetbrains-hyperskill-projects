import itertools


dishes = list(itertools.product(main_courses, desserts, drinks))
prices = list(itertools.product(price_main_courses, price_desserts, price_drinks))

relation = zip(dishes, prices)
for i, (dish, price) in enumerate(relation):
    if sum(price) <= 30:
        print(*dish, sum(price))




