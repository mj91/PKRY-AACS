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
import main.java.jdzj.pkryaacs.utils.StringUtils;

public class DekryptorTab extends JPanel {

	public DekryptorTab(){
		SpringLayout layout=new SpringLayout();
		setLayout(layout);
		
		Dekryptor dekryptor=new Dekryptor();
		List<String> devices=new ArrayList<String>();
		Container mw=this.getParent();
		boolean mkbLoaded=false;
		boolean devicesLoaded=false;


		JButton loadMKBButton=new JButton("Wczytaj MKB z pliku");
		JButton loadDevicesButton=new JButton("Wczytaj urz¹dzenia z pliku");
		JButton findMKButton=new JButton("ZnajdŸ MediaKey");
		JButton decodeFileButton=new JButton("Odszyfruj plik");
		
		ActionListener loadMKBButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc=new JFileChooser();
				if(fc.showOpenDialog(mw)!=JFileChooser.CANCEL_OPTION){
					File f=fc.getSelectedFile();
					try{
						dekryptor.loadMKB(f.getAbsolutePath());
						if(devices.size()>0) findMKButton.setEnabled(true);
					}catch(Exception e){
						System.out.println("Error loading MKB file ("+f.getAbsolutePath()+")");
					}
				}
			}
		};
		

		loadMKBButton.addActionListener(loadMKBButtonActionListener);
		add(loadMKBButton);
		layout.putConstraint(SpringLayout.WEST, loadMKBButton,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, loadMKBButton,20,SpringLayout.NORTH, this);
		

		JLabel chooseDeviceLabel=new JLabel("Device:");
		JComboBox chooseDeviceSelect=new JComboBox();
		chooseDeviceSelect.addItem(new String("Najpierw wczytaj listê z pliku"));
		add(chooseDeviceLabel);
		add(chooseDeviceSelect);
		layout.putConstraint(SpringLayout.WEST, chooseDeviceLabel,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, chooseDeviceSelect,140,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, chooseDeviceLabel,60,SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, chooseDeviceSelect,60,SpringLayout.NORTH, this);

		

		
//		JLabel deviceLabel=new JLabel("Device:");
//		JTextField deviceField=new JTextField("",30);
//		add(deviceLabel);
//		add(deviceField);
//		layout.putConstraint(SpringLayout.WEST, deviceLabel,40,SpringLayout.WEST, this);
//		layout.putConstraint(SpringLayout.WEST, deviceField,140,SpringLayout.WEST, this);
//		layout.putConstraint(SpringLayout.NORTH, deviceLabel,60,SpringLayout.NORTH, this);
//		layout.putConstraint(SpringLayout.NORTH, deviceField,60,SpringLayout.NORTH, this);

		
		ActionListener loadDevicesButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc=new JFileChooser();
				if(fc.showOpenDialog(mw)!=JFileChooser.CANCEL_OPTION){
					File f=fc.getSelectedFile();
					Path path=Paths.get(f.getAbsolutePath());

					try{
						devices.addAll(Files.readAllLines(path));
						if(devices.size()>0){
							chooseDeviceSelect.removeAllItems();
							for(String d : devices){
								chooseDeviceSelect.addItem(d.substring(0,8));
							}
							if(dekryptor.isMKBLoaded()) findMKButton.setEnabled(true);
						}
					}catch(Exception e){
						System.out.println("Error loading device(s) from file ("+f.getAbsolutePath()+")");
					}
				}
			}
		};
		
		loadDevicesButton.addActionListener(loadDevicesButtonActionListener);
		add(loadDevicesButton);
		layout.putConstraint(SpringLayout.WEST, loadDevicesButton,200,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, loadDevicesButton,20,SpringLayout.NORTH, this);

		ActionListener findMKButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int i=chooseDeviceSelect.getSelectedIndex();
				dekryptor.loadDevice(devices.get(i));
				try{
					dekryptor.findMediaKey();
					if(dekryptor.isMediaKeyFound()) decodeFileButton.setEnabled(true);
				}catch(Exception e){
					System.out.println("Error while looking for Media Key.");
				}
			}
		};

		findMKButton.addActionListener(findMKButtonActionListener);
		findMKButton.setEnabled(false);
		add(findMKButton);
		layout.putConstraint(SpringLayout.WEST, findMKButton,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, findMKButton,100,SpringLayout.NORTH, this);


		loadDevicesButton.addActionListener(loadDevicesButtonActionListener);
		add(loadDevicesButton);
		layout.putConstraint(SpringLayout.WEST, loadDevicesButton,200,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, loadDevicesButton,20,SpringLayout.NORTH, this);

		ActionListener decodeFileButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc=new JFileChooser();
				if(fc.showOpenDialog(mw)!=JFileChooser.CANCEL_OPTION){
					File f=fc.getSelectedFile();
					try{
						dekryptor.decodeFile(f.getAbsolutePath());
					}catch(Exception e){
						System.out.println("Error encoding file ("+f.getAbsolutePath()+")");
					}
				}
			}
		};
		
		decodeFileButton.addActionListener(decodeFileButtonActionListener);
		decodeFileButton.setEnabled(false);
		add(decodeFileButton);
		layout.putConstraint(SpringLayout.WEST, decodeFileButton,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, decodeFileButton,140,SpringLayout.NORTH, this);
		
	}
	
}
