import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.Iterator;

class variables_port{
    String name="" ;
    String type="" ;
    int isNull = 0 ;
}

class controlFlow_node{
    treeNode node = new treeNode() ;
    ArrayList<controlFlow_node> precursorNodes = new ArrayList<>() ;
    ArrayList<controlFlow_node> successorNodes = new ArrayList<>() ;
    String description = "" ;
    ArrayList<variables_port> vars_state = new ArrayList<>() ;
}

public class dataflow {

    static String moduleName = null ;
    static ArrayList<String> results = new ArrayList<>();
    static ArrayList<controlFlow_node> controlFlow_nodes = new ArrayList<>() ;
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
        String str = results.get(i);
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
        for( i=i+1 ; i<results.size() ; i++ )
        {
            String strTmp = results.get(i) ;
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

    /**
     * print the treeNode
     * @param node
     * @param level
     */
    public static void printNode( treeNode node , int level )
    {
        for( int i=0 ; i<level ; i++ )
        {
            System.out.print(" ");
        }
        System.out.print(node.type);
        System.out.print(": ");
        System.out.print(node.value);
        if( node.attribute!=null )
        {
            System.out.print(", "+node.attribute);
        }
        System.out.print(" (at ");
        System.out.print(node.line+")\n");
        if( node.childList.size()>0 )
        {
            for( treeNode childnode : node.childList )
            {
                printNode(childnode,level+2);
            }
        }
    }



    public static void printCtrlFlow()
    {
        System.out.println("--------------------------------------------------------------------------------");
        for( controlFlow_node n : controlFlow_nodes )
        {
            System.out.print(n.node.type+": "+n.node.value+" "+n.node.line+" "+n.description);
            System.out.print("  [");
            for( controlFlow_node n1 : n.precursorNodes )
            {
                System.out.print(n1.node.type+": "+n1.node.value+" "+n1.node.line+" "+n1.description+"; ");
            }
            System.out.print("]  [");
            for( controlFlow_node n1 : n.successorNodes )
            {
                System.out.print(n1.node.type+": "+n1.node.value+" "+n1.node.line+" "+n1.description+"; ");
            }
            System.out.println("]");
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void printDataFlow()
    {
        System.out.println("***********************************************************************************");
        //System.out.println("controlFlow_nodes.size() = " + controlFlow_nodes.size());
        for( controlFlow_node n : controlFlow_nodes )
        {
            System.out.print(n.node.type+": "+n.node.value+" "+n.node.line+" "+n.description);
            for( variables_port var : n.vars_state )
            {
                System.out.print("["+var.name+": "+var.isNull+"]  ");
            }
            System.out.println();
        }
        System.out.println("***********************************************************************************");
    }


    public static void scanBlockNode( controlFlow_node BlockNode )
    {
        ArrayList<controlFlow_node> preNodes = BlockNode.precursorNodes ;
        ArrayList<controlFlow_node> suNodes = BlockNode.successorNodes ;
        if( BlockNode.node.childList.size()>0 )
        {
            controlFlow_node pre_tmpNode = new controlFlow_node() ;
            for( int i=0 ; i<BlockNode.node.childList.size() ; i++ )
            {
                controlFlow_node tmpNode = new controlFlow_node() ;
                tmpNode.node = BlockNode.node.childList.get(i) ;
                if( i==0 )
                {
                    for( controlFlow_node pre1 : preNodes )
                    {
                        Iterator<controlFlow_node> iterator = pre1.successorNodes.iterator();
                        while (iterator.hasNext())
                        {
                            controlFlow_node n = iterator.next();
                            if( n.node.type.equals(BlockNode.node.type) && n.node.line==BlockNode.node.line )
                            {
                                iterator.remove();
                            }
                        }
                        pre1.successorNodes.add(tmpNode) ;
                    }
                    tmpNode.precursorNodes = preNodes ;
                    pre_tmpNode = tmpNode ;
                    if( i==BlockNode.node.childList.size()-1 )//only 1 childnode
                    {
                        for( controlFlow_node su1 : suNodes )
                        {
                            Iterator<controlFlow_node> iterator = su1.precursorNodes.iterator();
                            while (iterator.hasNext())
                            {
                                controlFlow_node n = iterator.next();
                                if( n.node.type.equals(BlockNode.node.type) && n.node.line==BlockNode.node.line )
                                {
                                    iterator.remove();
                                }
                            }
                            su1.precursorNodes.add(tmpNode) ;
                        }
                        tmpNode.successorNodes = suNodes ;
                    }
                }
                else if( i==BlockNode.node.childList.size()-1 )
                {
                    for( controlFlow_node su1 : suNodes )
                    {
                        Iterator<controlFlow_node> iterator = su1.precursorNodes.iterator();
                        while (iterator.hasNext())
                        {
                            controlFlow_node n = iterator.next();
                            if( n.node.type.equals(BlockNode.node.type) && n.node.line==BlockNode.node.line )
                            {
                                iterator.remove();
                            }
                        }
                        su1.precursorNodes.add(tmpNode) ;
                    }
                    tmpNode.successorNodes = suNodes ;
                    pre_tmpNode.successorNodes.add(tmpNode) ;
                    tmpNode.precursorNodes.add(pre_tmpNode) ;
                    pre_tmpNode = tmpNode ;
                }
                else
                {
                    pre_tmpNode.successorNodes.add(tmpNode) ;
                    tmpNode.precursorNodes.add(pre_tmpNode) ;
                    pre_tmpNode = tmpNode ;
                }
                controlFlow_nodes.add(tmpNode) ;
            }
        }
        else
        {
            System.err.println("Block node has no child");
        }
    }

    public static void scanIfNode( controlFlow_node IfNode )
    {
        ArrayList<controlFlow_node> preNodes = IfNode.precursorNodes ;
        ArrayList<controlFlow_node> suNodes = IfNode.successorNodes ;
        if( IfNode.node.childList.size()==3 )
        {
            //condition
            controlFlow_node conditionNode = new controlFlow_node() ;
            conditionNode.node = IfNode.node.childList.get(0) ;
            for( controlFlow_node pre1 : preNodes )
            {
                Iterator<controlFlow_node> iterator = pre1.successorNodes.iterator();
                while (iterator.hasNext())
                {
                    controlFlow_node n = iterator.next();
                    if( n.node.type.equals(IfNode.node.type) && n.node.line==IfNode.node.line )
                    {
                        iterator.remove();
                    }
                }
                pre1.successorNodes.add(conditionNode) ;
            }
            conditionNode.precursorNodes = preNodes ;
            controlFlow_nodes.add(conditionNode) ;
            //if
            controlFlow_node ifNode = new controlFlow_node() ;
            ifNode.node = IfNode.node.childList.get(1) ;
            for( controlFlow_node su1 : suNodes )
            {
                Iterator<controlFlow_node> iterator = su1.precursorNodes.iterator();
                while (iterator.hasNext())
                {
                    controlFlow_node n = iterator.next();
                    if( n.node.type.equals(IfNode.node.type) && n.node.line==IfNode.node.line )
                    {
                        iterator.remove();
                    }
                }
                su1.precursorNodes.add(ifNode) ;
            }
            ifNode.successorNodes = suNodes ;
            conditionNode.successorNodes.add(ifNode) ;
            ifNode.precursorNodes.add(conditionNode) ;
            controlFlow_nodes.add(ifNode) ;
            //else
            controlFlow_node elseNode = new controlFlow_node() ;
            elseNode.node = IfNode.node.childList.get(2) ;
            for( controlFlow_node su1 : suNodes )
            {
                Iterator<controlFlow_node> iterator = su1.precursorNodes.iterator();
                while (iterator.hasNext())
                {
                    controlFlow_node n = iterator.next();
                    if( n.node.type.equals(IfNode.node.type) && n.node.line==IfNode.node.line )
                    {
                        iterator.remove();
                    }
                }
                su1.precursorNodes.add(elseNode) ;
            }
            elseNode.successorNodes = suNodes ;
            conditionNode.successorNodes.add(elseNode) ;
            elseNode.precursorNodes.add(conditionNode) ;
            controlFlow_nodes.add(elseNode) ;
        }
        else
        {
            System.err.println("IfStatement node has strange child");
        }
    }

    public static void scanForNode( controlFlow_node ForNode )
    {
        ArrayList<controlFlow_node> preNodes = ForNode.precursorNodes ;
        ArrayList<controlFlow_node> suNodes = ForNode.successorNodes ;
        if( ForNode.node.childList.size()==4 )
        {
            //initial
            controlFlow_node initialNode = new controlFlow_node() ;
            initialNode.node = ForNode.node.childList.get(0) ;
            for( controlFlow_node pre1 : preNodes )
            {
                Iterator<controlFlow_node> iterator = pre1.successorNodes.iterator();
                while (iterator.hasNext())
                {
                    controlFlow_node n = iterator.next();
                    if( n.node.type.equals(ForNode.node.type) && n.node.line==ForNode.node.line )
                    {
                        iterator.remove();
                    }
                }
                pre1.successorNodes.add(initialNode) ;
            }
            initialNode.precursorNodes = preNodes ;
            controlFlow_nodes.add( initialNode );
            //condition
            controlFlow_node conditionNode = new controlFlow_node() ;
            conditionNode.node = ForNode.node.childList.get(1) ;
            for( controlFlow_node su1 : suNodes )
            {
                Iterator<controlFlow_node> iterator = su1.precursorNodes.iterator();
                while (iterator.hasNext())
                {
                    controlFlow_node n = iterator.next();
                    if( n.node.type.equals(ForNode.node.type) && n.node.line==ForNode.node.line )
                    {
                        iterator.remove();
                    }
                }
                su1.precursorNodes.add(conditionNode) ;
            }
            conditionNode.successorNodes = suNodes ;
            initialNode.successorNodes.add(conditionNode) ;
            conditionNode.precursorNodes.add(initialNode) ;
            controlFlow_nodes.add( conditionNode );
            //execution
            controlFlow_node executionNode = new controlFlow_node() ;
            executionNode.node = ForNode.node.childList.get(3) ;
            conditionNode.successorNodes.add(executionNode) ;
            executionNode.precursorNodes.add(conditionNode) ;
            controlFlow_nodes.add( executionNode );
            //iteration
            controlFlow_node iterationNode = new controlFlow_node() ;
            iterationNode.node = ForNode.node.childList.get(2) ;
            executionNode.successorNodes.add(iterationNode) ;
            iterationNode.precursorNodes.add(executionNode) ;
            iterationNode.successorNodes.add(conditionNode) ;
            conditionNode.precursorNodes.add(iterationNode) ;
            controlFlow_nodes.add( iterationNode );

        }
        else
        {
            System.err.println("ForStatement node has strange child");
        }
    }




    public static void scanAlways( treeNode node )
    {

        System.out.println("always block : -----------------------------------------------------------------------------");
        //printNode( node , 0 );

        controlFlow_node startNode = new controlFlow_node() ;
        startNode.description = "start" ;
        startNode.successorNodes = new ArrayList<>() ;
        controlFlow_nodes.add(startNode) ;
        controlFlow_node endNode = new controlFlow_node() ;
        endNode.description = "end" ;
        endNode.precursorNodes = new ArrayList<>() ;
        controlFlow_nodes.add(endNode) ;

        //第一轮构建控制流图
        if( node.childList.size()>0 )
        {
            for( treeNode child : node.childList )
            {
                if( !child.type.equals("SensList") )
                {
                    controlFlow_node ctrlNode = new controlFlow_node() ;
                    ctrlNode.node = child ;
                    if( startNode.successorNodes.size()==0 )
                    {
                        startNode.successorNodes.add( ctrlNode ) ;
                        ctrlNode.precursorNodes.add( startNode ) ;
                    }
                    else
                    {
                        controlFlow_node tmp_node = startNode ;
                        while( tmp_node.successorNodes.size()>0 )
                        {
                            tmp_node = tmp_node.successorNodes.get(0) ;
                        }
                        tmp_node.successorNodes.add(ctrlNode) ;
                        ctrlNode.precursorNodes.add(tmp_node) ;
                    }
                    controlFlow_nodes.add(ctrlNode) ;
                }
            }
            if( startNode.successorNodes.size()==0 )
            {
                startNode.successorNodes.add( endNode ) ;
                endNode.precursorNodes.add( startNode ) ;
                System.err.println("the Always only has sens list, no control flow");
            }
            else
            {
                controlFlow_node tmp_node = startNode ;
                while( tmp_node.successorNodes.size()>0 )
                {
                    tmp_node = tmp_node.successorNodes.get(0) ;
                }
                tmp_node.successorNodes.add(endNode) ;
                endNode.precursorNodes.add(tmp_node) ;
            }
        }
        else
        {
            System.err.println("the Always has no child");
        }
        //printCtrlFlow();
        //不断迭代

        while( true )
        {
            int flag = 0 ;
            ArrayList<controlFlow_node> tmp_nodes = controlFlow_nodes ;
            for( int i=0 ; i<tmp_nodes.size() ; i++ )
            {
                controlFlow_node n = controlFlow_nodes.get(i) ;
                if( n.node.type.equals("Block") )
                {
                    flag++ ;
                    controlFlow_nodes.remove(i) ;
                    scanBlockNode(n);
                }
                else if( n.node.type.equals("IfStatement") )
                {
                    flag++ ;
                    controlFlow_nodes.remove(i) ;
                    scanIfNode(n) ;
                }
                else if( n.node.type.equals("ForStatement") )
                {
                    flag++ ;
                    controlFlow_nodes.remove(i) ;
                    scanForNode(n) ;
                }
            }
            //printCtrlFlow();
            if( flag==0 )
            {
                break ;
            }

        }
        //printCtrlFlow();

//        scanCtrlNode(startNode);
//        System.out.println("After the 2nd scan, controlFlow is :");
//        printCtrlFlow(startNode , 0);






    }

    public static ArrayList<variables_port> scanTree( treeNode node ,ArrayList<variables_port> variables_portArrayList  )
    {
        if( node.type.equals("ModuleDef") )
        {
            moduleName = node.value ;
        }
        else if( node.type.equals("Input") )
        {
            variables_port newVar = new variables_port() ;
            newVar.type = "Input" ;
            newVar.name = node.value ;
            variables_portArrayList.add(newVar) ;
        }
        else if( node.type.equals("Output") )
        {
            variables_port newVar = new variables_port() ;
            newVar.type = "Output" ;
            newVar.name = node.value ;
            variables_portArrayList.add(newVar) ;
        }
        else if( node.type.equals("Integer") )
        {
            variables_port newVar = new variables_port() ;
            newVar.type = "Integer" ;
            newVar.name = node.value ;
            variables_portArrayList.add(newVar) ;
        }
        else if( node.type.equals("Always") )
        {
            if( node.childList.size()>0 )
            {
                treeNode node1 = node.childList.get(0) ;
                if( node1.childList.size()>0 )
                {
                    treeNode node2 = node1.childList.get(0) ;
                    if( node2.type.equals("Sens") )
                    {
                        if( node2.value.equals("level") )
                        {
                            scanAlways( node );
                        }
                    }
                }
            }
        }
        for( treeNode child : node.childList )
        {
            scanTree( child , variables_portArrayList);
        }

        return variables_portArrayList ;
    }

    public static void controlflow( treeNode root , ArrayList<variables_port> variables_portArrayList  )
    {
        System.out.println("controlflow analyzing");


        scanTree( root , variables_portArrayList );

        System.out.println("variables & ports : -----------------------------------------------------------------------");
        for( variables_port var : variables_portArrayList )
        {
            if( var.type.equals("Input") )
            {
                var.isNull = 1 ;
            }
        }
        for( int i=1 ; i<=variables_portArrayList.size() ; i++ )
        {
            variables_port var = variables_portArrayList.get(i-1) ;
            System.out.println(var.type + "  " + var.name+" " + var.isNull);
        }

        System.out.println("controlflow analyzed");
    }

    public static void initialDataflow( controlFlow_node ctrlNode )
    {
        if( ctrlNode.description.equals("start") )
        {
            System.out.print("start!");
        }
        else if( ctrlNode.description.equals("end") )
        {
            System.out.print("end!");
        }
        else
        {
            System.out.print(ctrlNode.node.type+":"+ctrlNode.node.value+" "+ctrlNode.node.line );
        }
        System.out.print("    [");
        for( controlFlow_node n : ctrlNode.precursorNodes ) System.out.print(n.node.type+":"+n.node.value+" "+n.node.line+n.description+" ");
        System.out.print("    ]");
        System.out.print("    [");
        for( controlFlow_node n : ctrlNode.successorNodes ) System.out.print(n.node.type+":"+n.node.value+" "+n.node.line+n.description+" ");
        System.out.println("    ]");

        if( ctrlNode.successorNodes!=null )
        {
            for( controlFlow_node suNode : ctrlNode.successorNodes )
            {
                initialDataflow( suNode );
            }
        }
    }

    public static ArrayList<variables_port> assignUpdate( ArrayList<variables_port> vars , treeNode node )
    {

        if( node.type.equals("Lvalue") )
        {
            if( node.childList.size()==1 )
            {
                treeNode identifierNode = node.childList.get(0) ;
                for( variables_port v : vars )
                {
                    if( v.name.equals(identifierNode.value) )
                    {
                        v.isNull = 2 ;
                        break ;
                    }
                }
            }
            else
            {
                System.err.println("Lvalue child error");
            }
        }
        else
        {
            for( treeNode child : node.childList )
            {
                vars = assignUpdate(vars,child) ;
            }
        }
        return vars ;
    }



    public static int updateDataflow( controlFlow_node node,ArrayList<variables_port> variables_portArrayList  )
    {
        int count = 0 ;
        if( node.description.equals("end")||node.description.equals("start") )
        {
            return 0 ;
        }
        ArrayList<variables_port> new_vars = new ArrayList<>() ;
        for( variables_port v : node.vars_state )
        {
            variables_port v1 = new variables_port() ;
            v1.name = v.name ;
            v1.type = v.type ;
            v1.isNull = v.isNull ;
            new_vars.add(v1) ;
        }
        for( int i=0 ; i<new_vars.size() ; i++)
        {
            if( new_vars.get(i).isNull == 2 )
            {
                int flag = 0 ;
                for( controlFlow_node preNode : node.precursorNodes )
                {
                    if( preNode.vars_state.get(i).isNull==0 )
                    {
                        flag++ ;
                    }
                }
                if( flag==0 )
                {
                    new_vars.get(i).isNull = 1 ;
                }
            }
            else
            {
                int flag = 0 ;
                for( controlFlow_node preNode : node.precursorNodes )
                {
                    if( preNode.vars_state.get(i).isNull==0 )
                    {
                        flag++ ;
                    }
                }
                if( flag==0 )
                {
                    new_vars.get(i).isNull = 1 ;
                }
                else
                {
                    new_vars.get(i).isNull = 0 ;
                }
            }
        }
        for( int i=0 ; i<new_vars.size() ; i++ )
        {
            if( node.vars_state.get(i).isNull!=(new_vars.get(i).isNull) )
            {
                count++ ;
            }
        }
        node.vars_state = new_vars ;

        return count ;
    }

    public static void dataflow(ArrayList<variables_port> variables_portArrayList  )
    {
        System.out.println("dataflow analyzing");
        controlFlow_node startNode = new controlFlow_node() ;
        //第一轮 初始化
        for( controlFlow_node node : controlFlow_nodes )
        {
            for( variables_port v : variables_portArrayList )
            {
                variables_port v1 = new variables_port() ;
                v1.name = v.name ;
                v1.type = v.type ;
                v1.isNull = v.isNull ;
                node.vars_state.add( v1 ) ;
            }
        }
        //printDataFlow();

        //第二轮 赋值更新
        for( controlFlow_node node : controlFlow_nodes )
        {
            node.vars_state = assignUpdate( node.vars_state , node.node ) ;
        }
        //printDataFlow();

        //第三轮  迭代更新
        while( true )
        {
            int flag = 0;
            for( controlFlow_node node :  controlFlow_nodes )
            {
                flag += updateDataflow(node,variables_portArrayList) ;
            }
            if( flag == 0 )
            {
                break;
            }
        }

        //printDataFlow();

        System.out.println("dataflow analyzed");
    }


    public static void detectFunc1( ArrayList<variables_port> vars , treeNode node , boolean ifInRvalue )
    {
        if( ifInRvalue )
        {
            if( node.type.equals("Identifier") )
            {
                for( variables_port v : vars )
                {
                    if( v.name.equals(node.value) )
                    {
                        if( v.isNull!=1 )
                        {
                            System.err.println("Error011 [Variable assignment before undefined.] at line " + node.line);
                        }
                        break ;
                    }
                }
            }
            for( treeNode child : node.childList )
            {
                detectFunc1(vars,child,true); ;
            }
        }
        else
        {
            if( node.type.equals("Rvalue") )
            {
                for( treeNode child : node.childList )
                {
                    detectFunc1(vars,child,true); ;
                }
            }
            else
            {
                for( treeNode child : node.childList )
                {
                    detectFunc1(vars,child,false); ;
                }
            }
        }

    }

    public static void detectFunc2( treeNode node , boolean ifInRvalue , boolean ifSequentialLogical, ArrayList<variables_port> vars)
    {
        if( ifSequentialLogical )
        {
            if( ifInRvalue)
            {
                if( node.type.equals("Identifier") )
                {
                    for( variables_port v : vars )
                    {
                        if( v.name.equals(node.value) )
                        {
                            if( v.type.equals("Output") )
                            {
                                System.err.println("Error011 [Variable assignment before undefined.] at line " + node.line);
                            }
                            break ;
                        }
                    }
                }
                for( treeNode child : node.childList )
                {
                    detectFunc2(child,true,true,vars); ;
                }
            }
            else
            {
                if( node.type.equals("Rvalue") )
                {
                    for( treeNode child : node.childList )
                    {
                        detectFunc2(child,true,true,vars); ;
                    }
                }
                else
                {
                    for( treeNode child : node.childList )
                    {
                        detectFunc2(child,false,true,vars); ;
                    }
                }
            }
        }
        else
        {
            if( node.type.equals("Always") )
            {
                if( node.childList.size()>0 )
                {
                    treeNode node1 = node.childList.get(0) ;
                    if( node1.childList.size()>0 )
                    {
                        treeNode node2 = node1.childList.get(0) ;
                        if( node2.type.equals("Sens") )
                        {
                            if( !node2.value.equals("level") )
                            {
                                for( treeNode child : node.childList )
                                {
                                    detectFunc2(child,false,true,vars); ;
                                }
                            }
                        }
                    }
                    else
                    {
                        System.err.println("Always has no grandchild");
                    }
                }
                else
                {
                    System.err.println("Always has no child");
                }
            }
            else
            {
                for( treeNode child : node.childList )
                {
                    detectFunc2(child,false,false,vars); ;
                }

            }
        }
    }

    public static void detectError( ArrayList<variables_port> variables_portArrayList , treeNode root )
    {
        System.out.println("deteting");

        //组合逻辑  依据数据流分析结果
        for( controlFlow_node node : controlFlow_nodes )
        {
            detectFunc1( node.vars_state , node.node , false) ;
        }

        //时序逻辑  依据语法树和变量列表
        detectFunc2( root,false ,false , variables_portArrayList);


        System.out.println("detected");
    }

    public static void VariableAssignmentBeforeUndefined(treeNode rootNode) {
        ArrayList<variables_port> variables_portArrayList = new ArrayList<>() ;
        controlflow( rootNode , variables_portArrayList );
        dataflow( variables_portArrayList );
        detectError(variables_portArrayList , rootNode) ;
        printDataFlow();
        //printCtrlFlow();
    }
}
