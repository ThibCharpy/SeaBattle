package fr.univ.orleans.android.seabattle.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import fr.univ.orleans.android.seabattle.front.exceptions.IAPlayOutOfBoundException;
import fr.univ.orleans.android.seabattle.front.ServiceImpl;
import fr.univ.orleans.android.seabattle.front.exceptions.CellAlreadyShootException;
import fr.univ.orleans.android.seabattle.front.exceptions.PutNextToAnotherShipExeption;
import fr.univ.orleans.android.seabattle.front.exceptions.PutOverAnotherShipException;
import fr.univ.orleans.android.seabattle.front.exceptions.PutShipOutOfBattlefieldException;
import fr.univ.orleans.android.seabattle.front.exceptions.RotateNextToAnotherShip;
import fr.univ.orleans.android.seabattle.front.exceptions.RotateOutOfBattlefieldException;
import fr.univ.orleans.android.seabattle.front.exceptions.RotateOverAnotherShipException;
import fr.univ.orleans.android.seabattle.model.Battlefield;
import fr.univ.orleans.android.seabattle.model.Profil;
import fr.univ.orleans.android.seabattle.model.Ship;
import fr.univ.orleans.android.seabattle.front.utils.Pair;

/**
 * Created by thibault on 15/12/16.
 */

public class Controller implements Serializable {

    private ServiceImpl myFront;

    private Profil currentProfil;

    private static String ENNEMY = "ennemy";
    private static String PLAYER = "player";

    private static String EASY = "easy";
    private static String MEDIUM = "medium";

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

    public Profil getCurrentProfil() {
        return currentProfil;
    }

    public void setCurrentProfil(Profil currentProfil) {
        this.currentProfil = currentProfil;
    }

    public void resetProfil(){
        this.currentProfil.setWonGames(0);
    }

    public void setDifficulty(String difficulty){
        this.myFront.setDifficulty(difficulty);
    }

    public void initializeBattlefield(){
        this.myFront.initializeBattlefields();
    }

    public void initializeFleet(){
        this.myFront.initializeFleet();
    }

    public Map<Ship,Integer> getFleet(){
        return this.myFront.getFleet();
    }

    public List<Battlefield> getBattlefields(){
        return this.myFront.getBattlefields();
    }

    public void putShipOnBattlefield(int ship_size, int row, int col) throws PutShipOutOfBattlefieldException, PutOverAnotherShipException, PutNextToAnotherShipExeption {
        this.myFront.putShipOnBattlefield(ship_size,row,col);
    }

    public int getCellNature(int row, int col){
        return this.myFront.getCellNature(row,col);
    }

    public void rotateShip(int row, int col) throws RotateOverAnotherShipException, RotateOutOfBattlefieldException, RotateNextToAnotherShip {
        this.myFront.rotateShipOnBattlefield(row,col);
    }

    public void removeShip(int row, int col){
        this.myFront.removeShipOnBattlefield(row,col);
    }


    public void randomPlacementOnPlayerBattlefield(){
        this.myFront.randomPlacementOnBattlefield(PLAYER);
    }

    public void clearPlayerBattlefield(){
        this.myFront.clearPlayerBattlefield();
    }

    public Pair<Integer,Boolean> attackEnemyBattlefield(int row, int col) throws CellAlreadyShootException, IAPlayOutOfBoundException {
        return this.myFront.attackBattlefield(ENNEMY,row,col);
    }

    public void initializeEnemyBattlefiel(){
        this.myFront.randomPlacementOnBattlefield(ENNEMY);
    }

    public Pair<Pair<Integer,Integer>,Pair<Integer,Boolean>> iaAttackBattlefield(){
        return this.myFront.randomAttackPlayerBattlefield(MEDIUM);
    }

    public void loadBattlefields(String playerBattlefield, String enemyBattlefield){
        this.myFront.loadBattlefields(playerBattlefield,enemyBattlefield);
    }

    public void saveBattlefields() {
        List<Battlefield> battlefields = this.myFront.getBattlefields();
        //this.currentProfil.setEnemy(battlefields.get(0).save());
        //this.currentProfil.setPlayer(battlefields.get(0).save());
    }
}
