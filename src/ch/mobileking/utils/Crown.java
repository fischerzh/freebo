package ch.mobileking.utils;

import com.google.gson.annotations.SerializedName;

public class Crown {
	
	@SerializedName("rank")
	private int rank;

	@SerializedName("crownstatus")
	private int crownstatus;

	@SerializedName("salespoint")
	private String salespoint;

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
	 * @return the crownstatus
	 */
	public int getCrownstatus() {
		return crownstatus;
	}

	/**
	 * @param crownstatus the crownstatus to set
	 */
	public void setCrownstatus(int crownstatus) {
		this.crownstatus = crownstatus;
	}

	/**
	 * @return the salespoint
	 */
	public String getSalespoint() {
		return salespoint;
	}

	/**
	 * @param salespoint the salespoint to set
	 */
	public void setSalespoint(String salespoint) {
		this.salespoint = salespoint;
	}

}
