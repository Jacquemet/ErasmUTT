package fr.utt.erasmutt.sqlite.model;

public class Review {

	private int idReview;
	private int idUser;
	private int idActivity;
	private String title;
	private String description;
	private float mark;
	private String dateTime;
	private String language;
	
	public Review() {
		super();
	}

	public Review(int idReview, int idUser, int idActivity, String title,
			String description, float mark, String dateTime, String language) {
		super();
		this.idReview = idReview;
		this.idUser = idUser;
		this.idActivity = idActivity;
		this.title = title;
		this.description = description;
		this.mark = mark;
		this.dateTime = dateTime;
		this.language = language;
	}

	public int getIdReview() {
		return idReview;
	}

	public void setIdReview(int idReview) {
		this.idReview = idReview;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getMark() {
		return mark;
	}

	public void setMark(float mark) {
		this.mark = mark;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	
	
}
