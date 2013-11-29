package fr.utt.erasmutt.model;

public class User {

    static String token;
    static String message;
    static String mail;
    static String firstname;
    static String lastname;
    
    public String getToken() {
        return token;
    }
    public void setToken(String Token) {
        token = Token;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String Message) {
    	message = Message;
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
