import re
import sys
import os
import ast


def check_s001(local_path, sample: str, i: int):
    if len(sample) > 79:
        print(f'{local_path}: Line {i}: S001 Too long')


def check_s002(local_path, sample: str, i: int):
    if sample.startswith(' '):
        l = 1
        while sample[l] == ' ':
            l += 1
        if l % 4 != 0:
            print(f'{local_path}: Line {i}: S002 Indentation is not a multiple of four')


def check_s003(local_path, sample: str, i: int):
    semicolon = sample.find(';')
    comment = sample.find('#')
    if semicolon != -1:
        if comment != -1 and semicolon > comment:
            pass
        else:
            if re.search("[\'\"].*;.*[\'\"]", sample):
                pass
            else:
                print(f'{local_path}: Line {i}: S003 Unnecessary semicolon')


def check_s004(local_path, sample: str, i: int):
    comment = sample.find('#')
    if comment and comment >= 2:
        if sample[comment-1] == ' ' and sample[comment-2] == ' ':
            pass
        else:
            print(f'{local_path}: Line {i}: S004 At least two spaces required before inline comments')


def check_s005(local_path, sample: str, i: int):
    comment = sample.find('#')
    todo = sample.lower().find('todo')
    if comment != -1 and comment < todo:
        print(f'{local_path}: Line {i}: S005 TODO found')


def check_s006(local_path, i: int):
    if content[i] != '\n':
        if content[i - 1].strip() == '' and content[i - 2].strip() == '' and content[i - 3].strip() == '':
            print(f'{local_path}: Line {i + 1}: S006 More than two blank lines used before this line')


def check_s007(local_path, sample: str, i: int):
    words = ['def', 'class']
    for word in words:
        if word in sample:
            if re.search(f'{word} [^\s]', sample):
                pass
            else:
                print(f'{local_path}: Line {i}: S007 Too many spaces after {word}')


def check_s008(local_path, sample: str, i: int):
    if 'class' in sample:
        temp = re.findall(r'class \w+:', sample)
        for equal in temp:
            class_name = equal[6:-1]
            if '(' in class_name:
                index = class_name.index('(')
                class_name = class_name[:index]
            if re.fullmatch(r'([A-Z][a-z0-9]*)*', class_name):
                pass
            else:
                print(f'{local_path}: Line {i}: S008 Class name {class_name} should be written in CamelCase')


def check_s009(local_path, sample: str, i: int):
    if 'def' in sample:
        temp = re.findall(r'def \w+\(', sample)
        for equal in temp:
            def_name = temp[0][4:-1]
            if re.fullmatch(r'[a-z0-9_]*', def_name):
                pass
            else:
                print(f'{local_path}: Line {i}: S009 Function name {def_name} should be written in snake_case')


def check_s010(local_path, sample: str, i: int):
    if 'def' in sample:
        temp = re.findall(r'def \w+\(', sample)
        def_name = temp[0][4:-1]
        script = open(local_path).read()
        func_parse = ast.walk(ast.parse(script))
        for node in func_parse:
            if isinstance(node, ast.FunctionDef) and node.name == def_name:
                args = [a.arg for a in node.args.args]
                for arg in args:
                    if re.fullmatch(r'[a-z0-9_]+', arg):
                        pass
                    else:
                        print(f'{local_path}: Line {i}: S010 Argument name \'{arg}\' should be written in snake_case')


def check_s011(local_path, sample: str, i: int):
    if re.match(r'\w+.=', sample.strip()):
        variable = re.search(r'\w+ =', sample.strip()).group()[:-2]
        if re.fullmatch('[a-z0-9_]+', variable):
            pass
        else:
            print(f'{local_path}: Line {i}: S011 Variable \'\' in function should be snake_case')


def check_s012(local_path, sample: str, i: int):
    if 'def' in sample:
        temp = re.findall(r'def \w+\(', sample)
        def_name = temp[0][4:-1]
        script = open(local_path).read()
        func_parse = ast.walk(ast.parse(script))
        for node in func_parse:
            if isinstance(node, ast.FunctionDef) and node.name == def_name:
                d_args = [d for d in node.args.defaults]
        for a in d_args:
            if isinstance(a, ast.List):
                print(f'{local_path}: Line {i}: S012 Default argument value {a} is mutable')


path_name = sys.argv[1]

total_list = []

if os.path.isdir(path_name):
    for path, dirs, files in os.walk(path_name):
        for name in files:
            element = os.path.join(path, name)
            total_list.append(element)
else:
    total_list.append(path_name)

for element in total_list:
    if element.endswith('.py'):
        f = open(element, 'r', encoding='utf-8')
        content = f.readlines()
        i = 0
        for line in content:
            check_s001(element, line, i + 1)
            check_s002(element, line, i + 1)
            check_s003(element, line, i + 1)
            check_s004(element, line, i + 1)
            check_s005(element, line, i + 1)
            if i >= 2:
                check_s006(element, i)
            check_s007(element, line, i + 1)
            check_s008(element, line, i + 1)
            check_s009(element, line, i + 1)
            check_s010(element, line, i + 1)
            check_s011(element, line, i + 1)
            check_s012(element, line, i + 1)
            i += 1
        f.close()
