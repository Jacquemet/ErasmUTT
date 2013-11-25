package fr.utt.erasmutt;

public class DataConnection {

    boolean error;
    static String token;
    static String urlRoot = "http://www.ws.kevin-larue.fr/"; 
    static String message;
    static String mail;
    static String firstname;
    static String lastname;
    
    public boolean getError() {
        return error;
    }
    public void setError(Boolean Error) {
        error = Error;
    }

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
