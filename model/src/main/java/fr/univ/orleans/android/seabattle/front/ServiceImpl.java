package fr.univ.orleans.android.seabattle.front;

import java.util.ArrayList;
import java.util.List;

import fr.univ.orleans.android.seabattle.model.Anyone.Player;

/**
 * Created by thibault on 15/12/16.
 */

public class ServiceImpl implements PlayerManagement {

    List<Player> players;

    public ServiceImpl(){
        players = new ArrayList<>();

    }

    @Override
    public Player addPlayer(String name, String username) {
        Player p = new Player(name,username);
        this.players.add(p);
        return p;
    }

    @Override
    public int getPlayersNumber() {
        return this.players.size();
    }

    public Player getPlayerByUsername (String username){
        for (Player p: players){
            if (p.getUsername().equals(username))
                return p;
        }
        return null;
    }


}
