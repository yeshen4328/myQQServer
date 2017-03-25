package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import message.message;

public class server extends JFrame {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ArrayList<message> loginList = new ArrayList<message>();
	private ServerSocket serversocket;
	private Map<Socket,message> socTolog = new ConcurrentHashMap<Socket,message>();
	//��������ͻ���֮�����ͨ�ŵĶ˿ں�
	private static int port = 3000;
	
	JTextArea t1 = new JTextArea();
	JTextArea t2 = new JTextArea();
	
	public server(){
		
		setTitle("P2P������");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 414, 242);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel userInfo = new JLabel("�û���Ϣ");
		userInfo.setBounds(10, 8, 54, 15);
		panel.add(userInfo);
		
		JLabel linkInfo = new JLabel("������Ϣ");
		linkInfo.setBounds(148, 8, 54, 15);
		panel.add(linkInfo);
		
		JScrollPane sp1 = new JScrollPane();
		sp1.setBounds(20, 33, 104, 199);
		panel.add(sp1);
		
		t1.setEditable(false);
		t1.setLineWrap(true);
		sp1.setViewportView(t1);
		
		JScrollPane sp2 = new JScrollPane();
		sp2.setBounds(169, 33, 193, 199);
		panel.add(sp2);
		
		t2.setEditable(false);
		t2.setLineWrap(true);
		sp2.setViewportView(t2);
		
		this.setVisible(true);
		
		new Thread(new handleCon()).start();
	}
	
	class handleCon implements Runnable{
		@Override
		public void run() {
			try {
				//�����������˼���
				serversocket = new ServerSocket(2020);
				while(true){
					//���пͻ���������ʱ�������߳������ͻ��˷��͹�������Ϣ
					Socket s = serversocket.accept();
					new Thread(new readClientInfo(s)).start();
				}
			}catch(Exception e){
				System.out.println("�˿ںű�ռ��");
				System.exit(1);
			}
		}
	}
	
	class readClientInfo implements Runnable{
		private Socket s;
		public readClientInfo(Socket s){
			this.s = s;
		}
		@Override
		public void run() {
			while(true){
				try {
					Object obj = new ObjectInputStream(s.getInputStream()).readObject();
					//�����յ�����Ϣ��message���ʵ����
					if(obj instanceof message){
						message mes = (message)obj;
						int k=0;
						//�ж��û����Ƿ��ѱ�ʹ��
						for(k = 0;k<loginList.size();k++){
							if(loginList.get(k).getName().equals(mes.getName())){
								//���û�����ʹ��ʱ�����ͻ��˷���һ��֪ͨ��Ϣ���Ͽ�����
								new ObjectOutputStream(s.getOutputStream()).writeObject("�û�����ʹ��");
								socTolog.remove(s);
								break;
							}
						}
						//�û���û�б�ʹ��
						if(k==loginList.size()){
							mes.setPort(port++);
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
							Date d = new Date();
							String str = df.format(d);
							t2.append("�û�"+mes.getName()+"��"+str+"��¼");
							t2.append("\n");
							loginList.add(mes);
							t1.setText("");
							for(message m:loginList){
								t1.append("�û�����"+m.getName()+"\n");
							}
							loginList.add(mes);
							socTolog.put(s, mes);
							allocateMsg();
						}
					}else if(obj instanceof String){       /*������Ϣ��String��ʵ����˵���ͻ��˶Ͽ�������*/
						String str = (String)obj;
						if(str.equals("�Ͽ�����")){
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
							Date d = new Date();
							String date = df.format(d);
							t2.append("�û�"+socTolog.get(s).getName()+"��"+date+"�˳�");
							t2.append("\n");
							socTolog.remove(s);
							allocateMsg();
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					break;
				}
			}
		}
		
	}
	
	//�ַ��û��б���Ϣ
	public void allocateMsg(){
		loginList.clear();
		for(message m:socTolog.values()){
			loginList.add(m);
		}
		for(Socket tempS:socTolog.keySet()){
			try {
				new ObjectOutputStream(tempS.getOutputStream()).writeObject(loginList);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public static void main(String[] args){
		server s = new server();
	}
}
