package fr.univ.orleans.android.seabattle.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thibault on 20/12/16.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_PROFILS = "profils";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_WONGAMES = "wongames";

    private static final String DATABASE_NAME = "profils.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PROFILS + "( " + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USERNAME + " text not null, "
            + COLUMN_WONGAMES + " integer not null);";

    public static final String TABLE_PROFILS_DROP =  "DROP TABLE IF EXISTS " + TABLE_PROFILS + ";";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_PROFILS_DROP);
        onCreate(db);
    }
}
