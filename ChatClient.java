package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 9999;

	public static void main(String[] args) {

		Socket socket = null;
		Scanner scanner = null;

		try {
			// 1. 키보드 연결
			scanner = new Scanner(System.in);

			// 2. 소켓 생성
			socket = new Socket();

			// 3. 서버에 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			// 4-1. reader 생성
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			// 4-2. writer 생성
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

			// 5.join 프로토콜
			System.out.print("닉네임>>");
			String nickname = scanner.nextLine();
			printWriter.println("join:" + nickname);
			String ack = bufferedReader.readLine();
			if ("join: ok".equals(ack)) {
				System.out.println(nickname + "님이 입장합니다.");
			}
			printWriter.flush();

			// 6. ChatClientThread 시작
			new ChatClientThread(socket).start();

			// 7. 키보드 입력 처리
			while(true) {

				String chat = scanner.nextLine();

				if ("quit".equals(chat)) {
					printWriter.println("quit");
					// System.exit(0);
					break;
				} else {

					printWriter.println("message: " + chat);
				}
			}
		} catch (IOException ex) {
			System.out.println("error: " + ex);

		} finally {
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}