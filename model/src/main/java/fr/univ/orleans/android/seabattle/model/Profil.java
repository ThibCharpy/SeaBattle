package fr.univ.orleans.android.seabattle.model;

/**
 * Created by thibault on 15/12/16.
 */

public class Profil {

    private String username;
    private long id;
    private int wonGames;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getWonGames() { return wonGames; }

    public void setWonGames(int wonGames) { this.wonGames = wonGames; }

    @Override
    public String toString() {
        return "Profil: " +
                "username='" + username + '\'' +
                ", id=" + id +
                ", wonGames=" + wonGames +
                '.';
    }
}
