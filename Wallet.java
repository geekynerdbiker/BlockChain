package BlockChain;

import java.util.*;
import java.security.*;
import java.security.spec.*;

public class Wallet {
	public PrivateKey prKey;
	public PublicKey pbKey;
	
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); // Only UTXOs owned by this wallet
	

	public Wallet() {
		generateKeyPair();
	}

	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair

			keyGen.initialize(ecSpec, random);
			// 256 bytes provides an acceptable security level
			
			KeyPair keyPair = keyGen.generateKeyPair();
			// Set the public and private keys from the keyPair
			
			prKey = keyPair.getPrivate();
			pbKey = keyPair.getPublic();

		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// Returns balance and stores the UTXO's owned by this wallet in this.UTXOs
		public float getBalance() {
			float total = 0;	
	        for (Map.Entry<String, TransactionOutput> item: BlockChain.UTXOs.entrySet()){
	        	TransactionOutput UTXO = item.getValue();
	            if(UTXO.isMine(pbKey)) { //if output belongs to me ( if coins belong to me )
	            	UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
	            	total += UTXO.value ; 
	            }
	        }  
			return total;
		}
		// Generates and returns a new transaction from this wallet.
		public Transaction sendFunds(PublicKey _recipient,float value ) {
			if(getBalance() < value) { //gather balance and check funds.
				System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
				return null;
			}
	    // Create array list of inputs
			ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	    
			float total = 0;
			for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
				TransactionOutput UTXO = item.getValue();
				total += UTXO.value;
				inputs.add(new TransactionInput(UTXO.id));
				if(total > value) break;
			}
			
			Transaction newTransaction = new Transaction(pbKey, _recipient , value, inputs);
			newTransaction.generateSignature(prKey);
			
			for(TransactionInput input: inputs){
				UTXOs.remove(input.transactionOutputId);
			}
			return newTransaction;
		}
}
