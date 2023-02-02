import math

#get args
print("What do you want to calculate?")
print("type 'n' - for number of monthly payments,")
print("type 'a' - for annuity monthly payment amount,")
print("type 'p' - for loan principal:")
action = str(input())

# Switch to action ->

# Calculate the number of monthly payments

# loan_principal
# monthly_payment
# loan_interest
# years
# months

if action == "n":
    print("Enter the loan principal: ")
    loan_principal = int(input())
    print("Enter the monthly payment: ")
    monthly_payment = int(input())
    print("Enter the loan interest: ")
    loan_interest = float(input())

    nomial_rate = loan_interest / (12 * 100)

    number_of_payments = monthly_payment / (monthly_payment - nomial_rate * loan_principal)

    x = math.log(number_of_payments, nomial_rate + 1)

    if x < 12:
        print(f'It will take {round(x)} months to repay this loan!')
    elif x > 12 and round(x) % 12 != 0:
        print(f'It will take {x // 12} years and {math.ceil(x % 12)} months to repay this loan!')
    else:
        if x == 12:
            print('It will take 1 year to repay this loan!')
        else:
            print(f'It will take {round(x) // 12} years to repay this loan!')

# Calculate the monthly payment(the annuity payment)

# loan_principal
# number_of_periods
# loan_interest

elif action == "a":
    print("Enter the loan principal: ")
    loan_principal = int(input())
    print("Enter the number of periods: ")
    number_of_periods = int(input())
    print("Enter the loan interest: ")
    loan_interest = float(input())
    
    nomial_rate = loan_interest / (12 * 100)

    annuity_payment = (nomial_rate * (1 + nomial_rate) ** number_of_periods) / ((1 + nomial_rate) ** number_of_periods - 1)
    annuity_payment = math.ceil(loan_principal * annuity_payment)

    print("Your monthly payment = "+ str(annuity_payment) +"!")


#  Calculating the loan principal

# loan_principal
# number_of_periods
# loan_interest

elif action == "p":
    print("Enter the annuity payment: ")
    annuity_payment = float(input())
    print("Enter the number of periods: ")
    number_of_periods = int(input())
    print("Enter the loan interest: ")
    loan_interest = float(input())

    nomial_rate = loan_interest / (12 * 100)

    loan_principal = (nomial_rate * (1 + nomial_rate) ** number_of_periods / ((1 + nomial_rate) ** number_of_periods - 1))
    loan_principal = math.floor(annuity_payment / loan_principal)

    print("Your loan principal = " + str(loan_principal) + "!")

else:
    print("Error, invalid input")
    print("")
