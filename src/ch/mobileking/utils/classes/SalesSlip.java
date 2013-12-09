package ch.mobileking.utils.classes;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class SalesSlip {

	@SerializedName("salespoint")
	private String salespoint;

	@SerializedName("scandate")
	private String scandate;

	@SerializedName("isapproved")
	private Integer isapproved = 0;
	
	@SerializedName("filename")
	private String filename;
	
	@SerializedName("isuploaded")
	private Boolean isuploaded = false;
	
	@SerializedName("totalparts")
	private Integer totalparts = 0;
	
	private Integer part;
	
	private String simpleFileName;
	
	public SalesSlip(String filename, String scandate, String simpleFileName, Integer part, Integer totalParts)
	{
		this.setFilename(filename);
		this.setScanDate(scandate);
		
		this.setSimpleFileName(simpleFileName);
		this.setPart(part);
		this.setTotalparts(totalParts);
		this.setIsapproved(1);
		this.setIsuploaded(false);
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

	/**
	 * @return the scanDate
	 */
	public String getScanDate() {
		return scandate;
	}

	/**
	 * @param scanDate the scanDate to set
	 */
	public void setScanDate(String scanDate) {
		this.scandate = scanDate;
	}

	/**
	 * @return the isapproved
	 */
	public Integer getIsapproved() {
		return isapproved;
	}

	/**
	 * @param isapproved the isapproved to set
	 */
	public void setIsapproved(Integer isapproved) {
		this.isapproved = isapproved;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the isuploaded
	 */
	public Boolean getIsuploaded() {
		return isuploaded;
	}

	/**
	 * @param isuploaded the isuploaded to set
	 */
	public void setIsuploaded(Boolean isuploaded) {
		this.isuploaded = isuploaded;
	}

	/**
	 * @return the totalparts
	 */
	public Integer getTotalparts() {
		return totalparts;
	}

	/**
	 * @param totalparts the totalparts to set
	 */
	public void setTotalparts(Integer totalparts) {
		this.totalparts = totalparts;
	}

	/**
	 * @return the part
	 */
	public Integer getPart() {
		return part;
	}

	/**
	 * @param part the part to set
	 */
	public void setPart(Integer part) {
		this.part = part;
	}

	/**
	 * @return the simpleFileName
	 */
	public String getSimpleFileName() {
		return simpleFileName;
	}

	/**
	 * @param simpleFileName the simpleFileName to set
	 */
	public void setSimpleFileName(String simpleFileName) {
		this.simpleFileName = simpleFileName;
	}


	
	
}
