package blockchain_lab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.common.hash.Hashing;

import blockchain_lab6.Transaction.Output;


public class Blockchain {
	private List<Block> chain;

	public static final String genBlockPrevBlock = "0000000000000000000000000000000000000000000000000000000000000000";
	
	public Blockchain() {
		setChain(new ArrayList<Block>());
	}

	public List<Block> getChain() {
		return chain;
	}

	public void setChain(List<Block> chain) {
		this.chain = chain;
	}
	
	public void add(Block block) {
		chain.add(block);
	}
	
	public int size() {
		return this.chain.size();
	}
	
	public void addGenesis() {
		//create genesis block and adds it to the chain
		List<Transaction> txns = createGenesisBlockTransactions("GenesisTransactionIDs.txt");
		Block genesis = createGenesisBlock(txns);
		chain.add(genesis);
	}
	
	private static Block createGenesisBlock(List<Transaction> transactions) {
		List<String> ids = new ArrayList<>();
		for (Transaction t : transactions) {
			ids.add(t.getTransactionHash());
		}
		Merklizer merklizer = new Merklizer(ids);
		String genMerkleRoot = merklizer.getMerkleRoot();
		String timeStamp = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss").format(new Date()); // get current time

		String prevBlock = genBlockPrevBlock;
		
		//calculate the size of the header 
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		Header blockHeader = new Header(prevBlock, genMerkleRoot, timeStamp, 0);
		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		blockHeader.setBits((beforeUsedMem-afterUsedMem)); //approximate size of genBlockHeader

		Block block = new Block(blockHeader, transactions, getShaTwiceHashed(blockHeader.toString()), 0, 0, transactions.size()); //per instructions block size is always 0;
		return block;
	}
	

	private static List<Transaction> createGenesisBlockTransactions(String filename) {
		// read in transaction id's from filename and convert to List<String>
		InputStream is = BlockchainDriver.class.getResourceAsStream(filename);
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		List<String> transactionIds = new ArrayList<String>();
		String line;
		try {
			while ((line = r.readLine()) != null) {
				transactionIds.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Transaction> transactions = new ArrayList<Transaction>();
		for (String id : transactionIds) {
			StringBuilder s = new StringBuilder();
			s.append(id);
			s.reverse();
			Output o = new Output(500, 0, "");
			Transaction temp = new Transaction(1, 1, Arrays.asList(new String[] { id }), 1,
					Arrays.asList(new Output[] { o }), " ");
			String transHash = getShaTwiceHashed(String.valueOf(temp.getVersionNumber())+String.valueOf(temp.getInCounter()) + temp.getListOfInputs() + String.valueOf(temp.getOutCounter()) + temp.getListOfOutputs());
			temp.setTransactionHash(transHash);
			transactions.add(temp);
		}

		return transactions;
	}
	
	
	private static String getShaTwiceHashed(String input) {
		/**
		 * returns the double sha256 hash of input as a String
		 */
		String k = Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
		return Hashing.sha256().hashString(k, StandardCharsets.UTF_8).toString();
	}

	
}
