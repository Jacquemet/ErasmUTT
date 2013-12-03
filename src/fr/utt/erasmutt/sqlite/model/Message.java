package fr.utt.erasmutt.sqlite.model;

public class Message {
	private int idMessage;
	private int idUSerSender;
	private int idUserReceiver;
	private String message;
	private String date;
	private int read;
	
	public Message() {
		super();
	}
	
	public Message(int idMessage, int idUSerSender, int idUserReceiver,
			String message, String date, int read) {
		super();
		this.idMessage = idMessage;
		this.idUSerSender = idUSerSender;
		this.idUserReceiver = idUserReceiver;
		this.message = message;
		this.date = date;
		this.read = read;
	}
	public int getIdMessage() {
		return idMessage;
	}
	public void setIdMessage(int idMessage) {
		this.idMessage = idMessage;
	}
	public int getIdUSerSender() {
		return idUSerSender;
	}
	public void setIdUSerSender(int idUSerSender) {
		this.idUSerSender = idUSerSender;
	}
	public int getIdUserReceiver() {
		return idUserReceiver;
	}
	public void setIdUserReceiver(int idUserReceiver) {
		this.idUserReceiver = idUserReceiver;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
}
