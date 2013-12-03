package fr.utt.erasmutt.sqlite.model;

public class Group {

	private int idGroup;
	private String name;
	private String descriptionGroup;
	private String pictureGroup;
	
	public Group() {
		super();
	}

	public Group(int idGroup, String name, String descriptionGroup,
			String pictureGroup) {
		super();
		this.idGroup = idGroup;
		this.name = name;
		this.descriptionGroup = descriptionGroup;
		this.pictureGroup = pictureGroup;
	}

	public int getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptionGroup() {
		return descriptionGroup;
	}

	public void setDescriptionGroup(String descriptionGroup) {
		this.descriptionGroup = descriptionGroup;
	}

	public String getPictureGroup() {
		return pictureGroup;
	}

	public void setPictureGroup(String pictureGroup) {
		this.pictureGroup = pictureGroup;
	}
	
	
}
