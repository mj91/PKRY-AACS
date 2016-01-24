package main.java.jdzj.pkryaacs.model.MKB;
import java.util.*;
import java.nio.*;

import main.java.jdzj.pkryaacs.utils.ByteUtils;

public class MKDRecord extends Record {
	private List<byte[]> mkdList;
	public MKDRecord(){
		mkdList=new ArrayList<byte[]>();
	}
	public void push(byte[] k){
		this.mkdList.add(k);
	}
	public byte[] toByteArray(){
		int n=mkdList.size();
		byte[] bytes=new byte[n*16+4];
		bytes[0]=ByteUtils.h2b(0x05);
		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt(n*16+4);
		byte[] temp=b.array();
		System.arraycopy(temp, 1, bytes, 1, 3);
		for(n=0;n<mkdList.size();n++){
			System.arraycopy(mkdList.get(n), 0, bytes, 4+n*16, 16);
		}
		return bytes;
	}
	public int byteLength(){
		return mkdList.size()*16+4;
	}
	public int load(byte[] mkb, int i){
		mkdList.clear();
		byte[] b=new byte[4]; b[0]=0x0;
		System.arraycopy(mkb, i+1, b, 1, 3);
		ByteBuffer bb = ByteBuffer.wrap(b);
		int n=bb.getInt()/16;
		System.out.println("load MKD bytes="+(4+n*16));
		for(int j=0; j<n; j++){
			byte[] k=new byte[16];
			System.arraycopy(mkb, i+4+16*j, k, 0, 16);
			this.push(k);
		}
		return i+4+n*16;
	}
	public byte[] getMK(int i){
		return mkdList.get(i);
	}
}
