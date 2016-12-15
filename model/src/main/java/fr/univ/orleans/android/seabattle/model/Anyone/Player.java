package fr.univ.orleans.android.seabattle.model.Anyone;

/**
 * Created by thibault on 15/12/16.
 */

public class Player extends Someone{
    private String username;
    private long id;

    private static int ids = 0;

    public Player(String name, String username) {
        super(name);
        this.username = username;
        this.id=ids;
        this.ids++;
    }

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
}
