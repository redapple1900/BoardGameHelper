package game.redapple1900.playerdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlayerSQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_PLAYERS = "players";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_WIN = "win";
  public static final String COLUMN_LOSE = "lose";
  public static final String COLUMN_DATE = "date";

  private static final String DATABASE_NAME = "players.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  /*	
   * "create table "
      + TABLE_COMMENTS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_COMMENT
      + " text not null);";
   */
  private static final String DATABASE_CREATE = "create table "
      + TABLE_PLAYERS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_NAME+" text not null, "+COLUMN_WIN+" integer not null, "+COLUMN_LOSE
      + " integer not null, "+COLUMN_DATE+" text not null);";

  public PlayerSQLiteHelper (Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(PlayerSQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
    onCreate(db);
  }

}