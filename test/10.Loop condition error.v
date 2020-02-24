module top
  (
   input CLK,
   input RST,
   input enable,
   input [31:0] value,
   input [7:0] number,
   output [7:0] led
  );
  reg [31:0] count;
  reg [2:0] state;
  reg [3:0] Count_aux;
  reg j;
  integer i;
  always @(number)
  begin
  i = 0;
  Count_aux = 4'b0000;
  while(i <= 8)
	begin
        if(!number[i])
            Count_aux= Count_aux + 1;
        i= i + 1;
	end
  end
  always @(number)
  begin
  i = 0;
  Count_aux = 4'b0000;
  for (i = 0; i < 8; i = i + 1)
	begin
        if(!number[i])
            Count_aux= Count_aux + 1;
	end
  end
  always @(number)
  begin
  j = 0;
  Count_aux = 4'b0000;
  while(j < 2)						//error
	begin
        if(!number[j])
            Count_aux= Count_aux + 1;
        j= j + 1;
	end
  end
  always @(number)
  begin
  state = 0;
  Count_aux = 4'b0000;
  for (state = 0; state < 8; state = state + 1)  	//error
	begin
        if(!number[state])
            Count_aux= Count_aux + 1;
	end
  end
endmodule
