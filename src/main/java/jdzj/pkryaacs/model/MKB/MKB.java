package main.java.jdzj.pkryaacs.model.MKB;
import java.io.*;
import java.nio.file.*;

import main.java.jdzj.pkryaacs.utils.ByteUtils;

public class MKB {
	private TaVRecord TAV;
	private HRLRecord HRL;
	private DRLRecord DRL;
	private VMKRecord VMK;
	private SDIRecord SDI;
	private ESDRecord ESD;
	private MKDRecord MKD;
	private EoMKBRecord EoMKB;
	private Record[] MKBStructure;
	
	/*
	 * 
	 * Proper MKB structure (with Record Type):
	 * TaV (ex. 1) 0x10
	 * HRL (ex. 1) 0x21
	 * DRL (ex. 1) 0x20
	 * VMK (ex. 1) 0x81
	 * SDI (optional) 0x07 
	 * ESD (ex. 1) 0x04
	 * MKD (ex. 1) 0x05
	 * EoMKB  (ex. 1) 0x02
	 * 
	 */
	

	public MKB(){
		MKBStructure=new Record[8];
		MKBStructure[0]=TAV=new TaVRecord();
		MKBStructure[1]=HRL=new HRLRecord();
		MKBStructure[2]=DRL=new DRLRecord();
		MKBStructure[3]=VMK=new VMKRecord();
		MKBStructure[4]=SDI=new SDIRecord();
		MKBStructure[5]=ESD=new ESDRecord();
		MKBStructure[6]=MKD=new MKDRecord();
		MKBStructure[7]=EoMKB=new EoMKBRecord();
	}
	public MKB(byte[] _MKB){
		this();
		load(_MKB);
	}
	public MKB(String fileName) throws IOException{
		this();
		loadFromFile(fileName);
	}
	
	public boolean load(byte[] _MKB){
		int i=0;
		while(i<_MKB.length){
			if(_MKB[i]==0x10) 		i=MKBStructure[0].load(_MKB,i);
			else if(_MKB[i]==0x21) 	i=MKBStructure[1].load(_MKB,i);
			else if(_MKB[i]==0x20) 	i=MKBStructure[2].load(_MKB,i);
			else if(_MKB[i]==ByteUtils.h2b(0x81)) 	i=MKBStructure[3].load(_MKB,i);
			else if(_MKB[i]==0x07) 	i=MKBStructure[4].load(_MKB,i);
			else if(_MKB[i]==0x04) 	i=MKBStructure[5].load(_MKB,i);
			else if(_MKB[i]==0x05) 	i=MKBStructure[6].load(_MKB,i);
			else if(_MKB[i]==0x02) 	i=MKBStructure[7].load(_MKB,i);
		}
		return true;
	}
	public boolean loadFromFile(String fileName) throws IOException{
		Path path=Paths.get(fileName);
		return load(Files.readAllBytes(path));
	}
	public byte[] toByteArray(){
		int length=0;
		int[] lengths=new int[8];
		
		for(int i=0; i<8; i++){
			lengths[i]=MKBStructure[i].byteLength(); length+=lengths[i];
		}
		
		byte[] _MKB=new byte[length];
		
		int offset=0;
		for(int i=0; i<8; i++){
			System.arraycopy(MKBStructure[i].toByteArray(), 0, _MKB, offset, lengths[i]);
			offset+=lengths[i];
		}
		return _MKB;
	}
	public boolean saveToFile(String fileName) throws IOException{
		byte[] _MKB=toByteArray();
		Path path=Paths.get(fileName);
		Files.write(path,_MKB);
		return true;
	}
	public boolean setRecord(Record record) throws IOException{
		
		for(int i=0; i<MKBStructure.length; i++){
			if(record.getClass()==MKBStructure[i].getClass()){
				MKBStructure[i]=record;
				return true;
			}
		}
		return false;
	}
	public ESDRecord getESD(){
		return ESD;
	}
	public MKDRecord getMKD(){
		return MKD;
	}
	public VMKRecord getVMK(){
		return VMK;
	}
}
