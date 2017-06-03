package zi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPutil {
	Socket s = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;

	public void connect() {
		try {
			s = new Socket("127.0.0.1", 8888);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			System.out.println("Connected!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
//			dos.close();
//			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
