
/*
 * 実装テスト用
 * clispの実装には関係ない
 */
import java.util.*;


class Main {
	
	public static void change(Integer i){
		i = 100;
	}

    public static void main( String args[] ){
    Integer i = new Integer(10);
    
    System.out.println(i + "!");
    change(i);
    System.out.println(i + "!");
    
    ConsCell a = new ConsCell("a",null,null);
    ConsCell cur = a;
    ConsCell temp = new ConsCell("temp",null,null);
    cur.setBack(temp);
    cur = temp;
    System.out.println(a.getType() + " " + cur.getType());
    System.out.println(a.getBack().getType()+"!!" + a.getType());
    
	Scanner in = new Scanner(System.in);
	String line = in.nextLine();
	System.out.println("input = " + line);
	Lexer lex = new Lexer(line);
	ArrayList<String> arr = lex.lexer();
	in.close();
    }

}
