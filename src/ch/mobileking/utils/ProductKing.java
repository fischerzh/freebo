package ch.mobileking.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.Message;

import ch.mobileking.utils.classes.Badge;
import ch.mobileking.utils.classes.Crown;
import ch.mobileking.utils.classes.Leaderboard;
import ch.mobileking.utils.classes.Products;
import ch.mobileking.utils.classes.SalesSlip;

import com.google.gson.annotations.SerializedName;

public class ProductKing implements Serializable{
	
	private static final long serialVersionUID = 1L;


	private static ProductKing singletonProdKing;

	
	@SerializedName("username")
	private String username = " ";
	
	@SerializedName("isactiveapp")
	private Boolean isactiveapp;
	
	@SerializedName("avatarid")
	private Integer avatarId;
	
	@SerializedName("email")
	private String email = " ";
		
	@SerializedName("recommendations")
	private List<Products> recommendations;
	
	@SerializedName("products")
	private List<Products> products;
	
	@SerializedName("badges")
	private List<Badge> badges;
	
	@SerializedName("leaderboard")
	private List<Leaderboard> leaderboard;
	
	@SerializedName("salesslips")
	private List<SalesSlip> salesslips;
	
	@SerializedName("status")
	private String status = " ";

	@SerializedName("exception")
	private String exception = " ";
	
	private static List<Products> staticProducts;
	
	private static List<Products> recommenderProducts;
	
	private static List<Badge> staticBadges;
	
	private static List<Leaderboard> staticLeaderboard;
	
	private static List<SalesSlip> staticSalesSlips;
	
	private static Boolean isActive = false;
	
	private static List<SalesSlip> staticSalesSlipsParts;
	
	public static ProductKing getInstance()
	{	

		if(singletonProdKing == null)
		{
			System.out.println("Loading JSON from Local!");
			singletonProdKing = Utils.getJsonResultLocal();
			
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
	 * @return the salesslips
	 */
	public List<SalesSlip> getSalesslips() {
		return salesslips;
	}

	/**
	 * @param salesslips the salesslips to set
	 */
	public void setSalesslips(List<SalesSlip> salesslips) {
		this.salesslips = salesslips;
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

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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

	public static List<SalesSlip> getStaticSalesSlips() {
		if(staticSalesSlips==null)
			staticSalesSlips = new ArrayList<SalesSlip>();
		return staticSalesSlips;
	}

	public static void setStaticSalesSlips(List<SalesSlip> salesSlipList)
	{
		ProductKing.staticSalesSlips = salesSlipList;
	}
	
	public static List<SalesSlip> getSalesSlipsParts()
	{
		if(staticSalesSlipsParts==null)
			staticSalesSlipsParts = new ArrayList<SalesSlip>();
		return ProductKing.staticSalesSlipsParts;
	}
	
}




	
