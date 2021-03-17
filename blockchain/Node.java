package blockchain;

// Use one-way Linked List: default
import java.util.*;

public class Node {
	public String hash, pHash;
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.
	public long timeStamp; // As s number of millisecond since 1/1/1970
	public int nonce;

	// Block constructor:
	public Node(String previousHash ) {
		this.pHash = previousHash;
		this.timeStamp = new Date().getTime();

		this.hash = Hashing(); //Making sure we do this after we set the other values.
	}


	public String Hashing() {
		// Calculate new hash based on blocks contents:
		String Hashing = StringUtil.applySha256(
				pHash + Long.toString(timeStamp) + 
				Integer.toString(nonce) + merkleRoot);

		return Hashing;
	}

	// Increases nonce value until hash target is reached.
	public void mineBlock(int difficulty) {
		merkleRoot = StringUtil.getMerkleRoot(transactions);
		String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0" 

		while(!hash.substring(0, difficulty).equals(target)) {
			nonce ++;
			hash = Hashing();
		}
		System.out.println("Block Mined. : " + hash);
	}

	// Add transactions to this block
	public boolean addTransaction(Transaction transaction) {

		// Process transaction and check if valid, unless block is genesis block then ignore
		if(transaction == null) return false;		
		if((pHash != "0")) {
			if((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}
}


