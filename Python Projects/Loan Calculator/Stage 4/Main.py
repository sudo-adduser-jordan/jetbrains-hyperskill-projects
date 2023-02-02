import argparse
import math

# Get args
parser = argparse.ArgumentParser()
parser.add_argument("--type", 
    type=str, 
    choices=["annuity", "diff"])
parser.add_argument("--principal", type=int)
parser.add_argument("--periods", type=int) 
parser.add_argument("--interest", type=float)
parser.add_argument("--payment", type=int) 
args = vars(parser.parse_args())

# Set args
type_ = args["type"]
payment = args["payment"]
principal = args["principal"]
interest = args["interest"]
periods = args["periods"]

# Check args
if (type_ is None) or (type_ == "diff" and payment is not None) or (interest is None) or (periods is not None and periods < 0):
    print("Incorrect parameters")
else:
    nomial_interest = interest / (12 * 100)

    # Calculate diff
    if type_ == "diff":
        overpayment = principal
        for i in range(1, periods + 1):
            result = math.ceil(principal/periods + nomial_interest * (principal - principal*(i - 1)/periods))
            overpayment -= result
            print(f"Month {i}: paid out {result}")
        print(f"\r\nOverpayment = {abs(overpayment)}")

    # Calculate annuity
    elif type_ == "annuity":
        if periods is None:
            periods = math.ceil(math.log(payment / (payment - nomial_interest * principal), 1 + nomial_interest))
            years = periods // 12
            months = periods % 12
            print(f"You need {years} years and {months} months to repay this credit!")
        elif principal is None:
            principal = math.floor(payment / (nomial_interest / (1 - 1 / math.pow(1 + nomial_interest, periods))))
            print(f"Your credit principal = {principal}!")
        else:
            payment = math.ceil(principal * nomial_interest / (1 - 1 / math.pow(1 + nomial_interest, periods)))
            print(f"Your annuity payment = {payment}!")
        overpayment = periods * payment - principal
        print(f"Overpayment = {overpayment}")
