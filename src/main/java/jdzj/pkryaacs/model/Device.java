package main.java.jdzj.pkryaacs.model;
import java.nio.ByteBuffer;

import main.java.jdzj.pkryaacs.utils.*;

public class Device {
	private int deviceNumber;
	private DeviceKey[] deviceKeySet;
	public Device(int _deviceNumber, DeviceKey[] _deviceKeySet){
		this.deviceNumber=_deviceNumber;
		this.deviceKeySet=new DeviceKey[_deviceKeySet.length];
		System.arraycopy(_deviceKeySet, 0, deviceKeySet, 0, _deviceKeySet.length);
	}
	public Device(String _device){
		byte[] device=StringUtils.hexStringToByteArray(_device);
		int deviceKeySetLength=0;

		byte[] b=new byte[4];
		System.arraycopy(device, 0, b, 0, 4);
		ByteBuffer bb = ByteBuffer.wrap(b);
		this.deviceNumber=bb.getInt();
		bb.clear();
		System.arraycopy(device, 4, b, 0, 4);
		bb = ByteBuffer.wrap(b);
		deviceKeySetLength=bb.getInt();
		bb.clear();
		this.deviceKeySet=new DeviceKey[deviceKeySetLength];
		for(int i=0; i<deviceKeySetLength; i++){
			int dp,du;
			byte[] dk=new byte[16];
			System.arraycopy(device, 8+i*24, b, 0, 4);
			bb = ByteBuffer.wrap(b);
			dp=bb.getInt();
			bb.clear();
			System.arraycopy(device, 12+i*24, b, 0, 4);
			bb = ByteBuffer.wrap(b);
			du=bb.getInt();
			bb.clear();
			System.arraycopy(device, 16+i*24, dk, 0, 16);
			this.deviceKeySet[i]=new DeviceKey(dp,du,dk);
		}
	}
	public String toString(){
		String result=StringUtils.leftPad(Integer.toHexString(this.deviceNumber),"0",8);
		result+=StringUtils.leftPad(Integer.toHexString(deviceKeySet.length),"0",8);
		for(int i=0;i<deviceKeySet.length;i++){
			result+=deviceKeySet[i].toString();
		}
		return result;
	}
	public String toPrettyString(){
		String result="d:"+StringUtils.leftPad(Integer.toHexString(this.deviceNumber),"0",8)+", ";
		result+="n:"+StringUtils.leftPad(Integer.toHexString(deviceKeySet.length),"0",8)+"\ndk's:\n";
		for(int i=0;i<deviceKeySet.length;i++){
			result+="   "+deviceKeySet[i].toPrettyString()+"\n";
		}
		return result;
	}
	public int getNumber(){
		return this.deviceNumber;
	}
	public DeviceKey[] getDeviceKeys(){
		return this.deviceKeySet;
	}
}
