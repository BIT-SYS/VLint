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
                    line = resultList.get(++i1);
                    Lvalue.add(utils.record_variable(line));
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
            //System.out.print("Lvalue if :  ");
            //utils.print_array(Lvalue_if);
            if (i1 == resultList.size() - 1 || utils.count_space(resultList.get(i1)) < num_space + 2) {
                //System.out.println("---------------------------------------------------------------out if structure suddenly : " + resultList.get(i1) + "\n\n");
                System.err.println("Error [without else in a if structure] at line " + if_structure_line);
                return Lvalue_if;
            }
            Lvalue_else = IForELSE_process(resultList, "else");
            //System.out.print("Lvalue else :  ");
            //utils.print_array(Lvalue_else);
            if (!Lvalue_if.equals(Lvalue_else) && !Lvalue_else.isEmpty()) {
                //System.out.println("different");
                System.err.println("Error [Lvalues are not the same between if and else] at line : " + if_structure_line);
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
                    //System.out.println("--------------------------------------------into case structure at : " + line );
                }
            }
            else
            {
                if( i==length-1 || utils.count_space(resultList.get(i+1)) <= case_space )
                {
                    //System.out.println("---------------------------------------------last line of case : " + line + "\n\n");
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
                        //System.out.println("-----case at : "+line);
                        if( utils.count_space(resultList.get(i+2))>case_space+4 )
                        {
                            //System.out.println("find default! ");
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
                                    System.err.println("Error [sensitive list without variable \"" + Rvalue + "\"] at line "+sensitive_list_line );
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
                                System.err.println("Error [Nonblocking substitution is used for combination logic.] at line " + sensitive_list_line);
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
                                System.err.println("Error [Blocking substitution is used for sequence logic.] at line " + sensitive_list_line);
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
     * ⑩Variable multiple assignments in different always.
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
            if (line.contains("LessThan") && resultList.get(i - 1).contains("WhileStatement"))
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
            if (line.contains("LessEq") && resultList.get(i - 1).contains("WhileStatement"))
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
                    sensitive_list_line = utils.record_line(resultList.get(i-1));
                    System.err.println("Error [The Cycle Condition is always true \"" + "\"] at line " + sensitive_list_line);
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
        ArrayList<String> AlwaysDecl = new ArrayList<String>();
        boolean inAlways = false;
        int always_space = 0;
        int length = resultList.size();
        int sensitive_list_line = 0;

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
                if (line.contains("Always:"))
                {
                    //System.out.println("-------------------------------------------------------into the always : " + line);
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
                                System.err.println("Error [Variable " + lvalue + " multiple assignments in different \"always\".] at line " + sensitive_list_line);
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
    public static void SameJudgmentConditions (ArrayList<String> resultList)
    {
        boolean inJudgement = false;
        int judgement_space = 0;
        int length = resultList.size();
        int sensitive_list_line = 0;

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
                        if (line.contains("IfStatement:"))
                        {
                            if (!(resultList.get(i+2).contains("Identifier:") && resultList.get(i+2).contains("IntConst:")))
                                break;
                            judge_symbol = utils.record_symbol(resultList.get(i+1));
                            lvalue = utils.record_variable(resultList.get(i+2));
                            rvalue = utils.record_variable(resultList.get(i+3));
                            IfJudgeConditions.add(judge_symbol+lvalue+rvalue);
                            if (utils.if_duplicate(IfJudgeConditions))
                            {
                                sensitive_list_line = utils.record_line(resultList.get(i));
                                System.err.println("Error [Same Judgment Conditions in \"if\".] at line " + sensitive_list_line);
                                break;
                            }
                        }
                        i++;
                        line = resultList.get(i);
                    }
                }
                if (line.contains("CaseStatement:"))
                {
                    ArrayList<String> CaseConditions = new ArrayList<String>();
                    String case_value;

                    //System.out.println("--------------------------------------------------into the case branch : " + line);
                    inJudgement = true ;
                    judgement_space = utils.count_space(line);

                    line = resultList.get(i);
                    while (true)
                    {
                        //String line = resultList.get(i);
                        if (i == length - 1)
                            break;
                        if (utils.count_space(line) < judgement_space)
                        {
                            i--;
                            break;
                        }
                        if (line.contains("Case:"))
                        {
                            case_value = utils.record_variable(resultList.get(i+1));
                            CaseConditions.add(case_value);
                            if (utils.if_duplicate(CaseConditions))
                            {
                                sensitive_list_line = utils.record_line(resultList.get(i));
                                System.err.println("Error [Same Judgment Conditions in \"case\".] at line " + sensitive_list_line);
                                break;
                            }
                        }
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
        String integer;
        for (int i = 0; i < length; i++)
        {
            String line = resultList.get(i);
            if (line.contains("Case:"))
            {
                i = i + 1;
                if (line.contains("Identifier") || line.contains("Block"))
                line = resultList.get(i);
                integer = utils.record_variable(line);
                if (integer.length() > 2 && integer.charAt(1) != '\'')
                    System.err.println("Error [Wrong Use of Integer Base in \"case\".]" + resultList.get(i));
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
//        String integer1;
//        String integer2;
        int integer1;
        int integer2;
        boolean eqflag = false;
        boolean signflag = false;
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

            if (i+4 < length && (line.contains("Rvalue:") || line.contains("Lvalue:")))
            {
                String temp_string;
                if (resultList.get(i+1).contains("Eq:"))
                {
                    eqflag = true;
                }
                if (resultList.get(i+1).contains("Plus:") || resultList.get(i+1).contains("Minus:"))
                {
                    signflag = true;
                }
                if (resultList.get(i+4).contains("IntConst:"))
                {
                    while(true)
                    {
                        if (resultList.get(i).contains("Identifier"))
                            break;
                        i = i + 1;
                    }
                    if (!resultList.get(i+1).contains("IntConst") || !resultList.get(i+2).contains("IntConst"))
                    {
                        continue;
                    }
                    temp_string = utils.record_variable(resultList.get(i));
                    int index = VariableList.indexOf(temp_string);
                    int numflag = 0;
                    integer1 = utils.intconst_num(resultList.get(i+1));
                    integer2 = utils.intconst_num(resultList.get(i+2));
                    if (signflag || (!resultList.get(i+3).contains("IntConst") && !eqflag))
                        numflag = Math.abs(integer1 - integer2);
                    if (resultList.get(i+3).contains("IntConst") && !eqflag && !signflag)
                    {
                        System.out.println(resultList.get(i));
                        int k = utils.record_variable(resultList.get(i+3)).indexOf("'", 0);
                        System.out.println(k);
                        String temp = utils.record_variable(resultList.get(i+3)).substring(0, k);
                        System.out.println(temp);
                               // String.valueOf(utils.record_variable(resultList.get(i+3)).charAt(0));
                        numflag = Math.abs(integer1 - integer2) + Integer.parseInt(temp);
                        System.out.println(numflag);
                    }
                    if (resultList.get(i+3).contains("IntConst") && eqflag )
                    {
                        String temp = String.valueOf(utils.record_variable(resultList.get(i+3)).charAt(0));
                        if (Math.abs(integer1 - integer2)+1 != Integer.parseInt(temp))
                        {
                            System.err.println("Error [Variable bit width usage error.]" + resultList.get(i));
                        }
                    }

                    if (index > 0 && numflag > IntegerList.get(index))
                    {
                        System.err.println("Error [Variable bit width usage error.]" + resultList.get(i));
                        continue;
                    }

                    if ( integer1 > integer2)
                    {
                        if (index > 0 && (numflag > Integer.valueOf(IntegerList.get(index)) || HighFlag.get(index) != "Y"))
                            System.err.println("Error [Variable bit width usage error.]" + resultList.get(i));
                    }
                    else
                    {
                        if (index > 0 && (numflag < Integer.valueOf(IntegerList.get(index)) || HighFlag.get(index) != "N"))
                            System.err.println("Error [Variable bit width usage error.]" + resultList.get(i));
                    }
                }
                else
                {

                    continue;
                }
                eqflag = false;
                signflag = false;
            }
        }
    }




}
