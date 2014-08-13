package com.yuanwei.resistance.gameresultdatabase;

public class GameResult {
	  private long id;
	  private int num_player;
	  private String result;
	  private String Date;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }
	  public void setPlayerNumber(int Num){
		  this.num_player=Num;
	  }
	  public int getPlayerNumber(){
		  return this.num_player;
	  }
	  public String getResult(){
		  return result;
	  }
	  public void setResult(String result){
		  this.result=result;
	  }

	  public String getDate() {
	    return Date;
	  }

	  public void setDate(String Date) {
	    this.Date =Date;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return num_player+result+Date;
	  }
	} 
