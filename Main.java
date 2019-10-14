// Building virtual Block Chain system (6th Oct 2019)
// -----------------------------------------------------------------
// Node.java contains valid data.
// ++ Add mining function on Node.java
// Encode.java contains SHA-264 algorithm and MessageDigest Method.

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Main {
	public static final ArrayList<Node> blockchain = new ArrayList<Node>();
	public static int difficulty = 5;
	
	public static void main(String[] args) {
		// Add our blocks to the blockchain ArrayList:
		blockchain.add(new Node("This is genesis node.", "0"));
		System.out.println("Trying to mine Blcok 1");
		blockchain.get(0).mineBlock(difficulty);
		
		blockchain.add(new Node("Here is second node.", blockchain.get(blockchain.size()-1).hash));
		System.out.println("Trying to mine Blcok 2");
		blockchain.get(1).mineBlock(difficulty);
		
		blockchain.add(new Node("I am third node.", blockchain.get(blockchain.size()-1).hash));
		System.out.println("Trying to mine Blcok 3");
		blockchain.get(2).mineBlock(difficulty);
		
		System.out.println("\nBlockchain is Vaild: " + isChainValid());
	
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);		
		System.out.println("\0\0 The Blockchain : ");
		System.out.println(blockchainJson);
	}
	
	public static Boolean isChainValid() {	// Check Node is edited
		Node cNode; 
		Node pNode;
		String hashTarget = new String(new char[difficulty]).replace('\0','0');
		
		// Loop through blockchain to check hashes:
		for( int i = 1; i < blockchain.size(); i++ ) {
			cNode = blockchain.get(i);
			pNode = blockchain.get(i-1);
		
			// Compare registered hash and calculated hash:
			if( !cNode.hash.equals(cNode.Hashing()) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}
			
			// Compare previous hash and registered previous hash:
			if( !pNode.hash.equals(cNode.pHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			
			// Chech if hash is solved:
			if( !cNode.hash.substring(0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined.");
				return false;
			}
		}
		return true;
	}
}
