package ch.mobileking.utils.classes;

import java.util.Date;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class SalesSlipItem {

	@SerializedName("name")
	private String name;
	
	@SerializedName("producer")
	private String producer;

	@SerializedName("price")
	private String price;

	@SerializedName("quantity")
	private Integer quantity = 0;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the producer
	 */
	public String getProducer() {
		return producer;
	}

	/**
	 * @param producer the producer to set
	 */
	public void setProducer(String producer) {
		this.producer = producer;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	


	
	
}
