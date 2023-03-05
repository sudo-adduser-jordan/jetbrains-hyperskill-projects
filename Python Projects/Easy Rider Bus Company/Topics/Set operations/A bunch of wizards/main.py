gryffindor = set(input().split())
ravenclaw = set(input().split())
slytherin = set(input().split())
hufflepuff = set(input().split())

names = gryffindor.union(ravenclaw).union(slytherin).union(hufflepuff)
print(names)
