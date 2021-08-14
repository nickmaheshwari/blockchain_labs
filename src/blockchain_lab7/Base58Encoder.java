package blockchain_lab7;


public class Base58Encoder {
	private static final int OLD_BASE = 256;
	private static final int NEW_BASE = 58;
	public static final char[] CHARACTER_SET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
	
	public byte[] plaintext;
	public String encoded;


	public Base58Encoder(byte[] plaintext) {
		this.plaintext = plaintext;
		this.encoded = encodeBase58(plaintext);
	}

	private String encodeBase58(byte[] plainText) {
		/***
		 *  returns the base58 encoded version of input
		 *  
		 *  @param input : String to be encoded 
		 *  @return result : String representing the encoded input
		 */
		int leadingZeroCount = 0;
		
		//quick-exit case
		if (plainText.length == 0) {
			return "";
		}
		
		//need to know how many leading zeroes there are
		while (plainText[leadingZeroCount] == 0 && leadingZeroCount < plainText.length) {
			leadingZeroCount+=1;
		}
		
		
		char[] encodedText = new char[plainText.length * 2]; //encoded string cannot be larger than this
		int encodedIndex = encodedText.length;
		
		//start conversion from first non-zero (working backwards like base58 examples)
		for (int i = leadingZeroCount; i < plainText.length;) {
			encodedIndex -= 1;
			encodedText[encodedIndex] = CHARACTER_SET[getRemainder(plainText, i)]; //find the char in the alphabet corresponding to the remainder
			//pass over zeroes
			if (plainText[i] == 0) {
				i+=1; 
			}
		}
		
		//keep same amount of leading zeroes
		char zeroBase58 = CHARACTER_SET[0];
		while (encodedText[encodedIndex] == zeroBase58 && encodedIndex < encodedText.length) {
			encodedIndex+=1;
		}
		while (--leadingZeroCount >= 0) {
			encodedText[--encodedIndex] = zeroBase58;
		}
		
		// Return encoded string (including encoded leading zeros).
		String result = new String(encodedText, encodedIndex, encodedText.length - encodedIndex);
		return result;
	}

	private static byte getRemainder(byte[] input, int start) {
		/***
		 * simulating long division, returns the remainder
		 */
		int remainder = 0;
		for (int i = start; i < input.length; i++) {
			int digit = (int) input[i] & 0xFF;
			int temp = remainder * OLD_BASE + digit;
			input[i] = (byte) (temp / NEW_BASE);
			remainder = temp % NEW_BASE;
		}
		return (byte) remainder;
	}

	public byte[] getPlaintext() {
		return plaintext;
	}

	public void setPlaintext(byte[] plaintext) {
		this.plaintext = plaintext;
	}

	public String getEncoded() {
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}

}
