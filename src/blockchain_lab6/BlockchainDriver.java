package blockchain_lab6;

import java.util.List;

public class BlockchainDriver {

	public static void main(String[] args) {
		/***
		 * Driver for Block, Header and transaction. Creates a blockchain as a list of
		 * blocks and asks the user to provide inputs for search operations on the
		 * blockchain.
		 */
		// create Blockchain and add genesis block
		Blockchain blockchain = new Blockchain();
		blockchain.addGenesis();

		// Create txn pool. pre-creates 91 new transactions and adds them to the
		// TxnMemoryPool before mining
		TxnMemoryPool pool = new TxnMemoryPool();

		// create Miner with txnpool
		Miner miner = new Miner(pool);

		// createCandidateBlocks returns a list of valid blocks created from txn pool
		Block prevBlock = blockchain.getChain().get(0);
		List<Block> newBlocks = miner.getValidBlocks(prevBlock);
		for (Block block : newBlocks) {
			block.setHeight(blockchain.size()); // set block's height
			blockchain.add(block);
		}

		
		System.out.println("All blocks added to blockchain! Printing now...\n\n");
		for (Block block : blockchain.getChain()) { System.out.println(block); }

		// print out the block height of the tip of the chain.
		System.out.println("Blockchain Height at Tip: " + blockchain.getChain().get(blockchain.size()-1).getHeight());
	}

}
