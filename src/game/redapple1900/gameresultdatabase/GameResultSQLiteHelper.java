package game.redapple1900.gameresultdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GameResultSQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_COMMENTS = "gameresults";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NUM_PLAYER = "number";
  public static final String COLUMN_RESULT = "result";
  public static final String COLUMN_DATE = "date";

  private static final String DATABASE_NAME = "gameresults.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  /*	
   * "create table "
      + TABLE_COMMENTS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_COMMENT
      + " text not null);";
   */
  private static final String DATABASE_CREATE = "create table "
      + TABLE_COMMENTS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_NUM_PLAYER+" integer not null "+COLUMN_RESULT+" text not null "+COLUMN_DATE
      + " text not null);";

  public GameResultSQLiteHelper (Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(GameResultSQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
    onCreate(db);
  }

}

