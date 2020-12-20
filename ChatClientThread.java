package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ChatClientThread extends Thread {
	private Socket socket;
	
	public ChatClientThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try{
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

			while(true) {
				String chat = bufferReader.readLine();
				if(chat == null) {
					break;
				}
				System.out.println(chat);
			}
		} catch(SocketException ex){
			System.out.println("Socket Closed");
		} catch(IOException ex){
			ex.printStackTrace();
		} 
	}
}