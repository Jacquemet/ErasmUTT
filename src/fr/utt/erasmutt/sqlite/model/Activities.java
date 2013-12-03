package fr.utt.erasmutt.sqlite.model;

public class Activities {
	
	private int idActivity;
	private String name;
	private String desciptionActivity;
	private String pictureActivity;
	private float averageMark;
	private String longitude;
	private String latitude;
	private String website;
	private Boolean focusOn;
	
	public Activities() {
		super();
	}

	public Activities(int idActivity, String name,
			String desciptionActivity, String pictureActivity,
			float averageMark, String longitude, String latitude,
			String website, Boolean focusOn) {
		super();
		this.idActivity = idActivity;
		this.name = name;
		this.desciptionActivity = desciptionActivity;
		this.pictureActivity = pictureActivity;
		this.averageMark = averageMark;
		this.longitude = longitude;
		this.latitude = latitude;
		this.website = website;
		this.focusOn = focusOn;
	}

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesciptionActivity() {
		return desciptionActivity;
	}

	public void setDesciptionActivity(String desciptionActivity) {
		this.desciptionActivity = desciptionActivity;
	}

	public String getPictureActivity() {
		return pictureActivity;
	}

	public void setPictureActivity(String pictureActivity) {
		this.pictureActivity = pictureActivity;
	}

	public float getAverageMark() {
		return averageMark;
	}

	public void setAverageMark(float averageMark) {
		this.averageMark = averageMark;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Boolean getFocusOn() {
		return focusOn;
	}

	public void setFocusOn(Boolean focusOn) {
		this.focusOn = focusOn;
	}
	
}
