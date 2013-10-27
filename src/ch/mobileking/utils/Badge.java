package ch.mobileking.utils;

import com.google.gson.annotations.SerializedName;

public class Badge {
	
	@SerializedName("name")
	private String name;

	@SerializedName("group")
	private String group;

	@SerializedName("achieved")
	private Boolean achieved;

	@SerializedName("newachieved")
	private Boolean newachieved;

	@SerializedName("achievementdate")
	private Boolean achievementdate;
	
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
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the achieved
	 */
	public Boolean getAchieved() {
		return achieved;
	}

	/**
	 * @param achieved the achieved to set
	 */
	public void setAchieved(Boolean achieved) {
		this.achieved = achieved;
	}

	/**
	 * @return the newachieved
	 */
	public Boolean getNewachieved() {
		return newachieved;
	}

	/**
	 * @param newachieved the newachieved to set
	 */
	public void setNewachieved(Boolean newachieved) {
		this.newachieved = newachieved;
	}

	/**
	 * @return the achievementdate
	 */
	public Boolean getAchievementdate() {
		return achievementdate;
	}

	/**
	 * @param achievementdate the achievementdate to set
	 */
	public void setAchievementdate(Boolean achievementdate) {
		this.achievementdate = achievementdate;
	}

}
