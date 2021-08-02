package blockchain_lab5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.hash.Hashing;



public class Merklizer {

	private static int levelCount = 1; // class level variable to keep track of merkle tree's current level
	private static List<String> ids;
	
	public Merklizer(List<String> ids) {
		this.ids = ids;
	}
	
	public String getMerkleRoot() {
		List<MerkleNode> bottomLeaves = new ArrayList<MerkleNode>();
		List<MerkleNode> merkleTree = new ArrayList<MerkleNode>();
		
		for (String id: ids) {
			MerkleNode e = new MerkleNode(id, 0, "","","");
			bottomLeaves.add(e);
		}
		
		merkleTree = getMerkleTree(merkleTree, bottomLeaves);
		return merkleTree.get(merkleTree.size()-1).getHashValue();
	}
	private static List<MerkleNode> getMerkleTree(List<MerkleNode> merkleTree, List<MerkleNode> prevLevel) {

		// map to store a node's hash value (key) with its level on the tree (value)
		List<MerkleNode> newLevelList = new ArrayList<MerkleNode>();
		
		// Base case
		if (prevLevel.size() == 1) {
			/*System.out.println(
					"\nFINAL MERKLE ROOT: \n" + merkleTree.get(merkleTree.size()-1).getHashValue() + "\n\n");
			merkleTree.get(merkleTree.size()-1).setParent("root");*/
			return merkleTree;
		}

		/*System.out.println("Number of Branches in Level " + levelCount + " is " + prevLevel.size() + "\n");*/

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
	        
			/*
			 * //print branch info if(levelCount == 1) { System.out.println("Branch " + (i +
			 * 1) + " is " + h1); System.out.println("Branch " + (i + 2) + " is " + h2);
			 * }else { System.out.println("Branch " + (i + 1) + " is b'" + h1 + "'");
			 * System.out.println("Branch " + (i + 2) + " is b'" + h2 + "'"); }
			 * 
			 * System.out.println("Branch hash is b'" + result + "'\n");
			 */
			
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
		
		//System.out.println("Completed Level " + levelCount + "\n"
		//		+ "#################################################################################");
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
