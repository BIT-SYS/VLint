import java.io.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

class treeNode{
    String type = "";
    int line = 0;
    String value = "";
    String attribute = "";
    ArrayList<treeNode> childList = new ArrayList<>();
}

public class Main {

    static ArrayList<String> parseResult = new ArrayList<>();
    static String moduleName = null ;
    /**
     * return the number of prefix Spaces
     * @param str
     * @return
     */
    public static int prefixSpaces(String str){
        int spaceNum = 0;
        for( int i=0; i<str.length(); i++ )
        {
            if( str.charAt(i)!=' ' )
            {
                break;
            }
            spaceNum++;
        }
        return spaceNum;
    }

    /**
     * construct Tree node
     * @param i:the line number of the results arrayList
     * @return
     */
    public static treeNode constructTreeNode(int i){
        treeNode Node = new treeNode();
        String str = parseResult.get(i);
        int spaces = prefixSpaces(str);
        Node.type = str.trim().substring(0,str.trim().indexOf(":"));
        Node.line = Integer.valueOf(str.split("\\s")[str.split("\\s").length-1].substring(0,str.split("\\s")[str.split("\\s").length-1].indexOf(')')));
        if( str.contains(",") ){
            Node.value = str.substring( str.indexOf(':')+2 , str.indexOf(','));
            Node.attribute = str.substring( str.indexOf(',')+2 , str.indexOf('(')-1);
        }
        else{
            Node.value = str.substring( str.indexOf(':')+2 , str.indexOf('(')-1);
            Node.attribute = null;
        }
        ArrayList<treeNode> childs = new ArrayList<>();
        for( i=i+1 ; i<parseResult.size() ; i++ )
        {
            String strTmp = parseResult.get(i) ;
            int spacesTmp = prefixSpaces(strTmp);
            if( spacesTmp <= spaces )
            {
                break;
            }
            else if( spacesTmp == spaces+2 )
            {
                treeNode childNode = constructTreeNode(i) ;
                childs.add( childNode ) ;
            }
        }
        Node.childList = childs;
        return Node;
    }


    private static int line_num;
    public static void print_array( ArrayList<String> list )
    {
        for( String l : list )
            System.out.print(l+" ");
        System.out.println(" ");
    }

    public static void main( String args[] ) throws IOException {

        System.out.println("starts");
        try {
            String path = args[0];
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String strLine;
            while(null != (strLine = bufferedReader.readLine())){
                parseResult.add(strLine);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        treeNode rootNode = constructTreeNode(0);

        //parse.IfElseCase(parseResult);

        //parse.SensitiveList(parseResult);

        //parse.IncompleteCase(parseResult, rootNode);

        //parse.SingalState(parseResult);

        //parse.BlockorNonblockAssign(parseResult);

        //parse.CycleConditionError(parseResult);

        //parse.VarAssignMultipleInAlways(parseResult, rootNode);

        //parse.SameJudgmentConditions(parseResult, rootNode);

        parse.BasedIntegerInCase(parseResult);

        //parse.VariableBitWidthUsageError(parseResult);

        dataflow.VariableAssignmentBeforeUndefined(rootNode);

        //ArrayList<String> dataflowResult = utils.command("python3 examples/example_dataflow_analyzer.py -t top testFiles/test.v" , "/home/zzy/Documents/JDJ/pyverilog");

        //print_array(dataflowResult);

        //int length = parseResult.size();

        //System.out.println(length);

        //System.out.println(stringResult);

        //parse.StoreVarAndWidth(parseResult);

        //print_array(parseResult);

        //parse.StoreVarAndWidth(parseResult);

        System.out.println("finished");
    }
}
