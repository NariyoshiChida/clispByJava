import java.util.*;
import java.io.*;


class Main {

    public static void main( String args[] ){
	Scanner in = new Scanner(System.in);
	String line = in.nextLine();
	System.out.println("input = " + line);
	Lexer lex = new Lexer(line);
	ArrayList<String> arr = lex.lexer();
	for(int i=0;i<arr.size();i++){
	    System.out.print("'" + arr.get(i) + "'" );
	    if( i != arr.size()-1 ) System.out.print(" + ");
	}   System.out.println("");
    }

}
