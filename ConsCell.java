
public class ConsCell {
    private String type;
    private ConsCell front,back; // ConsCell -> [front|back]
    
    public ConsCell(String type,ConsCell front, ConsCell back){
	this.type = type;
	this.front = front;
	this.back = back;
    }
    
    public void setType(String s) { type = s; }
    public void setFront(ConsCell f) { front = f; }
    public void setBack(ConsCell b) { back = b; }
    public String getType() { return type; }
    public ConsCell getFront() { return front; }
    public ConsCell getBack() { return back; }

}
