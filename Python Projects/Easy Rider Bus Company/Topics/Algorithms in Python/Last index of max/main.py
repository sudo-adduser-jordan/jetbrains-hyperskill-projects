def last_indexof_max(numbers):
    index = 0

    for i in range(1, len(numbers)):
        if numbers[i] >= numbers[index]:
            index = i

    return index