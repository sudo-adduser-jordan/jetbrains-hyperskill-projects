
def submit_data(data, client, address):
    client.connect(address)
    data = data.encode()
    client.send(data)
