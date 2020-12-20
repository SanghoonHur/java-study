package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ChatServerThread extends Thread {
	private Socket socket;
	private String nickname;
	private List<Writer> listWriter;
	
	public ChatServerThread( Socket socket, List<Writer> listWriter ) {
		this.socket = socket;
		this.listWriter = listWriter;
	}
	
	
	@Override
	public void run() {
		InetSocketAddress remoteInetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		InetAddress remoteInetAddress = remoteInetSocketAddress.getAddress();
		String remoteHostAddress = remoteInetAddress.getHostAddress();
		int remotePort = remoteInetSocketAddress.getPort();
		System.out.println("[server] connected by clinet [" + remoteHostAddress + ":" + remotePort + "]");

		try {
			BufferedReader bufferReader = new BufferedReader( new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter printWriter = new PrintWriter( new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

			while(true) {
				String chat = bufferReader.readLine();
				if(chat == null) {
					doQuit(printWriter);
					break;
				}
				
				String[] tokens = chat.split( ":" );
				if( "join".equals(tokens[0])) {
					doJoin(tokens[1], printWriter);
				} else if( "message".equals(tokens[0])) {
					doMessage(tokens[1]);
				} else if( "quit".equals(tokens[0])) {
					doQuit(printWriter);
					break;
				}
			}
		} catch (SocketException ex) {
			System.out.println("[client] suddenly closed by servers");
		} catch (IOException ex) {
			System.out.println("[server] error: " + ex);
		} finally {
			try {
				if(socket != null && !socket.isClosed()) {
					socket.close();
				}
			}catch( IOException ex) {
				System.out.println("[server] error: " + ex);
			}
		}
	}
	
	private void doQuit(Writer writer) {
		removeWriter(writer);
		broadcastMessage( nickname + "님이 퇴장 하였습니다." );
	}
	
	private void removeWriter(Writer writer) {
		synchronized(writer) {
			listWriter.remove(writer);
		}
	}
	
	
	private void doMessage(String chat) {
		broadcastMessage(nickname + ": " + chat);
	}
	
	
	private void doJoin(String nickname, PrintWriter writer ){
		this.nickname = nickname;
		broadcastMessage(nickname + "님이 입장했습니다");
		addWriter(writer);
	}
	
	private void addWriter(Writer writer) {
		synchronized(listWriter) {
			listWriter.add(writer);
		}
	}
	
	
	
	private void broadcastMessage(String chat) {
		synchronized(listWriter) {
			for(Writer writer : listWriter ) {
				((PrintWriter) writer).println(chat);
			}
		}
	}
	
}