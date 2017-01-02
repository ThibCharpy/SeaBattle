package fr.univ.orleans.android.seabattle.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fr.univ.orleans.android.seabattle.model.Profil;

import static android.R.attr.dashGap;
import static android.R.attr.id;

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

    public void createProfil(Profil profil) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ID,profil.getId());
        values.put(MySQLiteHelper.COLUMN_USERNAME, profil.getUsername());
        values.put(MySQLiteHelper.COLUMN_WONGAMES, profil.getWonGames());

        long rowID = database.insert(MySQLiteHelper.TABLE_PROFILS,null,values);
        System.out.println("insert row "+rowID);

    }

    public void deleteProfil(Profil profil) {
        long id = profil.getId();
        database.delete(MySQLiteHelper.TABLE_PROFILS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void updateProfil(String username, long id, int wonGames) {
        ContentValues data = new ContentValues();
        data.put(MySQLiteHelper.COLUMN_USERNAME, username);
        data.put(MySQLiteHelper.COLUMN_WONGAMES, wonGames);
        database.update(MySQLiteHelper.TABLE_PROFILS, data, "_id="+id, null);
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

    public void clearTable () {
        database.delete(MySQLiteHelper.TABLE_PROFILS,null,null);
    }

    private Profil cursorToProfil(Cursor cursor) {
        Profil profil = new Profil();
        profil.setId(cursor.getLong(0));
        profil.setUsername(cursor.getString(1));
        profil.setWonGames(Integer.valueOf(cursor.getString(2)));
        return profil;
    }

    public void addAllProfils(Context context, List<Profil> profils){
        for (Profil profil : profils){
            createProfil(profil);
        }
    }

}
