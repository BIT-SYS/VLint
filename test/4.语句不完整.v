/*
    if模块和else模块中赋值对象不一致
    考虑if-else的嵌套
    判断是否缺少与if配对的else
*/
module top
  (clk,rst_n,data,add );
  input clk;
  input rst_n;
  input[3:0] data;
  output[2:0] add;
  reg[2:0] add;
  always @ (posedge clk) 
          begin
              case(data)  
              0:        add <= 1;
              8,9,10,11:      add <= 3;
              default:        ;
              endcase
              case(data)  
              0:        add <= 1;
              8,9,10,11:      add <= 3;
              endcase
          end
  always
    begin
      case(clk)
       1: ;
       2: ;
       default : ;
       endcase
       case(df)
       default : ;
       endcase  
    end
endmodule

