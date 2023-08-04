package ezen.chat.client;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import ezen.chat.protocol.MessageType;

/**
 * TCP/IP 기반의 ChatClient
 */
public class ChatClient {
	
	private static final String SERVER_IP = "192.168.20.12";
//	private static final String SERVER_IP = "192.168.20.35";
	private static final int SERVER_PORT = 7777;
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	ChatFrame chatFrame;
	
	
	public ChatClient(ChatFrame chatFrame) {
		this.chatFrame = chatFrame;
	}
	
	// 서버 연결
	public void connectServer() throws IOException {
		socket = new Socket(SERVER_IP, SERVER_PORT);
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
	}
	
	// 서버 연결 종료
	public void disConnectServer() throws IOException {
		if(socket != null) {
			socket.close();
		}
	}
	
	// 메시지 전송
	public void sendMessage(String message) throws IOException {
		out.writeUTF(message);
//		out.flush();
	}
	
	// 메시지 수신
	public void receiveMessage() {
		// 서버로부터 전송되는 메시지를 실시간 수신하기 위해 스레드 생성 및 시작
		Thread thread = new Thread() {
			public void run() {
				try {
					while (true) {
						String serverMessage = in.readUTF();
						
						String[] tokens = serverMessage.split("\\|");
						MessageType messageType = MessageType.valueOf(tokens[0]);
						String senderNickName = tokens[1];
						
						
//						클라이언트 전송 메시지 종류에 따른 처리
						switch (messageType) {
							case CONNECT:
								chatFrame.appendMessage("@@@@ "+senderNickName+"님이 연결하였습니다 @@@@");
								chatFrame.clean();
								break;
							case CHAT_MESSAGE:
								String chatMessage = tokens[2];
								chatFrame.appendMessage("["+senderNickName+"] : "+chatMessage);
								break;
							case DIS_CONNECT:
								chatFrame.appendMessage("#### "+senderNickName+"님이 연결 해제하였습니다 ####");
								chatFrame.clean();
								break;	
							case NICK_NAME:
								chatFrame.appendUserList(tokens[1]);
								break;
							case DM_MESSAGE:
								String chatMessage1 = tokens[2];
								chatFrame.appendMessage("[(귓속말) "+senderNickName+"]"+chatMessage1);
								break;
							
								
						}
						
					}
				} catch (IOException e) {
					
				} 
				finally {
					
				}
			}
		};
		thread.start();
	}
}








