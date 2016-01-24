package main.java.jdzj.pkryaacs.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AES {

	public static byte[][] G3(byte[] k) throws Exception {
		byte[] s0=DatatypeConverter.parseHexBinary("7B103C5DCB08C4E51A27B01799053BD9");
		byte[][] result=new byte[3][16];
		
		Cipher AES128D=Cipher.getInstance("AES/ECB/NoPadding"); // PKCS5Padding
		Key aesKey = new SecretKeySpec(k, "AES");
		AES128D.init(Cipher.ENCRYPT_MODE, aesKey);
		result[0]=ByteUtils.xor(AES128D.doFinal(s0),s0);
		s0=ByteUtils.increment(s0);
		result[1]=ByteUtils.xor(AES128D.doFinal(s0),s0);
		s0=ByteUtils.increment(s0);
		result[2]=ByteUtils.xor(AES128D.doFinal(s0),s0);
		
		return result;
	}

	public static byte[] Encrypt(byte[] k,byte[] src) throws Exception {
		Cipher AES128D=Cipher.getInstance("AES/ECB/NoPadding"); // PKCS5Padding
		Key aesKey = new SecretKeySpec(k, "AES");
		AES128D.init(Cipher.ENCRYPT_MODE, aesKey);
		return AES128D.doFinal(src);
	}
	public static byte[] Decrypt(byte[] k,byte[] src) throws Exception {
		Cipher AES128D=Cipher.getInstance("AES/ECB/NoPadding"); // PKCS5Padding
		Key aesKey = new SecretKeySpec(k, "AES");
		AES128D.init(Cipher.DECRYPT_MODE, aesKey);
		return AES128D.doFinal(src);
	}
	public static byte[] EncryptWithPadding(byte[] k,byte[] src) throws Exception {
		Cipher AES128D=Cipher.getInstance("AES/ECB/PKCS5Padding"); // PKCS5Padding
		Key aesKey = new SecretKeySpec(k, "AES");
		AES128D.init(Cipher.ENCRYPT_MODE, aesKey);
		return AES128D.doFinal(src);
	}
	public static byte[] DecryptWithPadding(byte[] k,byte[] src) throws Exception {
		Cipher AES128D=Cipher.getInstance("AES/ECB/PKCS5Padding"); // PKCS5Padding
		Key aesKey = new SecretKeySpec(k, "AES");
		AES128D.init(Cipher.DECRYPT_MODE, aesKey);
		return AES128D.doFinal(src);
	}
}
