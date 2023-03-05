import json
import re

from collections import Counter
from dataclasses import dataclass, fields


@dataclass
class Bus:
    """
    Keeps track of bus data
    """
    bus_id: int
    stop_id: int
    stop_name: str
    next_stop: int
    stop_type: str
    a_time: str

    @property
    def error_fields(self) -> dict[str, bool]:
        error_dict: dict[str, bool] = {field.name: True for field in fields(self)}
        optional_str_fields: list[str] = ["stop_type"]
        stop_name_pattern: str = r"([A-Z].*) (Road|Avenue|Boulevard|Street)$"
        stop_type_values: set = {'S', 'O', 'F', ""}
        a_time_pattern: str = r"^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"

        for field in fields(self):
            value = getattr(self, field.name)
            if not isinstance(value, field.type):
                """raise ValueError(f"Expected {field.name} to be {field.type}, got {repr(value)}")"""
            elif isinstance(value, str) and field.name not in optional_str_fields and value == "":
                """raise Exception("No empty strings")"""
            elif field.name == "stop_name" and not re.match(stop_name_pattern, value):
                """raise Exception("Incorrect format")"""
            elif field.name == "stop_type" and value not in stop_type_values:
                """raise Exception("Invalid type")"""
            elif field.name == "a_time" and not re.match(a_time_pattern, value):
                """raise Exception("Invalid time")"""
            else:
                error_dict[field.name] = False

        return error_dict


def input_json() -> list[dict]:
    return json.loads(input())


# Stage 1 & 2 Function
def print_error_count(list_of_data: list[Bus], relevant_fields: list[str]):
    data_error_counter: dict[str, int] = {field.name: 0 for field in fields(list_of_data[0])}

    for data in list_of_data:
        for key in data.error_fields:
            data_error_counter[key] += data.error_fields[key]

    total_errors: int = sum(data_error_counter.values())
    print(f"Format validation: {total_errors} errors")
    for field_name, error_count in data_error_counter.items():
        if field_name in relevant_fields:
            print(f"{field_name}: {error_count}")


# Stage 3 Function
def print_bus_line_info(bus_data: list[Bus]):
    print("Line names and number of stops:")
    bus_id_data: list[int] = [bus.bus_id for bus in bus_data]
    bus_id_counter = Counter(bus_id_data)
    for bus_id, stops in bus_id_counter.items():
        print(f"bus_id: {bus_id}, stops: {stops}")


# Stage 4 Function
def validate_stops(bus_data: list[Bus]) -> bool:
    all_bus_ids: set[int] = {bus.bus_id for bus in bus_data}
    bus_id_to_stop_types: dict[int, set[str]] = {bus_id: set() for bus_id in all_bus_ids}
    for bus in bus_data:
        bus_id_to_stop_types[bus.bus_id].add(bus.stop_type)

    for bus_id, stop_types in bus_id_to_stop_types.items():
        if not {'S', 'F'}.issubset(stop_types):
            print(f"There is no start or end stop for the line: {bus_id}.")
            return False

    return True


# Stage 4 Function refactored
def get_special_stops(bus_data: list[Bus]) -> tuple[set, set, set]:
    all_stop_ids: list[int] = [bus.stop_id for bus in bus_data]
    all_next_stops: list[int] = [bus.next_stop for bus in bus_data]
    start_stops: set[str] = {bus.stop_name for bus in bus_data if bus.stop_id not in all_next_stops}
    transfer_stops: set[str] = {bus.stop_name for bus in bus_data if all_stop_ids.count(bus.stop_id) > 1}
    finish_stops: set[str] = {bus.stop_name for bus in bus_data if bus.next_stop not in all_stop_ids}

    return start_stops, transfer_stops, finish_stops


# Stage 4 Function
def print_special_stops(bus_data: list[Bus]):
    # Moved "get" methods to another function during Stage 6
    start_stops, transfer_stops, finish_stops = get_special_stops(bus_data)
    print("Start stops:", len(start_stops), sorted(start_stops))
    print("Transfer stops:", len(transfer_stops), sorted(transfer_stops))
    print("Finish stops:", len(finish_stops), sorted(finish_stops))


# Stage 5 Function
def validate_arrival_times(bus_data: list[Bus]):
    all_bus_ids: set[int] = {bus.bus_id for bus in bus_data}
    bus_id_to_a_times: dict[int, list[Bus]] = {bus_id: [] for bus_id in all_bus_ids}
    for bus in bus_data:
        bus_id_to_a_times[bus.bus_id].append(bus)

    error_tally: list[tuple[int, str]] = []
    # The following assumes that stop_id and next_stop are sorted correctly
    for bus_id, bus_sublist in bus_id_to_a_times.items():
        for i, bus in enumerate(bus_sublist):
            if i > 0 and bus_sublist[i - 1].a_time > bus.a_time:
                error_tally.append((bus_id, bus.stop_name))
                break

    print("Arrival time test:")
    if len(error_tally) == 0:
        return print("OK")

    for bus_id, stop_name in error_tally:
        print(f"bus_id line {bus_id}: wrong time on station {stop_name}")


# Stage 6 Function
def validate_on_demand_stops(bus_data: list[Bus]):
    start_stops, transfer_stops, finish_stops = get_special_stops(bus_data)
    all_special_stops: set[str] = start_stops | transfer_stops | finish_stops

    error_tally: list = []
    for bus in bus_data:
        if bus.stop_type == 'O' and bus.stop_name in all_special_stops:
            error_tally.append(bus.stop_name)

    print("On demand stops test:")
    if len(error_tally) == 0:
        print("OK")
    else:
        error_tally.sort()
        print(f"Wrong stop type: {error_tally}")


def main():
    buses: list[dict] = input_json()
    bus_data = [Bus(**bus) for bus in buses]
    # relevant_fields: list[str] = ["stop_name", "stop_type", "a_time"]  # Stage 3 input
    validate_on_demand_stops(bus_data)


if __name__ == "__main__":
    main()