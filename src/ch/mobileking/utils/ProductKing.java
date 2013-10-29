package ch.mobileking.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

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
	
	private static List<Products> staticProducts;
	
	private static List<Products> recommenderProducts;
	
	private static List<Badge> staticBadges;
	
	private static List<String> notifications;
	
	private static Boolean isActive = false;
	
	public static void initRecommendProducts()
	{
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages/";
		
		Products pr1 = new Products();
		pr1.setName("Zweifel Chips");
		pr1.setImagepath(path+"1.png");
		pr1.setId(1);

		Products pr2 = new Products();
		pr2.setName("Nestea Peach");
		pr2.setImagepath(path+"2.png");
		pr2.setId(2);
		
		Products pr3 = new Products();
		pr3.setName("Rivella Blau");
		pr3.setImagepath(path+"2.png");
		pr3.setId(3);
		
		Products pr4 = new Products();
		pr4.setName("Studentenfutter");
		pr4.setImagepath(path+"3.png");
		pr4.setId(2);

		
		recommenderProducts = new ArrayList<Products>();
		
		recommenderProducts.add(pr1);
		recommenderProducts.add(pr2);
		recommenderProducts.add(pr3);
		recommenderProducts.add(pr4);
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
	 * @return the recommenderProducts
	 */
	public static List<Products> getRecommenderProducts() {
		if(recommenderProducts==null)
			initRecommendProducts();
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
	public static List<String> getNotifications() {
		return notifications;
	}

	/**
	 * @param notifications the notifications to set
	 */
	public static void initNotifications() {
		ProductKing.notifications = new ArrayList<String>();
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


	
}




	
