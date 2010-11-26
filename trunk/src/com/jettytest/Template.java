package com.jettytest;

public class Template{
	String name = "";
	int floors;
	int elevators;
	String algorithm = "";
	boolean random = false;
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setFloors(int floors){
		this.floors = floors;
	}
	
	public int getFloors(){
		return floors;
	}
	
	public void setElevators(int elevators){
		this.elevators = elevators;
	}
	
	public int getElevators(){
		return elevators;
	}
	
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
}
