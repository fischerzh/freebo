package ch.mobileking.utils.classes;

import com.google.gson.annotations.SerializedName;

public class Leaderboard {
	
	@SerializedName("userid")
	private Integer userid;
	
	@SerializedName("rank")
	private int rank;

	@SerializedName("points")
	private Float points;

	@SerializedName("username")
	private String username;
	
	@SerializedName("avatarid")
	private Integer avatarId;

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @return the userid
	 */
	public Integer getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return the points
	 */
	public Float getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Float points) {
		this.points = points;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the avatarId
	 */
	public Integer getAvatarId() {
		return avatarId;
	}

	/**
	 * @param avatarId the avatarId to set
	 */
	public void setAvatarId(Integer avatarId) {
		this.avatarId = avatarId;
	}



}
