package main.java.jdzj.pkryaacs.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import main.java.jdzj.pkryaacs.utils.*;
import main.java.jdzj.pkryaacs.utils.Console;

public class MainWindow extends JFrame {
	public MainWindow(){
		super("PKRY AACS");
		setSize(500, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout layout=new SpringLayout();
		setLayout(layout);

		JTextArea console=new JTextArea();
		console.setEditable(false);
        JScrollPane scrollPane = new JScrollPane( console );
        scrollPane.setPreferredSize(new Dimension(483,154));
        Console.redirectOutput( console );

		layout.putConstraint(SpringLayout.WEST, scrollPane,0,SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, scrollPane,400,SpringLayout.NORTH, this);

		add(scrollPane);

		JPanel administrator=new AdministratorTab();
		JPanel enkryptor=new EnkryptorTab();
		JPanel dekryptor=new DekryptorTab();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Administrator", administrator);
		tabbedPane.addTab("Enkryptor", enkryptor);
		tabbedPane.addTab("Dekryptor", dekryptor);
		tabbedPane.setPreferredSize(new Dimension(483,400));
		
		add(tabbedPane);
		
		

        
		
		setVisible(true);
	}
}
