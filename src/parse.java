import org.omg.CORBA.ARG_IN;
import sun.plugin.viewer.LifeCycleManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class parse {

    /*******************************************************************************************************************
     * ①if-else or case branch statements are inconsistent
     * ④Incomplete statement, if missing else, case missing default
     * @param resultList
     */
    private static int i1;

    public static void If_else_case(ArrayList<String> resultList) throws IOException {
        int length = resultList.size();
        for (i1 = 0; i1 < length; i1++) {
            String line = resultList.get(i1);
            if (line.contains("IfStatement")) {
                if_structure(resultList);
                i1--;
            }
        }
    }


    public static ArrayList<String> IForELSE_process(ArrayList<String> resultList, String IForELSE) throws IOException {
        //此时的line指向if或者else部分的入口
        String line = resultList.get(i1);
        System.out.println("-----------into " + IForELSE + " : " + line);
        int IForELSE_space = utils.count_space(line);
        ArrayList<String> Lvalue = new ArrayList<String>();
        if (line.contains("IfStatement")) {
            Lvalue.addAll(if_structure(resultList));
            i1--;
        }
        while (true) {
            line = resultList.get(++i1);
            if (i1 == resultList.size() - 1 || utils.count_space(line) <= IForELSE_space) {
                System.out.println("-----------out " + IForELSE + " : " + line);
                if (Lvalue != null) {
                    Lvalue = utils.duplicate_removal(Lvalue);
                    Collections.sort(Lvalue);
                }
                return Lvalue;
            }
            if (line.contains("IfStatement")) {
                Lvalue.addAll(if_structure(resultList));
                i1--;
            } else {
                if (line.contains("Lvalue")) {
                    line = resultList.get(++i1);
                    Lvalue.add(utils.record_variable(line));
                    System.out.println("-->new lvalue in  : " + utils.record_variable(line));
                    i1 = i1 + 2;
                    if (i1 == resultList.size() - 1) {
                        System.out.println("-----------out " + IForELSE + " : " + line);
                        if (Lvalue != null) {
                            Lvalue = utils.duplicate_removal(Lvalue);
                            Collections.sort(Lvalue);
                        }
                        return Lvalue;
                    }
                }
            }
        }
    }


    /**
     * Handle if structure
     *
     * @param resultList
     * @return
     * @throws IOException
     */
    private static ArrayList<String> if_structure(ArrayList<String> resultList) throws IOException {
        ArrayList<String> Lvalue_if = new ArrayList<String>() ;
        ArrayList<String> Lvalue_else = new ArrayList<String>() ;
        String line = resultList.get(i1);
        int num_space = utils.count_space(line);
        int if_structure_line = utils.record_line(line);
        System.out.println("--------------------------------------------------------------into if structure : " + line);
        try {
            line = resultList.get(++i1) ;
            while (true) {
                line = resultList.get(++i1) ;
                if (utils.count_space(line) == num_space + 2)   //进入if的内部模块
                {
                    break;
                }
            }
            Lvalue_if = IForELSE_process(resultList, "if");
            System.out.print("Lvalue if :  ");
            utils.print_array(Lvalue_if);
            if (i1 == resultList.size() - 1 || utils.count_space(resultList.get(i1)) < num_space + 2) {
                System.out.println("---------------------------------------------------------------out if structure suddenly : " + resultList.get(i1) + "\n\n");
                System.err.println("Error [without else in a if structure] at line " + if_structure_line);
                return Lvalue_if;
            }
            Lvalue_else = IForELSE_process(resultList, "else");
            System.out.print("Lvalue else :  ");
            utils.print_array(Lvalue_else);
            if (!Lvalue_if.equals(Lvalue_else)) {
                System.out.println("different");
                System.err.println("Error [Lvalues are not the same between if and else] at line : " + if_structure_line);
            } else System.out.println("same");
            System.out.println("---------------------------------------------------------------out if structure : " + resultList.get(i1) + "\n\n");
            Lvalue_if.addAll(Lvalue_else);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Lvalue_if;
    }


    /*******************************************************************************************************************
     * ④Check if the case block is incomplete without default
     * @param resultList
     */
    public static void Incomplete_Case( ArrayList<String> resultList )
    {
        int length = resultList.size() ;
        boolean inCase = false ;
        boolean hasDefault = false ;
        int case_space = 0 ;
        int case_line = 0 ;
        for( int i=0 ; i<length ; i++ )
        {
            String line = resultList.get(i) ;
            if( !inCase )
            {
                if( line.contains("CaseStatement:  "))
                {
                    case_line = utils.record_line(line) ;
                    case_space = utils.count_space(line) ;
                    inCase = true ;
                    hasDefault = false ;
                    System.out.println("--------------------------------------------into case structure at : " + line );
                }
            }
            else
            {
                if( i==length-1 || utils.count_space(resultList.get(i+1)) <= case_space )
                {
                    System.out.println("---------------------------------------------last line of case : " + line + "\n\n");
                    if( !hasDefault )
                    {
                        System.err.println("Error [case intructure without default] at line : " + case_line);
                    }
                    inCase = false ;
                }
                else
                {
                    if( line.contains("Case:  ") && i+2<length )
                    {
                        System.out.println("-----case at : "+line);
                        if( utils.count_space(resultList.get(i+2))>case_space+4 )
                        {
                            System.out.println("find default! ");
                            hasDefault = true ;
                        }
                    }
                }
            }
        }
    }


    /*******************************************************************************************************************
     * ②incomplete variables in always sensitive list
     * @param resultList
     */
    public static void SensitiveList(ArrayList<String> resultList)
    {
        boolean inAlways = false;
        ArrayList<String> sensitiveList = new ArrayList<String>();
        int always_space = 0;
        int sensitive_list_line = 0;
        int length = resultList.size();

        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (inAlways)
            {
                if( utils.count_space(line)==always_space || i==length-1 ) //出always
                {
                    System.out.println("------------------------------------------------------out of the always : "+line+"\n\n");
                    inAlways = false ;
                }
                else
                {
                    if( line.contains("Rvalue:") || line.contains("CaseStatement:") || line.contains("IfStatement:") )
                    {
                        int tmp_i = i ;
                        tmp_i++ ;
                        line = resultList.get(tmp_i) ;
                        while( true )
                        {
                            if( !line.contains("And:") && !line.contains("Or:") && !line.contains("Xor:") && !line.contains("Ulnot:")
                                    && !line.contains("Unot:") && !line.contains("Identifier:") )
                            {
                                break ;
                            }
                            if( line.contains("Identifier:") )
                            {
                                String Rvalue = utils.record_variable(line) ;
                                System.out.print("Rvalue : "+Rvalue);
                                if( !sensitiveList.contains(Rvalue) )
                                {
                                    System.out.println("  x");
                                    System.err.println("Error [sensitive list without variable \"" + Rvalue + "\"] at line "+sensitive_list_line );
                                }
                                else
                                {
                                    System.out.println("  o");
                                }
                            }
                            if( tmp_i==length-1 )   break;
                            tmp_i++ ;
                            line = resultList.get(tmp_i) ;
                        }
                    }
                    continue;
                }
            }
            else
            {
                if (line.contains("Always:"))
                {
                    System.out.println("------------------------------------------------------into the always : "+line);
                    inAlways = true ;
                    always_space = utils.count_space(line) ;
                    sensitiveList = new ArrayList<String>() ;
                    sensitive_list_line = utils.record_line(line) ;

                    i = i + 2 ;
                    line = resultList.get(i) ;
                    while(true)
                    {
                        if (i == length - 1)
                            break;
                        if (utils.count_space(line) <= always_space + 2)
                        {
                            System.out.print("sensitiveList : ");
                            utils.print_array(sensitiveList);
                            i-- ;
                            break ;
                        }
                        if( line.contains("Sens: all") || line.contains("Sens: posedge") || line.contains("Sens: negedge") )                  //always引导的敏感列表:上升沿/下降沿/ALL  直接跳过
                        {
                            inAlways = false ;
                            System.out.println("------------------------------------------------------jump always " + utils.record_variable(line) + " : "+line + "\n\n");
                            break ;
                        }
                        if( line.contains(" Identifier:"))
                        {
                            sensitiveList.add(utils.record_variable(line)) ;
                        }
                        i++ ;
                        line = resultList.get(i) ;
                    }
                }
            }
        }
    }

    /*******************************************************************************************************************
     * ④The unified always module uses both the posedge and the negedge of a signal
     * @param resultList
     */
    public static void SingalState (ArrayList<String> resultList)
    {
        boolean inAlways = false;
        ArrayList<String> posedgeList = new ArrayList<String>();
        ArrayList<String> negedgeList = new ArrayList<String>();
        int always_space = 0;
        int length = resultList.size();
        int sensitive_list_line = 0;

        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (inAlways)
            {
                if( utils.count_space(line)==always_space || i==length-1 ) //出always
                {
                    System.out.println("------------------------------------------------------out of the always : "+line+"\n\n");
                    inAlways = false ;
                    i--;
                }
            }
            else
            {
                if (line.contains("Always:"))
                {
                    System.out.println("------------------------------------------------------into the always : " + line);
                    inAlways = true ;
                    always_space = utils.count_space(line) ;
                    posedgeList = new ArrayList<String>() ;
                    sensitive_list_line = utils.record_line(line) ;
                    negedgeList = new ArrayList<String>() ;

                    i = i + 2 ;
                    line = resultList.get(i) ;
                    while(true)
                    {
                        if (i == length - 1)
                            break;
                        if (utils.count_space(line) <= always_space + 2)
                        {
                            System.out.print("posedgeList : ");
                            utils.print_array(posedgeList);
                            System.out.print("negedgeList : ");
                            utils.print_array(negedgeList);
                            ArrayList<String> result = new ArrayList<String>();
                            result = utils.find_same(posedgeList, negedgeList);
                            if (!result.isEmpty())
                                System.err.println("Error [posedge and negedge are used for the same signal \"" + result + "\"] at line "+sensitive_list_line );
                            i-- ;
                            break ;
                        }
                        if (line.contains("posedge"))
                        {
                            line = resultList.get(++i);
                            posedgeList.add(utils.record_variable(line)) ;

                        }
                        if (line.contains("negedge"))
                        {
                            line = resultList.get(++i);
                            negedgeList.add(utils.record_variable(line)) ;

                        }
                        i++ ;
                        line = resultList.get(i) ;
                    }
                }
            }
        }
    }


    /*******************************************************************************************************************
     * ⑦Abusing blocking assignment and nonblocking assignment
     * @param resultList
     */
    public static void BlockorNonblockAssign (ArrayList<String> resultList)
    {
        boolean inAlways = false;
        boolean ifSequence = false;
        int always_space = 0;
        int length = resultList.size();
        int sensitive_list_line = 0;

        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (inAlways)
            {
                if( utils.count_space(line)==always_space || i==length-1 ) //出always
                {
                    System.out.println("------------------------------------------------------out of the always : "+line+"\n\n");
                    inAlways = false ;
                    i--;
                }
            }
            else
            {
                if (line.contains("Always:") && resultList.get(i + 2).contains("Sens: all"))
                {
                    System.out.println("-----------------------------------------------into the combination always : " + line);
                    inAlways = true ;
                    ifSequence = false ;
                    always_space = utils.count_space(line) ;
                    sensitive_list_line = utils.record_line(line) ;

                    i = i + 2;
                    line = resultList.get(i) ;
                    while (true)
                    {
                        if (i == length - 1)
                            break;
                        if (utils.count_space(line) == always_space)
                        {
                            i-- ;
                            break ;
                        }
                        if (line.contains("Lvalue: "))
                        {
                            i = i - 1;
                            if (resultList.get(i).contains("BlockingSubstitution"))
                            {
                                i = i + 3;
                            }
                            else if (resultList.get(i).contains("NonblockingSubstitution"))
                            {
                                System.err.println("Error [Nonblocking substitution is used for sequence logic.]" + resultList.get(i));
                                i = i + 2;
                            }
                        }
                        i++ ;
                        line = resultList.get(i) ;
                    }
                }
                else if (line.contains("Always:") && !resultList.get(i + 2).contains("Sens: all"))
                {
                    System.out.println("--------------------------------------------into the sequence always : " + line);
                    inAlways = true ;
                    ifSequence = true ;
                    always_space = utils.count_space(line) ;
                    sensitive_list_line = utils.record_line(line) ;

                    i = i + 2;
                    line = resultList.get(i) ;
                    while (true)
                    {
                        if (i == length - 1)
                            break;
                        if (utils.count_space(line) == always_space)
                        {
                            i-- ;
                            break ;
                        }
                        if (line.contains("Lvalue: "))
                        {
                            i = i - 1;
                            if (resultList.get(i).contains("NonblockingSubstitution"))
                            {
                                i = i + 3;
                            }
                            else if (resultList.get(i).contains("BlockingSubstitution"))
                            {
                                System.err.println("Error [Blocking substitution is used for combination logic.]" + resultList.get(i));
                                i = i + 2;
                            }
                        }
                        i++ ;
                        line = resultList.get(i) ;
                    }
                }
            }
        }
    }
    
    /*******************************************************************************************************************
     * ⑩Cycle condition error, cycle will always run
     * @param resultList
     */
    public static void CycleConditionError (ArrayList<String> resultList)
    {
        ArrayList<String> varList = new ArrayList<String>();
        ArrayList<Integer> numList = new ArrayList<Integer>();
        int length = resultList.size();
        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (line.contains("IntConst") && resultList.get(i - 1).contains("IntConst"))
            {
                String var = resultList.get(i - 3).substring( resultList.get(i - 3).indexOf(":")+2 , resultList.get(i - 3).lastIndexOf(",") ) ;
                int bits = utils.intconst_num(resultList.get(i)) - utils.intconst_num(resultList.get(i - 1));
                bits = java.lang.Math.abs(bits) + 1;
                varList.add(var);
                numList.add(bits);
            }
            if (line.contains("LessThan"))
            {
                i = i + 1;
                String tempVar = resultList.get(i).substring( resultList.get(i).indexOf(":")+2 , resultList.get(i).lastIndexOf("(")-1 ) ;
                int indexVar = varList.indexOf(tempVar);
                int tempNum = utils.intconst_num(resultList.get(i + 1));
                int bitsNum;
                if (indexVar == -1)
                    bitsNum = 1;
                else
                    bitsNum = numList.get(indexVar);
                if (Math.pow(2,bitsNum) - 1 < tempNum)
                {
                    System.err.println("Error [The Cycle Condition is always true] at line " + resultList.get(i-1));
                }
            }
            if (line.contains("LessEq"))
            {
                i = i + 1;
                String tempVar = resultList.get(i).substring( resultList.get(i).indexOf(":")+2 , resultList.get(i).lastIndexOf("(")-1 ) ;
                int tempNum = utils.intconst_num(resultList.get(i + 1));
                int indexVar = varList.indexOf(tempVar);
                int bitsNum;
                if (indexVar == -1)
                    bitsNum = 1;
                else
                    bitsNum = numList.get(indexVar);
                if (Math.pow(2,bitsNum) < tempNum)
                {
                    System.err.println("Error [The Cycle Condition is always true \"" + "\"] at line " + resultList.get(i-1));
                }
            }
        }
    }
    
     /*******************************************************************************************************************
     * ⑧ariable multiple assignments in different always
     * @param resultList
     */
    public static void VarAssignMultipleInAlways (ArrayList<String> resultList)
    {
        ArrayList<String> AllLvalue = new ArrayList<String>();
        ArrayList<String> AlwaysLvalue = new ArrayList<String>();
        boolean inAlways = false;
        int always_space = 0;
        int length = resultList.size();

        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            String lvalue;
            if (inAlways)
            {
                if( utils.count_space(line)==always_space || i==length-1 )
                {
                    System.out.println("------------------------------------------------------out of the always : "+line+"\n\n");
                    inAlways = false ;
                    AllLvalue.addAll(AlwaysLvalue);
                    AllLvalue = utils.duplicate_removal(AllLvalue);
                    i--;
                }
            }
            else
            {
                if (line.contains("Always:"))
                {
                    System.out.println("-------------------------------------------------------into the always : " + line);
                    inAlways = true ;
                    always_space = utils.count_space(line) ;

                    i = i + 2;
                    line = resultList.get(i) ;
                    while (true)
                    {
                        if (i == length - 1)
                            break;
                        if (utils.count_space(line) == always_space)
                        {
                            i-- ;
                            break ;
                        }
                        if (line.contains("Lvalue: "))
                        {
                            i = i + 1;
                            lvalue = utils.record_variable(resultList.get(i));
                            if (!AlwaysLvalue.contains(lvalue))
                                AlwaysLvalue.add(lvalue);
                            if (AllLvalue.contains(lvalue))
                            {
                                System.err.println("Error [Variable multiple assignments in different \"always\".]" + resultList.get(i));
                            }
                        }
                        i++ ;
                        line = resultList.get(i) ;
                    }
                }
            }
        }


    
}
