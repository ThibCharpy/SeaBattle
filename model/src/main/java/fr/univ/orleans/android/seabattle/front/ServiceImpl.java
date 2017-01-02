package fr.univ.orleans.android.seabattle.front;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.univ.orleans.android.seabattle.model.Profil;

/**
 * Created by thibault on 15/12/16.
 */

public class ServiceImpl implements ProfilsManagement {

    private static int MAX_NUMBER_OF_PROFIL = 5;

    List<Profil> profils;

    public ServiceImpl(){}

    @Override
    public void initializeProfilsList() {
        this.profils = new ArrayList<>();
    }

    @Override
    public void loadProfilsList(List<Profil> profils) {
        this.profils = profils;
    }

    @Override
    public void addProfil(Profil profil) {
        profils.add(profil);
    }

    @Override
    public void removeProfil(String profil_username) {
        Iterator<Profil> iterator = this.profils.iterator();
        while (iterator.hasNext()) {
            Profil p = iterator.next();
            if (p.getUsername().equals(profil_username)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void setProfilUsername(Profil p, String new_username) {
        for (Profil profil: this.profils) {
            if (profil.getUsername().equals(p.getUsername())){
                profil.setUsername(new_username);
            }
        }
    }

    @Override
    public int getProfilsNumber() {
        return this.profils.size();
    }

    @Override
    public List<Profil> getProfilsList() {
        return this.profils;
    }

    @Override
    public boolean usernameAlreadyExist(String username) {
        for (Profil p : this.profils){
            if (p.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMaxNumberOfProfil() {
        return MAX_NUMBER_OF_PROFIL;
    }

    public Profil getProfilByUsername (String username){
        for (Profil p: profils){
            if (p.getUsername().equals(username))
                return p;
        }
        return null;
    }


}
