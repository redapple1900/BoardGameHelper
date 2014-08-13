package com.yuanwei.resistance.playerdatabase;

public class Player {
	  private long id;
	  private String name;
	  private int win;
	  private int lose;
	  private String last_date;

	  public long getId() {
	    return id;
	  }
	  public void setId(long id) {
	    this.id = id;
	  }
	  public String getName(){
		  return name;
	  }
	  public void setName(String name){
		  this.name=name;
	  }
	  public int getWin() {
		    return win;
		  }
		  public void setWin(int win) {
		    this.win = win;
		  }
		  public int getLose() {
			    return lose;
			  }
			  public void setLose(int lose) {
			    this.lose = lose;
			  }
			  public String getLastDate() {
				    return last_date;
				  }
				  public void setLastDate(String date) {
				    this.last_date = date;
				  }
	

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return name;
	  }
	} 