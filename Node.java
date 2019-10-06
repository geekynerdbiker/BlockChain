// Use one-way Linked List: default
import java.util.Date;

public class Node {
	public String hash, pHash;
	private String data;
	private long timeStamp;
	
	public Node (String data, String pHash) {
		this.data = data;
		this.pHash = pHash;
		this.timeStamp = new Date().getTime();
	}
	
	public String getData() {
		return data;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
}


