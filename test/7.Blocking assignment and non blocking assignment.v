module top
  (
   input CLK,
   input RST,
   input enable,
   input [31:0] value,
   output [7:0] led
  );
  reg [31:0] count;
  reg [7:0] state;
  assign led <= count[23:16];
  always @(posedge CLK)       	//correct
  begin
    if(RST)             
      count <= state ;
    else 
      count <= 1 ;
  end
  always @(posedge CLK) 		//line 24 should be <=
  begin
    if(RST)            
      count <= 0 ;
    else 
      count = 1 ;
  end
  always @(*) 					//line 29,31 should be =
  begin
    if(RST)              
      count <= 0 ;				
    else 
      count <= 1 ;				
  end
  always @(*)					//correct
  begin
	state = led;
  end
endmodule