## VLint
There are constructs in Verilog used in ways syntactically correct, but yield unexpected or undesirable results. VLint is a Static analysis tool for Verilog, detecting design defects in Verilog source code. 

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
### Requirements
* Linux like system
* Python 3.7 or higher version
* pyverilog: a Python-based Hardware Design Processing Toolkit for Verilog HDL. Installation instructions can be found at https://pypi.org/project/pyverilog/

### Running
Run the jar file in the out directory with the project source path:  
    
    java -jar VLint.jar verilog_src_dir

## People

Weixing Ji  : jwx@bit.edu.cn  
Dejiang Jing  
Zhi Zhou  
