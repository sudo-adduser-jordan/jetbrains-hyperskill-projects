def tallest_people(**kwargs):
    tallest = max(kwargs.values())

    for key, value in sorted(kwargs.items()):
        if value == tallest:
            print(f'{key} : {value}')