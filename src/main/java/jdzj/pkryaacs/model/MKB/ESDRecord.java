package main.java.jdzj.pkryaacs.model.MKB;
import java.nio.ByteBuffer;
import java.util.*;
import java.lang.Math.*;

import main.java.jdzj.pkryaacs.utils.ByteUtils;
import main.java.jdzj.pkryaacs.utils.StringUtils;

public class ESDRecord extends Record {
	private List<Integer> uList;
	private List<Integer> uvList;
	public ESDRecord(){
		uList=new ArrayList<Integer>();
		uvList=new ArrayList<Integer>();
	}
	public void push(int u, int uv){
		this.uList.add(u);
		this.uvList.add(uv);
	}
	public byte[] toByteArray(){
		int n=uList.size();
		int m=n;
		byte[] bytes=new byte[n+m*4+4];
		bytes[0]=ByteUtils.h2b(0x04);
		n=n+m*4;

		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt(n);
		byte[] temp=b.array();
		System.arraycopy(temp, 1, bytes, 1, 3);
		int x;

		for(n=0;n<m;n++){
			x=uList.get(n);
			byte h=0; int z=~x; while(z>0){ z>>=1; h++; }
			bytes[4+n*5]=h;
			x=uvList.get(n);
			b.clear();
			b.putInt(x);
			temp=b.array();
			System.arraycopy(temp, 0, bytes, 5+n*5, 4);
		}
		return bytes;
	}
	public int byteLength(){
		return uList.size()+uvList.size()*4+4;
	}
	public int load(byte[] mkb, int i){
		uList.clear();
		uvList.clear();
		byte[] b=new byte[4]; b[0]=0x0;
		System.arraycopy(mkb, i+1, b, 1, 3);
		ByteBuffer bb = ByteBuffer.wrap(b);
		int n=bb.getInt()/5;
		System.out.println("load ESD bytes="+(4+n*5));
		for(int j=0; j<n; j++){
			int u=0xFFFFFFFE,uv;
			byte ub=mkb[i+4+j*5];
			for(byte ubi=0;ubi<ub;ubi++) u<<=1;
			u>>=1;
			byte[] uvb=new byte[4];
			System.arraycopy(mkb, i+5+j*5, uvb, 0, 4);
			bb.clear();
			bb=ByteBuffer.wrap(uvb);
			uv=bb.getInt();
			this.push(u,uv);
		}
		return i+4+n*5;
	}
	public int[] getUV(int i){
		return new int[]{uList.get(i),uvList.get(i)};
	}
	public int getUVLength(){
		return uList.size();
	}
}
