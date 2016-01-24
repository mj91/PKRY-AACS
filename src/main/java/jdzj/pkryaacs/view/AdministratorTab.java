package main.java.jdzj.pkryaacs.view;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.ByteBuffer;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;

import main.java.jdzj.pkryaacs.controller.*;
import main.java.jdzj.pkryaacs.model.MKB.MKB;
import main.java.jdzj.pkryaacs.utils.StringUtils;

public class AdministratorTab extends JPanel {

	public AdministratorTab(){
		SpringLayout layout=new SpringLayout();
		setLayout(layout);
		
		Container mw=this.getParent();

		JTextArea MKBStats=new JTextArea(Administrator.mkbStats());
		MKBStats.setPreferredSize(new Dimension(160,100));
		add(MKBStats);
		layout.putConstraint(SpringLayout.WEST, MKBStats,240,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, MKBStats,140,SpringLayout.NORTH, this);

		ActionListener loadMKBButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc=new JFileChooser();
				if(fc.showOpenDialog(mw)!=JFileChooser.CANCEL_OPTION){
					File f=fc.getSelectedFile();
					try{
						Administrator.loadMKB(f.getAbsolutePath());
						MKBStats.setText(Administrator.mkbStats());
					}catch(Exception e){
						System.out.println("Error loading MKB file ("+f.getAbsolutePath()+")");
					}
				}
			}
		};
		ActionListener saveMKBButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc=new JFileChooser();
				if(fc.showSaveDialog(mw)!=JFileChooser.CANCEL_OPTION){
					File f=fc.getSelectedFile();
					try{
						Administrator.exportMKB(f.getAbsolutePath());
					}catch(Exception e){
						System.out.println("Error saving MKB file ("+f.getAbsolutePath()+")");
					}
				}
			}
		};
		ActionListener clearMKBButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Administrator.clearMKB();
				MKBStats.setText(Administrator.mkbStats());
			}
		};

		JButton loadMKBButton=new JButton("Wczytaj MKB z pliku");
		loadMKBButton.addActionListener(loadMKBButtonActionListener);
		add(loadMKBButton);
		layout.putConstraint(SpringLayout.WEST, loadMKBButton,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, loadMKBButton,20,SpringLayout.NORTH, this);
		
		JButton clearMKBButton=new JButton("Resetuj MKB");
		clearMKBButton.addActionListener(clearMKBButtonActionListener);
		add(clearMKBButton);
		layout.putConstraint(SpringLayout.WEST, clearMKBButton,200,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, clearMKBButton,20,SpringLayout.NORTH, this);

		JLabel mkLabel=new JLabel("MediaKey:");
		JTextField mkField=new JTextField("77777777777777777777777777777777",24);
		add(mkLabel);
		add(mkField);
		layout.putConstraint(SpringLayout.WEST, mkLabel,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, mkField,140,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mkLabel,60,SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, mkField,60,SpringLayout.NORTH, this);

		JLabel rkLabel=new JLabel("RootKey:");
		JTextField rkField=new JTextField("7B103C5DCB08C4E51A27B01799053BD9",24);
		add(rkLabel);
		add(rkField);
		layout.putConstraint(SpringLayout.WEST, rkLabel,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, rkField,140,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, rkLabel,100,SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, rkField,100,SpringLayout.NORTH, this);

		JLabel uvLabel=new JLabel("UV:");
		JTextField uvField=new JTextField("00000005",6);
		add(uvLabel);
		add(uvField);
		layout.putConstraint(SpringLayout.WEST, uvLabel,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, uvField,140,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, uvLabel,140,SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, uvField,140,SpringLayout.NORTH, this);

		JLabel uLabel=new JLabel("U mask:");
		JTextField uField=new JTextField("FFFFFFF8",6);
		add(uLabel);
		add(uField);
		layout.putConstraint(SpringLayout.WEST, uLabel,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, uField,140,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, uLabel,180,SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, uField,180,SpringLayout.NORTH, this);

		JLabel vLabel=new JLabel("V mask:");
		JTextField vField=new JTextField("FFFFFFFE",6);
		add(vLabel);
		add(vField);
		layout.putConstraint(SpringLayout.WEST, vLabel,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, vField,140,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, vLabel,220,SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, vField,220,SpringLayout.NORTH, this);

		ActionListener generateSubsetButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				byte[] uvb=StringUtils.hexStringToByteArray(uvField.getText());
				ByteBuffer wrapped = ByteBuffer.wrap(uvb);
				int uv = wrapped.getInt();
				wrapped.clear();
				byte[] ub=StringUtils.hexStringToByteArray(uField.getText());
				wrapped = ByteBuffer.wrap(ub);
				int u = wrapped.getInt();
				wrapped.clear();
				byte[] vb=StringUtils.hexStringToByteArray(vField.getText());
				wrapped = ByteBuffer.wrap(vb);
				int v = wrapped.getInt();
				String rootKey=rkField.getText();
				String mediaKey=mkField.getText();
				try{
					Administrator.generateSubset(mediaKey,rootKey,uv,u,v);
					MKBStats.setText(Administrator.mkbStats());
				}catch(Exception e){
					System.out.println("Error while generating subset.");
				}
			}
		};
		

		ActionListener saveDevicesButtonActionListener=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc=new JFileChooser();
				if(fc.showSaveDialog(mw)!=JFileChooser.CANCEL_OPTION){
					File f=fc.getSelectedFile();
					try{
						Administrator.exportDevices(f.getAbsolutePath());
					}catch(Exception e){
						System.out.println("Error saving devices file ("+f.getAbsolutePath()+")");
					}
				}
			}
		};
		
		JButton generateMKBButton=new JButton("Generuj poddrzewo");
		generateMKBButton.addActionListener(generateSubsetButtonActionListener);
		add(generateMKBButton);
		layout.putConstraint(SpringLayout.WEST, generateMKBButton,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, generateMKBButton,260,SpringLayout.NORTH, this);
		
		JButton saveMKBButton=new JButton("Zapisz MKB do pliku");
		saveMKBButton.addActionListener(saveMKBButtonActionListener);
		add(saveMKBButton);
		layout.putConstraint(SpringLayout.WEST, saveMKBButton,40,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, saveMKBButton,300,SpringLayout.NORTH, this);
		
		JButton saveDevicesButton=new JButton("Zapisz urz¹dzenia do pliku");
		saveDevicesButton.addActionListener(saveDevicesButtonActionListener);
		add(saveDevicesButton);
		layout.putConstraint(SpringLayout.WEST, saveDevicesButton,200,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, saveDevicesButton,300,SpringLayout.NORTH, this);
		
		
		
	}
	
}
