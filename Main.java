// Building virtual Block Chain system (6th Oct 2019)
// -----------------------------------------------------------------
// Node.java contains valid data.
// ++ Add mining function on Node.java
// Encode.java contains SHA-264 algorithm and MessageDigest Method.

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Main {
	public static final ArrayList<Node> blockchain = new ArrayList<Node>();
	
	public static void main(String[] args) {
		blockchain.add(new Node("This is genesis node.", "b00eb94750971cf53b76d3941544ebeb8f5992c8694533039d5c5ca3aae42545"));
		blockchain.add(new Node("Here is second node.", blockchain.get(blockchain.size()-1).hash));
		blockchain.add(new Node("I am third node.", blockchain.get(blockchain.size()-1).hash));
	
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);		
		System.out.println(blockchainJson);
	}
	
	public static Boolean isChainValid() {	// Check Node is edited
		Node cNode; 
		Node pNode;
		
		for( int i = 1; i < blockchain.size(); i++ ) {
			cNode = blockchain.get(i);
			pNode = blockchain.get(i-1);
		
			if( !cNode.hash.equals(cNode.Hashing()) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}
		
			if( !pNode.hash.equals(cNode.pHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
		}
		return true;
	}
}
