package ch.mobileking.utils;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.annotations.SerializedName;

public class Products {
	
	@SerializedName("id")
	private int id;
	
	@SerializedName("ean")
	private String ean = " ";
	
	@SerializedName("name")
	private String name = " ";
	
	@SerializedName("producer")
	private String producer = " ";	
	
	@SerializedName("ingredients")
	private String ingredients = " ";	
	
	@SerializedName("category")
	private String category = " ";	
	
	@SerializedName("imagelink")
	private String imagelink = " ";
	
	@SerializedName("imagepath")
	private String imagepath = " ";
	
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
	
	@SerializedName("crowns")
	private List<Crown> crowns;
	
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
	 * @return the producer
	 */
	public String getProducer() {
		return producer;
	}

	/**
	 * @param producer the producer to set
	 */
	public void setProducer(String producer) {
		this.producer = producer;
	}

	/**
	 * @return the ingredient
	 */
	public String getIngredients() {
		return ingredients;
	}

	/**
	 * @param ingredient the ingredient to set
	 */
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
	 * @return the imagepath
	 */
	public String getImagepath() {
		return imagepath;
	}

	/**
	 * @param imagepath the imagepath to set
	 */
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
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

	/**
	 * @return the crowns
	 */
	public List<Crown> getCrowns() {
		return crowns;
	}

	/**
	 * @param crowns the crowns to set
	 */
	public void setCrowns(List<Crown> crowns) {
		this.crowns = crowns;
	}

}
