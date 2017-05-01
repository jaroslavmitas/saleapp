package cz.jaroslavmitas.dto;

public class Sale {

	private String id;
	
	private String messageType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Override
	public String toString() {
		return "Sale [id=" + id + ", messageType=" + messageType + "]";
	}

		
}
