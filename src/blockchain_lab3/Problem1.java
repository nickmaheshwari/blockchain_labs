package blockchain_lab3;

import java.util.ArrayList;
import java.util.List;

public class Problem1 {
	
	public static void main(String[] args)
    {
		bruteForceDecrypt();
    }
	
	
	public static void bruteForceDecrypt() {
		String encrypted = "maxzxxlxmatmetbwmaxzhewxgxzzlunmgxoxkvtvdexw";
		String alpha = "abcdefghijklmnopqrstuvwxyz";
		List<String> results = new ArrayList<String>();
		
		for(int i=1; i <26; i++) { //need to try every shift value
			String temp = ""; //redeclare temp
			for(char c : encrypted.toCharArray()) {	//need to shift every letter in the encrypted text
				for(char x : alpha.toCharArray()) { //need to find the letter's index value
					if(c ==x) {
						temp += alpha.charAt((alpha.indexOf(x)+i)%26);
					}
				}
			}
			results.add(temp);
		}
		System.out.println("Correct Rotation Factor For String "+ encrypted+" : 6");
		System.out.println("Plaintext: "+results.get(6));
		System.out.println("Quote From Winston Churchill");
	}
	
}
