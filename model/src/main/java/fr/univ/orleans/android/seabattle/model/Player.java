package fr.univ.orleans.android.seabattle.model;

/**
 * Created by thibault on 15/12/16.
 */

public class Player extends Someone{
    private String username;
    private long id;

    public Player(){
        super();
    }

    public Player(String name, long id, String username) {
        super(name);
        this.username = username;
        this.id = id;
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
