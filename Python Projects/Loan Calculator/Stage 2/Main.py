import math

print("Enter the loan principal: ")
loan_principal = int(input())

print("What do you want to calculate?")
print("type 'm' - for number of monthly payments,")
print("type 'p' - for the monthly paryment:")
action = str(input())

if action == "m":
    print("Enter the monthly payment: ")
    monthly_payment = int(input())
    result = round(loan_principal / monthly_payment)
    if result == 1:
        print("It will take " + str(result) + "month to repay the loan")
    else:
        print("It will take " + str(result) + "months to repay the loan")

elif action == "p":
    print("Enter the number of months: ")
    number_of_months = int(input())
    payment = math.ceil(loan_principal / number_of_months)
    last_payment = loan_principal - (number_of_months - 1) * payment 
    if last_payment == 0:
        print("Your monthly payment = " + str(payment))
    else:
        print("Your monthly payment = " + str(payment) + " and the last payment = " + str(last_payment) + ".")

else:
    print("Error, invalid input")



