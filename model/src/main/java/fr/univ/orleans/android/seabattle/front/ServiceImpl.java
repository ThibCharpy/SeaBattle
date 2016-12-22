package fr.univ.orleans.android.seabattle.front;

import java.util.ArrayList;
import java.util.List;

import fr.univ.orleans.android.seabattle.model.Profil;

/**
 * Created by thibault on 15/12/16.
 */

public class ServiceImpl implements ProfilsManagement {

    List<Profil> profils;

    public ServiceImpl(){
        profils = new ArrayList<>();
    }

    @Override
    public void addProfil(Profil profil) {
        profils.add(profil);
    }

    @Override
    public int getProfilsNumber() {
        return this.profils.size();
    }

    public Profil getProfilByUsername (String username){
        for (Profil p: profils){
            if (p.getUsername().equals(username))
                return p;
        }
        return null;
    }


}
