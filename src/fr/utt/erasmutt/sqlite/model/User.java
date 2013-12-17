package fr.utt.erasmutt.sqlite.model;

public class User {

	private int idUser;
	private String firstname;
	private String lastname;
	private byte[] picture;
	private String mail;
	private String token;
	private String pictureString;

    public User() {
		super();
	}

    public User(int idUser, String firstname, String lastname, byte[] picture,
			String mail, String token, String picString) {
		super();
		this.idUser = idUser;
		this.firstname = firstname;
		this.lastname = lastname;
		this.picture = picture;
		this.mail = mail;
		this.token = token;
		this.pictureString = picString;
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
	public String getPictureString() {
		return pictureString;
	}
	public void setPictureString(String picture) {
		this.pictureString = picture;
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
    
    public void logout(){
    	idUser=0;
    	pictureString="";
    	firstname="";
    	lastname="";
    	token="";
    	mail="";    
    	picture = null;
    }

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
}
