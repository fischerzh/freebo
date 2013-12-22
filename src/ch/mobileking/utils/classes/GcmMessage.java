package ch.mobileking.utils.classes;

import java.util.Date;

import ch.mobileking.utils.Utils;

public class GcmMessage {
	
	private String uuid;
	
	private String content = "";
	
	private String title = "";
	
	private Date createDate;
	
	private Boolean read = false;
	
	private Date readDate;
	
	private Date syncDate;
	
	private Boolean isSynced = false;
	
	private String locationInfo = "";
	
	public GcmMessage(String content, String title, String uuid, String location)
	{
		this.content = content;
		this.title = title;
		if(uuid.isEmpty() || uuid == null)
		{
			this.uuid = Utils.getRandomMsgId();
		}
		else
		{
			this.uuid = uuid;
		}
		this.setLocationInfo(location);
		this.createDate = new Date();
		this.read = false;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the read
	 */
	public Boolean getRead() {
		return read;
	}

	/**
	 * @param read the read to set
	 */
	public void setRead(Boolean read) {
		this.read = read;
	}

	/**
	 * @return the readDate
	 */
	public Date getReadDate() {
		return readDate;
	}

	/**
	 * @param readDate the readDate to set
	 */
	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}

	/**
	 * @return the isSynced
	 */
	public Boolean getIsSynced() {
		return isSynced;
	}

	/**
	 * @param isSynced the isSynced to set
	 */
	public void setIsSynced(Boolean isSynced) {
		this.isSynced = isSynced;
	}

	/**
	 * @return the syncDate
	 */
	public Date getSyncDate() {
		return syncDate;
	}

	/**
	 * @param syncDate the syncDate to set
	 */
	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}

	/**
	 * @return the locationInfo
	 */
	public String getLocationInfo() {
		return locationInfo;
	}

	/**
	 * @param locationInfo the locationInfo to set
	 */
	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

}