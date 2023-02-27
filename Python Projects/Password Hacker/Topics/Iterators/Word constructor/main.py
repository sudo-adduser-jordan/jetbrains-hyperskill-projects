string_one = input()
string_two = input()

string_result = ""
for a, b in zip(string_one, string_two):
    string_result += a + b
print(string_result)
