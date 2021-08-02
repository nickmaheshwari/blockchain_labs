package blockchain_lab3;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Problem3 {
	
	public static void main(String[] args)
    {
		shaHash();
    }
	private static void shaHash() {
        long t = System.currentTimeMillis(); //unix epoch time
    
		String time = String.valueOf(t); //current time to string
        String d = "1.00000";	
        //System.out.println(time.getBytes().hashCode());
        //create sets of data
		Set<String> set1 = new HashSet<String>();
		set1.add("0000000036dc2ce23cdd934eff4bae120155de8b8712de8489c8870b06e334ff");
		set1.add("c0ba5ba1c5ba02fb02c4ea93ec16cae7ea1994b5b3f9ef0e293b841081eb3003");
		set1.add(time);
		set1.add(d);
		set1.add("5251252");
		
		Set<String> set2 = new HashSet<String>();
		set2.add("00000000d84724559f1691d916b2ed63a44884d495d155197647ce7667116b16");
		set2.add("69a14e6b050d10d6621faee3dac6682809feb0ffa76320b33c5c09f1059f06c7");
		set2.add(time);
		set2.add(d);
		set2.add("12486777");
		 
		
		//serialize data
		byte[] set1Data = serialize(set1);
		byte[] set2Data = serialize(set2);
		//System.out.println(set1Data + "," + set2Data);
		
		//get shaLeft and shaRight
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256"); //hash byte stream
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] shaLeft = digest.digest(digest.digest(set1Data)); //twice hash set1Data
		byte[] shaRight = digest.digest(digest.digest(set2Data)); //twice hash set2Data
		System.out.println("shaLeft: "+shaLeft.hashCode() + ", shaRight:" + shaRight.hashCode());
		
		//combine shaLeft and shaRight into one byte stream "root", run  root through sha256
		byte[] temp = new byte[shaLeft.length + shaRight.length ];
		ByteBuffer buff = ByteBuffer.wrap(temp);
		buff.put(shaLeft);
		buff.put(shaRight);
		byte[] root = digest.digest(buff.array());
		
		System.out.println("Root: "+ root.hashCode());
		
		
		/*
		 * String sha256hex = Hashing.sha256() .hashString(time, StandardCharsets.UTF_8)
		 * .toString();
		 */
	}
	
	public static byte[] serialize(Set<String> set) {
		// write to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream( baos );
			oos.writeObject( set );
	        oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return baos.toByteArray(); 
		
		
	}
	
}
