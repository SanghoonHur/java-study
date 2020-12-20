package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final String SERVER_IP = "127.0.0.1";
	private static final int PORT = 9999;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		List<Writer> listWriter = new ArrayList<Writer>();

		try {
			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();

			// 2. 바인딩
			serverSocket.bind(new InetSocketAddress(SERVER_IP, PORT));
			System.out.println("연결 대기" + SERVER_IP + ":" + PORT);

			// 3. 요청
			while(true) {
				Socket socket = serverSocket.accept();
				new ChatServerThread(socket, listWriter).start();
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}