package fr.utt.erasmutt.sqlite.model;

public class User {

	private int idUser;
	private String firstname;
	private String lastname;
	private String picture;
	private String mail;
	private String token;

    public User() {
		super();
	}

    public User(int idUser, String firstname, String lastname, String picture,
			String mail, String token) {
		super();
		this.idUser = idUser;
		this.firstname = firstname;
		this.lastname = lastname;
		this.picture = picture;
		this.mail = mail;
		this.token = token;
	}

	public String getToken() {
        return token;
    }
    
	public void setToken(String Token) {
        token = Token;
    }

    public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getMail() {
        return mail;
    }
    public void setMail(String Mail) {
    	mail = Mail;
    }
    
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String Firstname) {
    	firstname = Firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String Lastname) {
    	lastname = Lastname;
    }
}
