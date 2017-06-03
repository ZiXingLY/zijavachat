package zi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;


public class ZiServer{
	boolean started=false;
	ServerSocket ss=null;
	DatagramSocket ds=null;
	boolean loginstatu = false;//�Д��䛠�B
	public static void main(String[] args) {
		
		new ZiServer().start();
	}
	
	public void start(){
		try{
			ss = new ServerSocket(8888);
			started = true;
			
		}catch(BindException e){
			System.out.println("�˿����ڱ�ʹ�ã�");
			System.out.println("���˿�ռ�ã�����������");
			System.exit(0);
		}catch(IOException e){
			e.printStackTrace();
		}
		try {
			ds = new DatagramSocket(6666);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			while(started){
				Socket s = ss.accept();
				loginstatu = false;
//				�Д��Ƿ��ѽ���䛵�䛳ɹ�����ChatThread
				if(!loginstatu){
					Thread loginT = new Thread(new LoginThread(s));
					loginT.start();
					System.out.println("�Ñ��M���䛽���"+s.getInetAddress());	
					//statu=true;
					if(loginstatu){
//						��ꑳɹ��������쾀��
						new Thread(new ChatThread(ds)).start();
					}
				}
//				Client c = new Client(s);
//				new Thread(c).start();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void run() {
		
	}
//	����䛳ɹ��Ologinstatu��true
	class LoginThread implements Runnable{
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos=null;
		private boolean bConnected = false;
		private Vector uservector = null;
		LoginThread(Socket s){
			this.s=s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void run() {
			DButil db = new DButil();
			this.uservector = db.getUserList();
			int n=uservector.size();
			System.out.println("verser"+n);
			try {
				dos.writeInt(n);
				dos.flush();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			Friend f=null;
			String str;
			for(int i=0;i<n;i++){
				f=(Friend)uservector.elementAt(i);
				str=f.getuserNo()+"@"+f.getNick()+"@"+f.getloginip();
				try {
					dos.writeUTF(str);
					dos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String statu=null;
			try{
				while(!loginstatu&&bConnected&&dis!=null){
					try {
						statu = dis.readUTF();
					} catch (IOException e2) {
						System.out.println("dis errrrrrrrrrr");
						e2.printStackTrace();
					}
					if(statu.equals("login")){
							
							str = dis.readUTF();
							String name = str.substring(0, str.indexOf('@'));
							String pass = str.substring(str.indexOf('@')+1, str.length());
							if(db.Logindb(name, pass)){
								dos.writeBoolean(true);
								dos.flush();
								loginstatu = true;
							}
							else{
								dos.writeBoolean(false);
								dos.flush();
							}							
						}
						else if(statu.equals("reg")){
//							DButil db = new DButil();
						
								str = dis.readUTF();
								String name = str.substring(0, str.indexOf('@'));
								String pass = str.substring(str.indexOf('@')+1, str.length());
								if(db.regdb(name,pass)==1){
									dos.writeBoolean(true);
									dos.flush();
								}
								else{
									dos.writeBoolean(false);
									dos.flush();
								}
						System.out.println("ע��");
						
					}
					
				}
			}catch(EOFException e){
				System.out.println("Client Closed!");			
			}
			catch(IOException e){
				e.printStackTrace();
				System.out.println("����Ͽ�����");
			}catch(Exception e2){
				
			}
			finally{
				
				try {
//					if(dos != null) dos.close();
//					if(dis != null) dis.close();
					if(s != null) s.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
		
	}
	
	class ChatThread implements Runnable{
//		private Socket s;
//		private DataInputStream dis = null;
//		private DataOutputStream dos=null;
//		private boolean bConnected = false;
//		ChatThread(Socket s){
//			this.s=s;
//			try {
//				dis = new DataInputStream(s.getInputStream());
//				dos = new DataOutputStream(s.getOutputStream());
//				bConnected = true;
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		private DatagramSocket ds = null;
		private DatagramPacket dp = null;
		private boolean bConnected = false;
		
		ChatThread(DatagramSocket ds){
			this.ds = ds;
			
			
		}
		
		public void run(){
			
			
			while(bConnected){
				String datagram = null;
				byte[] dataBuf = null;
				dataBuf = new byte[512];
				dp = new DatagramPacket(dataBuf,512);
				try {
					ds.receive(dp);
					datagram = new String(dp.getData());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("δ���յ�����˷�������");
				}
//				�����ݿ⣻���͸�Ŀ��IP��
			}
			
//			try{
//				DButil db = new DButil();
//				while(bConnected){
//					String str = dis.readUTF();
////					String str = dis.readUTF();
////					String name = str.substring(0, str.indexOf('@'));
////					String pass = str.substring(str.indexOf('@')+1, str.length());
////					if(db.Logindb(name, pass)){
////						dos.writeBoolean(true);
////					}
////					else{
////						dos.writeBoolean(false);
////					}
//				}
//			}catch(EOFException e){
//				System.out.println("Client Closed!");			
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				try {
//					if(dis != null) dis.close();
//					if(s != null) s.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
//			}
			
		}
	}

}