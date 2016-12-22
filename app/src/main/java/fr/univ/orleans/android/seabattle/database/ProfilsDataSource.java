package fr.univ.orleans.android.seabattle.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fr.univ.orleans.android.seabattle.model.Profil;

/**
 * Created by thibault on 20/12/16.
 */

public class ProfilsDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_USERNAME, MySQLiteHelper.COLUMN_WONGAMES };

    public ProfilsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Profil createProfil(String username) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USERNAME, username);
        values.put(MySQLiteHelper.COLUMN_WONGAMES, 0);
        long insertId = database.insert(MySQLiteHelper.TABLE_PROFILS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFILS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Profil newProfil = cursorToProfil(cursor);
        cursor.close();
        return newProfil;
    }

    public List<Profil> getAllProfils() {
        List<Profil> profils = new ArrayList<Profil>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFILS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Profil profil = cursorToProfil(cursor);
            profils.add(profil);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return profils;
    }

    private Profil cursorToProfil(Cursor cursor) {
        Profil profil = new Profil();
        profil.setId(cursor.getLong(0));
        profil.setUsername(cursor.getString(1));
        profil.setWonGames(Integer.valueOf(cursor.getString(2)));
        return profil;
    }

}
