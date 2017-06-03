package com.zinian;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.Window.*;
import javax.swing.border.Border;

public class Theframe {
//	static Socket s=null;
//	static DataOutputStream dos=null;
	public static void main(String[] args) {
//		try {
//			s = new Socket("127.0.0.1",8888);
//			dos = new DataOutputStream(s.getOutputStream());
//			mainFrame ff = new mainFrame();
//			new loginFrame();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// TODO Auto-generated method stub
//		mainFrame ff = new mainFrame();
		new loginFrame();
		
		
	}
}
class mainFrame extends Frame{
	
	Panel pl,pr,pb,pi;
	TextArea output;
	TextField input;
	Button bsend,bclear;
	List friendlist;
	Socket s=null;
	DataOutputStream dos=null;
	DataInputStream dis=null;
	mainFrame(){
		try {
			s = new Socket("127.0.0.1",8888);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		friendlist = new List(15); 
		friendlist.add("test1");
		friendlist.add("test1");
		friendlist.add("test1");
		friendlist.add("test1");
		pb.add(bclear);
		pb.add(bsend);
		pi.add(input,BorderLayout.CENTER);
		pi.add(pb,BorderLayout.EAST);
		pr.add(output,BorderLayout.CENTER);
		pr.add(pi,BorderLayout.SOUTH);
		pl.add(friendlist, BorderLayout.CENTER);
		this.add(pr, BorderLayout.CENTER);
		this.add(pl, BorderLayout.WEST);
		
		setSize(480,320);
		this.setLocation(400, 300);
		setVisible(true);
		addWindowListener(new HandleClose());
	}
	
	class ButtonAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//发送事件
			String str;
			str=input.getText();
			if(e.getSource()==bsend){
				try {
					dos.writeUTF(str);
					dos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				input.setText("");
			}
			//清空事件
			else{
				str=input.getText();
				input.setText("");
				
			}
		}
	}
	class HandleClose extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			disconnect();
			dispose();
			System.exit(0);
		}
	}
	
	void disconnect(){
		try {
			dos.close();
			dis.close();
			s.close();
			System.out.println("已关闭");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
class loginFrame extends Frame{
	
	TextField pass,name;
	Button breg,blogin;
	Label wellabel,lpass,lname;
	Panel pbut,ppass,pname;
	Socket s=null;
	DataOutputStream dos=null;
	DataInputStream dis = null;
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
		addWindowListener(new HandleClose());
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
	}
	class ButtonAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//登录事件
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
							 new mainFrame();
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
			disconnect();
			System.exit(0);
		}
	}
	void disconnect(){
		try {
			
			if(dos != null) dos.close();
			if(dis != null) dis.close();
			if(s != null ) s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("关闭异常");
			
		}
	}
	
}
