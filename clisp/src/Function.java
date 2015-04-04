import java.util.HashMap;

public class Function {
  private String name;
  private HashMap<String,Integer> variable;
  private ConsCell AST;
  
  public Function(String name,HashMap<String,Integer> variable,ConsCell AST) {
	  this.name = name;
	  this.variable = variable;
	  this.AST = AST;
  }
  
  
}
