module top
  (
   input CLK,
   input RST,
   input enable,
   input [1:0] value,
   output [7:0] led
  );
  reg [31:0] count;
  reg [7:0] state;
  assign led = count[23:16];

  always@(posedge CLK or negedge RST)
  begin
    case(value)
	00 : state <= CLK;
	01 : state <= 1;
	10 : state <= 0;
	11 : state <= 1;
	endcase
  end
  
  always@(posedge CLK or negedge RST)
  begin
    case(value)
	2'b00 : state <= CLK;
	2'b01 : state <= 1;	
	2'b10 : state <= 0;
	2'b11 : state <= 0;
	endcase
  end
endmodule