import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Clisp {
	
	private static boolean DEBUG = false;
	
	private static String prompt = ">>>";
	private static HashMap<String,Integer> variable;
	private static HashMap<String,ConsCell> function;
	
	private static boolean isInteger(String s){
		for(int i=0;i<s.length();i++){
			if( !( '0' <= s.charAt(i) && s.charAt(i) <= '9' ) ) return false;
		}
		return true;
	}
	
	private static Integer eval(ConsCell cell){
		String type = cell.getType();
		if( DEBUG ) System.out.println("type = " + type);
		if( type.equals("+") ) {
			Integer front = eval(cell.getFront());
			Integer back  = eval(cell.getBack());
			return front + back;
		} else if( type.equals("-") ) {
			Integer front = eval(cell.getFront());
			Integer back  = eval(cell.getBack());
			return front - back;
		} else if( type.equals("*") ) {
			Integer front = eval(cell.getFront());
			Integer back  = eval(cell.getBack());
			return front * back;
		} else if( type.equals("/") ) {
			Integer front = eval(cell.getFront());
			Integer back  = eval(cell.getBack());
			assert back != 0;
			return front / back;
		} else if( type.equals("=") ) {
			Integer front = eval(cell.getFront());
			Integer back  = eval(cell.getBack());
			if( front.equals(back) ) return 1;
			else                     return 0;
		} else if( type.equals("<") ) {
			Integer front = eval(cell.getFront());
			Integer back  = eval(cell.getBack());
			if( front.compareTo(back) == -1 ) return 1;
			else                              return 0;
		} else if( type.equals("<=") ) {
			Integer front = eval(cell.getFront());
			Integer back  = eval(cell.getBack());
			if( front.compareTo(back) >= 0 )  return 1;
			else                              return 0;
		} else if( type.equals("if") ) {
			ConsCell condition = cell.getFront();
			Integer res = eval(condition);
			if( res == 1 ) return eval(cell.getBack().getFront());
			else           return eval(cell.getBack().getBack());
		} else if( variable.containsKey(type) ) {
			return variable.get(type);
		} else if( isInteger(type) ) {
			return Integer.valueOf(type);
		} else { //関数
			assert function.containsKey(type);
			if( DEBUG ) System.out.println("now function is " + type);
			ConsCell f = function.get(type);
			ConsCell parameter = f.getFront();
			ConsCell my_parameter = cell.getBack();
			HashMap<String,Integer> store = new HashMap<String,Integer>();
			while( parameter != null ){
				if( variable.containsKey(parameter.getFront().getType()) ) {
					store.put(parameter.getFront().getType(), variable.get(parameter.getFront().getType()));
				} else {
					store.put(parameter.getFront().getType(), null);
				}
				variable.put(parameter.getFront().getType(),Integer.valueOf(eval(my_parameter.getFront())));
				parameter = parameter.getBack();
				my_parameter = my_parameter.getBack();
				//if( parameter == null ) break;
			}
			Integer ret = eval(f.getBack());
			for(Map.Entry<String,Integer> itr : store.entrySet() ){
				if( itr.getValue() == null ) {
					variable.remove(itr.getValue());
				} else {
					variable.put(itr.getKey(),itr.getValue());
				}
			}
			return ret;
		}
	}
	
	private static void tree_walk(ConsCell cell){
		System.out.print("("+cell.getType()+")[");
		if( cell.getFront() != null ) {
			tree_walk(cell.getFront());
		} else {
			System.out.print("null");
		}
		System.out.print(" | ");
		if( cell.getBack() != null ) {
			tree_walk(cell.getBack());
		} else {
			System.out.print("null");
		}
		System.out.print("] ");
	}
	
	public static void execute(String buffer){
		if( DEBUG ) System.out.println("buffer = " + buffer);
		Lexer lexer = new Lexer(buffer);
		ArrayList<String> token = lexer.lexer();
		if( DEBUG ) {
			System.out.println("token--- : " + token.size());
			for(int i=0;i<token.size();i++) {
				if( i != 0 ) System.out.print(", ");
				System.out.print(token.get(i));
			}   System.out.println("");
		}
		Parser parse = new Parser(token);
		parse.buildAST();
		if( DEBUG ) {
			System.out.println("finish builing AST");
			tree_walk(parse.getAST());
			System.out.println("");
		}
		
		ConsCell AST = parse.getAST();
		
		if( AST.getType().equals("setq") ) {
			Integer value = eval(AST.getBack());
			variable.put(AST.getFront().getType(),value);
			System.out.println(AST.getFront().getType() + " = " + value);
		} else if( AST.getType().equals("defun") ) {
			function.put(AST.getBack().getType(),AST.getBack());
			System.out.println("define " + AST.getBack().getType());
		} else if( AST.getType().equals("if") ) {
			Integer res = eval(AST);
			if( res.equals(1) ) System.out.println("True");
			else                System.out.println("Nil");
		} else {
			if( DEBUG ) tree_walk(AST);
			System.out.println(eval(AST));
		}

	}
	
	public static void compute(String buffer){
		
		if( DEBUG ) System.out.println("initial buffer =" + buffer);
		String buffer2 = "";
		int counter = 0;
		int sp = 0;
		while( sp < buffer.length() && buffer.charAt(sp) == ' ' ) ++sp;
		for(int i=sp;i<buffer.length();i++){
			Character c = buffer.charAt(i);
			if( c == '(' ) ++counter;
			else if( c == ')' ) --counter;
			buffer2 += String.valueOf(c);
			if( counter == 0 ) {
				execute(buffer2);
				buffer2 = "";
				++i;
				while( i < buffer.length() && buffer.charAt(i) == ' ' ) ++i;
				--i;
			}
		}
	}
	
	public static void main(String[] args) {
		variable = new HashMap<String,Integer>();
		function = new HashMap<String,ConsCell>();
		// TODO Auto-generated method stub
		try {
			boolean interactive = false;
			Scanner in;
			if( args.length == 0 ) {
				interactive = true;
				in = new Scanner(System.in);
			} else {
				in = new Scanner(new File(args[0]));
			}
			if( interactive ) System.out.print(prompt);
			int counter = 0;
			String buffer = "";
			while( in.hasNext() ){
				String line = in.nextLine();
				for(int i=0;i<line.length();i++) {
					if( line.charAt(i) == '(' ) ++counter;
					else if( line.charAt(i) == ')' ) --counter;
					assert counter >= 0;
				}
				buffer += line;
				if( counter == 0 ) {
					compute(buffer);
					buffer = "";
					counter = 0;
					if( interactive ) System.out.print(prompt);
				}
				
			}
			in.close();
		} catch ( FileNotFoundException e ) {
			System.out.println(e);
		}
	}

}
