package blockchain_lab7;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;


public class BitcoinAddressGenerator {
	
	public static void main(String[] args) {
		try {
			generateBitcoinAddress();
		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}

	private static void generateBitcoinAddress() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		
		//Generate keys
		ECGenParameterSpec ec = new ECGenParameterSpec("secp256k1");
		KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
		generator.initialize(ec, new SecureRandom());
		KeyPair keypair = generator.generateKeyPair();
		PublicKey publicKey = keypair.getPublic();
		PrivateKey privateKey = keypair.getPrivate();
		
		//Step 0: private ECDSA key
		ECPrivateKey ecPrivateKey = (ECPrivateKey)privateKey;
		String privateKeyString = adjustTo64(ecPrivateKey.getS().toString(16));
		System.out.println("STEP 0 is: " + privateKeyString);
		
		//Step 1: Take the corresponding public key generated with it (33 bytes, 1 byte 0x02 (y-coord is even), and 32 bytes corresponding to X coordinate)
		ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
		ECPoint pt = ecPublicKey.getW();
		byte[] publicKeyBytes = new byte[33];
		publicKeyBytes[0] = 2; //0x02
		System.arraycopy(pt.getAffineX().toByteArray(), 0, publicKeyBytes, 1, 32);
		System.out.println("STEP 1 is: " + getStringFromHex(publicKeyBytes));
		
		//Step 2: sha-256 hash the public key
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		byte[] shaHash = sha.digest(publicKeyBytes);
		System.out.println("STEP 2 is: " +  getStringFromHex(shaHash));
		
		//Step 3: Perform RIPEMD-160 hashing on the result of SHA-256
		byte[] ripeMDHash = Ripemd160.getHash(shaHash); //Ripemd160.java is courtesy of Nayuki's opensource Bitcoin cryptography library (https://github.com/nayuki/Bitcoin-Cryptography-Library)
		System.out.println("STEP 3 is: " + getStringFromHex(ripeMDHash));
		
		//Step 4: Add version byte in front of RIPEMD-160 hash (0x00 for Main Network)
		byte[] paddedRipeMDHash = new byte[ripeMDHash.length + 1];
		paddedRipeMDHash[0] = 0;
		System.arraycopy(ripeMDHash, 0, paddedRipeMDHash, 1, ripeMDHash.length);	
		System.out.println("STEP 4 is: " + getStringFromHex(paddedRipeMDHash));

		//Step 5: Perform SHA-256 hash on the extended RIPEMD-160 result
		byte[] shaFirstHash = sha.digest(paddedRipeMDHash);
		System.out.println("STEP 5 is: " + getStringFromHex(shaFirstHash));
		
		//Step 6: Perform SHA-256 hash on the result of the previous SHA-256 hash
		byte[] shaSecondHash = sha.digest(shaFirstHash);
		System.out.println("STEP 6 is: " + getStringFromHex(shaSecondHash));
		
		//Step 7: Take the first 4 bytes of the second SHA-256 hash. This is the address checksum
		byte[] checkSumBytes = new byte[25];
		System.arraycopy(paddedRipeMDHash, 0, checkSumBytes, 0, 21);
		System.out.println("STEP 7 is: " + getStringFromHex(checkSumBytes));
		
		//Step 8: Add the 4 checksum bytes from stage 7 at the end of extended RIPEMD-160 hash from stage 4. This is the 25-byte binary Bitcoin Address.
		System.arraycopy(shaSecondHash, 0, checkSumBytes, 21, 4);
		System.out.println("STEP 8 is: " + getStringFromHex(checkSumBytes));
		
		//Step 9: Convert the result from a byte string into a base58 string using Base58Check encoding
		Base58Encoder encoder = new Base58Encoder(checkSumBytes);
		System.out.println("STEP 9 is: " +encoder.getEncoded());
	}
	
	private static final String HEX_REFERENCE = "0123456789abcdef"; //needed for hex conversion
	public static String getStringFromHex(byte[] bytes) {
		final StringBuilder result = new StringBuilder(2 * bytes.length);
		for (final byte b : bytes) {
			result.append(HEX_REFERENCE.charAt((b & 0xF0) >> 4)).append(HEX_REFERENCE.charAt((b & 0x0F)));
		}
		return result.toString();
	}

	static private String adjustTo64(String s) {
	        switch(s.length()) {
	        case 62: return "00" + s;
	        case 63: return "0" + s;
	        case 64: return s;
	        default:
	            throw new IllegalArgumentException("Invalid key: " + s);
	        }
	    }
	
	
}
