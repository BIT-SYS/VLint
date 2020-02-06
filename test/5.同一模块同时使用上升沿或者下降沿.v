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
  assign led = count[23:16];
  always @(posedge CLK or  negedge CLK)
  begin
    if(RST)
      count <= CLK ;
    else
      count <= 1 ;
  end
  always@(posedge CLK or negedge RST)
  begin
    if(RST)
      count <= CLK ;
    else
      count <= 1 ;
  end
endmodule