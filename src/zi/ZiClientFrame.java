package zi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.ItemSelectable;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;


public class ZiClientFrame {
	public static void main(String args[]){
		
		new loginFrame();
	}		
}
class loginFrame extends Frame{
	TextField pass,name;
	Button breg,blogin;
	Label wellabel,lpass,lname;
	Panel pbut,ppass,pname;
	Socket s = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	Vector uservector;
	loginFrame(){
		pbut = new Panel();
		pass = new TextField(20);
		name = new TextField(20);
		lpass = new Label("密码");
		lname = new Label("账号");
		ppass = new Panel();
		ppass.setLayout(new BorderLayout());
		pname = new Panel();
		pname.setLayout(new BorderLayout());
		ppass.add(lpass, BorderLayout.WEST);
		ppass.add(pass, BorderLayout.CENTER);
		pname.add(lname, BorderLayout.WEST);
		pname.add(name, BorderLayout.CENTER);
		pass.setEchoChar('*');
		breg = new Button("reg");
		blogin = new Button("login");
		breg.addActionListener(new ButtonAction());
		blogin.addActionListener(new ButtonAction());
		
		wellabel = new Label("please input the pass&name!");
		pbut.setLayout(new GridLayout());
		pbut.add(breg, BorderLayout.EAST);
		pbut.add(blogin, BorderLayout.CENTER);
		this.setLayout(new GridLayout(4, 1));
		this.add(wellabel);
		this.add(pname);
		this.add(ppass);
		this.add(pbut);
		this.setLocation(400, 300);
		this.setSize(240, 180);
		this.setVisible(true);
		this.addWindowListener(new HandleClose());	
		try {
			s = new Socket("127.0.0.1",8888);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
		} catch (UnknownHostException e1) {
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
			wellabel.setText("网络无连接或服务器错误请检查网络");
			
		}
		uservector = new Vector(15,10);
		uservector.addElement(new Friend("zixing", "admin", "127.0.0.1"));
		try{
			String userNo = null,nick = null,loginip = null,str = null;
			int n = dis.readInt();
			System.out.println("用户数："+n);
				while(n!=0){
					String t;
					str = dis.readUTF();
//					System.out.println(str);
					userNo=str.substring(0, str.indexOf('@'));
					t=str.substring(str.indexOf('@')+1, str.length());
					nick=t.substring(0, t.indexOf('@'));
					loginip=t.substring(t.indexOf('@')+1, t.length());
					uservector.addElement(new Friend(userNo, nick, loginip));
					n--;
				}
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	class ButtonAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			String sname=null,spass=null,statu=null;
			
			sname=name.getText();
			spass=pass.getText();
		
			if(e.getSource().equals(blogin)){
				if(sname.equals("")||spass.equals("")){
					wellabel.setText("请输入用户名和密码！");
				}
				else{
					statu="login";
					try {
						dos.writeUTF(statu);
						dos.flush();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					//System.out.println(dos.toString());
					try {
						dos.writeUTF(sname+"@"+spass);
						dos.flush();
						if(dis.readBoolean()){
							 new chatFrame(uservector);
							 dispose();
						}
						else{
							wellabel.setText("用户名或密码错误！");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			//注册事件
			else{
				statu="reg";
				try {
					dos.writeUTF(statu);
					dos.flush();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					dos.writeUTF(sname+"@"+spass);
					dos.flush();
					if(dis.readBoolean()){
						wellabel.setText("注册成功");
					}else{
						wellabel.setText("请重试 ");
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
				
		}
			
	}
	class HandleClose extends WindowAdapter{
		public void windowClosing(WindowEvent e){
		//	if(dis.)
			dispose();
//			disconnect();
			System.exit(0);
		}
	}
}

class chatFrame extends Frame{
	Panel pl,pr,pb,pi;
	TextArea output;
	TextField input;
	Button bsend,bclear;
	List friendlist;
	Vector friendvector;
	DatagramSocket ds = null;
	DatagramPacket dp = null;
	InetAddress ip;
	
	chatFrame(Vector uservector){
		pl = new Panel();
		pr = new Panel();
		pb = new Panel();
		pi = new Panel();
		pi.setLayout(new BorderLayout());
		pl.setLayout(new BorderLayout());
		pr.setLayout(new BorderLayout());
		pb.setLayout(new GridLayout(2,1));
		this.setLayout(new BorderLayout());
		output = new TextArea(10,45);
		input = new TextField();
		bsend = new Button("send");
		bclear = new Button("clear");
		bsend.addActionListener(new ButtonAction());
		bclear.addActionListener(new ButtonAction());
		friendvector = new Vector(15,10);
//		friendvector.addElement(new Friend("test1"));
//		friendvector.addElement(new Friend("test2"));
		System.out.println("xxxxxxxxxx"+uservector.size());
		for(int i = 0;i<uservector.size();i++){
			friendvector.add((Friend)uservector.elementAt(i));
		}
		friendlist = new List(15); 
		for(int i=0;i<friendvector.size();i++){
			friendlist.add(((Friend)friendvector.elementAt(i)).getNick());
		}
//		friendlist.add("test1");
//		friendlist.add("test1");
//		friendlist.add("test1");
//		friendlist.add("test1");
		pb.add(bclear);
		pb.add(bsend);
		pi.add(input,BorderLayout.CENTER);
		pi.add(pb,BorderLayout.EAST);
		pr.add(output,BorderLayout.CENTER);
		pr.add(pi,BorderLayout.SOUTH);
		pl.add(friendlist, BorderLayout.CENTER);
		this.add(pr, BorderLayout.CENTER);
		this.add(pl, BorderLayout.WEST);
		
		this.setSize(480,320);
		this.setLocation(400, 300);
		this.setVisible(true);
		friendlist.addItemListener(new selectFriend());
		bsend.addActionListener(new ButtonAction());
		this.addWindowListener(new HandleClose());
		try {
			ds = new DatagramSocket(6667);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ip = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			System.out.println("not found the 127.0.0.1");
			e1.printStackTrace();
		}
		
	}
	class ButtonAction implements ActionListener{
	
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==bsend){
				String datagram = input.getText();
				byte[] dataBuf = datagram.getBytes();
				dp = new DatagramPacket(dataBuf, dataBuf.length,ip,6666);
				try {
					ds.send(dp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("向服务端发送数据失败");
				}
				dataBuf = new byte[512];
				dp = new DatagramPacket(dataBuf,512);
				try {
					ds.receive(dp);
					datagram = new String(dp.getData());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("未接收到服务端发来数据");
				}
			}
		}
			
	}
	class HandleClose extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			//	if(dis.)
				dispose();
//				s.close();
//				disconnect();
				System.exit(0);
			}
	}
	class selectFriend implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
class Friend{
	private String userNo;
	private String nick;
	private String loginip;
	Friend(String userNo){
		this.userNo = userNo;
		this.nick = userNo;
		
	}
	Friend(String userNo,String nick){
		this.userNo = userNo;
		this.nick = nick;
	}
	Friend(String userNo,String nick,String loginip){
		this.userNo = userNo;
		this.nick = nick;
		this.loginip = loginip;
	}
	String getNick(){
		return nick;
	}
	String getuserNo(){
		return userNo;
	}
	String getloginip(){
		return loginip;
	}
	void setName(String userNo){
		this.userNo = userNo;
	}
	void setNick(String nick){
		this.nick = nick;
	}
	void setuserNo(String userNo){
		this.userNo = userNo;
	}
	
	
}
