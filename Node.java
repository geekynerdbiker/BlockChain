// Use one-way Linked List: default
import java.util.Date;

public class Node {
	public String hash, pHash;
	private String data;	// Our data will be a simple message.
	private long timeStamp; // As s number of millisecond since 1/1/1970.
	private int nonce;
	
	// Block constructor:
	public Node (String data, String pHash) {
		this.data = data;
		this.pHash = pHash;
		this.timeStamp = new Date().getTime();
		this.hash = Hashing(); // Making sure we do this after we set the other values.
	}


	public String Hashing() {
		// Calculate new hash based on blocks contents:
		String Hashing = Encode.SHA256(pHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);
		
		return Hashing;
	}
	
	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0');
		
		while(!hash.substring(0, difficulty).equals(target)) {
			nonce++;
			hash = Hashing();
		}
		System.out.println("Block Mined. : " + hash);
	}
}


