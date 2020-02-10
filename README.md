# VLint
An source code defect detection tool for VHDL.

# News
6th February,2020, version 0.1 open sourced.

# Features
## Verilog source code parsing
Based on open source project pyverilog.
## Verilog data flow analysis
Based on open source project pyverilog.
## Verilog Source code defect detection
Supported defects:
1. The assignment objects in if module and else module are inconsistent.
2. Consider if else nesting to determine if there is no "else" matching with "if".
3. Incomplete variables in sensitive list.
4. The same module uses posedge or negedge.
5. Blocking assignment and non blocking assignment.
6. Cycle condition error.
# Build and Install
pyverilog
# Team members
Weixing Ji  
Dejiang Jing  
Zhi Zhou  
