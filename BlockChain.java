package BlockChain;
/*
 <Building virtual Block Chain system (since 6th Oct 2019)>
 ----------------------------------------------------------------------------------------------
 [Day1] ver 1.0.0
 
 Node.java contains valid data.
 ++ Add mining function on Node.java
 Encode.java contains SHA-264 algorithm and MessageDigest Method.
 ----------------------------------------------------------------------------------------------
 [Day4] ver 1.0.1
 
 StringUtil.java contains applying SHA-256 and EBDSA Algorithm Method, some of Gether Method.
 Wallet.java contains Public Key and Private Key and Key Pair, Balance Gether Method.
 Transaction.java contains Methods of Encode.java and some Method for transaction function.
 ++ No more use Encode.java
 ----------------------------------------------------------------------------------------------
 [Day5] ver 1.0.2

 Change Summary Day Count to Version
 ++ Remove Encode.java
 ++ Move valid data from Node.java to Wallet.java.
 TransactionInput(Output).java contains Gether and Putter Method for Transaction.java
 ----------------------------------------------------------------------------------------------
*/

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
//import com.google.gson.GsonBuilder;

public class BlockChain {
	public static final ArrayList<Node> blockchain = new ArrayList<Node>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); // List of all unspent transactions 

	public static int difficulty = 5;
	public static float minimumTransaction = 0.1f;
	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;

	public static void main(String[] args) {

		// Add our blocks to the blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

		// Create wallets:
		walletA = new Wallet();
		walletB = new Wallet();		
		Wallet coinbase = new Wallet();

		// Create genesis transaction, which sends 100 NoobCoin to walletA: 
		genesisTransaction = new Transaction(coinbase.pbKey, walletA.pbKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.prKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

		System.out.println("Creating and Mining Genesis block... ");
		Node genesis = new Node("0");
		genesis.addTransaction(genesisTransaction);
		addNode(genesis);

		//testing
		Node block1 = new Node(genesis.hash);
		System.out.println("\nWallet A's balance is: " + walletA.getBalance());
		System.out.println("\nWallet A is Attempting to send funds (40) to Wallet B...");
		block1.addTransaction(walletA.sendFunds(walletB.pbKey, 40f));
		addNode(block1);
		System.out.println("\nWallet A's balance is: " + walletA.getBalance());
		System.out.println("Wallet B's balance is: " + walletB.getBalance());

		Node block2 = new Node(block1.hash);
		System.out.println("\nWallet A Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.pbKey, 1000f));
		addNode(block2);
		System.out.println("\nWallet A's balance is: " + walletA.getBalance());
		System.out.println("Wallet B's balance is: " + walletB.getBalance());

		Node block3 = new Node(block2.hash);
		System.out.println("\nWallet B is Attempting to send funds (20) to Wallet A...");
		block3.addTransaction(walletB.sendFunds( walletA.pbKey, 20));
		System.out.println("\nWallet A's balance is: " + walletA.getBalance());
		System.out.println("Wallet B's balance is: " + walletB.getBalance());

		isChainValid();
	}

	public static Boolean isChainValid() {	// Check Node is edited
		Node cNode; 
		Node pNode;
		String hashTarget = new String(new char[difficulty]).replace('\0','0');

		// A temporary working list of unspent transactions at a given block state
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); 
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

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

			// Check if hash is solved:
			if( !cNode.hash.substring(0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined.");
				return false;
			}

			// Loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t=0; t <cNode.transactions.size(); t++) {
				Transaction currentTransaction = cNode.transactions.get(t);

				if(!currentTransaction.verifiySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				}

				for(TransactionInput input: currentTransaction.inputs) {	
					tempOutput = tempUTXOs.get(input.transactionOutputId);

					if(tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}

					if(input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}

					tempUTXOs.remove(input.transactionOutputId);
				}

				for(TransactionOutput output: currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}

				if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
			}
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	
	public static void addNode(Node newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}
