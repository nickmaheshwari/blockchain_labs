package blockchain_lab5;

public class Header {
	
	private int version = 1;
	private String hashPrevBlock;
	private String hashMerkleRoot;
	private String timeStamp;
	private long bits;
	private int nonce = 0;
	
	public Header(String hashPrevBlock, String hashMerkleRoot, String timeStamp, long bits) {
		this.hashPrevBlock = hashPrevBlock;
		this.hashMerkleRoot = hashMerkleRoot;
		this.timeStamp = timeStamp;
		this.bits = bits;
	}

	public String toString() {
		//this will be used to stringalyze the header for hashing
		return this.timeStamp+this.hashMerkleRoot+this.bits+this.nonce+this.hashPrevBlock;
	}
	
	public String outputString() {
		return "Header:\n " +"\tTime: "+this.timeStamp+ "\n\tMerkle Root: "+this.hashMerkleRoot+ "\n\tSize (bits): " 
				+this.bits+"\n\tNonce: "+this.nonce+"\n\tPrev Block:"+this.hashPrevBlock + "\n";
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getHashPrevBlock() {
		return hashPrevBlock;
	}

	public void setHashPrevBlock(String hashPrevBlock) {
		this.hashPrevBlock = hashPrevBlock;
	}

	public String getHashMerkleRoot() {
		return hashMerkleRoot;
	}

	public void setHashMerkleRoot(String hashMerkleRoot) {
		this.hashMerkleRoot = hashMerkleRoot;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getBits() {
		return bits;
	}

	public void setBits(long bits) {
		this.bits = bits;
	}

	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}
	
	
}
