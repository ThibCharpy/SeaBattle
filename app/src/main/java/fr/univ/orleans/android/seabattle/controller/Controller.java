package fr.univ.orleans.android.seabattle.controller;

import java.io.Serializable;
import java.util.List;

import fr.univ.orleans.android.seabattle.front.ServiceImpl;
import fr.univ.orleans.android.seabattle.model.Profil;

/**
 * Created by thibault on 15/12/16.
 */

public class Controller implements Serializable {
    private ServiceImpl myFront;

    public Controller(){
        this.myFront = new ServiceImpl();
    }

    public void initializeProfilsList(){
        this.myFront.initializeProfilsList();
    }

    public void addProfil(Profil profil){
        this.myFront.addProfil(profil);
    }

    public void removeProfil(String username){
        this.myFront.removeProfil(username);
    }

    public void setProfilUsername(Profil p, String new_username){
        this.myFront.setProfilUsername(p,new_username);
    }

    public Profil getProfilbyUsername(String username) {
        return this.myFront.getProfilByUsername(username);
    }

    public void loadProfilsList(List<Profil> profils){
        this.myFront.loadProfilsList(profils);
    }

    public List<Profil> getProfilsList(){
        return this.myFront.getProfilsList();
    }

    public boolean usernameAlreadyExist(String username){
        return this.myFront.usernameAlreadyExist(username);
    }

    public int getMaxNumberOfProfil(){
        return this.myFront.getMaxNumberOfProfil();
    }

}
