package fr.utt.erasmutt.sqlite.model;

import java.sql.Blob;

public class Activities {
	
	private int idActivity;
	private String name;
	private String desciptionActivity;
	private byte[] pictureActivity;
	private float averageMark;
	private String longitude;
	private String latitude;
	private String website;
	private String address;
	private Integer focusOn;
	private String pictureActivityString;
	public Activities() {
		super();
	}

	public Activities(int idActivity, String name,
			String desciptionActivity, byte[] pictureActivity,
			float averageMark, String longitude, String latitude,
			String website, Integer focusOn,String pictureActivityString) {
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
		this.pictureActivityString=pictureActivityString;
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

	public byte[] getPictureActivity() {
		return pictureActivity;
	}

	public void setPictureActivity(byte[] pictureActivity) {
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

	public Integer getFocusOn() {
		return focusOn;
	}

	public void setFocusOn(Integer focusOn) {
		this.focusOn = focusOn;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String Address) {
		this.address = Address;
	}

	public void setPictureActivityString(String pictureString) {
		this.pictureActivityString = pictureString;
	}
	public String getPictureActivityString() {
		return pictureActivityString;
	}
	
	
}
