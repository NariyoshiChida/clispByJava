import java.util.*;

/*
 * 引数や戻り値のない関数はないものとする　
 */

public class Parser {
    
    private ArrayList<String> tokens;
    private ConsCell AST;
    private HashMap<String,Integer> variable;
    private int pos;
    
    public Parser(ArrayList<String> tokens) {
    	this.tokens = tokens;
    	this.variable = new HashMap<String,Integer>();
    }
    
    /*
 	先にbuildASTを行うこと
    */
    public ConsCell getAST(){ return AST; }
    public HashMap<String,Integer> getVariable(){return variable; }
    
    /*
     引数を返す
     Fig .1 (a)[null|pointer to (b)] -> (b)[null|pointer to(c) ] -> ... -> (z)[null|null]
     Fig .1の場合は図のような木を作り(a)を返す
     */
    public ConsCell getParameter() {
    	assert pos < tokens.size() && tokens.get(pos).equals("(");
    	++pos;
    	ConsCell ret = new ConsCell(tokens.get(pos),null,null);
    	ConsCell cur = ret;
    	++pos;
    	while( pos < tokens.size() && !tokens.get(pos).equals(")") ){
    		ConsCell temp = new ConsCell(tokens.get(pos),null,null);
    		cur.setBack(temp);
    		cur = temp;
    		++pos;
    	}
    	++pos;
    	return ret;
    }
    
    /*
     変数または定数ならそれ自身を、
     式ならば式を返す
     */
    public ConsCell term(){
    	if( tokens.get(pos).equals("(") ) { //式 
    		ConsCell ret = new ConsCell("",null,null);
    		exp(ret);
    		return ret;
    	} else { // 変数または定数
    		ConsCell ret = new ConsCell(tokens.get(pos),null,null);
    		++pos;
    		return ret;
    	}
    }
    
    public void exp(ConsCell ast){
    	if( pos < tokens.size() && !tokens.get(pos).equals("(") ) {
    		ast.setType(tokens.get(pos));
    		ast.setFront(null);
    		ast.setBack(null);
    		++pos;
    		return;
    	}
    	if( pos+2 < tokens.size() && tokens.get(pos+2).equals(")") ) {
    		ast.setType(tokens.get(pos+1));
    		ast.setFront(null);
    		ast.setBack(null);
    		pos += 3;
    		return;
    	}
    	
    	++pos;
    	boolean found = true;
    	
    	if( tokens.get(pos).equals("+") ) {
    		ast.setType("+"); 
    	} else if( tokens.get(pos).equals("-") ) {
    		ast.setType("-");
    	} else if( tokens.get(pos).equals("*") ) {
    		ast.setType("*");
    	} else if( tokens.get(pos).equals("/") ) {
    		ast.setType("/");
    	} else if( tokens.get(pos).equals("<") ) {
    		ast.setType("<");
    	} else if( tokens.get(pos).equals("=") ) {
    		ast.setType("=");
    	} else found = false;
    	
    	    	
    	if( tokens.get(pos).equals("setq") ) {
    		ast.setType("setq");
    		++pos;
    		ConsCell front = term();
    		//ConsCell back  = term();
    		ConsCell back = getParameter();
    		++pos;
    		
    		ast.setFront(front);
    		ast.setBack(back);
    		
    		return;
    	}
    	
    	if( tokens.get(pos).equals("defun") ) {
    		ast.setType("defun");
    		++pos;
    		ConsCell function = new ConsCell(tokens.get(pos++),null,null);
    		ast.setBack(function);
    		ConsCell parameter = getParameter();
    		function.setFront(parameter);
    		ConsCell expr = new ConsCell("",null,null);
    		exp(expr);
    		function.setBack(expr);
    		
    		return;
    	}
    	
    	if( tokens.get(pos).equals("if") ) { // (if)[(条件)[null|null]|(処理)[(trueの場合)[null|null]|(nilの場合)[null|null]]]
    		ast.setType("if");
    		++pos;
    		ConsCell condition = new ConsCell("",null,null);
    		exp(condition);
    		
    		ConsCell process = new ConsCell("",null,null);
    		ConsCell process_true = new ConsCell("",null,null);
    		ConsCell process_nil  = new ConsCell("",null,null);
    		exp(process_true);
    		exp(process_nil);
    		process.setFront(process_true);
    		process.setBack(process_nil);
    		
    		ast.setFront(condition);
    		ast.setBack(process);
    		return;
    	}
    	
    	if( found == false ) { // 関数
    		ast.setType(tokens.get(pos));
    		++pos;
    		ast.setBack(getParameter());
    		return;
    	}
    	
    	
    	++pos;
    	ConsCell front = term();
    	ConsCell back  = term();
    	ast.setFront(front);
    	ast.setBack(back);
    	assert pos < tokens.size() && tokens.get(pos).equals(")");
    	++pos;
    	
    	
    }
    
    /*
       tokensからASTを作成
       メンバのASTにASTを、
       variableにAST内に出現する変数を入れる ( 変数の初期値は0とする 使用する際に値を入れること )
     */   
    public void buildAST(){
    	variable.clear();
    	pos = 0;
    	AST = new ConsCell("",null,null);
    	exp(AST);
        assert pos == tokens.size();
    }
    
    
}
