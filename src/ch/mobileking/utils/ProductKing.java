package ch.mobileking.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
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
	
	private static List<Leaderboard> staticLeaderboard;
	
	private static Boolean isActive = false;
	
	private static Context cont;
	
	private static ProductKing singletonProdKing;
	
	public static ProductKing getInstance()
	{

		if(singletonProdKing == null)
		{
			System.out.println("Loading JSON from Local!");
			singletonProdKing = Utils.getProductKingFromLocal();
		}
		return singletonProdKing;
	}
	
	/**
	 * @return the cont
	 */
	public static Context getContext() {
		return cont;
	}

	/**
	 * @param cont the cont to set
	 */
	public static void setContext(Context cont) {
		ProductKing.cont = cont;
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




	
