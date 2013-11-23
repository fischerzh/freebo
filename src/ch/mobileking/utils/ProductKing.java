package ch.mobileking.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.os.Environment;
import android.os.Message;

import com.google.gson.annotations.SerializedName;

public class ProductKing implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@SerializedName("username")
	private String username = " ";
	
	@SerializedName("isactiveapp")
	private Boolean isactiveapp;
	
	@SerializedName("recommendations")
	private List<Products> recommendations;
	
	@SerializedName("products")
	private List<Products> products;
	
	@SerializedName("badges")
	private List<Badge> badges;
	
	@SerializedName("leaderboard")
	private List<Leaderboard> leaderboard;
	
	@SerializedName("status")
	private String status = " ";

	@SerializedName("exception")
	private String exception = " ";
	
	private static List<Products> staticProducts;
	
	private static List<Products> recommenderProducts;
	
	private static List<Badge> staticBadges;
	
	private static List<GcmMessage> notifications;
	
	private static List<GcmMessage> userLogData;
	
	private static List<Leaderboard> staticLeaderboard;
	
	private static Boolean isActive = false;
	
	private static ProductKing singletonProdKing;
	
	public static ProductKing getInstance()
	{
		if(singletonProdKing == null)
		{
			singletonProdKing = new ProductKing();
			initNotifications();
			initUserLogData();
		}
		return singletonProdKing;
	}
	
	/**
	 * @return the products
	 */
	public List<Products> getProducts() {
		return products;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(List<Products> products) {
		this.products = products;
	}

	/**
	 * @return the badges
	 */
	public List<Badge> getBadges() {
		return badges;
	}

	/**
	 * @param badges the badges to set
	 */
	public void setBadges(List<Badge> badges) {
		this.badges = badges;
	}

	/**
	 * @return the leaderboard
	 */
	public List<Leaderboard> getLeaderboard() {
		return leaderboard;
	}

	/**
	 * @param leaderboard the leaderboard to set
	 */
	public void setLeaderboard(List<Leaderboard> leaderboard) {
		this.leaderboard = leaderboard;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the staticProducts
	 */
	public static List<Products> getStaticProducts() {
		return staticProducts;
	}

	/**
	 * @param staticProducts the staticProducts to set
	 */
	public static void setStaticProducts(List<Products> staticProducts) {
		ProductKing.staticProducts = staticProducts;
	}

	/**
	 * @return the staticBadges
	 */
	public static List<Badge> getStaticBadges() {
		return staticBadges;
	}

	/**
	 * @param staticBadges the staticBadges to set
	 */
	public static void setStaticBadges(List<Badge> staticBadges) {
		ProductKing.staticBadges = staticBadges;
	}

	/**
	 * @return the staticLeaderboard
	 */
	public static List<Leaderboard> getStaticLeaderboard() {
		return staticLeaderboard;
	}

	/**
	 * @param staticLeaderboard the staticLeaderboard to set
	 */
	public static void setStaticLeaderboard(List<Leaderboard> staticLeaderboard) {
		ProductKing.staticLeaderboard = staticLeaderboard;
	}
	
	public static List<Crown> getCrowns(String locationName)
	{
		
		HashMap<String, Crown> storeCrownList = new HashMap<String, Crown>();

		for(Products prod : ProductKing.staticProducts)
		{
			for (Crown cr : prod.getCrowns())
			{
				System.out.println("Crown salespoint: " + cr.getSalespoint());
				System.out.println("Location input name: " + locationName);
				if(cr.getSalespoint().toLowerCase().equalsIgnoreCase(locationName))
					storeCrownList.put(cr.getSalespoint(), cr);
			}
		}
		
		List<Crown> crownList = new ArrayList<Crown>(storeCrownList.values());
		return crownList;

	}
	
	public static List<Location> getLocations()
	{
		List<Location> locationList = new ArrayList<Location>();
		for(Products prod : staticProducts)
		{
//			prod.getPoints();
			for (Crown cr : prod.getCrowns())
			{
				Location loc = new Location(cr.getSalespoint());
				locationList.add(loc);
			}
		}
		
		return locationList;
	}
	
	/**
	 * @return the recommenderProducts
	 */
	public static List<Products> getRecommenderProducts() {
		if(recommenderProducts==null)
			recommenderProducts = new ArrayList<Products>();
		return recommenderProducts;
	}

	/**
	 * @param recommenderProducts the recommenderProducts to set
	 */
	public static void setRecommenderProducts(List<Products> recommenderProducts) {
		ProductKing.recommenderProducts = recommenderProducts;
	}

	/**
	 * @return the recommendations
	 */
	public List<Products> getRecommendations() {
		return recommendations;
	}

	/**
	 * @param recommendations the recommendations to set
	 */
	public void setRecommendations(List<Products> recommendations) {
		this.recommendations = recommendations;
	}

	/**
	 * @return the notifications
	 */
	public static List<GcmMessage> getNotifications() {
		return notifications;
	}

	/**
	 * @param notifications the notifications to set
	 */
	public static void initNotifications() {
		ProductKing.notifications = new ArrayList<GcmMessage>();
	}
	
	public static void addNotificationMsg(String msg, String title, String uuid)
	{
		System.out.println("Adding GCM-Notification-Message " +msg);
		GcmMessage gcmMsg = new GcmMessage(msg, title, uuid);
		getNotifications().add(gcmMsg);
	}
	
	public static GcmMessage getMessageById(String uuid)
	{
		GcmMessage returnMsg = null;
		Boolean found = false;
		for(GcmMessage msg : ProductKing.notifications)
		{
			System.out.println("msgId: " + msg.getUuid() +"messageCreateDate:" +msg.getCreateDate()+ "messageReadDate: " +msg.getReadDate());
			if(msg.getUuid().contentEquals(uuid) && !msg.getRead())
			{
				returnMsg = msg;
				ProductKing.notifications.get(ProductKing.notifications.indexOf(msg)).setRead(true);
				ProductKing.notifications.get(ProductKing.notifications.indexOf(msg)).setReadDate(new Date());
				found = true;
				break;
			}
		}

		return returnMsg;
	}
	
	public static void initUserLogData() {
		ProductKing.userLogData = new ArrayList<GcmMessage>();
	}
	
	/**
	 * @return the userLogData
	 */
	public static List<GcmMessage> getUserLogData() {
		return userLogData;
	}
	
	/**
	 * @param Set the userLogData
	 */
	public static void setUserLogData(List<GcmMessage> logData) {
		ProductKing.userLogData = logData;
	}

	public static void addLogMsg(String msg)
	{
		GcmMessage gcmMsg = new GcmMessage(msg, "UserActivityLog", "");
		getUserLogData().add(gcmMsg);
		System.out.println("Add Log Message: " +msg +  gcmMsg.getCreateDate());

	}
	
	/**
	 * @return the isactiveapp
	 */
	public Boolean getIsactiveapp() {
		return isactiveapp;
	}

	/**
	 * @param isactiveapp the isactiveapp to set
	 */
	public void setIsactiveapp(Boolean isactiveapp) {
		this.isactiveapp = isactiveapp;
	}
	
	/**
	 * @return the isActive
	 */
	public static Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public static void setIsActive(Boolean isActive) {
		ProductKing.isActive = isActive;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
	}


	
}




	
