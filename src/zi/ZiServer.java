package zi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;


public class ZiServer{
	boolean started=false;
	ServerSocket ss=null;
	DatagramSocket ds=null;
	boolean loginstatu = false;//判斷登錄狀態
	InetAddress ip = null;
	static int serverport = 6666;
	static int clientport = 6667;
	public static void main(String[] args) {
		
		new ZiServer().start();
	}
	
	public void start(){
		try{
			ss = new ServerSocket(8888);
			started = true;
			
		}catch(BindException e){
			System.out.println("端口正在被使用！");
			System.out.println("检查端口占用，并重新运行");
			System.exit(0);
		}catch(IOException e){
			e.printStackTrace();
		}
		try {
			ds = new DatagramSocket(serverport);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ip = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			System.out.println("not found the 127.0.0.1");
			e1.printStackTrace();
		}
		try{
			while(started){
				Socket s = ss.accept();
				loginstatu = false;
//				判斷是否已經登錄登錄成功啟動ChatThread
				if(!loginstatu){
					Thread loginT = new Thread(new LoginThread(s));
					loginT.start();
					loginstatu = true;
					System.out.println("用戶進入登錄界面"+s.getInetAddress());	
					//statu=true;
					if(loginstatu){
//						登陸成功啟動聊天線程
						System.out.println("启动chat线程");
						new Thread(new ChatThread()).start();
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
	
//	
//	public void run() {
//		
//	}
//	若登錄成功設loginstatu為true
	class LoginThread implements Runnable{
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos=null;
		private boolean bConnected = false;
		private Vector uservector = null;
//		private InetAddress ip;
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
			System.out.println("当前注册人数："+n);
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
				while(bConnected&&dis!=null){
//					!loginstatu&&
					try {
						statu = dis.readUTF();
					} catch (IOException e2) {
						System.out.println("意外断开连接 无法获得按钮状态");
//						System.out.println("dis errrrrrrrrrr");
//						e2.printStackTrace();
					}
					if(statu.equals("login")){
							
							str = dis.readUTF();
							String name = str.substring(0, str.indexOf('@'));
							String pass = str.substring(str.indexOf('@')+1, str.length());
							if(db.Logindb(name, pass)){
								dos.writeBoolean(true);
								dos.flush();
//								loginstatu = true;
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
						System.out.println("注册");
						
					}
					
				}
			}catch(EOFException e){
				System.out.println("Client Closed!");			
			}
			catch(IOException e){
//				e.printStackTrace();
				System.out.println("意外断开连接");
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
//		private DatagramSocket ds = null;
//		private DatagramPacket dp = null;
		private boolean bConnected = false;
//		private InetAddress ip = null;
//		ChatThread(DatagramSocket ds){
//			this.ds = ds;
////			this.ip = ip;
//			
//		}
		
		public void run(){
			if(ds == null)
				return;
			while(true){
				
				try{
					byte[] dataBuf = new byte[512];
					DatagramPacket ServerPacket;
					InetAddress remoteHost;
					int remotePort;
					String datagram,s;
					ServerPacket = new DatagramPacket(dataBuf,512);
					ds.receive(ServerPacket);
					remoteHost = ServerPacket.getAddress();
					remotePort = ServerPacket.getPort();
					datagram = new String(ServerPacket.getData());
					System.out.println("收到如下主机发来邮件"+remoteHost.getHostName()+"\n"+datagram);
					datagram = new String(remoteHost.getHostName()+": mailServere"+InetAddress.getLocalHost().getHostName()+"has already get your mails.");
					dataBuf = datagram.getBytes();
					System.out.println("发送过去得到长度"+datagram.length());
					ServerPacket = new DatagramPacket(dataBuf, dataBuf.length,remoteHost,remotePort);
					ds.send(ServerPacket);
				}catch(Exception e){
					System.err.println(e);
				}
				
			}
			
			
			
//			while(bConnected){
//				String datagram = null;
//				byte[] dataBuf = null;
//				dataBuf = new byte[512];
//				dp = new DatagramPacket(dataBuf,512);
//				try {
//					ds.receive(dp);
//					datagram = new String(dp.getData());
//					System.out.println(datagram);
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//					System.out.println("未接收到服务端发来数据");
//				}
////				存数据库；发送给目标IP；
//				datagram = "收到信息";
//				dataBuf = datagram.getBytes();
//				dp = new DatagramPacket(dataBuf, dataBuf.length,ip,clientport);
//				try {
//					ds.send(dp);
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//					System.out.println("向服务端发送数据失败");
//				}
//			}
		}
		protected void finalize(){
			
			if(ds != null){
				ds.close();
				ds = null;
				System.out.println("服务已关闭");
			}
			
		}
	}
}
