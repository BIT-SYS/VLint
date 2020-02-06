/*
  always引导的敏感列表:上升沿/下降沿/ALL  直接跳过
  if  case  赋值语句中引用的变量
  与 或 非 抑或 按位取反的组合 
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
  always @(posedge CLK)       //jump
  begin
    if(RST)             
      count <= state ;
    else 
      count <= 1 ;
  end
  always @( CLK, state , value) 
  begin
    if(RST)                 // x  RST
      count <= value ;      // o  value
    else 
      state <= CLK ;        // O  CLK
    if(enable)              // x  enable
    begin
      count <= state ;      // O  state
      state <= CLK ;        // o  CLK
    end
    else
    begin 
      count <= value ;      // o  value
    end
  end
  always @(CLK , state) 
  begin
    if(RST)                 // x  RST
    begin
      if( enable )          // x  enable
        count <=state ;     // o  state
      else
        count <= value ;    // x  value
    end
    else 
    begin
      state <= 1 ;  
      if( enable )          // x  enable
        count <= RST ;      // x  RST
      else
        count <= 1 ;
    end
    if(CLK)                 // o  CLK
      count <= 1 ;      
  end
  always @(enable,count)
  begin
    count = enable ;        // o  enable
    case(state)             // X  state
      1: enable <= count ;  // o  count
      default: ;
    endcase 
  end
  always 
    count = state ;         // jump
  always @(enable) 
  begin
  if( a & enable )          // x  a  o  enable
    count <=  a & b ;       // x  a  x  b
  else
    count <=  enable | d ;  // o enable  x  d
    count <=  e ^ f ;       // x  e  x  f
  if( !h )                  // x  h
    count <=  !enable ;     // o  enable
  else
    count <=  ~g ;          // x  g
    count <=  ~(( i & enable ) & !(x|y)) ;  // x  i  o  enable  x  x  x  y
  end
  
endmodule

