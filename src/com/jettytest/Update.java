package com.jettytest;

public class Update {
	String algorithm = "";
	boolean random = false;
	Object time;
	Object distance;
	Object limit;
	
	public void setAlgorithm(String algorithm){
		this.algorithm = algorithm;
	}
	
	public String getAlgorithm(){
		return algorithm;
	}
	
	public void setRandom(boolean random){
		this.random = random;
	}
	
	public boolean getRandom(){
		return random;
	}
	
	public void setTime(Object time){
		this.time = time;
	}
	
	public Object getTime(){
		return time;
	}
	
	public void setDistance(Object distance){
		this.distance = distance;
	}
	
	public Object getDistance(){
		return distance;
	}
	
	public void setLimit(Object limit){
		this.limit = limit;
	}
	
	public Object getLimit(){
		return limit;
	}
}
