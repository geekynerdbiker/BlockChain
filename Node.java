// Use one-way Linked List: default
import java.util.Date;

public class Node {
	public String hash, pHash;
	private String data;
	private long timeStamp;
	private int nonce;
	
	public Node (String data, String pHash) {
		this.data = data;
		this.pHash = pHash;
		this.timeStamp = new Date().getTime();
		this.hash = Hashing();
	}

	public String Hashing() {
		String Hashing = Encode.SHA256(pHash + Long.toString(timeStamp) + data);
		
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


