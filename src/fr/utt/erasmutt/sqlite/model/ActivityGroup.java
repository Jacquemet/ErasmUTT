package fr.utt.erasmutt.sqlite.model;

public class ActivityGroup {

	private int idGroup;
	private int idActivity;
	
	public ActivityGroup() {
		super();
	}

	public ActivityGroup(int idGroup, int idActivity) {
		super();
		this.idGroup = idGroup;
		this.idActivity = idActivity;
	}

	public int getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}
	
}
