# write your code here
with open('salary.txt') as salary, open('salary_year.txt', 'w') as salary_year:
    for line in salary:
        salary_year.write(str(int(line) * 12) + "\n")
