import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class parse {

    public static ArrayList<String> AllVariable = new ArrayList<>();
    public static ArrayList<Integer> AllVarWidth= new ArrayList<>();
    public static ArrayList<String> AllParameter = new ArrayList<>();
    public static ArrayList<Integer> AllParValue  = new ArrayList<>();
    public static ArrayList<String> AllParString = new ArrayList<>();

    public static void StoreVarAndWidth(ArrayList<String> resultList)
    {
        int length = resultList.size();
        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (line.contains(",") && (line.contains("False") || line.contains("True")))
            {
                if (line.contains("Parameter:") || line.contains("Localparam:"))
                {
                    //System.out.println(line);
                    AllParameter.add(utils.record_define_variable(resultList.get(i)));
                    if (resultList.get(i+2).contains("IntConst:"))
                    {
                        AllParValue.add(utils.intconst_num(resultList.get(i+2)));
                        AllParString.add(utils.record_variable(resultList.get(i+2)));
                    }
                    else if (resultList.get(i+2).contains("Plus:"))
                    {
                        if (resultList.get(i+3).contains("Identifier:"))
                        {
                            int index = AllParameter.indexOf(utils.record_variable(resultList.get(i+3)));
                            int value = AllParValue.get(index);
                            value += utils.intconst_num(resultList.get(i+4));
                            AllParValue.add(value);
                            AllParString.add(String.valueOf(value));
                        }
                        else if (resultList.get(i+3).contains("IntConst:"))
                        {
                            int index = AllParameter.indexOf(utils.record_variable(resultList.get(i+4)));
                            int value = AllParValue.get(index);
                            value += utils.intconst_num(resultList.get(i+3));
                            AllParValue.add(value);
                            AllParString.add(String.valueOf(value));
                        }
                    }
                    else if (resultList.get(i+2).contains("Minus:"))
                    {
                        if (resultList.get(i+3).contains("Identifier:"))
                        {
                            int index = AllParameter.indexOf(utils.record_variable(resultList.get(i+3)));
                            int value = AllParValue.get(index);
                            value -= utils.intconst_num(resultList.get(i+4));
                            AllParValue.add(value);
                            AllParString.add(String.valueOf(value));
                        }
                        else if (resultList.get(i+3).contains("IntConst:"))
                        {
                            int index = AllParameter.indexOf(utils.record_variable(resultList.get(i+4)));
                            int value = AllParValue.get(index);
                            value = utils.intconst_num(resultList.get(i+3)) - value;
                            AllParValue.add(value);
                            AllParString.add(String.valueOf(value));
                        }
                    }
                    else if (resultList.get(i+2).contains("Power:"))
                    {
                        if (resultList.get(i+3).contains("Identifier:"))
                        {
                            int index = AllParameter.indexOf(utils.record_variable(resultList.get(i+3)));
                            int value = AllParValue.get(index);
                            value = (int)Math.pow(value, utils.intconst_num(resultList.get(i+4)));
                            AllParValue.add(value);
                            AllParString.add(String.valueOf(value));
                        }
                        else if (resultList.get(i+3).contains("IntConst:"))
                        {
                            int index = AllParameter.indexOf(utils.record_variable(resultList.get(i+4)));
                            int value = AllParValue.get(index);
                            value = (int)Math.pow(utils.intconst_num(resultList.get(i+3)), value);
                            AllParValue.add(value);
                            AllParString.add(String.valueOf(value));
                        }
                    }
                    else
                    {
                        AllParValue.add(-1);
                        AllParString.add(String.valueOf(-1));
                    }
                }
                else
                {
                    AllVariable.add(utils.record_define_variable(resultList.get(i)));
                    if (resultList.get(i+1).contains("Width:"))
                    {
                        if (resultList.get(i+2).contains("IntConst:"))
                        {
                            int width = Math.abs(utils.intconst_num(resultList.get(i+2))-utils.intconst_num(resultList.get(i+3)))+1;
                            AllVarWidth.add(width);
                        }
                        else if (resultList.get(i+2).contains("Minus:"))
                        {
                            if (resultList.get(i+3).contains("Identifier:"))
                            {
                                String var = utils.record_variable(resultList.get(i+3));
                                int index = AllParameter.indexOf(var);
                                if (index != -1)
                                {
                                    int number = AllParValue.get(index);
                                    int width = Math.abs(number - utils.intconst_num(resultList.get(i+4)) - utils.intconst_num(resultList.get(i+5)))+1;
                                    AllVarWidth.add(width);
                                }
                            }
                            else if (resultList.get(i+3).contains("IntConst:"))
                            {
                                if (resultList.get(i+4).contains("IntConst:"))
                                {
                                    int width = Math.abs(utils.intconst_num(resultList.get(i+3))-utils.intconst_num(resultList.get(i+4))-utils.intconst_num(resultList.get(i+5)))+1;
                                    AllVarWidth.add(width);
                                }
                            }
                            else if (resultList.get(i+3).contains("Divide:"))
                            {
                                if (resultList.get(i+4).contains("Identifier:"))
                                {
                                    String var = utils.record_variable(resultList.get(i+4));
                                    if (AllParameter.indexOf(var) != -1)
                                    {
                                        int number = AllParValue.get(AllParameter.indexOf(var));
                                        int width = Math.abs(number / utils.intconst_num(resultList.get(i+5)) - utils.intconst_num(resultList.get(i+6)))+1;
                                        AllVarWidth.add(width);
                                    }
                                }
                            }
                            else if (resultList.get(i+3).contains("Times:"))
                            {
                                if (resultList.get(i+4).contains("Identifier:"))
                                {
                                    String var = utils.record_variable(resultList.get(i+4));
                                    if (AllParameter.indexOf(var) != -1)
                                    {
                                        int number = AllParValue.get(AllParameter.indexOf(var));
                                        int width = Math.abs(number * utils.intconst_num(resultList.get(i+5)) - utils.intconst_num(resultList.get(i+6)))+1;
                                        AllVarWidth.add(width);
                                    }
                                }
                            }
                        }
                        else if (resultList.get(i+2).contains("Identifier:") && utils.count_space(resultList.get(i+2)) == utils.count_space(resultList.get(i+3)))
                        {
                            String var = utils.record_variable(resultList.get(i+2));
                            int index = AllParameter.indexOf(var);
                            int number = AllParValue.get(index);
                            int width = number - utils.intconst_num(resultList.get(i+3)) + 1;
                            AllVarWidth.add(width);
                        }
                    }
                    else
                    {
                        AllVarWidth.add(1);
                    }
                }
            }
        }
    }



    /*******************************************************************************************************************
     * ①if-else or case branch statements are inconsistent
     * ④Incomplete statement, if missing else, case missing default
     * @param resultList
     */
    private static int i1;
    private static int index = 0;
    public static void IfElseCase(ArrayList<String> resultList) throws IOException {
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
        //System.out.println("-----------into " + IForELSE + " : " + line);
        int IForELSE_space = utils.count_space(line);
        ArrayList<String> Lvalue = new ArrayList<String>();
        if (line.contains("IfStatement")) {
            Lvalue.addAll(if_structure(resultList));
            i1--;
        }
        while (true) {
            line = resultList.get(++i1);
            if (i1 == resultList.size() - 1 || utils.count_space(line) <= IForELSE_space) {
                //System.out.println("-----------out " + IForELSE + " : " + line);
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
                    if (resultList.get(i1+2).contains("Identifier"))
                    {
                        line = resultList.get(i1+2);
                        i1 += 2;
                    }
                    else
                    {
                        line = resultList.get(i1+1);
                        i1 += 1;
                    }
//                    line = (resultList.get(i1+2).contains("Identifier")) ? resultList.get(i1+2) : resultList.get(i1+1);
//                    if (!line.contains("Identifier"))
//                    line = resultList.get(++i1);
                    Lvalue.add(utils.record_variable(line));
                    //System.err.println(Lvalue + "---------");
                    //System.out.println("-->new lvalue in  : " + utils.record_variable(line));
                    i1 = i1 + 2;
                    if (i1 == resultList.size() - 1) {
                        //System.out.println("-----------out " + IForELSE + " : " + line);
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
        //System.out.println("--------------------------------------------------------------into if structure : " + line);
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
            //System.err.println("Lvalue_if = " +  Lvalue_if);
            if (i1 == resultList.size() - 1 || utils.count_space(resultList.get(i1)) < num_space + 2) {
                //System.out.println("---------------------------------------------------------------out if structure suddenly : " + resultList.get(i1) + "\n\n");
                System.err.println("Error004 [without else in a if structure] at line " + if_structure_line);
                return Lvalue_if;
            }
            Lvalue_else = IForELSE_process(resultList, "else");
            //System.err.println("Lvalue_else = " +  Lvalue_else);
            if (!Lvalue_if.equals(Lvalue_else) && !Lvalue_else.isEmpty()) {
                System.err.println("Error001 [Lvalues are not the same between if and else] at line : " + if_structure_line);
            } //else System.out.println("same");
            //System.out.println("---------------------------------------------------------------out if structure : " + resultList.get(i1) + "\n\n");
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
    public static void IncompleteCase( ArrayList<String> resultList, treeNode rootNode)
    {
        int length = resultList.size() ;
        boolean inCase = false ;
        boolean hasDefault = false ;
        int case_space = 0 ;
        int case_line = 0 ;
        for( int i=0 ; i<length ; i++ )
        {
            String line = resultList.get(i) ;
            if( line.contains("CaseStatement:  "))
            {
                ArrayList<String> CaseConditions = new ArrayList<String>();
                String case_value ;
                String str_type = "CaseStatement";
                int line_num = utils.record_line(line);
                treeNode caseNode ;
                boolean flag_defult = false;
                boolean flag_two = false;

                caseNode = utils.find_node(rootNode, 0, str_type, line_num);
                for (int index_out = 0; index_out < caseNode.childList.size(); index_out++)
                {
                    if (caseNode.childList.get(index_out).type.equals("Case") )
                    {
                        //System.err.println(1);
                        int index_in = 0;
                        while(true)
                        {
                            if (caseNode.childList.get(index_out).childList.get(index_in).type.equals("Identifier") || caseNode.childList.get(index_out).childList.get(index_in).type.equals("IntConst"))
                            {
                                case_value = caseNode.childList.get(index_out).childList.get(index_in).value;
                                //System.err.println(case_value);
                                CaseConditions.add(case_value);
                                index_in++;
                            }
                            else break;
                        }
                    }
                }
                case_line = utils.record_line(line) ;
                case_space = utils.count_space(line) ;
                inCase = true ;
                hasDefault = false ;
                if (!utils.is_two_power(CaseConditions.size()) && CaseConditions.size() != 0) {
//                    System.err.println(CaseConditions.size());
//                    System.err.println("Error [case intructure without default] at line : " + case_line);
                    flag_two = true;
                }
                if (caseNode.childList.get(caseNode.childList.size() - 1).childList.size() != 1)
                {
                    //System.err.println("Error [case intructure without default] at line : " + case_line);
                    flag_defult = true;
                }
                if (flag_defult && flag_two)
                {
                    System.err.println("Error004 [case intructure without default] at line : " + case_line);
                }
            }
//            if( !inCase )
//            {
//
//            }
//            else
//            {
//                if( i==length-1 || utils.count_space(resultList.get(i+1)) <= case_space )
//                {
//                    //System.out.println("---------------------------------------------last line of case : " + line + "\n\n");
//                    if( !hasDefault )
//                    {
//                        System.err.println("Error [case intructure without default] at line : " + case_line);
//                    }
//                    inCase = false ;
//                }
//                else
//                {
//                    if( line.contains("Case:  ") && i+2<length )
//                    {
//                        //System.out.println("-----case at : "+line);
//                        if( utils.count_space(resultList.get(i+2))>case_space+4 )
//                        {
//                            hasDefault = true ;
//                        }
//                    }
//                }
//            }
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
        ArrayList<String> parameterList = new ArrayList<String>();
        int always_space = 0;
        int sensitive_list_line = 0;
        int length = resultList.size();

        for (int j = 0; j < length; j++)
        {
            String line = resultList.get(j);
            if (line.contains("Parameter"))
            {
                parameterList.add(utils.record_define_variable(line));
            }
        }

        utils.duplicate_removal(parameterList);
        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (inAlways)
            {
                if( utils.count_space(line)==always_space || i==length-1 )
                {
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
                            if( line.contains("Identifier:") && !parameterList.contains(utils.record_variable(line)))
                            {
                                String Rvalue = utils.record_variable(line) ;
                                if( !sensitiveList.contains(Rvalue) )
                                {
                                    System.err.println("Error002 [sensitive list without variable \"" + Rvalue + "\"] at line "+sensitive_list_line );
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
                            i-- ;
                            break ;
                        }
                        if( line.contains("Sens: all") || line.contains("Sens: posedge") || line.contains("Sens: negedge") )
                        {
                            inAlways = false ;
                            break ;
                        }
                        if( line.contains(" Identifier:") && !parameterList.contains(utils.record_variable(line)))
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
     * ⑤The unified always module uses both the posedge and the negedge of a signal
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
                    //System.out.println("------------------------------------------------------out of the always : "+line+"\n\n");
                    inAlways = false ;
                    i--;
                }
            }
            else
            {
                if (line.contains("Always:"))
                {
                    //System.out.println("------------------------------------------------------into the always : " + line);
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
                            //System.out.print("posedgeList : ");
                            //utils.print_array(posedgeList);
                            //System.out.print("negedgeList : ");
                            //utils.print_array(negedgeList);
                            ArrayList<String> result = new ArrayList<String>();
                            result = utils.find_same(posedgeList, negedgeList);
                            if (!result.isEmpty())
                                System.err.println("Error005 [posedge and negedge are used for the same signal \"" + result + "\"] at line "+sensitive_list_line );
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
        int for_space = 0;
        int length = resultList.size();
        int sensitive_list_line = 0;

        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (inAlways)
            {
                if( utils.count_space(line)==always_space || i==length-1 ) //出always
                {
                    //System.out.println("------------------------------------------------------out of the always : "+line+"\n\n");
                    inAlways = false ;
                    i--;
                }
            }
            else
            {
                if (line.contains("Always:") && (resultList.get(i + 2).contains("Sens: all") || resultList.get(i + 2).contains("Sens: level")))
                {
                    //System.out.println("---------------------------------------------into the combination always : " + line);
                    inAlways = true ;
                    always_space = utils.count_space(line) ;
                    sensitive_list_line = utils.record_line(line) ;

                    i = i + 2;
                    line = resultList.get(i) ;
                    while (true)
                    {
                        if (line.contains("ForStatement"))
                        {
                            i += 12;
                        }
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
                                sensitive_list_line = utils.record_line(resultList.get(i));
                                System.err.println("Error007 [Nonblocking substitution is used for combination logic.] at line " + sensitive_list_line);
                                i = i + 2;
                            }
                        }
                        i++ ;
                        line = resultList.get(i) ;
                    }
                }
                else if (line.contains("Always:") && !(resultList.get(i + 2).contains("Sens: all") || resultList.get(i + 2).contains("Sens: level")))
                {
                    //System.out.println("--------------------------------------------into the sequence always : " + line);
                    inAlways = true ;
                    ifSequence = true ;
                    always_space = utils.count_space(line) ;
                    sensitive_list_line = utils.record_line(line) ;

                    i = i + 2;
                    line = resultList.get(i) ;
                    while (true)
                    {
                        if (line.contains("ForStatement"))
                        {
                            i += 12;
                        }
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
                                sensitive_list_line = utils.record_line(resultList.get(i));
                                System.err.println("Error007 [Blocking substitution is used for sequence logic.] at line " + sensitive_list_line);
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
     * ⑩Wrong use of cyclic variable bit width.
     * @param resultList
     */
    public static void CycleConditionError (ArrayList<String> resultList)
    {
        ArrayList<String> varList = new ArrayList<String>();
        ArrayList<Integer> numList = new ArrayList<Integer>();
        int length = resultList.size();
        int sensitive_list_line;
        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (line.contains("IntConst") && resultList.get(i - 1).contains("IntConst") && resultList.get(i - 4).contains("Decl:"))
            {
                String var = resultList.get(i - 3).substring( resultList.get(i - 3).indexOf(":")+2 , resultList.get(i - 3).lastIndexOf(",") ) ;
                int bits = utils.intconst_num(resultList.get(i)) - utils.intconst_num(resultList.get(i - 1));
                bits = java.lang.Math.abs(bits) + 1;
                varList.add(var);
                numList.add(bits);
            }
            if (line.contains("LessThan"))
            {
                int j = i;
                int Less_space = utils.count_space(line);
                while(true)
                {
                    j--;
                    if (utils.count_space(resultList.get(j)) == Less_space - 2)
                    {
                        if (!resultList.get(j).contains("ForStatement"))
                            break;
                    }
                }
                i = i + 1;
                String tempVar = resultList.get(i).substring( resultList.get(i).indexOf(":")+2 , resultList.get(i).lastIndexOf("(")-1 ) ;
                int indexVar = varList.indexOf(tempVar);
                if (!resultList.get(i + 1).contains("IntConst"))
                {
                    continue;
                }
                int tempNum = utils.intconst_num(resultList.get(i + 1));
                int bitsNum;
                if (indexVar == -1)
                    bitsNum = 64;
                else
                    bitsNum = numList.get(indexVar);
                if (Math.pow(2,bitsNum) - 1 < tempNum)
                {
                    sensitive_list_line = utils.record_line(resultList.get(i-1));
                    System.err.println("Error010 [The Cycle Condition is always true] at line " + sensitive_list_line);
                }
            }
            if (line.contains("LessEq"))
            {
                int j = i;
                int Less_space = utils.count_space(line);
                while(true)
                {
                    j--;
                    if (utils.count_space(resultList.get(j)) == Less_space - 2)
                    {
                        if (!resultList.get(j).contains("ForStatement"))
                            break;
                    }
                }
                i = i + 1;
                String tempVar = resultList.get(i).substring( resultList.get(i).indexOf(":")+2 , resultList.get(i).lastIndexOf("(")-1 ) ;
                int tempNum;
                if (resultList.get(i+1).contains("IntConst"))
                {
                    tempNum = utils.intconst_num(resultList.get(i + 1));
                }
                else continue;
                int indexVar = varList.indexOf(tempVar);
                int bitsNum;
                if (indexVar == -1)
                    bitsNum = 1;
                else
                    bitsNum = numList.get(indexVar);
                if (Math.pow(2,bitsNum) < tempNum)
                {
                    sensitive_list_line = utils.record_line(resultList.get(i-1));
                    System.err.println("Error010 [The Cycle Condition is always true] at line " + sensitive_list_line);
                }
            }
        }
    }


    /*******************************************************************************************************************
     * ⑧ariable multiple assignments in different always
     * @param resultList
     */
    public static void VarAssignMultipleInAlways (ArrayList<String> resultList, treeNode rootNode)
    {
        ArrayList<String> AllLvalue = new ArrayList<String>();
        ArrayList<String> AlwaysLvalue = new ArrayList<String>();
        ArrayList<String> AlwaysDecl = new ArrayList<String>();
        treeNode alwaysNode = new treeNode();
        treeNode Node = new treeNode();
        boolean inAlways = false;
        int always_space = 0;
        int length = resultList.size();
        int sensitive_list_line = 0;
        int if_index = 1;

        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            String lvalue;

            if (inAlways)
            {
                i--;
                if( utils.count_space(line)<=always_space || i==length-1 )
                {
                    //System.out.println("------------------------------------------------------out of the always : "+line+"\n\n");
                    inAlways = false ;
                    AllLvalue.addAll(AlwaysLvalue);
                    AllLvalue = utils.duplicate_removal(AllLvalue);
                    i--;
                    if_index = 1;
                }
            }
            else
            {
                if (line.contains("ModuleDef:"))
                {
                    AllLvalue.clear();
                    AlwaysLvalue.clear();
                    AlwaysDecl.clear();
                }
                if (line.contains("IfStatement"))
                {
                    String str_type = "IfStatement";
                    int line_num = utils.record_line(line);
                    Node = utils.find_node(rootNode, 0, str_type, line_num);
                    alwaysNode = Node.childList.get(1);
                }
                if (line.contains("Always:"))
                {
                    //System.out.println("-------------------------------------------------------into the always : " + line);
                    inAlways = true ;
                    always_space = utils.count_space(line) ;

                    i = i + 2;
                    line = resultList.get(i) ;
                    while (true)
                    {
                        int always_flag = 0;
                        int if_flag = 0;
                        if (alwaysNode.childList != null && !alwaysNode.childList.isEmpty() )
                        {
                            //System.out.println("alwaysNode  = " + alwaysNode.type);
                            for (treeNode childnode : alwaysNode.childList)
                            {
                                if (childnode.line == utils.record_line(resultList.get(i)))
                                {
                                    always_flag = 1;
                                }
                            }
                        }
                        //System.out.println("Node  = " + Node);
                        if (Node.childList != null && !Node.childList.isEmpty())
                        {
                            //System.out.println("Node  = " + Node);
                            for (treeNode childnode : Node.childList)
                            {
                                if (childnode.line == utils.record_line(resultList.get(i)))
                                {
                                    if_flag = 1;
                                    if_index++;
                                }
                            }
                        }
                        if (if_flag == 1)
                        {
                            alwaysNode = Node.childList.get(if_index);
                        }
                        if (i == length - 1)
                            break;
                        if (utils.count_space(resultList.get(i)) <= always_space && always_flag == 0)
                        {
                            if (if_flag == 0)
                            {
                                i-- ;
                                break ;
                            }
                        }
                        if (line.contains("Decl:"))
                        {
                            while(true)
                            {
                                if (resultList.get(i+1).contains(","))
                                {
                                    AlwaysDecl.add(utils.record_define_variable(resultList.get(i+1)));
                                    i++;
                                }
                                else break;
                            }
                        }
                        if (line.contains("Lvalue: "))
                        {
                            while (!resultList.get(i).contains("Identifier"))
                            {
                                i = i + 1;
                            }
                            if (!resultList.get(i+1).contains("IntConst:") && !AlwaysDecl.contains(utils.record_variable(resultList.get(i))))
                            {
                                lvalue = utils.record_variable(resultList.get(i));
                            }
                            else
                            {
                                i++ ;
                                line = resultList.get(i) ;
                                continue;
                            }
                            if (!AlwaysLvalue.contains(lvalue))
                                AlwaysLvalue.add(lvalue);
                            if (AllLvalue.contains(lvalue))
                            {
                                sensitive_list_line = utils.record_line(resultList.get(i));
                                System.err.println("Error008 [Variable " + lvalue + " multiple assignments in different \"always\".] at line " + sensitive_list_line);
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
     * ⑨Same judgment conditions in case or if statement
     * @param resultList
     */
    public static void SameJudgmentConditions (ArrayList<String> resultList, treeNode rootNode)
    {
        boolean inJudgement = false;
        int judgement_space = 0;
        int length = resultList.size();
        int sensitive_list_line;

        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (inJudgement)
            {
                if (utils.count_space(line) == judgement_space || i == length - 1)
                {
                    //System.out.println("------------------------------------------------------out of the branch : " + line + "\n\n");
                    inJudgement = false;
                    //i--;
                }
            }
            else
            {
                if (line.contains("IfStatement:"))
                {
                    ArrayList<String> IfJudgeConditions = new ArrayList<String>();
                    String rvalue;
                    String lvalue;
                    String judge_symbol;
                    //System.out.println("----------------------------------------------------into the if branch : " + line);
                    inJudgement = true ;

                    line = resultList.get(i);
                    judgement_space = utils.count_space(line);

                    while (true)
                    {
                        //String line_in_if = resultList.get(i);
                        if (i == length - 1)
                            break;
                        if (utils.count_space(line) < judgement_space)
                        {
                            i--;
                            break;
                        }
                        if (line.contains("IfStatement:") && resultList.get(i+1).contains("Eq"))
                        {
                            if (!(resultList.get(i+2).contains("Identifier:") && resultList.get(i+3).contains("IntConst:")))
                                break;
                            judge_symbol = utils.record_symbol(resultList.get(i+1));
                            lvalue = utils.record_variable(resultList.get(i+2));
                            rvalue = utils.record_variable(resultList.get(i+3));
                            IfJudgeConditions.add(judge_symbol+lvalue+rvalue);
                            if (utils.if_duplicate(IfJudgeConditions))
                            {
                                sensitive_list_line = utils.record_line(resultList.get(i));
                                System.err.println("Error009 [Same Judgment Conditions in \"if\".] at line " + sensitive_list_line);
                                break;
                            }
                        }
                        i++;
                        line = resultList.get(i);
                    }
                }
                if (line.contains("CaseStatement:"))
                {
                    treeNode Node ;
                    ArrayList<String> CaseConditions = new ArrayList<String>();
                    String case_value ;

                    //System.out.println("--------------------------------------------------into the case branch : " + line);
                    inJudgement = true ;
                    judgement_space = utils.count_space(line);

                    line = resultList.get(i);
                    String str_type = "CaseStatement";
                    int line_num = utils.record_line(line);
                    Node = utils.find_node(rootNode, 0, str_type, line_num);
                    //System.err.println(Node.type+Node.line);
                    for (int index_out = 0; index_out < Node.childList.size(); index_out++)
                    {
                        if (Node.childList.get(index_out).type.equals("Case") )
                        {
                            //System.err.println(1);
                            int index_in = 0;
                            while(true)
                            {
                                if (Node.childList.get(index_out).childList.get(index_in).type.equals("Identifier") || Node.childList.get(index_out).childList.get(index_in).type.equals("IntConst"))
                                {
                                    case_value = Node.childList.get(index_out).childList.get(index_in).value;
                                    //System.err.println(Node.childList.get(index_out).childList.get(index_in).line);
                                    CaseConditions.add(case_value);
                                    index_in++;
                                }
                                else break;
                            }
                        }
                    }
                    //System.out.println("CaseConditions = " + CaseConditions);
                    if (utils.if_duplicate(CaseConditions))
                    {
                        sensitive_list_line = utils.record_line(resultList.get(i));
                        HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
                        int hash_index = 1;
                        ArrayList<String> result = new ArrayList<String>();
                        for (String str : CaseConditions)
                        {
                            if (hashMap.get(str) != null)
                            {
                                Integer num = hashMap.get(str);
                                if (num == null)
                                {
                                    hashMap.put(str, hash_index+1);
                                    hash_index++;
                                }
                                else
                                {
                                    result.add(str);
                                }
                            }
                            else
                            {
                                hashMap.put(str, 1);
                            }
                        }
                        System.err.println("Error009 [Same Judgment " + result + " in \"case\".] at line "  + sensitive_list_line);
                        //break;
                    }
                    while (true)
                    {
                        //System.out.println(i);
                        if (i == length - 1)
                            break;
                        if (utils.count_space(line) < judgement_space)
                        {
                            i--;
                            break;
                        }


//                        if (line.contains("Case:"))
//                        {
//                            int case_space = utils.count_space(line);
//                            while(true)
//                            {
//                                i++;
//                                line = resultList.get(i);
//                                if ((line.contains("IntConst:") || line.contains("Identifier:")) && utils.count_space(line) == case_space+2)
//                                {
//                                    case_value = utils.record_variable(resultList.get(i));
//                                    CaseConditions.add(case_value);
//                                }
//                                else
//                                    break;
//                            }
//
//                            if (utils.if_duplicate(CaseConditions))
//                            {
//                                sensitive_list_line = utils.record_line(resultList.get(i));
//                                System.out.println(CaseConditions);
//                                System.err.println("Error [Same Judgment Conditions in \"case\".] at line " + sensitive_list_line);
//                                break;
//                            }
//                        }
                        i++;
                        line = resultList.get(i);
                    }
                }
            }
        }
    }


    /*******************************************************************************************************************
     * ⑥Wrong use of integer base in case statement
     * @param resultList
     */
    public static void BasedIntegerInCase(ArrayList<String> resultList)
    {
        int length = resultList.size();
        int sensitive_list_line;
        String integer;
        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (line.contains("Case:"))
            {
                i = i + 1;
                if (resultList.get(i).contains("Identifier") || resultList.get(i).contains("Block"))
                    continue;
                if (resultList.get(i).contains("IntConst"))
                {
                    line = resultList.get(i);
                    integer = utils.record_variable(line);
                    if (integer.indexOf('\'') == -1 )
                    {
                        sensitive_list_line = utils.record_line(resultList.get(i));
                        System.err.println("Error006 [Wrong Use of Integer Base in \"case\".] at line " + sensitive_list_line);
                    }
                }
            }
        }
    }


    /*******************************************************************************************************************
     * ③Variable bit width usage error
     * @param resultList
     */
    public static void  VariableBitWidthUsageError(ArrayList<String> resultList)
    {
        int length = resultList.size();
        ArrayList<String> VariableList = new ArrayList<String>();
        ArrayList<Integer> IntegerList = new ArrayList<Integer>();
        ArrayList<String> HighFlag = new ArrayList<String>();
        String define_variable;
        int integer1;
        int integer2;
        int sensitive_list_line;
        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (line.contains("ModuleDef:"))
            {
                VariableList.clear();
                IntegerList.clear();
                HighFlag.clear();
            }
            if (line.contains("Width:") && resultList.get(i+1).contains("IntConst"))
            {
                i = i - 1;
                if (!resultList.get(i).contains(","))
                {
                    i = i + 1;
                    continue;
                }
                define_variable = utils.record_define_variable(resultList.get(i));
                VariableList.add(define_variable);
                i = i + 2;
                integer1 = utils.intconst_num(resultList.get(i));
                integer2 = utils.intconst_num(resultList.get(i+1));
                if (Integer.valueOf(integer1) > Integer.valueOf(integer2))
                {
                    IntegerList.add(Integer.valueOf(integer1)-Integer.valueOf(integer2));
                    HighFlag.add("Y");
                }
                else
                {
                    IntegerList.add(Integer.valueOf(integer2)-Integer.valueOf(integer1));
                    HighFlag.add("N");
                }
            }

            if ((line.contains("Rvalue:") || line.contains("Lvalue:"))&& i+2 < length)
            {
                //System.out.println(i);
                String temp_string;
//                if (resultList.get(i-1).contains("NonblockingSubstitution") || resultList.get(i-1).contains("BlockingSubstitution"))
//                {
                    while(true)
                    {
                        if (resultList.get(i).contains("Identifier") || resultList.get(i).contains("IntConst"))
                            break;
                        i = i + 1;
                    }
                    if (resultList.get(i).contains("IntConst"))
                    {
                        continue;
                    }
                    if (  i+2 < length && !(resultList.get(i+1).contains("IntConst") && resultList.get(i+2).contains("IntConst")))
                    {
                        continue;
                    }
                    temp_string = utils.record_variable(resultList.get(i));
                    int index = VariableList.indexOf(temp_string);
                    integer1 = utils.intconst_num(resultList.get(i+1));
                    integer2 = utils.intconst_num(resultList.get(i+2));
                    if (index >= 0 && Math.abs(integer1 - integer2) > IntegerList.get(index))
                    {
                        sensitive_list_line = utils.record_line(resultList.get(i));
                        System.err.println("Error003 [Variable bit width usage error.] at line " + sensitive_list_line);
                        //continue;
                    }

                    if ( integer1 > integer2)
                    {
                        if (index >= 0 && (integer1 - integer2 > Integer.valueOf(IntegerList.get(index)) || HighFlag.get(index) != "Y"))
                        {
                            sensitive_list_line = utils.record_line(resultList.get(i));
                            System.err.println("Error003 [Variable bit width usage error.] at line " + sensitive_list_line);
                        }
                    }
                    else
                    {
                        if (index >= 0 && (integer2 - integer1 > Integer.valueOf(IntegerList.get(index)) || HighFlag.get(index) != "N"))
                        {
                            sensitive_list_line = utils.record_line(resultList.get(i));
                            System.err.println("Error [Variable bit width usage error.] at line " + sensitive_list_line);
                        }
                    }
//                }
//                else
//                {
//                    continue;
//                }
            }
        }
    }
}
