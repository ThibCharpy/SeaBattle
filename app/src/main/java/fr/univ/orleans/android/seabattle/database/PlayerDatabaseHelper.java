package fr.univ.orleans.android.seabattle.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thibault on 19/12/16.
 */

public class PlayerDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "players";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";

    private static final String DATABASE_NAME = "players.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_USERNAME
            + " text not null);";

    public PlayerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
