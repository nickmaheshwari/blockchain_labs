package blockchain_lab6;

import java.util.List;

public class Block {
	
	private int magicNumber = 0xD9B4BEF9;
	private int blockSize = 0;
	private Header header; 
	private int transactionCounter = 0;
	private List<Transaction> transactions;
	private String blockhash; //The Block's Blockhash is a hash of the current block's header.  
	private int height;
	
	
	public Block(Header header, List<Transaction> transactions, String blockhash, int height, int size, int transactionCounter) {
		this.header = header;
		this.transactions = transactions;
		this.blockhash = blockhash;
		this.height = height;
		this.blockSize = size;
		this.transactionCounter = transactionCounter;
	}
	
	public String toString() {
		String t = "";
		if(this.height==0) {
			t = "Block #" + this.height + " - Genesis Block\n----------------------------------------------------------------------------------------\n";
		}else {
			t = "Block #" + this.height + "\n----------------------------------------------------------------------------------------\n";
		}
		return t+this.header.outputString() + "\nBlock Transactions:"+this.transactions + "\nTransaction Counter: "+ this.transactionCounter + "\nBlockhash: " + this.blockhash + "\nBlock Size: "+this.blockSize + "\n\n";
	}
	
	public String printBlock() {
		return this.toString();
	}


	public int getMagicNumber() {
		return magicNumber;
	}


	public void setMagicNumber(int magicNumber) {
		this.magicNumber = magicNumber;
	}


	public int getBlockSize() {
		return blockSize;
	}


	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}


	public Header getHeader() {
		return header;
	}


	public void setHeader(Header header) {
		this.header = header;
	}


	public int getTransactionCounter() {
		return transactionCounter;
	}


	public void setTransactionCounter(int transactionCounter) {
		this.transactionCounter = transactionCounter;
	}


	public List<Transaction> getTransactions() {
		return transactions;
	}


	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}


	public String getBlockhash() {
		return blockhash;
	}


	public void setBlockhash(String blockhash) {
		this.blockhash = blockhash;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	
	
	
	
}
