package fr.univ.orleans.android.seabattle.controller;

import fr.univ.orleans.android.seabattle.front.ServiceImpl;
import fr.univ.orleans.android.seabattle.model.Anyone.Player;

/**
 * Created by thibault on 15/12/16.
 */

public class Controller {
    private ServiceImpl myFront;

    public Controller(){
        this.myFront = new ServiceImpl();
    }

    public Player addPlayer(String name, String username){
        return this.myFront.addPlayer(name,username);
    }

    public Player getPlayerbyUsername(String username) {
        return this.myFront.getPlayerByUsername(username);
    }


}
