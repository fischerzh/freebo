package ch.mobileking.utils;

import com.google.gson.annotations.SerializedName;

public class Leaderboard {
	
	@SerializedName("rank")
	private int rank;

	@SerializedName("points")
	private int points;

	@SerializedName("username")
	private String username;
	
	@SerializedName("avatarid")
	private String avatarId;

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
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
	public int getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
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
	public String getAvatarId() {
		return avatarId;
	}

	/**
	 * @param avatarId the avatarId to set
	 */
	public void setAvatarId(String avatarId) {
		this.avatarId = avatarId;
	}



}
