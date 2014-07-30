package game.redapple1900.gameresultdatabase;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GameResultDataSource {

  // Database fields
  private SQLiteDatabase database;
  private GameResultSQLiteHelper dbHelper;
  private String[] allColumns = { GameResultSQLiteHelper.COLUMN_ID,
      GameResultSQLiteHelper.COLUMN_NUM_PLAYER,GameResultSQLiteHelper.COLUMN_RESULT ,GameResultSQLiteHelper.COLUMN_DATE  };

  public GameResultDataSource(Context context) {
    dbHelper = new GameResultSQLiteHelper(context);
  }


public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public GameResult createGameResult(int number,String result,String date) {
    ContentValues values = new ContentValues();
    values.put(GameResultSQLiteHelper.COLUMN_NUM_PLAYER, number);
    values.put(GameResultSQLiteHelper.COLUMN_RESULT, result);
    values.put(GameResultSQLiteHelper.COLUMN_DATE, date);
    long insertId = database.insert(GameResultSQLiteHelper.TABLE_COMMENTS, null,
        values);
    Cursor cursor = database.query(GameResultSQLiteHelper.TABLE_COMMENTS,
        allColumns, GameResultSQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    GameResult newGameResult = cursorToGameResult(cursor);
    cursor.close();
    return newGameResult;
  }

  public void deleteGameResult(GameResult GameResult) {
    long id = GameResult.getId();
    System.out.println("GameResult deleted with id: " + id);
    database.delete(GameResultSQLiteHelper.TABLE_COMMENTS, GameResultSQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<GameResult> getAllGameResults() {
    List<GameResult> GameResults = new ArrayList<GameResult>();

    Cursor cursor = database.query(GameResultSQLiteHelper.TABLE_COMMENTS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      GameResult GameResult = cursorToGameResult(cursor);
      GameResults.add(GameResult);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return GameResults;
  }

  private GameResult cursorToGameResult(Cursor cursor) {
    GameResult GameResult = new GameResult();
    GameResult.setId(cursor.getLong(0));
    GameResult.setPlayerNumber(cursor.getInt(1));
    GameResult.setResult(cursor.getString(2));
    GameResult.setDate(cursor.getString(3));
    return GameResult;
  }
} 