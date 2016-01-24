package main.java.jdzj.pkryaacs.view;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.xml.bind.DatatypeConverter;

import main.java.jdzj.pkryaacs.controller.*;
import main.java.jdzj.pkryaacs.utils.AES;
import main.java.jdzj.pkryaacs.utils.ByteUtils;
import main.java.jdzj.pkryaacs.utils.StringUtils;

public class EnkryptorTab extends JPanel {

	public EnkryptorTab(){
		SpringLayout layout=new SpringLayout();
		setLayout(layout);
		Container mw=this.getParent();
		
		Enkryptor enkryptor=new Enkryptor();

		JLabel mkLabel=new JLabel("MediaKey:");
		JTextField mkField=new JTextField("77777777777777777777777777777777",24);
		add(mkLabel);
		add(mkField);
		layout.putConstraint(SpringLayout.WEST, mkLabel,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, mkField,140,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mkLabel,20,SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, mkField,20,SpringLayout.NORTH, this);

		JButton encodeFileButton=new JButton("Zaszyfruj plik");
		
		ActionListener encodeFileButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc=new JFileChooser();
				if(fc.showOpenDialog(mw)!=JFileChooser.CANCEL_OPTION){
					File f=fc.getSelectedFile();
					try{
						enkryptor.encodeFile(f.getAbsolutePath(), DatatypeConverter.parseHexBinary(mkField.getText()));
					}catch(Exception e){
						System.out.println("Error encoding file ("+f.getAbsolutePath()+")");
					}
				}
			}
		};

		encodeFileButton.addActionListener(encodeFileButtonActionListener);
		add(encodeFileButton);
		layout.putConstraint(SpringLayout.WEST, encodeFileButton,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, encodeFileButton,60,SpringLayout.NORTH, this);
		
	}
	
}
