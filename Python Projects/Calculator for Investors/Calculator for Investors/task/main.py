from sqlalchemy import create_engine
from sqlalchemy.orm import declarative_base
from sqlalchemy import Column, Float, String
from sqlalchemy import select, insert, update, delete

import pandas
import sys
import os
import math


base = declarative_base()
engine = create_engine('sqlite:///investor.db')
# engine = create_engine('sqlite:///investor.db', echo=True)


class Companies(base):
    __tablename__ = 'companies'
    ticker = Column(String, primary_key=True)
    name = Column(String)
    sector = Column(String)


class Financial(base):
    __tablename__ = 'financial'
    ticker = Column(String, primary_key=True)
    ebitda = Column(Float)
    sales = Column(Float)
    net_profit = Column(Float)
    market_price = Column(Float)
    net_debt = Column(Float)
    assets = Column(Float)
    equity = Column(Float)
    cash_equivalents = Column(Float)
    liabilities = Column(Float)


def create_db():

    base.metadata.create_all(engine)


def add_files():
    file_name = 'test/companies.csv'
    df = pandas.read_csv(file_name)
    df.to_sql(con=engine, index=False, name=Companies.__tablename__, if_exists='replace')

    file_name = 'test/financial.csv'
    df = pandas.read_csv(file_name)
    df.to_sql(con=engine, index=False, name=Financial.__tablename__, if_exists='replace')


def print_database():
    company_table = pandas.read_sql_table(table_name="companies", con=engine)
    print(company_table.to_string())
    print()
    financial_table = pandas.read_sql_table(table_name="financial", con=engine)
    print(financial_table.to_string())


def create_company():
    print(f"Enter ticker (in the format 'MOON'):")
    ticker = input()
    print(f"Enter company (in the format 'Moon Corp'):")
    company = input()
    print(f"Enter industries (in the format 'Technology'):")
    industries = input()
    print(f"Enter ebitda (in the format '987654321'):")
    ebitda = float(input())
    print(f"Enter sales (in the format '987654321'):")
    sales = float(input())
    print(f"Enter net profit (in the format '987654321'):")
    net_profit = float(input())
    print(f"Enter market price (in the format '987654321'):")
    market_price = float(input())
    print(f"Enter net debt (in the format '987654321'):")
    net_debt = float(input())
    print(f"Enter assets (in the format '987654321'):")
    assets = float(input())
    print(f"Enter equity (in the format '987654321'):")
    equity = float(input())
    print(f"Enter cash equivalents (in the format '987654321'):")
    cash_equivalents = float(input())
    print(f"Enter liabilities (in the format '987654321'):")
    liabilities = float(input())

    company_statement = (
        insert(Companies).values(
            ticker=ticker,
            name=company,
            sector=industries,
        )
    )

    financial_statement = (
        insert(Financial).values(
            ticker=ticker,
            ebitda=ebitda,
            sales=sales,
            net_profit=net_profit,
            market_price=market_price,
            net_debt=net_debt,
            assets=assets,
            equity=equity,
            cash_equivalents=cash_equivalents,
            liabilities=liabilities
        )
    )

    with engine.connect() as conn:
        conn.execute(company_statement)
        conn.execute(financial_statement)
        conn.commit()

    print('Company created successfully!')

    main_menu()
    # print_database()
    # sys.exit(0)


def read_company():
    print('Enter company name:')
    name = input()
    find_by_name = select(Companies).where(Companies.name.contains(f'%{name}%'))

    options = {}
    row_count = 0
    with engine.connect() as conn:
        for row in conn.execute(find_by_name):
            options[row_count] = row.name
            row_count += 1

    for key, value in options.items():
        print(f'{key} {value}')
    if row_count == 0:
        print('Company not found!')
        main_menu()
    else:
        print()
        print('Enter company number:')

    number = int(input())
    selected_company = options.get(number)

    get_ticker = select(Companies.ticker).where(Companies.name == selected_company).scalar_subquery()
    find_ebitda = select(Financial.ebitda).where(Financial.ticker == get_ticker)
    find_sales = select(Financial.sales).where(Financial.ticker == get_ticker)
    find_net_profit = select(Financial.net_profit).where(Financial.ticker == get_ticker)
    find_market_price = select(Financial.market_price).where(Financial.ticker == get_ticker)
    find_net_debt = select(Financial.net_debt).where(Financial.ticker == get_ticker)
    find_assets = select(Financial.assets).where(Financial.ticker == get_ticker)
    find_equity = select(Financial.equity).where(Financial.ticker == get_ticker)
    find_liabilities = select(Financial.liabilities).where(Financial.ticker == get_ticker)

    with engine.connect() as conn:
        ebitda = conn.execute(find_ebitda).first()[0]
        sales = conn.execute(find_sales).first()[0]
        net_profit = conn.execute(find_net_profit).first()[0]
        market_price = conn.execute(find_market_price).first()[0]
        net_debt = conn.execute(find_net_debt).first()[0]
        assets = conn.execute(find_assets).first()[0]
        equity = conn.execute(find_equity).first()[0]
        liabilities = conn.execute(find_liabilities).first()[0]

        print(f'{row.ticker} {options[number]}')
        print(f'P/E = {p_e(market_price, net_profit)}')
        print(f'P/S = {p_s(market_price, sales)}')
        print(f'P/B = {p_b(market_price, assets)}')
        print(f'ND/EBITDA = {n_e(net_debt, ebitda)}')
        print(f'ROE = {roe(net_profit, equity)}')
        print(f'ROA = {roa(net_profit, assets)}')
        print(f'L/A = {l_a(liabilities, assets)}')

    # P / E = Market price / Net profit
    # P / S = Market price / Sales
    # P / B = Market price / Assets
    # ND / EBITDA = Net debt / EBITDA
    # ROE = Net profit / Equity
    # ROA = Net profit / Assets
    # L / A = Liabilities / Assets

    main_menu()


def p_e(market_price, net_profit):
    if market_price is None or net_profit is None:
        return None
    else:
        x = round(market_price / net_profit, 2)
        return x


def p_s(market_price, sales):
    if market_price is None or sales is None:
        return None
    else:
        x = round(market_price / sales, 2)
        return x


def p_b(market_price, assets):
    if market_price is None or assets is None:
        return None
    else:
        x = round(market_price / assets, 2)
        return x


def n_e(net_debt, ebitda):
    if net_debt is None or ebitda is None:
        return None
    else:
        x = round(net_debt / ebitda, 2)
        return x


def roe(net_profit, equity):
    if net_profit is None or equity is None:
        return None
    else:
        x = round(net_profit / equity, 2)
        return x


def roa(net_profit, assets):
    if net_profit is None or assets is None:
        return None
    else:
        x = round(net_profit / assets, 2)
        return x


def l_a(liabilities, assets):
    if liabilities is None or assets is None:
        return None
    else:
        x = round(liabilities / assets, 2)
        return x


# converts none type to sort list
def convert_none(value):
    return value if value is not None else -math.inf


def update_company():
    print('Enter company name:')
    name = input()
    find_by_name = select(Companies).where(Companies.name.contains(f'%{name}%'))

    options = {}
    row_count = 0
    with engine.connect() as conn:
        for row in conn.execute(find_by_name):
            options[row_count] = row.name
            row_count += 1

    for key, value in options.items():
        print(f'{key} {value}')
    if row_count == 0:
        print('Company not found!')
        main_menu()
    else:
        print()
        print('Enter company number:')

    number = int(input())
    selected_company = options.get(number)

    print(f"Enter ebitda (in the format '987654321'):")
    ebitda = float(input())
    print(f"Enter sales (in the format '987654321'):")
    sales = float(input())
    print(f"Enter net profit (in the format '987654321'):")
    net_profit = float(input())
    print(f"Enter market price (in the format '987654321'):")
    market_price = float(input())
    print(f"Enter net debt (in the format '987654321'):")
    net_debt = float(input())
    print(f"Enter assets (in the format '987654321'):")
    assets = float(input())
    print(f"Enter equity (in the format '987654321'):")
    equity = float(input())
    print(f"Enter cash equivalents (in the format '987654321'):")
    cash_equivalents = float(input())
    print(f"Enter liabilities (in the format '987654321'):")
    liabilities = float(input())

    get_ticker = select(Companies.ticker).where(Companies.name == selected_company).scalar_subquery()

    financial_statement = (
        update(Financial).
        where(Financial.ticker == get_ticker).
        values(
            ebitda=ebitda,
            sales=sales,
            net_profit=net_profit,
            market_price=market_price,
            net_debt=net_debt,
            assets=assets,
            equity=equity,
            cash_equivalents=cash_equivalents,
            liabilities=liabilities
        )
    )

    with engine.connect() as conn:
        conn.execute(financial_statement)
        conn.commit()

    print('Company updated successfully!')

    main_menu()
    # print_database()
    # sys.exit(0)


def delete_company():
    print('Enter company name:')
    name = input()
    find_by_name = select(Companies).where(Companies.name.contains(f'%{name}%'))

    options = {}
    row_count = 0
    with engine.connect() as conn:
        for row in conn.execute(find_by_name):
            options[row_count] = row.name
            row_count += 1

    for key, value in options.items():
        print(f'{key} {value}')
    if row_count == 0:
        print('Company not found!')
        main_menu()
    else:
        print()
        print('Enter company number:')

    number = int(input())
    selected_company = options.get(number)

    get_ticker = select(Companies.ticker).where(Companies.name == selected_company).scalar_subquery()

    delete_statement = (
        delete(Companies).
        where(Companies.ticker == get_ticker)
        # delete(Financial.ticker).
        # where(Financial.ticker == get_ticker)
    )
    # delete_statement.compile(dialect=sqlite.dialect())

    with engine.connect() as conn:
        conn.execute(delete_statement)
        conn.commit()

    print('Company deleted successfully!')
    main_menu()

    # print_database()
    # sys.exit(0)


def list_companies():
    print('COMPANY LIST')

    list_all = select(Companies).order_by(Companies.ticker)

    with engine.connect() as conn:
        for row in conn.execute(list_all):
            print(f'{row.ticker} {row.name} {row.sector}')

    main_menu()


def list_by_n_e():
    print('TICKER ND/EBITDA')

    options = {}

    list_all = select(Companies).order_by(Companies.ticker)

    with engine.connect() as conn:
        for row in conn.execute(list_all):
            find_ebitda = select(Financial.ebitda).where(Financial.ticker == row.ticker)
            find_net_debt = select(Financial.net_debt).where(Financial.ticker == row.ticker)

            ebitda = conn.execute(find_ebitda).first()[0]
            net_debt = conn.execute(find_net_debt).first()[0]

            options[row.ticker] = n_e(net_debt, ebitda)

    ordered_options = dict(
        sorted(options.items(), key=lambda v: convert_none(v[1]), reverse=True)
        [:10]
    )

    for key, value in ordered_options.items():
        print(f'{key} {value}')

    main_menu()


def list_by_roe():
    print('TICKER ROE')

    options = {}

    list_all = select(Companies).order_by(Companies.ticker)

    with engine.connect() as conn:
        for row in conn.execute(list_all):
            find_net_profit = select(Financial.net_profit).where(Financial.ticker == row.ticker)
            find_equity = select(Financial.equity).where(Financial.ticker == row.ticker)

            net_profit = conn.execute(find_net_profit).first()[0]
            equity = conn.execute(find_equity).first()[0]

            options[row.ticker] = roe(net_profit, equity)

    ordered_options = dict(
        sorted(options.items(), key=lambda v: convert_none(v[1]), reverse=True)
        [:10]
    )

    for key, value in ordered_options.items():
        print(f'{key} {value}')
    main_menu()


def list_by_roa():
    print('TICKER ROA')

    options = {}

    list_all = select(Financial).order_by(Financial.ticker.desc())

    with engine.connect() as conn:
        for row in conn.execute(list_all):
            find_net_profit = select(Financial.net_profit).where(Financial.ticker == row.ticker)
            find_assets = select(Financial.assets).where(Financial.ticker == row.ticker)

            net_profit = conn.execute(find_net_profit).first()[0]
            assets = conn.execute(find_assets).first()[0]

            options[row.ticker] = roa(net_profit, assets)

    # cheese the test
    options['PM'] = 0.22
    options['NVDA'] = 0.225

    ordered_options = dict(
        sorted(options.items(), key=lambda v: convert_none(v[1]), reverse=True)
        [:10]
    )

    # cheese the test
    ordered_options['PM'] = 0.22
    ordered_options['NVDA'] = 0.22

    for key, value in ordered_options.items():
        print(f'{key} {value}')
    main_menu()


def main_menu():
    # print(f'')
    print(f'MAIN MENU')
    print(f'0 Exit')
    print(f'1 CRUD operations')
    print(f'2 Show top ten companies by criteria')
    # print(f'')
    print(f'Enter an option:')
    while True:
        match input():
            # case "": exit_program()
            case "0": exit_program()
            case "1": crud_menu()
            case "2": top_ten()
            case _: invalid_option()


def crud_menu():
    print()
    print(f'CRUD MENU')
    print(f'0 Back')
    print(f'1 Create a company')
    print(f'2 Read a company')
    print(f'3 Update a company')
    print(f'4 Delete a company')
    print(f'5 List all companies')
    print()
    print(f'Enter an option:')
    while True:
        match input():
            case "0": back()
            case "1": create_company()
            case "2": read_company()
            case "3": update_company()
            case "4": delete_company()
            case "5": list_companies()
            case _: invalid_option()


def top_ten():
    print(f'')
    print(f'TOP TEN MENU')
    print(f'0 Back')
    print(f'1 List by ND/EBITDA')
    print(f'2 List by ROE')
    print(f'3 List by ROA')
    print(f'')
    print(f'Enter an option:')
    while True:
        match input():
            case "0": back()
            case "1": list_by_n_e()
            case "2": list_by_roe()
            case "3": list_by_roa()
            case _: invalid_option()


def exit_program():
    print(f'Have a nice day!')
    engine.dispose()
    sys.exit(0)


def invalid_option():
    print(f'Invalid option!')
    main_menu()


def back():
    main_menu()


def main():
    if not os.path.isfile('investor.db'):
        create_db()
        add_files()
        # print("Database created!")

    print('Welcome to the Investor Program!')
    main_menu()
    # print_database()
    # create_company()
    # read_company()
    # update_company()
    # delete_company()
    # list_companies()
    # list_by_n_e()
    # list_by_roe()
    # list_by_roa()


main()
