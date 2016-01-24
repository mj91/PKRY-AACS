package main.java.jdzj.pkryaacs.model.MKB;

import main.java.jdzj.pkryaacs.utils.ByteUtils;

public class VMKRecord extends Record {
	byte[] verificationData=new byte[16];
	public VMKRecord(byte[] _verificationData){
		System.arraycopy(_verificationData, 0, this.verificationData, 0, 16);
	}
	public VMKRecord(){}

	public void setVerificationData(byte[] _verificationData){
		System.arraycopy(_verificationData, 0, this.verificationData, 0, 16);
	}
	
	public byte[] getVerificationData(){
		return this.verificationData;
	}

	public byte[] toByteArray(){
		byte[] bytes=new byte[20];
		bytes[0]=ByteUtils.h2b(0x81);
		bytes[1]=ByteUtils.h2b(0x00);
		bytes[2]=ByteUtils.h2b(0x00);
		bytes[3]=ByteUtils.h2b(0x14);
		System.arraycopy(verificationData, 0, bytes, 4, 16);
		return bytes;
	}
	public int byteLength(){
		return 20;
	}
	public int load(byte[] mkb, int i){
		System.arraycopy(mkb, i+4, verificationData, 0, 16);
		System.out.println("load VMK bytes=20");
		return i+20;
	}
	
}
