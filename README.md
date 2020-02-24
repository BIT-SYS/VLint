## VLint
A static code analyzer for Verilog.

## News
* 2020-02-01, version 0.1 open sourced.

## Features
### Verilog source code parsing
Based on open source project pyverilog.
### Verilog data flow analysis
Based on open source project pyverilog.
### Verilog Source code defect detection
Supported defects:
1. The assignment objects in if module and else module are inconsistent.
2. Consider if else nesting to determine if there is no "else" matching with "if".
3. Incomplete variables in sensitive list.
4. The same module uses posedge or negedge.
5. Blocking assignment and non blocking assignment.
6. Cycle condition error.
7. Variable multiple assignments in different always.
8. Same judgment conditions in case or if statement.
9. Wrong use of integer base in case statement.
10. Variable bit width usage error.

## Build and Install
### Linux
The tool currently only supports the Linux operating system as a running environment.
### pyverilog
First You need pyverilog, a Python-based Hardware Design Processing Toolkit for Verilog HDL.
Installation steps can be found at https://pypi.org/project/pyverilog/
### Run
Run the jar file in the out directory using the Java command in the terminal, with the project path to be tested as an argument.
command: java -jar VLint.jar verilog_dir.

## Team members
Weixing Ji  : jwx@bit.edu.cn  
Dejiang Jing  
Zhi Zhou  
