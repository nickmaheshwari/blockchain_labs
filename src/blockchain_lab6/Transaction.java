package blockchain_lab6;

import java.util.List;

public class Transaction {
	
	private int versionNumber;
	private int inCounter;
	private List<String> listOfInputs;
	private int outCounter;
	private List<Output> listOfOutputs;
	private String transactionHash;
	
	public Transaction(int versionNumber, int inCounter, List<String> listOfInputs, int outCounter, List<Output> listOfOutputs, String transactionHash) {
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

	public List<Output> getListOfOutputs() {
		return listOfOutputs;
	}

	public void setListOfOutputs(List<Output> listOfOutputs) {
		this.listOfOutputs = listOfOutputs;
	}

	public String getTransactionHash() {
		return transactionHash;
	}

	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}
	
	
	//model class for outputs
	public static class Output{
		int value; //value is in milliNickcoins, aka a value of '1000' is one Nickcoin
		int index;
		String script;
		
		public Output(int value, int index, String script) {
			super();
			this.value = value;
			this.index = index;
			this.script = script;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getScript() {
			return script;
		}

		public void setScript(String script) {
			this.script = script;
		}
		
		public String toString() {
			return String.valueOf(this.value);
		}
		
	}
	
}
