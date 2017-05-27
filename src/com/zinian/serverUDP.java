package com.zinian;
import java.util.Scanner;
import java.io.*;
import java.net.*;
public class serverUDP {

	public static void main(){
		
		System.out.println("please input the port:");
		Scanner sc = new Scanner(System.in);
		UDPServerThread myUDPServer = new UDPServerThread(sc.nextInt());
		myUDPServer.start();
	}
}
class UDPServerThread extends Thread{
	
	private DatagramSocket UDPServerSocket = null;
	public UDPServerThread(int Port){
		try{
			UDPServerSocket = new DatagramSocket(Port);
			System.out.print("邮件服务监听器在端口"+UDPServerSocket.getLocalPort()+"\n");
		}
		catch(Exception e){
			System.err.println(e);
		}
	}
	public void run(){
		if(UDPServerSocket == null)
			return;
		while(true){
			
			try{
				byte[] dataBuf = new byte[512];
				DatagramPacket ServerPacket;
				InetAddress remoteHost;
				int remotePort;
				String datagram,s;
				ServerPacket = new DatagramPacket(dataBuf,512);
				UDPServerSocket.receive(ServerPacket);
				remoteHost = ServerPacket.getAddress();
				remotePort = ServerPacket.getPort();
				datagram = new String(ServerPacket.getData());
				System.out.println("收到如下主机发来邮件"+remoteHost.getHostName()+"\n"+datagram);
				datagram = new String(remoteHost.getHostName()+":\n mailServere"+InetAddress.getLocalHost().getHostName()+"has already get your mails.");
				dataBuf = datagram.getBytes();
				ServerPacket = new DatagramPacket(dataBuf, dataBuf.length,remoteHost,remotePort);
				UDPServerSocket.send(ServerPacket);
			}catch(Exception e){
				System.err.println(e);
			}
			
		}
	}
	protected void finalize(){
		
		if(UDPServerSocket != null){
			UDPServerSocket.close();
			UDPServerSocket = null;
			System.out.println("服务已关闭");
		}
		
	}
}
