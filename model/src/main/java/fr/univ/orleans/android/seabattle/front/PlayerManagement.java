package fr.univ.orleans.android.seabattle.front;

import fr.univ.orleans.android.seabattle.model.Player;

/**
 * Created by thibault on 15/12/16.
 */

public interface PlayerManagement {

    public Player addPlayer(String name, String username);

    public int getPlayersNumber();
}
