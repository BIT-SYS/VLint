import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Main {
    private static int line_num;
    public static void print_array( ArrayList<String> list )
    {
        for( String l : list )
            System.out.print(l+" ");
        System.out.println(" ");
    }

    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            int i = 0;
            int index = 0;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                if (i == 0) result.append(s + ";");
                else result.append('\n' + s + ";");
                ++i;
            }
            line_num = i;
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }


    public static void main( String args[] ) throws IOException {
        System.out.println("starts");

        File file = new File("D:/test/test.txt");

        String stringResult = txt2String(file);

        ArrayList<String> parseResult = new ArrayList<String>(Arrays.asList(stringResult.split(";")));

        //ArrayList<String> parseResult = new ArrayList<String>();
        //Collections.addAll(parseResult, stringResult.split(";"));

        int length = parseResult.size();


        System.out.println(length);

        //System.out.println(stringResult);

        //print_array(parseResult);

        //parse.If_else_case(parseResult);

        //parse.SensitiveList(parseResult);

        //parse.Incomplete_Case(parseResult);

        //parse.SingalState(parseResult);

        //parse.BlockorNonblockAssign(parseResult);
        
        //parse.CycleConditionError(parseResult);
        
        //parse.VarAssignMultipleInAlways(parseResult);

        //ArrayList<String> dataflowResult = utils.command("python3 examples/example_dataflow_analyzer.py -t top testFiles/test.v" , "/home/zzy/Documents/JDJ/pyverilog");

        //print_array(dataflowResult);

        System.out.println("finished");
    }
}
