package fr.utt.erasmutt;

public class ActivityForUser {
	private int id;
	private double averageMark;
	private String name;
	private String desciption;
	private String longitude;
	private String latitude;
	private String website;
	private String picture;
	private Boolean focusOn;
	
	public int getId() {
	    return id;
	}
	public void setId(int Id) {
		id = Id;
	}
	
	public double getAverageMark() {
	    return averageMark;
	}
	public void setAverageMark(double AverageMark) {
		averageMark = AverageMark;
	}
	
	public String getName() {
        return name;
    }
    public void setName(String Name) {
    	name = Name;
    }
    
    public String getDesc() {
        return desciption;
    }
    public void setDesc(String Desc) {
    	desciption = Desc;
    }
    
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String Longitude) {
    	longitude = Longitude;
    }
    
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String Latitude) {
    	latitude = Latitude;
    }
    
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String Website) {
    	website = Website;
    }
    
    public String getPicture() {
        return picture;
    }
    public void setPicture(String Picture) {
    	picture = Picture;
    }
	
    public Boolean getFocusOn(){
    	return focusOn;    	
    }
    public void setFocusOn(Boolean FocusOn) {
    	focusOn = FocusOn;
    }
	
}
