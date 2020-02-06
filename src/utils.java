import org.omg.PortableInterceptor.INACTIVE;

import java.io.*;
import java.util.ArrayList;

public class utils {

    /**
     * 计数line中前置的空格(缩进)数量
     * @param line
     * @return
     */
    public static int count_space( String line )
    {
        int i = 0 ;

        for( i=0 ; i<line.length() ; i++ )
        {
            if( line.charAt(i) > 'A' - 1 && line.charAt(i) < 'Z' + 1)
            {
                break ;
            }
        }
        //System.out.println("count space : " + i + line);
        return i ;
    }

    /**
     * 摘取line中末尾标识的行数
     * @param line
     * @return
     */
    public static int record_line( String line )
    {
        String string_line =  line.substring(line.lastIndexOf(' ')+1 , line.lastIndexOf(')')) ;
        int int_line = Integer.valueOf(string_line) ;
        return int_line ;
    }

    /**
     * 抽取line中的变量名称
     * @param line
     * @return
     */
    public static String record_variable( String line )
    {
        return line.substring( line.indexOf(":")+2 , line.lastIndexOf("(")-1 ) ;
    }

    /**
     * 去重
     * @param list1
     * @return
     */
    public static ArrayList<String> duplicate_removal(ArrayList<String> list1)
    {
        ArrayList<String> newList = new ArrayList<String>();
        for (String cd : list1) {
            if (!newList.contains(cd)) {
                newList.add(cd);
            }
        }
        return newList ;
    }

    /**
     * 打印列表
     * @param list
     */
    public static void print_array( ArrayList<String> list )
    {
        System.out.print("[ ");
        if( list!=null )
            for( String l : list )
                System.out.print(l+" ");
        else
            System.out.print("null");
        System.out.println("]");
    }


    public static ArrayList<String> find_same(ArrayList<String> pos, ArrayList<String> neg)
    {
        ArrayList<String> result = new ArrayList<String>();
        for (String same : neg)
        {
            if (pos.contains(same))
                result.add(same);
        }
        return result;
    }


}
