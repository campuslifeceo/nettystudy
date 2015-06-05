package chatroom.tools;

public class Message {
	private final String message;
	
	public Message(String message){
		this.message = message;
	}
	
	public Message(){
		this("");
	}
	
	public String value(){
		return message;
	}
	
	@Override
	public String toString(){
		return message;
	}
}
