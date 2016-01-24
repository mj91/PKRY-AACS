package main.java.jdzj.pkryaacs.controller;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.DatatypeConverter;

import main.java.jdzj.pkryaacs.model.*;
import main.java.jdzj.pkryaacs.model.MKB.*;
import main.java.jdzj.pkryaacs.utils.*;

public class Enkryptor {
	private boolean mkbLoaded;
	
	public Enkryptor(){
	}
	public void encodeFile(String fileName, byte[] k) throws Exception{
		Path path=Paths.get(fileName);
		byte[] content=Files.readAllBytes(path);
		byte[] result=AES.EncryptWithPadding(k, content);
		Files.write(path,result);
	}
}
