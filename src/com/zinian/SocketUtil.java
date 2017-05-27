package com.zinian;
import java.io.*;
import java.net.*;

public class SocketUtil{
	
	
		Socket s = null;
		DataOutputStream  dos = null;
		//String str=null;
		public void socketOut(String str){
				try {
					dos.writeUTF(str);
					dos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		
		public void connect(){
			try {
				s = new Socket("127.0.0.1", 8888);
				dos = new DataOutputStream(s.getOutputStream());
	System.out.println("Connected!");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void disconnect(){
			try {
				dos.close();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
