package blockchain_lab5;

import java.util.List;

public class Transaction {
	
	private int versionNumber;
	private int inCounter;
	private List<String> listOfInputs;
	private int outCounter;
	private List<String> listOfOutputs;
	private String transactionHash;
	
	public Transaction(int versionNumber, int inCounter, List<String> listOfInputs, int outCounter, List<String> listOfOutputs, String transactionHash) {
		this.versionNumber = versionNumber;
		this.inCounter = inCounter;
		this.listOfInputs = listOfInputs;
		this.outCounter = outCounter;
		this.listOfOutputs = listOfOutputs;
		this.transactionHash = transactionHash;
	}
	
	public String toString() {
		return "\nTransaction:\n "+"\tVersion: "+ this.versionNumber + "\n\tIn Counter: " + this.inCounter + "\n\tInputs: "+ this.listOfInputs
				+ "\n\tOut Counter: "+ this.outCounter + "\n\tOutputs: "+ this.listOfOutputs + "\n\tTransaction Hash: "+ this.transactionHash;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public int getInCounter() {
		return inCounter;
	}

	public void setInCounter(int inCounter) {
		this.inCounter = inCounter;
	}

	public List<String> getListOfInputs() {
		return listOfInputs;
	}

	public void setListOfInputs(List<String> listOfInputs) {
		this.listOfInputs = listOfInputs;
	}

	public int getOutCounter() {
		return outCounter;
	}

	public void setOutCounter(int outCounter) {
		this.outCounter = outCounter;
	}

	public List<String> getListOfOutputs() {
		return listOfOutputs;
	}

	public void setListOfOutputs(List<String> listOfOutputs) {
		this.listOfOutputs = listOfOutputs;
	}

	public String getTransactionHash() {
		return transactionHash;
	}

	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}
	
	
}
