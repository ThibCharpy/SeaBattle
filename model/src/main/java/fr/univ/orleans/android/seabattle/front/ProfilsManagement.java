package fr.univ.orleans.android.seabattle.front;

import java.util.List;

import fr.univ.orleans.android.seabattle.model.Profil;

/**
 * Created by thibault on 15/12/16.
 */

public interface ProfilsManagement {

    public void initializeProfilsList();

    public void loadProfilsList(List<Profil> profils);

    public void addProfil(Profil profil);

    public void removeProfil(String profil_username);

    public void setProfilUsername(Profil p, String new_username);

    public int getProfilsNumber();

    public List<Profil> getProfilsList();

    public boolean usernameAlreadyExist(String username);

    public int getMaxNumberOfProfil();

}
