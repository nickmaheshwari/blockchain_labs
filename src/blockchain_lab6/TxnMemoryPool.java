package blockchain_lab6;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.google.common.hash.Hashing;

import blockchain_lab6.Transaction.Output;

public class TxnMemoryPool {
	List<Transaction> txn;
	public static final String script = "Sample output script. Will likely be changed in next iteration";

	public TxnMemoryPool() {
		this.txn = createTransactions();
	}

	public List<Transaction> getTxn() {
		return txn;
	}

	public void setTxn(List<Transaction> txn) {
		this.txn = txn;
	}
	
	
	private static List<Transaction> createTransactions() {
		//creates 91 new transactions and returns them
		Random rand = new Random();
		List<Transaction> txns = new ArrayList<>();
		int counter = 0;
		
		while(counter < 91) {
			String timeStamp = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss").format(new Date()); // get current time
			
			//create 10 random inputs
			List<String> inputs = new ArrayList<>();
			int inCounter = 10;
			for(int i=0; i < inCounter; i++) {
				int n = rand.nextInt(50);
				String input = getShaTwiceHashed(timeStamp + String.valueOf(n));
				inputs.add(input);
			}
			
			//create 10 random outputs
			List<Output> outputs = new ArrayList<>();
			int outCounter = 10;
			for(int i =0; i<outCounter; i++) {
				Output o = new Output(500, i, script); //currently setting the value of each coin to 500, or .5 Nickcoins
				outputs.add(o);
			}
			
			//create the transaction and add to list
			Transaction t = new Transaction(1, inCounter, inputs, outCounter, outputs, "");
			String transHash = getShaTwiceHashed(String.valueOf(t.getVersionNumber())+String.valueOf(t.getInCounter()) + t.getListOfInputs() + String.valueOf(t.getOutCounter()) + t.getListOfOutputs());
			t.setTransactionHash(transHash);
			txns.add(t);
			counter++;
		}
		
		return txns;
	}
	
	private static String getShaTwiceHashed(String input) {
		/**
		 * returns the double sha256 hash of input as a String
		 */
		String k = Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
		return Hashing.sha256().hashString(k, StandardCharsets.UTF_8).toString();
	}
	
}
