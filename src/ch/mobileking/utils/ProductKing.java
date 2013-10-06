package ch.mobileking.utils;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ProductKing implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@SerializedName("username")
	private String username = " ";

	@SerializedName("products")
	private List<Products> products;
	
	private static List<Products> staticProducts;
	
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the staticProducts
	 */
	public static List<Products> getStaticProducts() {
		if(staticProducts.size()==0)
		{
			Products prod = new Products();
			prod.setName("Dein Lieblingsprodukt");
			prod.setOptin(true);
			prod.setProducer("Dein Lieblingshersteller");
			prod.setUserrank(1);
			staticProducts.add(prod);
		}
		return staticProducts;
	}

	/**
	 * @param staticProducts the staticProducts to set
	 */
	public static void setStaticProducts(List<Products> staticProducts) {
		ProductKing.staticProducts = staticProducts;
	}

	
}




	
