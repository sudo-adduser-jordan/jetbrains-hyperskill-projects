from pprint import pprint

dogs = [
    {
        "name": "Max",
        "breed": "Yorkshire",
        "age": 1,
        "owners": ["Susan, Camila, Paul"]
    },
    {
        "name": "Duke",
        "breed": "Bulldog",
        "age": 4,
        "owners": ["Thomas, David, Lucia"]
    }
]

pprint(dogs, depth=4, indent=4, width=20)
