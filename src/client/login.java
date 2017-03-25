package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import message.message;
import client.clientTrans;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class login extends JFrame {

	private static String myName;
	private Socket socket;
	private JPanel contentPane;
	private String localIp = "192.168.191.1";
	private JTextField textField;

	public login() {
		setTitle("P2P��¼��");
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			//localIp = addr.getHostAddress().toString();
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(230, 230, 250));
		panel.setBounds(0, 0, 434, 262);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel userName = new JLabel("   �û���");
		userName.setBackground(Color.WHITE);
		userName.setBounds(107, 73, 75, 30);
		panel.add(userName);
		
		textField = new JTextField();
		textField.setBounds(209, 74, 109, 30);
		panel.add(textField);
		textField.setColumns(10);
		
		//����ť��Ӽ��������������ťʱ����һ��message��Ϣ�����������Ա�ȷ���Ƿ��ܹ�����
		JButton btnOk = new JButton("��¼");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new Socket(localIp,2020);
					myName = textField.getText();
					if(myName.equals("")){
						JOptionPane.showMessageDialog(null, "�������û���");
						return;
					}
					new ObjectOutputStream(socket.getOutputStream()).writeObject(new message(myName,localIp));
					login.this.dispose();
					clientTrans ct = new clientTrans(socket,myName);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "������δ����");
					System.exit(1);
				}
				
			}
		});
		btnOk.setBounds(107, 156, 75, 23);
		panel.add(btnOk);
		
		JButton btnCancle = new JButton("ȡ��");
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
		btnCancle.setBounds(242, 156, 75, 23);
		panel.add(btnCancle);
		
		this.setVisible(true);
		
	}
	


	
	public static void main(String[] args){
		login lg  = new login();
	}
}
