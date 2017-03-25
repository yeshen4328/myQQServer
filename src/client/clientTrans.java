package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import message.message;

public class clientTrans extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel = new JPanel();
	private JPanel panel_1 = new JPanel();
	private JPanel panel_2 = new JPanel();
	private JPanel panel_3 = new JPanel();
	JTextArea rsgMes = new JTextArea();
	JTextArea sendMes = new JTextArea();
	JTextArea transFile = new JTextArea();
	private JTextField textField;
	private ArrayList<message> loginList;
	private Socket fromServer;
	private Socket toClient;
	private String desIp;
	private int desPort;
	private String myName;
	private int myPort;
	private String tempName;

	private File f;

	public clientTrans(Socket fromServer, String myName) {
		
		this.myName = myName;
		this.fromServer = fromServer;
		loginList = new ArrayList<message>();
		setTitle("P2P�ͻ���" + "���" + myName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 641, 400);
		// ���������Ӽ����������رմ���ʱ����ͻ����˳���¼����ʱ��Ҫ���������˷���һ����Ϣ�Ա�֪ͨ���������пͻ����˳�
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					new ObjectOutputStream(fromServer.getOutputStream())
							.writeObject("�Ͽ�����");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panel.setBackground(Color.LIGHT_GRAY);
		panel.setForeground(SystemColor.activeCaption);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel userList = new JLabel("�û��б�");
		userList.setBounds(10, 10, 54, 15);
		panel.add(userList);

		JLabel session = new JLabel("�Ự");
		session.setBounds(106, 10, 54, 15);
		panel.add(session);

		panel_1.setForeground(Color.BLACK);
		panel_1.setBackground(Color.CYAN);
		panel_1.setBounds(105, 35, 248, 287);
		panel.add(panel_1);
		panel_1.setLayout(null);

		rsgMes.setLineWrap(true);
		rsgMes.setEditable(false);
		rsgMes.setBounds(10, 10, 228, 129);

		JScrollPane rsgScroll = new JScrollPane();
		rsgScroll.setBounds(10, 10, 228, 129);
		rsgScroll.setViewportView(rsgMes);
		panel_1.add(rsgScroll);

		sendMes.setLineWrap(true);
		sendMes.setBounds(10, 167, 228, 57);
		panel_1.add(sendMes);

		JLabel sendLabel = new JLabel("������Ϣ");
		sendLabel.setBounds(10, 142, 54, 15);
		panel_1.add(sendLabel);

		JButton btnSend = new JButton("����");
		// ��������Ϣ���Ͱ�ť���Ӽ��������������ťʱ�����������ȡ��Ϣ�����д���
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* ���� */
				// �ж���Ϣ����֮ǰ�Ƿ�ѡ����ͨ�Ŷ���
				if (desIp == null) {
					JOptionPane.showMessageDialog(null, "��ѡ��ͨ�Ŷ���");
					return;
				}
				String mes = sendMes.getText().toString();
				mes = mes + " " + myName;
				sendMes.setText("");
				try {
					new ObjectOutputStream(toClient.getOutputStream())
							.writeObject(mes);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSend.setBounds(20, 242, 67, 23);
		panel_1.add(btnSend);

		JButton btnClear = new JButton("���");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* ��� */
				sendMes.setText("");
			}
		});
		btnClear.setBounds(145, 242, 67, 23);
		panel_1.add(btnClear);

		JLabel fileTrans = new JLabel("�ļ�����");
		fileTrans.setBounds(376, 10, 54, 15);
		panel.add(fileTrans);

		panel_2.setBackground(Color.ORANGE);
		panel_2.setBounds(376, 35, 210, 287);
		panel.add(panel_2);
		panel_2.setLayout(null);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(10, 27, 108, 21);
		panel_2.add(textField);
		textField.setColumns(10);

		JButton btnChoose = new JButton("�ļ�");
		// ����ļ���ť�ᵯ��һ���ļ�ѡ��Ի��򣬽��д����ļ���ѡ��
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(clientTrans.this);
				f = chooser.getSelectedFile();
				if (f != null) {
					String path = f.getPath();
					textField.setText(path);
				}
			}
		});
		btnChoose.setBounds(126, 26, 74, 23);
		panel_2.add(btnChoose);

		JLabel filePath = new JLabel("�ļ�·��");
		filePath.setBounds(10, 2, 54, 15);
		panel_2.add(filePath);

		JButton btnTrans = new JButton("����");
		// ���ļ����䰴ť���Ӽ�����
		btnTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// �ļ�����֮ǰ��Ҫ�ж��Ƿ�ѡ����ͨ�Ŷ���
					if (desIp == null) {
						JOptionPane.showMessageDialog(null, "��ѡ��ͨ�Ŷ���");
						return;
					}
					if (f != null) {
						Socket tempS = new Socket(desIp, desPort);
						FileInputStream fis = new FileInputStream(f);
						DataOutputStream dos = new DataOutputStream(tempS
								.getOutputStream());
						System.out.println();
						// �����ļ����ƺͳ����Լ����Ͷ��û���
						new ObjectOutputStream(tempS.getOutputStream())
								.writeObject(f.length());
						dos.writeUTF(f.getName().toString());
						dos.writeUTF(myName);
						 int fileLength = (int)f.length();
		                 int fileSent = 0;
						// �����ļ�
						byte[] sendBytes = new byte[1024];

						while (fileSent < fileLength)
	                    {
	                        if(fileLength - fileSent < 1024)
	                        {
	                            byte[] sendBytes2 = new byte[fileLength - fileSent];
	                            fis.read(sendBytes2);
	                            dos.write(sendBytes2);
	                            dos.flush();
	                            break;
	                        }
	                        fileSent +=  fis.read(sendBytes);
	                        dos.write(sendBytes);
	                        dos.flush();
	                    }

						fis.close();
						dos.close();
						tempS.close();

					}
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnTrans.setBounds(10, 64, 93, 23);
		panel_2.add(btnTrans);

		JLabel recieveList = new JLabel("�����ļ��б�");
		recieveList.setBounds(10, 109, 93, 15);
		panel_2.add(recieveList);

		transFile.setLineWrap(true);
		transFile.setBounds(10, 134, 190, 143);
		panel_2.add(transFile);

		JScrollPane fileScroll = new JScrollPane();
		fileScroll.setBounds(10, 134, 190, 143);
		fileScroll.setViewportView(transFile);
		panel_2.add(fileScroll);

		// �û��б�panel
		panel_3.setLayout(new GridLayout(10, 1));
		panel_3.setBounds(20, 35, 75, 287);
		panel.add(panel_3);

		this.setVisible(true);

		new Thread(new readloginList()).start();
	}

	// ���ڽ��շ�������ʵʱ���͹������û��б�
	class readloginList implements Runnable {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) {
				try {
					Object obj = new ObjectInputStream(
							fromServer.getInputStream()).readObject();
					// ���������˷��͵���ArrayList���ʵ����ʱ��˵�����յ�����Ϣ���û��б�
					if (obj instanceof ArrayList) {
						loginList = (ArrayList<message>) obj;
						panel_3.removeAll();
						for (message mes : loginList) {
							if (mes.getName().equals(myName)) {
								if (myPort == 0) {
									myPort = mes.getPort();
									new Thread(new readInfo()).start();
								}
							}
							JButton btnTemp = new JButton(mes.getName());
							btnTemp.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									tempName = ((JButton) e.getSource())
											.getText();
									rsgMes.setText("");
									rsgMes.append("���û�" + tempName + "ͨ��\n");
									for (message m : loginList) {
										if (m.getName().equals(tempName)) {
											desIp = m.getIp();
											desPort = m.getPort();
											try {
												toClient = new Socket(desIp,
														desPort);
											} catch (UnknownHostException e1) {
												e1.printStackTrace();
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
									}
								}

							});
							panel_3.add(btnTemp);
						}
						panel_3.updateUI();
					}
					// �����յ�����Ϣ��String���ʵ����ʱ��˵�����û�ע��������ѱ�ռ��
					if (obj instanceof String) {
						JOptionPane.showMessageDialog(null, "�û�����ע��");
						dispose();
						System.exit(1);
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					System.out.println("aaoq");
					break;
				}
			}
		}
	}
	
	// ��ȡ�ͻ��˷��͹�������Ϣ
	class readInfo implements Runnable {
		ServerSocket serverSocket1;

		public readInfo() {
		}

		@Override
		public void run() {
			try {
				serverSocket1 = new ServerSocket(myPort);
				while (true) {
					Socket socket1 = serverSocket1.accept();
					getInfo gti = new getInfo(socket1);
					new Thread(gti).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class getInfo implements Runnable {
		private Socket s;

		public getInfo(Socket s) {
			this.s = s;
		}

		@Override
		public void run() {
			while (true) {
				try {
					Object obj = null;
					try {
						obj = new ObjectInputStream(s.getInputStream())
								.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					// ������յ�����Ϣ��String���ʵ��������˵�����յ�����Ϣ����ͨ������Ϣ
					if (obj instanceof String) {
						String str = (String) obj;
						String[] getMes = str.split(" ");
						rsgMes.append("���յ�����" + getMes[1] + "����Ϣ" + "\n");
						rsgMes.append(getMes[0] + "\n");
					}
					// ������ǣ���˵�����յ������ļ�
					else {
						Long len = (Long) obj;
						String name = new DataInputStream(s.getInputStream())
								.readUTF();
						String userName = new DataInputStream(
								s.getInputStream()).readUTF();
						FileOutputStream fos = new FileOutputStream("E:\\"
								+ name);
						byte[] readBytes = new byte[1024];
						long lenReceived = 0;
						DataInputStream read = new DataInputStream(
								s.getInputStream());
						 while ((lenReceived < len) ) {
                             int rcvn = read.read(readBytes);
                             lenReceived +=rcvn;
                             fos.write(readBytes, 0, rcvn);
                             fos.flush();
                         }
						DateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date d = new Date();
						String date = df.format(d);
						transFile.append("��" + date + "���ܵ�����" + userName
								+ "���͵��ļ�" + "\n");
						read.close();
						fos.close();
						IOException e = new IOException("SSS");
						throw e;
					}
				} catch (Exception e) {
					if (e.getMessage().equals("SSS")) {
						break;
					}
				}
			}
		}
	}
}
