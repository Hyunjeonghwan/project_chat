package ezen.chat.client;

public class EzenTalk {

	public static void main(String[] args) {
		ChatFrame chatFrame = new ChatFrame("::: 재밌는 대화 나누세요.. :::");
		chatFrame.setSize(400, 500);
		chatFrame.init();
		chatFrame.addEventListner();
		chatFrame.setVisible(true);
	}

}
