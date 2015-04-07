import java.util.*;

public class Lexer {
    private String buffer;
    
    public Lexer (String s) { set(s); }

    public void set(String s) { buffer = s; } 

    public String get() { return buffer; }

    public boolean isValid(char c) {
	return ( 'a' <= c && c <= 'z' ) ||
	       ( 'A' <= c && c <= 'Z' ) ||
	       ( '0' <= c && c <= '9' ) ||
               c == '_';
    }

    /*
      bufferをトークン単位に分解する
     */
    public ArrayList<String> lexer() {
	ArrayList<String> ret = new ArrayList<String>();
	for(int i=0;i<buffer.length();i++) {
	    char c = buffer.charAt(i);
	    if( c == ' ' ) continue;
	    if( c == '(' || c == ')' || c == '+' || c == '/' || c == '*' || c == '=' || c == '<' ) {
	    	if( c == '<' && i+1 < buffer.length() && buffer.charAt(i+1) == '=' ) {
	    		ret.add("<=");
	    		i++;
	    	} else {
	    		ret.add(String.valueOf(c));
	    	}
	    } else if ( c == '-' ) {

		assert ( i + 1 ) < buffer.length();

		if( buffer.charAt(i+1) == ' ' ) {
		    ret.add(String.valueOf(c));
		} else {
		    String temp = "-";
		    ++i;
		    while( i < buffer.length() && isValid(buffer.charAt(i)) ) {
			temp += String.valueOf(buffer.charAt(i++));
		    }
		    /*
		      valid な値もしくは変数名かチェックする関数を作ってここで判定しても良い
		     */
		    --i;
		    ret.add(temp);
		}

	    } else {
		String temp = "";
		/*
		  ここ関数化しても良い
		 */
		while( i < buffer.length() && isValid(buffer.charAt(i)) ){
		    temp += String.valueOf(buffer.charAt(i++));
		}
		--i;
		ret.add(temp);
	    }
	}
	return ret;
    }

}
