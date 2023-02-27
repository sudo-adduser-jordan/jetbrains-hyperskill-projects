firms = OrderedDict(json.loads(input()))
firms.popitem(last=True)
firms.popitem(last=True)
print(firms)
