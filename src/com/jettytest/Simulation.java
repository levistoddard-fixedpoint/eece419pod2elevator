package com.jettytest;

public class Simulation {
	
	Integer id = 0;
	String name = "";
	String template = "";
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}	
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setTemplate(String template){
		this.template = template;
	}
	
	public String getTemplate(){
		return template;
	}
}
