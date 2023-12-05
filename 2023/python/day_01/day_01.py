import multiprocessing as mp
import re
import argparse

INPUT_FILE = "day_01_input.txt"

DIGIT_DICT = {
    "one": "1",
    "two": "2",
    "three": "3",
    "four": "4",
    "five": "5",
    "six": "6",
    "seven": "7",
    "eight": "8",
    "nine": "9",
}


def add_args(argparser: argparse.ArgumentParser):
    argparser.add_argument(
        "input_path", nargs="?", default=INPUT_FILE, help=f"path to the input file (default: {INPUT_FILE})"
    )
    argparser.add_argument(
        "-p", "--part", choices=[1, 2], default=1, type=int, help=f"part of the problem to be solved (default: 1)"
    )


def solve_part_1(line: str):
    regex_pattern = "\\d"
    matches = re.findall(regex_pattern, line)

    first_digit = matches[0]
    last_digit = matches[-1]

    return int(f"{first_digit}{last_digit}")


def solve_part_2(line: str):
    regex_pattern = "(?=(\\d|one|two|three|four|five|six|seven|eight|nine))"
    matches = re.findall(regex_pattern, line)

    first_digit = matches[0]
    if first_digit in DIGIT_DICT.keys():
        first_digit = DIGIT_DICT[first_digit]

    last_digit = matches[-1]
    if last_digit in DIGIT_DICT.keys():
        last_digit = DIGIT_DICT[last_digit]

    return int(f"{first_digit}{last_digit}")


if __name__ == "__main__":
    argparser = argparse.ArgumentParser()
    add_args(argparser)
    args = argparser.parse_args()

    input_lines = None
    with open(args.input_path) as f:
        input_lines = f.readlines()

    n_proc = mp.cpu_count()
    solution = 0
    with mp.Pool(n_proc) as pool:
        starmap_args = [(txt_input,) for txt_input in input_lines]
        match args.part:
            case 1:
                solve_fn = solve_part_1
            case 2:
                solve_fn = solve_part_2
            case _:
                solve_fn = solve_part_1

        for result in pool.starmap(solve_fn, starmap_args):
            solution += result

        print(f"{solve_fn.__name__}: {solution}")
