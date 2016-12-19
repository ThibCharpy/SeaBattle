package fr.univ.orleans.android.seabattle.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fr.univ.orleans.android.seabattle.model.Player;

/**
 * Created by thibault on 19/12/16.
 */

public class PlayersDataSource {
    // Database fields
    private SQLiteDatabase database;
    private PlayerDatabaseHelper dbHelper;
    private String[] allColumns = { PlayerDatabaseHelper.COLUMN_ID,
            PlayerDatabaseHelper.COLUMN_USERNAME };

    public PlayersDataSource(Context context) {
        dbHelper = new PlayerDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Player createPlayer(String name, String username) {
        ContentValues values = new ContentValues();
        values.put(PlayerDatabaseHelper.COLUMN_USERNAME , username);
        long insertId = database.insert(PlayerDatabaseHelper.TABLE_NAME, null,
                values);
        Cursor cursor = database.query(PlayerDatabaseHelper.TABLE_NAME,
                allColumns, PlayerDatabaseHelper.COLUMN_USERNAME + " = " + insertId, null,
                null, null, null);
        Player player = new Player(name,insertId,username);
        cursor.moveToFirst();
        cursor.close();
        return player;
    }

    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<Player>();

        Cursor cursor = database.query(PlayerDatabaseHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Player player = cursorToPlayer(cursor);
            players.add(player);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return players;
    }

    private Player cursorToPlayer(Cursor cursor) {
        Player player = new Player();
        player.setId(cursor.getLong(0));
        player.setUsername(cursor.getString(1));
        return player;
    }
}
