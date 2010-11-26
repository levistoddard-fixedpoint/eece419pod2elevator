package com.jettytest; 

public class Passenger {
      int time;
      int pfloor;
      int dfloor;
      int constraint; 

      public void setTime(int time){
            this.time = time;
      } 
      
      public int getTime(){
            return time;
      }
      
      public void setPfloor(int pfloor){
            this.pfloor = pfloor;
      }
      
      public int getPfloor(){
            return pfloor;
      }
      
      public void setDfloor(int dfloor){
            this.dfloor = dfloor;
      }
      
      public int getDfloor(){
            return dfloor;
      }
      
      public void setConstraint(int constraint){
            this.constraint = constraint;
      }
      
      public int getConstraint(){
            return constraint;
      }
}
