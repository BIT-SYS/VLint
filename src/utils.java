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
     * 抽取line中的定义变量名称
     * @param line
     * @return
     */
    public static String record_define_variable( String line )
    {
        return line.substring( line.indexOf(":")+2 , line.lastIndexOf(",") ) ;
    }

    /**
     * 抽取line中的符号名称
     * @param line
     * @return
     */
    public static String record_symbol( String line )
    {
        return line.substring( line.indexOf(":")-2 , line.indexOf(":") ) ;
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
     * 判断是否含有重复元素
     * @param list1
     * @return
     */
    public static boolean if_duplicate(ArrayList<String> list1)
    {
        ArrayList<String> newList = new ArrayList<String>();
        boolean result = false;
        for (String cd : list1) {
            if (!newList.contains(cd)) {
                newList.add(cd);
            }
            else
            {
                result = true;
            }
        }
        return result;
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

    /**
     * 找出重复元素
     * @param pos, neg
     */
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

    /**
     * 抽取IntConst行中的数字
     * @param line
     * @return
     */
    public static int intconst_num( String line )
    {
        String num =  line.substring( line.indexOf(":")+2 , line.lastIndexOf("(")-1 ) ;
        int i = 0;
        int index = num.indexOf('\'');
        if (num.length() > 2 && index != -1 && num.length() < 16)
        {
            num = num.substring(index + 1);
            if (num.charAt(0) == 'h')
            {
                num = num.substring(1);
                i = Integer.parseInt(num , 16);
            }
            else if (num.charAt(0) == 'o')
            {
                num = num.substring(1);
                i = Integer.parseInt(num , 8);
            }
            else  if (num.charAt(0) == 'd')
            {
                num = num.substring(1);
                i = Integer.parseInt(num, 10);
            }
            else if (num.charAt(0) == 'b')
            {
                if (num.contains("x") || num.contains("Z"))
                    return -1;
                else
                {
                    num = num.substring(1);
                    i = Integer.parseInt(num , 2);
                }
            }
            return i;
        }
        else if(num.length() > 16)
        {
            return -2;
        }
        else
        {
            i = Integer.parseInt( num );
            return i;
        }
    }


    /**
     * 抽取IntConst行中的位数
     * @param line
     * @return
     */
    public static int intconst_width( String line )
    {
        String num =  line.substring( line.indexOf(":")+2 , line.lastIndexOf("(")-1 ) ;
        int i = 0;
        int index = num.indexOf('\'');
        if (num.length() > 2 && index != -1)
        {
            num = num.substring(0, index);
            i = Integer.parseInt(num);
            return i;
        }
        else return Integer.parseInt(num);
    }

    /**
     * find the tree node
     * @param node, level, string type, line number
     * @return the aim node
     */
    public static treeNode find_node(treeNode node , int level , String str_type , int line_num)
    {
        treeNode return_node = new treeNode();
        if( node.childList.size()>0 )
        {
            for( treeNode childnode : node.childList )
            {
                //System.out.println(line_num);
                if ( childnode.line == line_num && childnode.type.equals(str_type))
                {
                    return childnode;
                }
                else {
                    return_node = find_node(childnode,level+2,str_type,line_num);
                    if( return_node.line!=0 )
                    {
                        return return_node ;
                    }
                }
            }
        }
        return return_node;
    }

    /**
     * 判断是否是2的倍数
     * @param n
     * @return
     */
    public static boolean is_two_power(int n) {
        return n>0 && (n&(n-1)) == 0;
    }

}
