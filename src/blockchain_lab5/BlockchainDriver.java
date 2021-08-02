package blockchain_lab5;

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
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.common.hash.Hashing;

public class BlockchainDriver {

	public static final String genBlockPrevBlock = "0000000000000000000000000000000000000000000000000000000000000000";

	// create the blockchain (list of blocks)
	static List<Block> blockchain = new ArrayList<Block>();
	
	public static void main(String[] args) {
		/***
		 * Driver for Block, Header and transaction. Creates a blockchain as a list of blocks and asks the user to
		 * provide inputs for search operations on the blockchain.
		 */

		// create and add genesis block to blockchain
		List<Transaction> genesisTransactions = createBlockTransactions("GenesisTransactionIDs.txt");
		Block genesisBlock = createBlock(genesisTransactions);

		blockchain.add(genesisBlock);

		//create next two blocks and add to blockchain 
		
		List<Transaction> allTransactions = createBlockTransactions("TransactionIDs.txt"); //get transactions based on TransactionIDs.txt
		List<Transaction> firstFive = allTransactions.subList(0, 5); //split into two
		List<Transaction> lastFive = allTransactions.subList(5, 10);
		
		sleep(); //look for the difference in the header's times!
		Block firstBlock = createBlock(firstFive);
		blockchain.add(firstBlock);
		
		sleep(); //look for the difference in the header's times!
		Block secondBlock = createBlock(lastFive);
		blockchain.add(secondBlock);
		
		System.out.println("All blocks added to blockchain! Printing now...\n\n");
		for (Block block : blockchain) {
			System.out.println(block);
		}
		
		
		//Find block by block height
		
		Boolean done = false;
		Scanner scan = new Scanner(System.in);
		
		while(done == false) {
			System.out.println("\n\n[Find block by height] Please enter an integer: ");
			int height = scan.nextInt();
			Block found = findByBlockHeight(height);
			
			if(found == null) {
				System.out.println("Invalid height, please try again...");
			}else {
				System.out.println(found);
				System.out.println("Try again? [Enter 'Y' for Yes or 'N' for No]");
				String response = scan.next().toLowerCase();
				
				if(response.equals("n")) {
					done = true;
				}
			}
		}
	
		//Find block by block hash
	
		done = false;
		while(done == false) {
			//Find block by block hash 
			System.out.println("\n\n[Find block by blockhash] Please enter a block hash: ");
			String hash = scan.next();
			Block found = findByBlockHash(hash);
			
			if(found == null) {
				System.out.println("Invalid blockhash, please try again...");
			}else {
				System.out.println(found);
				System.out.println("Try again? [Enter 'Y' for Yes or 'N' for No]");
				String response = scan.next().toLowerCase();
				
				if(response.equals("n")) {
					done = true;
				}
			}
		}
		
		//Find transaction by transaction hash
		done = false;
		while(done == false) {
			//Find block by block hash 
			System.out.println("\n\n[Find transaction by hash] Please enter a transaction hash: ");
			String hash = scan.next();
			Transaction found = findByTransactionHash(hash);
			
			if(found == null) {
				System.out.println("Invalid transaction hash, please try again...");
			}else {
				System.out.println(found);
				System.out.println("\n\nTry again? [Enter 'Y' for Yes or 'N' for No]");
				String response = scan.next().toLowerCase();
				
				if(response.equals("n")) {
					done = true;
				}
			}
		}
		
		
		scan.close(); //close scanner
	}



//////SEARCH FUNCTIONS/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Block findByBlockHeight(int height) {
		if(height>blockchain.size()-1) {
			return null;
		}else {
			return blockchain.get(height);
		}
	}

	private static Block findByBlockHash(String hash) {
		Block f = null;
		for (Block block : blockchain) {
			if(block.getBlockhash().equals(hash)) {
				f = block;
			}
		}
		return f;
	}
	
	private static Transaction findByTransactionHash(String hash) {
		Transaction t = null;
		for (Block block : blockchain) {
			for (Transaction x : block.getTransactions()) {
				if(x.getTransactionHash().equals(hash)) {
					t = x;
				}
			}
		}
		return t;
	}
	
	
//////HELPER FUNCTIONS///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static String getShaTwiceHashed(String input) {
		/**
		 * returns the double sha256 hash of input as a String
		 */
		String k = Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
		return Hashing.sha256().hashString(k, StandardCharsets.UTF_8).toString();
	}

	
	
	private static Block createBlock(List<Transaction> transactions) {
		List<String> ids = new ArrayList<>();
		for (Transaction t : transactions) {
			ids.add(t.getTransactionHash());
		}
		Merklizer merklizer = new Merklizer(ids);
		String genMerkleRoot = merklizer.getMerkleRoot();
		String timeStamp = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss").format(new Date()); // get current time

		String prevBlock = "";
		if(blockchain.size() == 0) {
			prevBlock = genBlockPrevBlock;
		}else {
			prevBlock = blockchain.get(blockchain.size()-1).getBlockhash();
		}
		
		//calculate the size of the header 
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		Header blockHeader = new Header(prevBlock, genMerkleRoot, timeStamp, 0);
		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		blockHeader.setBits((beforeUsedMem-afterUsedMem)); //approximate size of genBlockHeader

		Block block = new Block(blockHeader, transactions, getShaTwiceHashed(blockHeader.toString()), blockchain.size(), 0, transactions.size()); //per instructions block size is always 0;
		return block;
	}
	

	private static List<Transaction> createBlockTransactions(String filename) {
		// read in transaction id's from TransactionIDs.txt and convert to List<String>
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
			Transaction temp = new Transaction(1, 1, Arrays.asList(new String[] { id }), 1,
					Arrays.asList(new String[] { s.toString() }), " ");
			String transHash = getShaTwiceHashed(String.valueOf(temp.getVersionNumber())+String.valueOf(temp.getInCounter()) + temp.getListOfInputs() + String.valueOf(temp.getOutCounter()) + temp.getListOfOutputs());
			temp.setTransactionHash(transHash);
			transactions.add(temp);
		}

		return transactions;
	}
	
	private static void sleep() {
		/***
		 * Pauses system execution for 1 second
		 * I chose to implement this method to show that the header's created time indeed works
		 */
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

}
