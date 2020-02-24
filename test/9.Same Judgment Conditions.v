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
  
  always @(posedge CLK)
  begin
    if(value == 2'b00)
      count <= CLK ;
    else if (value == 2'b00)		//Error
      count <= 1 ;
	else if (value == 2'b01)
	  count <= 0;
  end
  
  always@(posedge CLK or negedge RST)
  begin
    if(value == 2'b00)
      count <= CLK ;
    else if (value == 2'b01)
      state <= 1 ;
	else if (value == 2'b01)		//Error
	  count <= 1 ;
	else count <= 0;
  end
  
  always@(posedge CLK or negedge RST)
  begin
    if(value == 2'b00)
      count <= CLK ;
    else if (value == 2'b01)
      state <= 1 ;
	else if (value == 2'b00)		//Error
	  count <= 1 ;
	else count <= 0;
  end
  
  always@(posedge CLK or negedge RST)
  begin
    case(value)
	2'b00 : state <= CLK;
	2'b01 : state <= 1;
	2'b00 : state <= 0;				//Error
	endcase
  end
  
  always@(posedge CLK or negedge RST)
  begin
    case(value)
	2'b01 : state <= CLK;
	2'b01 : state <= 1;				//Error
	2'b00 : state <= 0;
	endcase
  end
  
  always@(posedge CLK or negedge RST)
  begin
    case(value)
	2'b00 : state <= CLK;
	2'b01 : state <= 1;
	2'b01 : state <= 0;				//Error
	endcase
  end
  
endmodule