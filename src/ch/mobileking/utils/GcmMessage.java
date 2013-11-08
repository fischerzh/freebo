package ch.mobileking.utils;

import java.util.Date;

public class GcmMessage {
	
	private String uuid;
	
	private String content = "";
	
	private String title = "";
	
	private Date createDate;
	
	private Boolean read = false;
	
	private Date readDate;
	
	public GcmMessage(String content, String title, String uuid)
	{
		this.content = content;
		this.title = title;
		this.uuid = uuid;
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

}
