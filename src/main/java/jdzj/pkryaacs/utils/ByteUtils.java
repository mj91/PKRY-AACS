package main.java.jdzj.pkryaacs.utils;
import javax.xml.bind.DatatypeConverter;

public class ByteUtils {
	public static byte[] increment(byte[] a){

		int size=a.length;
		for(int i=size-1; i>=0; i--){
			byte _a=a[i];
			a[i]++;
			if((a[i] & 0xFF)>(_a & 0xFF)){
				break;
			}
		}
		return a;
	}
	public static byte[] xor(byte[] a,byte[] b){
		int size=a.length;
		if(size!=b.length) return null;
		
		byte[] c=new byte[size];
		
		int i = 0;
		for (byte x : a)
		    c[i] = (byte) (x ^ b[i++]);
		
		return c;
	}
	public static String toHexString(byte[] a){
		int size=a.length;
		String result="";
		int i = 0;
		for (byte x : a){
			String hex=Integer.toHexString(x & 0xFF);
			if(hex.length()%2==1) hex="0"+hex;
		    result+=hex;
		}
		
		return result;
	}
	public static byte hexToByte(int a){
		String hex=Integer.toHexString(a);
		if(hex.length()%2==1) hex="0"+hex;
		return DatatypeConverter.parseHexBinary(hex)[0];
	}
	public static byte h2b(int a){
		return hexToByte(a);
	}
}
