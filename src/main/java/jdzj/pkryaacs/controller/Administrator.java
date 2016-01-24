package main.java.jdzj.pkryaacs.controller;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import static java.lang.Math.pow;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.java.jdzj.pkryaacs.utils.*;
import main.java.jdzj.pkryaacs.model.*;
import main.java.jdzj.pkryaacs.model.MKB.*;

public class Administrator {
	private static List<Device> devices=new ArrayList<Device>();
	private static MKB mkb=new MKB();
	private static ESDRecord esd=new ESDRecord();
	private static MKDRecord mkd=new MKDRecord();
	private static VMKRecord vmk=new VMKRecord();
	
	public static void generateSubset(String mediaKey, String rootDeviceKey, int uv, int u, int v) throws Exception {

		byte[] k= DatatypeConverter.parseHexBinary(rootDeviceKey);
		byte[] mk= DatatypeConverter.parseHexBinary(mediaKey);
		
		int hu=0; int x=~u; while(x>0){ x>>=1; hu++; }
		int hv=0; 	  x=~v; while(x>0){ x>>=1; hv++; }
		int ld=0;

		DeviceKey[] deviceKeys=new DeviceKey[hu];
		byte[][] bDeviceKeys=new byte[hu][16];
		int ldk=0;
		
		int m=u,p=uv&u;
		int _m=m<<1,_p=uv&(u<<1);
		

		esd.push(u,uv);
		
		boolean condition=true;
		while(condition){
			if(m==0xFFFFFFFF && p==(uv|~u)) condition=false;
			
			if(_m<m && (p&v)==(uv&v) && m==v){
				// zejœcie w wykluczone poddrzewo v -> w górê
				byte[] mkXOR=DatatypeConverter.parseHexBinary(StringUtils.leftPad(Integer.toHexString(uv),"0",32));
				byte[] mkData=AES.Encrypt(AES.G3(k)[1],ByteUtils.xor(mk,mkXOR));
				mkd.push(mkData);
				ldk--;
				k=bDeviceKeys[ldk];
				_m=m; _p=p;
				
				if(p!=(p|((~m+1)>>1))){
					m<<=1;
					p=_p-(_p&~m);
				}else{
					m>>=1;
					
				}
				
				continue;
			}else if(m==0xFFFFFFFF){
				// liœæ -> w górê
				Device a=new Device(p,deviceKeys);
				devices.add(a);
				ld++; ldk--;
				k=bDeviceKeys[ldk];
				_m=m; _p=p;
				m-=1;
				if(p%2!=0) p=_p-(_p&~m);
			}else{
				if(_m<m){
					// zejœcie z góry -> w lewo
					deviceKeys[ldk]=new DeviceKey((p^((~m+1)>>1))|((~m+1)>>2),u,AES.G3(k)[2]);
					bDeviceKeys[ldk]=k;
					k=AES.G3(k)[0];
					ldk++;
					_m=m; _p=p;
					m>>=1;
					p=(_p&_m);
				}else if(_p==p){
					// podejœcie w górê od lewej -> w prawo
					deviceKeys[ldk]=new DeviceKey(_p|((~m+1)>>2),u,AES.G3(k)[0]);
					bDeviceKeys[ldk]=k;
					k=AES.G3(k)[2];
					ldk++;
					_m=m; _p=p;
					m>>=1;
					p=(_p&_m)+(_m^m);
				}else{
					// podejœcie w górê od prawej -> w górê
					ldk--;
					k=bDeviceKeys[ldk];
					_m=m; _p=p;
					m<<=1;
					p=_p-(_p&~m);
				}
			}
		}
		mkb.setRecord(mkd);
		mkb.setRecord(esd);
		vmk.setVerificationData(AES.Encrypt(DatatypeConverter.parseHexBinary(mediaKey), DatatypeConverter.parseHexBinary("0123456789ABCDEF0000000000000000")));
		mkb.setRecord(vmk);
		System.out.println("Subset generated, MKB hex view:");
		printMKB();
	}
	public static void exportMKB(String fileName) throws IOException{
		mkb.saveToFile(fileName);
	}
	public static void loadMKB(String fileName) throws Exception{
		clearMKB();
		mkb.loadFromFile(fileName);
	}
	public static void clearMKB(){
		mkb=new MKB();
		devices.clear();
	}
	public static String mkbStats(){
		String result="MKB Stats:\n";
		result+="-----\n";
		esd=mkb.getESD();
		result+="Subset(s) in current MKB: "+esd.getUVLength()+"\n";
		result+="Generated devices: "+devices.size()+"\n";
		return result;
	}
	public static void printMKB() throws Exception{
		byte[] mkbBytes=mkb.toByteArray();
		for(int i=0;i<mkbBytes.length;i+=4){
			byte[] line=new byte[4];

			System.arraycopy(mkbBytes, i, line, 0, ((mkbBytes.length-i)>=4)?4:(mkbBytes.length-i));
			System.out.println(ByteUtils.toHexString(line));
		}
		
	}
	public static void exportDevices(String fileName) throws IOException{
		String result="";
		for(Device d : devices) result+=d.toString()+"\n";
		byte[] content=result.getBytes();
		Path path=Paths.get(fileName);
		Files.write(path,content);
	}
	
}
