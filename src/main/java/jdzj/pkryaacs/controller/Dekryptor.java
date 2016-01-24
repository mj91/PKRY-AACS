package main.java.jdzj.pkryaacs.controller;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.DatatypeConverter;

import main.java.jdzj.pkryaacs.model.*;
import main.java.jdzj.pkryaacs.model.MKB.*;
import main.java.jdzj.pkryaacs.utils.*;

public class Dekryptor {
	private Device device;
	private MKB mkb;
	private boolean mkbLoaded;
	private byte[] mediaKey;
	private boolean mediaKeyFound=false;
	
	public Dekryptor(Device _device){
		this.device=_device;
	}
	public Dekryptor(){
		mkb=new MKB();
	}
	public void loadMKB(String fileName) throws Exception{
		mkb.loadFromFile(fileName);
		this.mkbLoaded=true;
	}
	public boolean isMKBLoaded(){
		return this.mkbLoaded;
	}
	public void loadDevice(String device){
		this.device=new Device(device);
	}
	public void findMediaKey() throws Exception{
		System.out.println("\n---\nTEST FOR DEVICE: "+this.device.getNumber());
		System.out.println("check device compatibility...");
		int d=this.device.getNumber();
		ESDRecord esd=mkb.getESD();
		int i,u=0,uv=0,v=0;
		for(i=0;i<esd.getUVLength();i++){
			System.out.print("subset "+i+": ");
			u =esd.getUV(i)[0];
			uv=esd.getUV(i)[1];
			v = 0xFFFFFFFF;
			if(uv!=0) while ((uv & ~v) == 0) v <<= 1;
			if(((d&u)==(uv&u))&&((d&v)!=(uv&v))){ System.out.println("passed"); break; }
			else{
				System.out.println("failed");
				if(i==esd.getUVLength()-1){
					System.out.println("there is no matching subset-difference tree for this device. it's probably revoked.");
					return;
				}
			}
		}

		System.out.println("check devicekeys compatibility...");
		int uvp,up,vp;
		DeviceKey[] DeviceKeys=device.getDeviceKeys();

		DeviceKey D;
		int di=0;
		do{
			if(di>0) System.out.println("failed!");
			D=DeviceKeys[di];
			uvp=D.getPath();
			up=D.getU();

			System.out.print("devicekey "+di+": ");
			vp=0xFFFFFFFF;
			if(uvp!=0) if(uvp!=0) while ((uvp & ~vp) == 0) vp <<= 1;
			di++;
		}while(!( (u==up)&&(uv&vp)==(uvp&vp) ));
		System.out.println("passed!");
		

		
		/*step1*/

		byte[] k=new byte[16];
		System.arraycopy(D.getKey(), 0, k, 0, 16);
		int m=vp;
		

		while(m!=v){
			/*step3*/
			if(uv!=(uv|((~m+1)>>1))){
				k=AES.G3(k)[0];

			}
			else{
				k=AES.G3(k)[2];
			}
	
			/*step4*/
			m>>=1;
		}
		
		
		k=AES.G3(k)[1];
		MKDRecord mkd=mkb.getMKD();
		k=AES.Decrypt(k,mkd.getMK(i));
		byte[] mkXOR=DatatypeConverter.parseHexBinary(StringUtils.leftPad(Integer.toHexString(uv),"0",32));
		k=ByteUtils.xor(k,mkXOR);
		
		System.out.print ("Media Key: ");
		System.out.println(ByteUtils.toHexString(k));
		
		System.out.println(ByteUtils.toHexString(mkb.getVMK().getVerificationData()));
		System.out.println("Verify (check if:");
		System.out.println("0123456789abcdefXXXXXXXXXXXXXXXX matches:");
		byte[] vb=AES.Decrypt(k, mkb.getVMK().getVerificationData());
		System.out.println(ByteUtils.toHexString(vb)+")");
		int vi=0;
		byte[] vpattern=DatatypeConverter.parseHexBinary("0123456789abcdef0000000000000000");
		boolean verified=true;
		for(byte b : vb){
			if(b!=vpattern[vi]){
				System.out.println("Verification failed at byte "+vi+" ("+Character.digit(b,16)+"!="+Character.digit(vpattern[vi],16)+")");
				verified=false; break; }
			if(vi>=16){ break; }
			vi++;
		}
		if(verified){
			this.mediaKey=k;
			this.mediaKeyFound=true;
			System.out.println("Delivered Media Key has been positively verified!");
		}
		else System.out.println("Delivered Media Key has not passed verification!");
		
	}
	public boolean isMediaKeyFound(){
		return this.mediaKeyFound;
	}
	public boolean decodeFile(String fileName) throws Exception{
		if(!mediaKeyFound) return false;

		Path path=Paths.get(fileName);
		byte[] content=Files.readAllBytes(path);
		byte[] result=AES.DecryptWithPadding(this.mediaKey, content);
		Files.write(path,result);
		return true;
	}
}
