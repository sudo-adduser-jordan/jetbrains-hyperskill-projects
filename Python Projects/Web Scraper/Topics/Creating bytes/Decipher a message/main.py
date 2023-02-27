encodedString = input()
key = input()
key = int(key)

int_to_bytes = sum(key.to_bytes(2, 'little'))

stringResult = ""
for i in range(len(encodedString)):
    x = encodedString[i]
    y = ord(x) + int_to_bytes
    z = chr(y)
    stringResult += z
print(stringResult)
