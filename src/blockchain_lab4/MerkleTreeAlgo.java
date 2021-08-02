package blockchain_lab4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;


public class MerkleTreeAlgo {

	private static int levelCount = 1; // class level variable to keep track of merkle tree's current level
	static MessageDigest digest;
	
	public static void main(String[] args) {

//////// STEP 1: construct merkle tree from transaction id's ///////////////////////////////////////////////////////////////////////////////////////////////		
		
		// read in transaction id's from TransactionIDs.txt and convert to List<String>
		InputStream is = MerkleTreeAlgo.class.getResourceAsStream("TransactionIDs.txt");
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


		List<MerkleNode> bottomLeaves = new ArrayList<MerkleNode>();
		List<MerkleNode> merkleTree = new ArrayList<MerkleNode>();
		
		for (String id: transactionIds) {
			MerkleNode e = new MerkleNode(id, 0, "","","");
			bottomLeaves.add(e);
		}
		// calculate merkle tree, print level by level, and return
		merkleTree = getMerkleTree(merkleTree, bottomLeaves);

		//  for (MerkleNode merkleNode : merkleTree) { System.out.println(merkleNode); } //print MerkleTree
		 

//////// STEP 2: Get path for searchKey ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String searchKey = "cfce9664889e17fa006cfa23dd82852a999f9a748478cf325e3791241dd27a50";
		List<MerkleNode> proofPath = getProofOfInclusionPath(merkleTree, searchKey);

		//print out path
		int i = 0;
		System.out.println("\n\n\n"+"VERIFICATION COMPLIMENTS" + "\n"
				+ "#################################################################################");
		for (MerkleNode merkleNode : proofPath) {
			String message = "";
			if(i==0) {
				message = "Transcation to be verified: ";
			}else {
				message = "Compliment #" + String.valueOf(merkleNode.getLevel()+1) + ": ";
			}
			System.out.println(message + merkleNode.getHashValue());
			i++;
		}
		 
	}


	private static List<MerkleNode> getMerkleTree(List<MerkleNode> merkleTree, List<MerkleNode> prevLevel) {

		// map to store a node's hash value (key) with its level on the tree (value)
		List<MerkleNode> newLevelList = new ArrayList<MerkleNode>();
		
		// Base case
		if (prevLevel.size() == 1) {
			System.out.println(
					"\nFINAL MERKLE ROOT: \n" + merkleTree.get(merkleTree.size()-1).getHashValue() + "\n\n");
			merkleTree.get(merkleTree.size()-1).setParent("root");
			return merkleTree;
		}

		System.out.println("Number of Branches in Level " + levelCount + " is " + prevLevel.size() + "\n");

		if(prevLevel.size() % 2 ==1) {
			MerkleNode newNode = prevLevel.get(prevLevel.size()-1);
			prevLevel.add(newNode);
		}
		
		// Hash the leaf transaction pair to get parent transaction
		for (int i = 0; i < prevLevel.size(); i += 2) {
			
			String h1 = prevLevel.get(i).getHashValue();
			String h2 = prevLevel.get(i+1).getHashValue();
			
			//Little Endian / Hex conversions
			byte[] b1 = hexStringToByteArray(h1);
			b1 = swapEndianness(b1);
			byte[] b2 = hexStringToByteArray(h2);
			b2 = swapEndianness(b2);
			
			byte[] concat = Arrays.copyOf(b1, b1.length + b2.length);
	        System.arraycopy(b2, 0, concat, b1.length, b2.length);
	        byte[] twiceHashedBytes = getBytesTwiceShaHash(getBytesTwiceShaHash(concat));
	        String result = getStringFromHex(swapEndianness(twiceHashedBytes));
	        
			//print branch info
	        if(levelCount == 1) {
				System.out.println("Branch " + (i + 1) + " is " + h1);
				System.out.println("Branch " + (i + 2) + " is " + h2);
	        }else {
	        	System.out.println("Branch " + (i + 1) + " is b'" + h1 + "'");
				System.out.println("Branch " + (i + 2) + " is b'" + h2 + "'");
	        }
			
			System.out.println("Branch hash is b'" + result + "'\n");
			
			//set parents
			prevLevel.get(i).setParent(result);
			prevLevel.get(i+1).setParent(result);

			//create and add new node to this level
			MerkleNode newNode = new MerkleNode(result, levelCount, h1, h2, "");
			newLevelList.add(newNode);
		}

		//add all new nodes to whole tree
		for(MerkleNode e : newLevelList) {
			merkleTree.add(e);
		}
		
		System.out.println("Completed Level " + levelCount + "\n"
				+ "#################################################################################");
		levelCount++;
		
		//recursive call
		return getMerkleTree(merkleTree, newLevelList);
	}
	
	public static byte[] getBytesTwiceShaHash(byte[] input) {
		// using guava library to hash input
		return Hashing.sha256().hashBytes(input).asBytes();
	}
	
	public static byte[] swapEndianness(byte[] hash) {
		byte[] result = new byte[hash.length];
		for (int i = 0; i < hash.length; i++) {
			result[i] = hash[hash.length - i - 1];
		}
		return result;
	}
	
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	
	private static final String HEX_REFERENCE = "0123456789abcdef"; //needed for hex conversion
	
	public static String getStringFromHex(byte[] bytes) {
		final StringBuilder result = new StringBuilder(2 * bytes.length);
		for (final byte b : bytes) {
			result.append(HEX_REFERENCE.charAt((b & 0xF0) >> 4)).append(HEX_REFERENCE.charAt((b & 0x0F)));
		}
		return result.toString();
	}
	    
	
	
	private static List<MerkleNode> getProofOfInclusionPath(List<MerkleNode> merkleTree, String searchKey) {
		/***
		 * This method returns the merkle tree verification path for the given merkleTree 
		 * and searchKay (represents the hash we're looking for).
		 */
		
		MerkleNode searchNode = findMerkleNode(merkleTree, searchKey);
		List<MerkleNode> path = findPathToSource(merkleTree, searchNode);
		return path;
	}

	
	
	private static List<MerkleNode> findPathToSource(List<MerkleNode> merkleTree, MerkleNode searchNode) {
		
		List<MerkleNode> path = new ArrayList<MerkleNode>();
		path.add(searchNode);
		// searches merkle tree for nodes in compliment path
		for (MerkleNode merkleNode : merkleTree) {
			if(merkleNode.getHashValue().equals(searchNode.getParent())) { //find parent
				
				if(searchNode.getHashValue().equals(merkleNode.getLeftChild())) { 
					MerkleNode compliment = findMerkleNode(merkleTree, merkleNode.getRightChild()); //need the search node's compliment
					path.add(compliment);
				}else {
					MerkleNode compliment = findMerkleNode(merkleTree, merkleNode.getLeftChild());
					path.add(compliment);
				}
				searchNode = merkleNode; //move up a level
			}
		}
		
		return path;
	}


	private static MerkleNode findMerkleNode(List<MerkleNode> merkleTree, String searchKey) {
		// returns the node with the id of search key from the merkleTree
		for (MerkleNode merkleNode : merkleTree) {
			if(merkleNode.getHashValue().equals(searchKey)) {
				return merkleNode;
			}
			else if(merkleNode.getLeftChild().equals(searchKey) || merkleNode.getRightChild().equals(searchKey)) {
				return new MerkleNode(searchKey, 0, "", "", merkleNode.getHashValue());
			}
		}
		return null;
	}



	//Model class for node within the Merkle Tree
	private static class MerkleNode{
		String hashValue;
		int level;
		String leftChild, rightChild, parent;
		
		public MerkleNode(String hashValue, int level, String leftChild, String rightChild, String parent) {
			super();
			this.hashValue = hashValue;
			this.level = level;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
			this.parent = parent;
		}

		public String toString() {
			return this.hashValue + ", lvl:" + this.level +", l_child:"+ this.leftChild + ", r_child:" + this.rightChild + ", parent:" + this.parent;
		}
		public String getHashValue() {
			return hashValue;
		}

		public void setHashValue(String hashValue) {
			this.hashValue = hashValue;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public String getLeftChild() {
			return leftChild;
		}

		public void setLeftChild(String leftChild) {
			this.leftChild = leftChild;
		}

		public String getRightChild() {
			return rightChild;
		}

		public void setRightChild(String rightChild) {
			this.rightChild = rightChild;
		}

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}
		
		
	}

}
