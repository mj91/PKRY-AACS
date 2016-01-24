package main.java.jdzj.pkryaacs.model;
import java.nio.ByteBuffer;

import main.java.jdzj.pkryaacs.utils.ByteUtils;
import main.java.jdzj.pkryaacs.utils.StringUtils;

public class DeviceKey {
	private int path;
	private int u;
	private byte[] key;
	public DeviceKey(int _path, int _u, byte[] _key){
		this.path=_path;
		this.u=_u;
		this.key=new byte[_key.length];
		System.arraycopy(_key, 0, this.key, 0, _key.length);
	}
	public DeviceKey(String _deviceKey){
		byte[] deviceKey=_deviceKey.getBytes();
		
		byte[] b=new byte[4];
		System.arraycopy(deviceKey, 0, b, 0, 4);
		ByteBuffer bb = ByteBuffer.wrap(b);
		this.path=bb.getInt();
		bb.clear();
		System.arraycopy(deviceKey, 4, b, 0, 4);
		bb = ByteBuffer.wrap(b);
		this.u=bb.getInt();
		this.key=new byte[16];
		System.arraycopy(deviceKey, 8, this.key, 0, 16);
	}
	public String toString(){
		return StringUtils.leftPad(Integer.toHexString(this.path),"0",8)
				+StringUtils.leftPad(Integer.toHexString(this.u),"0",8)
				+ByteUtils.toHexString(key);
	}
	public String toPrettyString(){
		return "path:"+StringUtils.leftPad(Integer.toHexString(this.path),"0",8)+", "
				+"u:"+StringUtils.leftPad(Integer.toHexString(this.u),"0",8)+", "
				+"k:"+ByteUtils.toHexString(key);
	}
	public byte[] getKey(){
		return this.key;
	}
	public int getU(){
		return this.u;
	}
	public int getPath(){
		return this.path;
	}
}
