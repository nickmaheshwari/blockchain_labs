package blockchain_lab3;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Problem2 {

	public static void main(String[] args)
    {
		freqAnalysisDecrypt();
    }

	private static void freqAnalysisDecrypt() {
		String e = "r glivcp gvvi kf gvvi mvijzfe fw vcvtkifezt trjy nflcu rccfn feczev grpdvekj kf sv jvek uzivtkcp wifd fev grikp kf refkyvi nzkyflk xfzex kyiflxy r wzeretzrc zejkzklkzfe. uzxzkrc jzxerklivj gifmzuv grik fw kyv jfclkzfe, slk kyv drze svevwzkj riv cfjk zw r kiljkvu kyziu grikp zj jkzcc ivhlzivu kf givmvek uflscv-jgveuzex. nv gifgfjv r jfclkzfe kf kyv uflscv-jgveuzex gifscvd ljzex r gvvi-kf-gvvi evknfib. kyv evknfib kzdvjkrdgj kirejrtkzfej sp yrjyzex kyvd zekf re fexfzex tyrze fw yrjy-srjvu giffw-fw-nfib, wfidzex r ivtfiu kyrk treefk sv tyrexvu nzkyflk ivufzex kyv giffw-fw-nfib. kyv cfexvjk tyrze efk fecp jvimvj rj giffw fw kyv jvhlvetv fw vmvekj nzkevjjvu, slk giffw kyrk zk trdv wifd kyv crixvjk gffc fw tgl gfnvi. rj cfex rj r drafizkp fw tgl gfnvi zj tfekifccvu sp efuvj kyrk riv efk tffgvirkzex kf rkkrtb kyv evknfib, kyvp'cc xvevirkv kyv cfexvjk tyrze reu flkgrtv rkkrtbvij. kyv evknfib zkjvcw ivhlzivj dzezdrc jkiltkliv. dvjjrxvj riv sifrutrjk fe r svjk vwwfik srjzj, reu efuvj tre cvrmv reu ivafze kyv evknfib rk nzcc, rttvgkzex kyv cfexvjk giffw-fw-nfib tyrze rj giffw fw nyrk yrggvevu nyzcv kyvp nviv xfev.";
		String encrypted = e.replaceAll(
		          "[^a-zA-Z0-9]", ""); //get rid of all alphanumeric chars
		
		//step 1
		HashMap<Character, Integer> freqTable = new HashMap<Character, Integer>(); //represents a table to keep track of the frequency of letters in the encrypted string
		for (int i = 0; i < encrypted.length(); i++) {
		    char c = encrypted.charAt(i);
		    Integer val = freqTable.get(c);
		    if (val != null) {
		    	freqTable.put(c,(val + 1));
		    }
		    else {
		    	freqTable.put(c, 1);
		   }
		}
		//System.out.println(freqTable.toString());
		
		//step 2
		String alpha = "abcdefghijklmnopqrstuvwxyz";
		Character[] alphaArray = 
			    alpha.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
		int shiftValue = getShiftValue(freqTable, alphaArray); //get shift value
		int rotationFactor = shiftValue; //used for storage
		
		//step 3
		List<CipherModel> alphaCipherTable = new ArrayList<CipherModel>(); //modeling the alphabet - cipher table seen in the step 3 directions (see README for directions link)
		for(int i = 0; i<26; i++) {
			if(shiftValue>26) {
				shiftValue = shiftValue % 26; //reset to beginning
			}
			shiftValue = shiftValue % 26;
			CipherModel cm = new CipherModel(alphaArray[i], alphaArray[shiftValue], i);
			alphaCipherTable.add(cm);
			shiftValue ++;
			
		}
		/*
		 * for(CipherModel cm:alphaCipherTable) { System.out.println(cm.toString()); }
		 */
		
		
		//step 4
		String plaintext = "";
		for(int i=0; i<encrypted.length(); i++) {
			for(int j=0; j<alphaCipherTable.size(); j++) {
				if( encrypted.charAt(i) == alphaCipherTable.get(j).getL1() ) {
					plaintext += alphaCipherTable.get(j).getL2();
				}
			}
		}
		
		
		//output results
		System.out.println("Plaintext (direct program output): " +plaintext);
		System.out.println("Plaintext (punctuation and spaces re-added): a purely peer to peer version of electronic cash would allow online payments to be sent directly from one party to another without going through a financial institution. digital signatures provide part of the solution, but the main benefits are lost if a trusted third party is still required to prevent double spending. we propose a solution to the double-spending problem using a peer to peer network. the network timestamps transactions by hashing them into an on going chain of hash-based proof-of-work, forming a record that cannot be changed without redoing the proof-of-work. the longest chain not only serves as proof of the sequence of events witnessed, but proof that it came from the largest pool of cpu power. as long as a majority of cpu power is controlled by nodes that are not cooperating to attack the network, they'll generate the longest chain and out pace attackers. the network itself requires minimal structure. messages are broadcast on a best effort basis, and nodes can leave and rejoin the network at will, accepting the longest proof-of-work chain as proof of what happened while they were gone.");
		System.out.println("Rotation Factor: "+rotationFactor);
		System.out.println("Quote From: Bitcoin: A Peer-to-Peer Electronic Cash System by Satoshi Nakamota");
	}
	
	
	
	private static int getShiftValue(HashMap<Character, Integer> freqTable, Character[] alphaArray) {
		//get most frequent character in the ciphertext
		Character mostFreqChar = Collections.max(freqTable.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
		
		int startValue= 0; //the value of where our cipher text letter is in the alphabet 
		
		for(int i=0; i<26; i++) {
			if(alphaArray[i].equals(mostFreqChar)) {
				startValue = i;
			}
		}
		int shiftValue = 26%(startValue - 4); //4 represents the index of 'e' in the alphabet array, e is the most common letter in the english alphabet
		//System.out.println(mostFreqChar);
		//System.out.println(shiftValue);
		return shiftValue;
	}



	//class used to model the cipher table seen in step 3 (for problem 2) on https://www.classes.cs.uchicago.edu/archive/2021/summer/56600-1/LABS/LAB.3/lab_3.html
	public static class CipherModel{
		char l1, l2; //l1 being the element from the standard alphabet (for plaintext generation), and l2 being the corresponding cipher element
		int index;
		
		public CipherModel(char l1, char l2, int index) {
			this.l1 = l1;
			this.l2 = l2;
			this.index = index;
		}
		
		public String toString() {
			String returnString = this.l1 + ", " + this.l2 + ", " + this.index;
			return returnString;
		}

		public char getL1() {
			return l1;
		}

		public void setL1(char l1) {
			this.l1 = l1;
		}

		public char getL2() {
			return l2;
		}

		public void setL2(char l2) {
			this.l2 = l2;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
}

