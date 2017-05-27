package com.zinian;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

//import com.chatsystem.view.ChatClient.TFListener;
public class ChatFrame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FunFrame ff=new FunFrame();
		//Login login = new Login();
	}

}
class FunFrame extends Frame{

	TextArea output,input;
	MenuBar m_MenuBar;
	Button send;
	Panel outP,outPP,outPP1;
	Menu User;
	MenuItem mi_User_menuCrate;
	String str;
	FunFrame()
	{
		input = new TextArea(10,45);
		//input.addKeyListener(new INListener());
		output = new TextArea(25,45);
		outP = new Panel();
		outPP = new Panel();
		send = new Button("发送");
		//str=input.getText();
		send.addActionListener(new HandleSend());
		outP.setLayout(new BorderLayout());
		outP.add(input,BorderLayout.NORTH);
		outP.add(outPP);
		m_MenuBar = new MenuBar();
		User = new Menu("User");
		mi_User_menuCrate = new MenuItem("crateUser");
		mi_User_menuCrate.addActionListener(new HandleAct());
		User.add(mi_User_menuCrate);
		m_MenuBar.add(User);
		this.setMenuBar(m_MenuBar);
		this.setLayout(new GridLayout(2,1));
		this.add(outP);
		this.add(output);
		addWindowListener(new HandleClose());
		setSize(480,600);
		setVisible(true);
	}
	class HandleAct implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getActionCommand()=="crateUser"){
				Login login = new Login();
			}
		}
	}
	class HandleClose extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			dispose();
			System.exit(0);
		}
	}

	class HandleSend implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource().equals(send)){
				
				
			}
			
		}
		
	}
	
}
class Login extends Frame implements ActionListener{
	
	TextField username;
	TextField password;
	Label usr,pass;
	Login(){
		username = new TextField(20);
		password = new TextField(20);
		usr = new Label("用户名:");
		pass = new Label("密码:");
		setLayout(new GridLayout(4,1));
		this.add(usr);
		this.add(username);
		this.add(pass);
		this.add(password);
		addWindowListener(new HandleClose());
		setSize(300,200);
		setVisible(true);
	}
	class HandleClose extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			dispose();
			//System.exit(0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
//class ziSocket{
//	Socket s = null;
//	DataOutputStream  dos = null;
//	public void connect(){
//		try {
//			s = new Socket("127.0.0.1", 8888);
//			dos = new DataOutputStream(s.getOutputStream());
//System.out.println("Connected!");
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void disconnect(){
//		try {
//			dos.close();
//			s.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	private class TFListener implements ActionListener{
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			// TODO Auto-generated method stub
//			String str = textField.getText().trim();//trim()去空格
//			//textArea.setText(str);
//			//textField.setText("");
//			try {
//				dos.writeUTF(str);
//				dos.flush();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//		
//	}
//}
