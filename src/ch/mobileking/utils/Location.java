package ch.mobileking.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Location {

	private String name;
	
	private int pointsCollected;
	
	private int goldCrowns;
	private int silverCrowns;
	private int bronceCrowns;
	
	
	public Location(String name)
	{
		this.name = name;
	}

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
	 * @return the pointsCollected
	 */
	public int getPointsCollected() {
		return pointsCollected;
	}

	/**
	 * @param pointsCollected the pointsCollected to set
	 */
	public void setPointsCollected(int pointsCollected) {
		this.pointsCollected = pointsCollected;
	}

//	
//	public List<Crown> getCrowns(String locationName)
//	{
//		
//		HashMap<String, Crown> storeCrownList = new HashMap<String, Crown>();
//		
//
//		for(Products prod : this.products)
//		{
//			for (Crown cr : prod.getCrowns())
//			{
//				if(cr.getSalespoint().toLowerCase().contains(locationName))
//					storeCrownList.put(cr.getSalespoint(), cr);
//			}
//		}
//		
//		List<Crown> crownList = new ArrayList<Crown>(storeCrownList.values());
//		return crownList;
//
//	}
	

}
