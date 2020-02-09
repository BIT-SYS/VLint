module top
  (
   input CLK,
   input RST,
   input enable,
   input [31:0] value,
   output [7:0] led
  );
  reg [7:0] state;
  reg [7:0] cnt;
  
  reg [7:0] ncnt;
  reg [7:0] nstate = 0;
  assign led = cnt[7:0];
  assign state = 0;
  always @(posedge CLK or  negedge RST)
  begin
    if(RST)
      cnt <= ncnt ;			//Error
    else
      cnt <= state ;      	//correct
  end
  always@(*)
  begin
      cnt = ncnt ;			//error
      cnt = nstate ;		//Error
  end
endmodule