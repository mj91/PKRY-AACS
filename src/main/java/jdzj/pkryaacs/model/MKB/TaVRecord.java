package main.java.jdzj.pkryaacs.model.MKB;
import main.java.jdzj.pkryaacs.utils.*;

public class TaVRecord extends Record {
	
	public TaVRecord(){
		
	}

	public byte[] toByteArray(){
		byte[] bytes=new byte[12];
		bytes[0]=ByteUtils.h2b(0x10);
		bytes[1]=ByteUtils.h2b(0x00);
		bytes[2]=ByteUtils.h2b(0x00);
		bytes[3]=ByteUtils.h2b(0x0C);
		bytes[4]=ByteUtils.h2b(0x00);
		bytes[5]=ByteUtils.h2b(0x03);
		bytes[6]=ByteUtils.h2b(0x10);
		bytes[7]=ByteUtils.h2b(0x03);
		bytes[8]=ByteUtils.h2b(0x00);
		bytes[9]=ByteUtils.h2b(0x00);
		bytes[10]=ByteUtils.h2b(0x00);
		bytes[11]=ByteUtils.h2b(0x00);
		return bytes;
	}
	public int byteLength(){
		return 12;
	}
	public int load(byte[] mkb, int i){
		System.out.println("load TaV bytes=12");
		return 12;
	}
}
