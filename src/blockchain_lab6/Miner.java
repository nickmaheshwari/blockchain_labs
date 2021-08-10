package blockchain_lab6;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.common.hash.Hashing;

import blockchain_lab6.Merklizer;
import blockchain_lab6.Transaction;
import blockchain_lab6.Transaction.Output;

public class Miner {
	private static final int EXPONENT = 0x20;
	private static final int COEFFICIENT = 0x7fffff;
	public static final int MAX_TXNS = 10;
	private static final String COINBASE_ID = "1111111111111111111111111111111111111111111111111111111111111111";
	private String target;
	private TxnMemoryPool pool;
	
	public Miner(TxnMemoryPool pool) {
		this.target = calculateTarget();
		this.pool = pool;
	}


	private String calculateTarget() {
		long x = 0x8 * (EXPONENT - 0x3);
		String target = String.valueOf(COEFFICIENT * Math.pow(0x2, x));
		return target;
	}
	
	public List<Block> getValidBlocks(Block prevBlock){
		List<Block> validBlocks = new ArrayList<>();
		int x=0;
		int y= MAX_TXNS;
		
		while(y < pool.getTxn().size() - MAX_TXNS) {
			Block candidate = createCandidateBlock(prevBlock, x, y);
			x+= MAX_TXNS;
			y+= MAX_TXNS;
			if(checkValidity(candidate)) {
				validBlocks.add(candidate);
				prevBlock = candidate;
			}
		}
		return validBlocks;
	}
	


	public Block createCandidateBlock(Block prevBlock, int x, int y) {
		
		// create block from coinbase txn and pool txns
		List<Transaction> blockTxns = new ArrayList<>();
		blockTxns.add(createCoinBaseTransaction());
		blockTxns.addAll(pool.getTxn().subList(x, y));
		List<String> ids = new ArrayList<>();
		for (Transaction t : blockTxns) {
			ids.add(t.getTransactionHash());
		}
		Merklizer merklizer = new Merklizer(ids);
		String merkleRoot = merklizer.getMerkleRoot();
		String timeStamp = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss").format(new Date()); // get current time
		Header header = new Header(prevBlock.getBlockhash(), merkleRoot, timeStamp, 0);
		Block candidateBlock = new Block(header, blockTxns, getShaTwiceHashed(header.toString()), 0, 0, MAX_TXNS);
		return candidateBlock;
	}
	
	
	public Transaction createCoinBaseTransaction() {
		Output o = new Output(500, 0, "");
		Transaction coinbaseTxn = new Transaction(1, 1, Arrays.asList(new String[] { COINBASE_ID }), 1,
				Arrays.asList(new Output[] { o }), " ");
		return coinbaseTxn;
	}
	

	private boolean checkValidity(Block block) { 
		//checks to see if a blockhash is less than target
		boolean valid = false;
		if(block.getBlockhash().compareTo(target) < 0) {
			valid = true;
		}
		return valid;
	}
	
	private static String getShaTwiceHashed(String input) {
		/**
		 * returns the double sha256 hash of input as a String
		 */
		String k = Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
		return Hashing.sha256().hashString(k, StandardCharsets.UTF_8).toString();
	}


	public String getTarget() {
		return target;
	}


	public void setTarget(String target) {
		this.target = target;
	}

}
