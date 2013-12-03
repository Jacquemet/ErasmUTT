package fr.utt.erasmutt.sqlite.model;

public class UserGroup {
	
	private int idUser;
	private int idGroup;
	
	public UserGroup() {
		super();
	}

	public UserGroup(int idUser, int idGroup) {
		super();
		this.idUser = idUser;
		this.idGroup = idGroup;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}
	
}
