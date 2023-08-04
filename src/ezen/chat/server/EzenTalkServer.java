package ezen.chat.server;

import java.io.IOException;

public class EzenTalkServer {

	public static void main(String[] args) {
		
		ChatServer chatServer = new ChatServer();
		try {
			chatServer.startup();
		} catch (IOException e) {
			System.out.println("ChatServer 실행중 예외 발생");
			System.out.println("-"+e.getMessage());
		}
		
	}

}
