package ch.freebo.utils;

import com.google.gson.annotations.SerializedName;

public class Products {
	
	@SerializedName("id")
	private int id;
	
	@SerializedName("ean")
	private String ean = " ";
	
	@SerializedName("name")
	private String name = " ";
	
	@SerializedName("imagelink")
	private String imagelink = " ";
	
	@SerializedName("optin")
	private Boolean optin = false;
	
	@SerializedName("isdeleted")
	private Boolean isdeleted = false;
	
	@SerializedName("points")
	private Integer points = 0;
	
	@SerializedName("userrank")
	private Integer userrank = 0;
	
	@SerializedName("participants")
	private Integer participants = 0;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the ean
	 */
	public String getEan() {
		return ean;
	}

	/**
	 * @param ean the ean to set
	 */
	public void setEan(String ean) {
		this.ean = ean;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the imagelink
	 */
	public String getImagelink() {
		return imagelink;
	}

	/**
	 * @param imagelink the imagelink to set
	 */
	public void setImagelink(String imagelink) {
		this.imagelink = imagelink;
	}

	/**
	 * @return the optin
	 */
	public Boolean getOptin() {
		return optin;
	}

	/**
	 * @param optin the optin to set
	 */
	public void setOptin(Boolean optin) {
		this.optin = optin;
	}

	/**
	 * @return the isdeleted
	 */
	public Boolean getIsdeleted() {
		return isdeleted;
	}

	/**
	 * @param isdeleted the isdeleted to set
	 */
	public void setIsdeleted(Boolean isdeleted) {
		this.isdeleted = isdeleted;
	}

	/**
	 * @return the points
	 */
	public Integer getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}

	/**
	 * @return the userrank
	 */
	public Integer getUserrank() {
		return userrank;
	}

	/**
	 * @param userrank the userrank to set
	 */
	public void setUserrank(Integer userrank) {
		this.userrank = userrank;
	}

	/**
	 * @return the participants
	 */
	public Integer getParticipants() {
		return participants;
	}

	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(Integer participants) {
		this.participants = participants;
	}

}
