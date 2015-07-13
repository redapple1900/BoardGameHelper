package com.yuanwei.resistance.playerdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.yuanwei.resistance.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerDataSource {

  // Database fields
  private SQLiteDatabase database;
  private PlayerSQLiteHelper dbHelper;
  private String[] allColumns = { PlayerSQLiteHelper.COLUMN_ID,
      PlayerSQLiteHelper.COLUMN_NAME,PlayerSQLiteHelper.COLUMN_WIN,PlayerSQLiteHelper.COLUMN_LOSE,PlayerSQLiteHelper.COLUMN_DATE  };

  public PlayerDataSource(Context context) {
    dbHelper = new PlayerSQLiteHelper(context);
  }


public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Player createPlayer(String name,int win,int lose,String date) {
    ContentValues values = new ContentValues();
    values.put(PlayerSQLiteHelper.COLUMN_NAME, name);
    values.put(PlayerSQLiteHelper.COLUMN_WIN, win);
    values.put(PlayerSQLiteHelper.COLUMN_LOSE, lose);
    values.put(PlayerSQLiteHelper.COLUMN_DATE, date);
    long insertId = database.insert(PlayerSQLiteHelper.TABLE_PLAYERS, null,
        values);
    Cursor cursor = database.query(PlayerSQLiteHelper.TABLE_PLAYERS,
        allColumns, PlayerSQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Player newPlayer = cursorToPlayer(cursor);
    cursor.close();
    return newPlayer;
  }
  public void updatePlayer(Player player) {
	  ContentValues values = new ContentValues();
	    values.put(PlayerSQLiteHelper.COLUMN_NAME, player.getName());
	    values.put(PlayerSQLiteHelper.COLUMN_WIN, player.getWin());
	    values.put(PlayerSQLiteHelper.COLUMN_LOSE, player.getLose());
	    values.put(PlayerSQLiteHelper.COLUMN_DATE, player.getLastDate());
	   database.update(PlayerSQLiteHelper.TABLE_PLAYERS, values, PlayerSQLiteHelper.COLUMN_ID + " = " + player.getId(), null);
	 
  }
  
public Player selectPlayerById(long id){
	
	 //String[] select ={PlayerSQLiteHelper.COLUMN_ID};
	   Cursor cursor = database.query(PlayerSQLiteHelper.TABLE_PLAYERS,
			   allColumns, PlayerSQLiteHelper.COLUMN_ID + " = " + id, null, null, null, PlayerSQLiteHelper.COLUMN_DATE+" desc");
	   cursor.moveToFirst();	
	   Player newPlayer = cursorToPlayer(cursor);
	   cursor.close();
	return newPlayer;
}
public List<Player> selectPlayerByName(String name){
	 List<Player> Players = new ArrayList<Player>();
	  Cursor cursor = database.query(PlayerSQLiteHelper.TABLE_PLAYERS,
			   allColumns, PlayerSQLiteHelper.COLUMN_NAME + " = " + name, null, null, null, PlayerSQLiteHelper.COLUMN_DATE+" desc");
	   cursor.moveToFirst();	   
	   while (!cursor.isAfterLast()) {
		      Player player = cursorToPlayer(cursor);
		      Players.add(player);
		      cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    return Players;
}

  public void deletePlayer(Player Player) {
    long id = Player.getId();
    System.out.println("Player deleted with id: " + id);
    database.delete(PlayerSQLiteHelper.TABLE_PLAYERS, PlayerSQLiteHelper.COLUMN_ID
        + " = '" + id+"'", null);
  }
  public void deletePlayer(long Id) {
	    
	    System.out.println("Player deleted with id: " + Id);
	    database.delete(PlayerSQLiteHelper.TABLE_PLAYERS, PlayerSQLiteHelper.COLUMN_ID
	    		 + " = '" + Id+"'", null);
	  }
  
  public List<Player> getAllPlayersByDate() {
    List<Player> Players = new ArrayList<Player>();

    Cursor cursor = database.query(PlayerSQLiteHelper.TABLE_PLAYERS,
        allColumns, null, null, null, null, PlayerSQLiteHelper.COLUMN_DATE+" desc");

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Player player = cursorToPlayer(cursor);
      Players.add(player);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return Players;
  }
  public List<Player> getAllPlayers() {
	    List<Player> Players = new ArrayList<Player>();

	    Cursor cursor = database.query(PlayerSQLiteHelper.TABLE_PLAYERS,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Player Player = cursorToPlayer(cursor);
	      Players.add(Player);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return Players;
	  }

  private Player cursorToPlayer(Cursor cursor) {
    Player Player = new Player();
    Player.setId(cursor.getLong(0));
    Player.setName(cursor.getString(1));
    Player.setWin(cursor.getInt(2));
    Player.setLose(cursor.getInt(3));
    Player.setLastDate(cursor.getString(4));
    return Player;
  }
} 