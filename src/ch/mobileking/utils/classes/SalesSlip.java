package ch.mobileking.utils.classes;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class SalesSlip implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("salespoint")
	private String salespoint;

	@SerializedName("scandate")
	private String scandate;
	
	@SerializedName("purchasedate")
	private String purchasedate;

	@SerializedName("isapproved")
	private Integer isapproved = 0;
	
	@SerializedName("rejectmessage")
	private String rejectmessage;
	
	@SerializedName("filename")
	private String filename;
	
	@SerializedName("isuploaded")
	private Boolean isuploaded = false;
	
	@SerializedName("totalparts")
	private Integer totalparts = 0;

	@SerializedName("imagelink")
	private String imagelink;
	
	@SerializedName("salesslipitems")
	private List<SalesSlipItem> salesslipitems;
	
	@SerializedName("part")
	private Integer part;
	
	@SerializedName("bitmapFile")
	private transient Bitmap bitmapFile;
	
	@SerializedName("simplefilename")
	private String simplefilename;
	
	public SalesSlip(String filename, Bitmap imageBitmap, String scandate, String simpleFileName, Integer part, Integer totalParts)
	{
		this.setFilename(filename);
		this.setScanDate(scandate);
		
		this.setSimpleFileName(simpleFileName);
		this.setPart(part);
		this.setTotalparts(totalParts);
		this.setIsapproved(1);
		this.setIsuploaded(false);
		this.setBitmapFile(imageBitmap);
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
	 * @return the purchasedate
	 */
	public String getPurchasedate() {
		return purchasedate;
	}

	/**
	 * @param purchasedate the purchasedate to set
	 */
	public void setPurchasedate(String purchasedate) {
		this.purchasedate = purchasedate;
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
	 * @return the rejectmessage
	 */
	public String getRejectmessage() {
		return rejectmessage;
	}

	/**
	 * @param rejectmessage the rejectmessage to set
	 */
	public void setRejectmessage(String rejectmessage) {
		this.rejectmessage = rejectmessage;
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
	 * @return the imageLink
	 */
	public String getImageLink() {
		return imagelink;
	}

	/**
	 * @param imageLink the imageLink to set
	 */
	public void setImageLink(String imageLink) {
		this.imagelink = imageLink;
	}

	/**
	 * @return the salesslipitems
	 */
	public List<SalesSlipItem> getSalesslipitems() {
		return salesslipitems;
	}

	/**
	 * @param salesslipitems the salesslipitems to set
	 */
	public void setSalesslipitems(List<SalesSlipItem> salesslipitems) {
		this.salesslipitems = salesslipitems;
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
		return simplefilename;
	}

	/**
	 * @param simpleFileName the simpleFileName to set
	 */
	public void setSimpleFileName(String simpleFileName) {
		this.simplefilename = simpleFileName;
	}

	/**
	 * @return the bitmapFile
	 */
	public Bitmap getBitmapFile() {
		return bitmapFile;
	}

	/**
	 * @param bitmapFile the bitmapFile to set
	 */
	public void setBitmapFile(Bitmap bitmapFile) {
		this.bitmapFile = bitmapFile;
	}


	
	
}
