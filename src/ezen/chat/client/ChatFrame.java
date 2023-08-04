package ezen.chat.client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import ezen.chat.protocol.MessageType;
import ezen.chat.server.ChatHandler;

public class ChatFrame extends Frame {
	
	Label nicknameL;
	TextField nicknameTF, inputTF;
	Button loginB, sendB;
	TextArea messageTA, nicknameList;
	
	Panel northP, southP;
	
	ChatClient chatClient;
	String nickName;
	
	Choice userChoice;
	
	public ChatFrame() {
		this("No-Title");
	}
	
	public ChatFrame(String title) {
		super(title);
		// 복합연관
		nicknameL = new Label("닉네임");
		nicknameTF = new TextField();
		inputTF = new TextField();
		loginB = new Button("연  결");
//		loginB.setBackground(new Color(255, 255, 255));
//		loginB.setBackground(Color.BLUE);
//		loginB.setForeground(Color.WHITE);
//		loginB.setFont(new Font("궁서", Font.BOLD, 35));
		sendB = new Button("보내기");
		messageTA = new TextArea();
		nicknameList = new TextArea(10, 10);
		userChoice = new Choice();
		userChoice.add("전체");
		northP = new Panel(new BorderLayout(5, 5));
		southP = new Panel(new BorderLayout(5, 5));
	}
	
//	컴포넌트 배치
	public void init() {
//		Frame의 디폴트레이아웃매니저 : BorderLayout
		northP.add(nicknameL, BorderLayout.WEST);
		northP.add(nicknameTF, BorderLayout.CENTER);
		northP.add(loginB, BorderLayout.EAST);
		
		southP.add(inputTF, BorderLayout.CENTER);
		southP.add(sendB, BorderLayout.EAST);
		southP.add(userChoice, BorderLayout.WEST);
		
		add(northP, BorderLayout.NORTH);
		add(messageTA, BorderLayout.CENTER);
		add(nicknameList, BorderLayout.EAST);
		add(southP, BorderLayout.SOUTH);
	}
	
	private void connect() {
		nickName = nicknameTF.getText();
		if(!Validator.hasText(nickName)) {
			JOptionPane.showMessageDialog(this, "닉네임을 입력하세요.");
			return;
		}
		chatClient = new ChatClient(this);
		try {
			chatClient.connectServer();
			setEnable(false);
			chatClient.sendMessage(MessageType.CONNECT+"|"+nickName);
			chatClient.receiveMessage();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "ChatServer를 연결할 수 없습니다.");
		}
	}
	
	private void setEnable(boolean enable) {
		nicknameTF.setEnabled(enable);
		loginB.setEnabled(enable);
	}
	
	public void appendMessage(String message) {
		messageTA.append(message+"\n");
	}
	
	private void sendChatMessage() {
		String message = inputTF.getText();
		String select = userChoice.getSelectedItem();
		if(Validator.hasText(message)) {
			try {
				if(select.equals("전체")) {
					chatClient.sendMessage(MessageType.CHAT_MESSAGE+"|"+nickName+"|"+message);
				}else {
					chatClient.sendMessage(MessageType.DM_MESSAGE+"|"+nickName+"|"+message+"|"+select);
				}
				inputTF.setText("");
			} catch (IOException e) {}
		}
	}
	
	public void choice(String messageType) {
		switch (messageType) {
		case "전체":
			System.out.println("전체채팅");
			break;
		}
	}
	
	public void appendUserList(String nickName) {
	    nicknameList.append(nickName+"\n");
	    userChoice.add(nickName);
	}
	
	public void clean() {
		nicknameList.setText("");
		userChoice.removeAll();
		userChoice.add("전체");
	}
	
	
	private void disConnect() {
		try {
			chatClient.sendMessage(MessageType.DIS_CONNECT+"|"+nickName);
			exit();
		} catch (IOException e) {
	
		}
	}
	
	private void exit() {
		setVisible(false);
		dispose();
		System.exit(0);
	}
	
	public void addEventListner() {
//		종료처리
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disConnect();
			}
		
		});
//		연결처리
		loginB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		
		nicknameTF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		
		inputTF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChatMessage();
			}
		});
		
		sendB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChatMessage();
			}
		});
		
		userChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					if(userChoice.getSelectedItem().equals("전체")) {
						choice("전체");
					}
				}
			}
		});
		
	}
	
	

}






