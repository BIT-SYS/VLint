/*
    if模块和else模块中赋值对象不一致
    考虑if-else的嵌套
    判断是否缺少与if配对的else
*/
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
  always @(posedge CLK) 
  begin
    if(RST)             // count - count    
      count <= 0 ;
    else 
      count <= 1 ;
  end
  always @(posedge CLK) 
  begin
    if(RST)             // count - state 
      count <= 0 ;
    else 
      state <= 0 ;
    if(enable)          // count state - count enaable state
    begin
      count <= 1 ;
      state <= 0 ;
    end
    else
    begin 
      case(state)            
        1: enable <= count ; 
        default: state <= 1 ;
      endcase 
      count <= 0 ;
    end
  end
  always @(posedge CLK) 
  begin
    if(RST)             //count - count state
    begin
      if( enable )      // count - count
        count <=1 ;
      else
        count <= 0 ;
    end
    else 
    begin
      state <= 1 ;  
      if( enable )      //count - count
        count <= 0 ;
      else
        count <= 1 ;
    end
    if(CLK)             //without else
      count <= 1 ;      
  end
  always @(enable,count)
  begin
    if(enable)          // □ - count
    begin
    end
    else
      count <= 1 ;
  end
endmodule

