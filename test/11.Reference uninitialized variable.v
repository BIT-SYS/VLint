module top
(
 input in1,
 input in2,
 input CLK,
 output out1,
 output out2
);

 integer i;
 always @(posedge CLK) begin
     out1 = out2 ;				//error
   end
 always@( in1 ) begin  

 if( in2 ) begin
   out1 = in2;
   out2 = in2;s
  end  
 else
   out1 = in2;

 out1 = out2+1 ;				//error

 end

endmodule