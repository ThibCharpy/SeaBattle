package fr.univ.orleans.android.seabattle.front;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import fr.univ.orleans.android.seabattle.front.exceptions.CellAlreadyShootException;
import fr.univ.orleans.android.seabattle.front.exceptions.IAPlayOutOfBoundException;
import fr.univ.orleans.android.seabattle.front.exceptions.PutNextToAnotherShipExeption;
import fr.univ.orleans.android.seabattle.front.exceptions.PutOverAnotherShipException;
import fr.univ.orleans.android.seabattle.front.exceptions.PutShipOutOfBattlefieldException;
import fr.univ.orleans.android.seabattle.front.exceptions.RotateNextToAnotherShip;
import fr.univ.orleans.android.seabattle.front.exceptions.RotateOutOfBattlefieldException;
import fr.univ.orleans.android.seabattle.front.exceptions.RotateOverAnotherShipException;
import fr.univ.orleans.android.seabattle.front.utils.Pair;
import fr.univ.orleans.android.seabattle.model.Battlefield;
import fr.univ.orleans.android.seabattle.model.Cell;
import fr.univ.orleans.android.seabattle.model.EnemyBattlefield;
import fr.univ.orleans.android.seabattle.model.PlayerBattlefield;
import fr.univ.orleans.android.seabattle.model.Profil;
import fr.univ.orleans.android.seabattle.model.Ship;
import fr.univ.orleans.android.seabattle.model.ShipPart;
import fr.univ.orleans.android.seabattle.model.Water;

/**
 * Created by thibault on 15/12/16.
 */

public class ServiceImpl implements ProfilsManagement, GameManagement, SettingsManagement, Serializable {

    private static int MAX_NUMBER_OF_PROFIL = 5;

    private static int NUMBER_OF_SHIP = 5;
    private static int NUMBER_OF_SHIP_SIZE_5 = 1;
    private static int NUMBER_OF_SHIP_SIZE_4 = 1;
    private static int NUMBER_OF_SHIP_SIZE_3 = 2;
    private static int NUMBER_OF_SHIP_SIZE_2 = 1;

    private static String PLAYER = "player";
    private static String EASY = "easy";
    private static String MEDIUM = "medium";

    private static String DIFFICULTY = EASY;

    List<Profil> profils;

    private Battlefield enemyBattlefield;
    private Battlefield playerBattlefield;
    List<Battlefield> battlefields;

    Map<Ship,Integer> fleet;

    private static Stack<Pair<Integer,Integer>> shotStack;
    private static String shotOrientation;
    private static Pair<Integer,Integer> previousAttack;
    private static boolean switchAttack;

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

    private Map<Ship,Integer> buildFleet() {
        Map<Ship,Integer> m = new HashMap<>();
        m.put(new Ship(5,Ship.HORIZONTAL),NUMBER_OF_SHIP_SIZE_5);
        m.put(new Ship(4,Ship.HORIZONTAL),NUMBER_OF_SHIP_SIZE_4);
        m.put(new Ship(3,Ship.HORIZONTAL),NUMBER_OF_SHIP_SIZE_3);
        m.put(new Ship(2,Ship.HORIZONTAL),NUMBER_OF_SHIP_SIZE_2);
        return m;
    }

    @Override
    public void initializeBattlefields() {
        shotStack = new Stack<>();
        shotOrientation = "";
        previousAttack = null;
        switchAttack = false;
        this.enemyBattlefield = new EnemyBattlefield();
        this.enemyBattlefield.initialize();
        this.playerBattlefield = new PlayerBattlefield();
        this.playerBattlefield.initialize();

        this.battlefields = new ArrayList<>();
        battlefields.add(this.enemyBattlefield);
        battlefields.add(this.playerBattlefield);
    }

    @Override
    public void initializeFleet() {
        this.fleet = buildFleet();
    }

    @Override
    public List<Battlefield> getBattlefields() {
        return this.battlefields;
    }

    @Override
    public Map<Ship,Integer> getFleet(){
        return this.fleet;
    }

    @Override
    public void putShipOnBattlefield(int ship_size, int row, int col) throws PutShipOutOfBattlefieldException, PutOverAnotherShipException, PutNextToAnotherShipExeption {
        this.playerBattlefield.putShip(ship_size,row,col,Ship.HORIZONTAL);
    }

    @Override
    public int getCellNature(int row, int col) {
        return this.playerBattlefield.getNature(row,col);
    }

    @Override
    public void rotateShipOnBattlefield(int row, int col) throws RotateOverAnotherShipException, RotateOutOfBattlefieldException, RotateNextToAnotherShip {
        this.playerBattlefield.rotateShip(row,col);
    }

    @Override
    public void removeShipOnBattlefield(int row, int col) {
        this.playerBattlefield.removeShip(row,col);
    }

    @Override
    public void randomPlacementOnBattlefield(String battlefield){
        Ship currentShip = null;
        int numberOfShip = 0;

        Battlefield currentBattleField = null;
        if (battlefield.equals(PLAYER))
            currentBattleField = this.playerBattlefield;
        else
            currentBattleField = this.enemyBattlefield;

        for (Map.Entry<Ship, Integer> entry : this.fleet.entrySet()) {
            currentShip = entry.getKey();
            numberOfShip = entry.getValue().intValue();
            for (int i=0; i<numberOfShip; i++){
                int row = (int) Math.floor(Math.random() * Battlefield.getHEIGHT());
                int col = (int) Math.floor(Math.random() * Battlefield.getWIDTH());
                int orientation = (int) Math.floor(Math.random() * 2)+1;
                System.out.println(orientation);
                String stringOrientation = "";
                if (1 == orientation)
                    stringOrientation = Ship.VERTICAL;
                else
                    stringOrientation = Ship.HORIZONTAL;
                try {
                    currentBattleField.putShip(currentShip.getSize(),row,col,stringOrientation);
                } catch (PutShipOutOfBattlefieldException e) {
                    i--;
                } catch (PutOverAnotherShipException e) {
                    i--;
                } catch (PutNextToAnotherShipExeption putNextToAnotherShipExeption) {
                    i--;
                }
            }
        }
    }

    @Override
    public void clearPlayerBattlefield(){
        this.playerBattlefield.clear();
    }

    @Override
    public Pair<Integer,Boolean> attackBattlefield(String battlefield, int row, int col) throws CellAlreadyShootException, IAPlayOutOfBoundException {
        Battlefield currentBattleField = null;
        if (battlefield.equals(PLAYER))
            currentBattleField = this.playerBattlefield;
        else
            currentBattleField = this.enemyBattlefield;
        Pair<Integer,Boolean> p = null;
        Cell c = null;
        try {
            c = currentBattleField.getField()[row][col];
            if (c.shootable()) {
                c.Shoot();
                if (currentBattleField instanceof PlayerBattlefield) {
                    if (c instanceof Water) {
                        Water w = (Water) this.playerBattlefield.getField()[row][col];
                        w.setNature(-1);
                        System.out.println("ia attack water");
                    } else {
                        System.out.println("ia attack ship");
                    }
                } else {
                    System.out.println("player attack");
                }
            } else {
                throw new CellAlreadyShootException();
            }
        }catch (ArrayIndexOutOfBoundsException e){
            throw new IAPlayOutOfBoundException();
        }

        if (c instanceof ShipPart){
            ShipPart shipPart = (ShipPart) c;
            Ship currentShip = shipPart.getShip();
            p = new Pair<>(shipPart.getNature(),Boolean.valueOf(currentShip.isSink()));
        }else{
            p = new Pair<>(c.getNature(),Boolean.FALSE);
        }
        return p;
    }

    private Pair<Integer,Integer> attack() throws IAPlayOutOfBoundException {
        Pair<Integer,Integer> p = new Pair<>();
        int row = (int) Math.floor(Math.random() * Battlefield.getHEIGHT());
        int col = (int) Math.floor(Math.random() * Battlefield.getWIDTH());
        if (DIFFICULTY.equals(EASY)){
            p.setFirst(row);
            p.setSecond(col);
        }else{
            if (shotStack.isEmpty()) {
                if (1 < this.playerBattlefield.getField()[row][col].getNature()) {
                    shotStack.push(p);
                    shotStack.push(new Pair<>(row - 1, col));
                    shotStack.push(new Pair<>(row, col - 1));
                    shotStack.push(new Pair<>(row + 1, col));
                    shotStack.push(new Pair<>(row, col + 1));
                }
                p.setFirst(row);
                p.setSecond(col);
                previousAttack = p;
            }else{
                if (!shotOrientation.equals("")){
                    if (!switchAttack) {
                        int nature = 0;
                        try{
                            nature = this.playerBattlefield.getField()[previousAttack.getFirst()][previousAttack.getSecond()].getNature();
                        }catch (ArrayIndexOutOfBoundsException e){
                            nature = -1;
                        }
                        if (-1 == nature) {
                            try {
                                if (shotOrientation.equals(Ship.HORIZONTAL)) {
                                    p.setFirst(previousAttack.getFirst());
                                    p.setSecond(previousAttack.getSecond() - 1);
                                } else {
                                    p.setFirst(previousAttack.getFirst() - 1);
                                    p.setSecond(previousAttack.getSecond());
                                }
                                switchAttack = true;
                            }catch (ArrayIndexOutOfBoundsException e){
                                switchAttack = true;
                                throw new IAPlayOutOfBoundException();
                            }
                        }else{
                            try {
                                if (shotOrientation.equals(Ship.HORIZONTAL)) {
                                    p.setFirst(previousAttack.getFirst());
                                    p.setSecond(previousAttack.getSecond() + 1);
                                } else {
                                    p.setFirst(previousAttack.getFirst() + 1);
                                    p.setSecond(previousAttack.getSecond());
                                }
                            }catch (ArrayIndexOutOfBoundsException e){
                                switchAttack = true;
                                throw new IAPlayOutOfBoundException();
                            }
                        }
                        previousAttack = p;
                    }else{
                        int nature = 0;
                        try {
                            nature = this.playerBattlefield.getField()[previousAttack.getFirst()][previousAttack.getSecond()].getNature();
                        }catch (ArrayIndexOutOfBoundsException e){
                            nature = -1;
                        }
                        if (-1 == nature) {
                            p.setFirst(previousAttack.getFirst());
                            p.setSecond(previousAttack.getSecond());
                            shotStack.clear();
                            shotOrientation = "";
                            previousAttack = null;
                            switchAttack = false;
                        }else {
                            try {
                                if (shotOrientation.equals(Ship.HORIZONTAL)) {
                                    p.setFirst(previousAttack.getFirst());
                                    p.setSecond(previousAttack.getSecond() - 1);
                                } else {
                                    p.setFirst(previousAttack.getFirst() - 1);
                                    p.setSecond(previousAttack.getSecond());
                                }
                            }catch (ArrayIndexOutOfBoundsException e){
                                throw new IAPlayOutOfBoundException();
                            }
                        }
                        previousAttack = p;
                    }
                }else{
                    Pair<Integer,Integer> shot = shotStack.pop();
                    try {
                        if (1 < this.playerBattlefield.getField()[shot.getFirst()][shot.getSecond()].getNature()) {
                            if (shot.getFirst() == previousAttack.getFirst()) {
                                shotOrientation = Ship.HORIZONTAL;
                            } else {
                                shotOrientation = Ship.VERTICAL;
                            }
                            Pair<Integer,Integer> currentShot = null;
                            while (true) {
                                currentShot = shotStack.peek();
                                if (previousAttack != currentShot)
                                    shotStack.pop();
                                else
                                    break;
                            }
                            previousAttack = p;
                        }
                        p.setFirst(shot.getFirst());
                        p.setSecond(shot.getSecond());
                    }catch (ArrayIndexOutOfBoundsException e){
                        throw new IAPlayOutOfBoundException();
                    }
                }
            }
        }
        return p;
    }

    @Override
    public Pair<Pair<Integer,Integer>,Pair<Integer,Boolean>> randomAttackPlayerBattlefield(String difficulty){
        boolean shooted = false;
        Pair<Integer, Boolean> result = new Pair<>();
        Pair<Integer,Integer> position = new Pair<>();
        while(!shooted) {
            try {
                Pair<Integer, Integer> attack = attack();
                Pair<Integer, Boolean> tmp = attackBattlefield(PLAYER, attack.getFirst(), attack.getSecond());
                if (DIFFICULTY.equals(MEDIUM)) {
                    if (tmp.getSecond()) {
                        shotStack.clear();
                        shotOrientation = "";
                        previousAttack = null;
                        switchAttack = false;
                    }
                }
                result.setFirst(tmp.getFirst());
                result.setSecond(tmp.getSecond());
                position.setFirst(Integer.valueOf(attack.getFirst()));
                position.setSecond(Integer.valueOf(attack.getSecond()));
                shooted = true;
            } catch (CellAlreadyShootException e){
                shooted = false;
            } catch (IAPlayOutOfBoundException e) {
                shooted = false;
            }
        }
        return  new Pair<>(position,result);
    }

    @Override
    public void loadBattlefields(String playerBattlefield, String enemyBattlefield) {
        for (int i=0; i<Battlefield.getHEIGHT(); i++) {
            for (int j = 0; j < Battlefield.getWIDTH(); j++) {
                int number = Integer.parseInt(String.valueOf(playerBattlefield.charAt(i* Battlefield.getHEIGHT()+j)));
                if (0 == number || 1 == number){
                    this.playerBattlefield.getField()[i][j]= new Water(number);
                }else{
                    //this.playerBattlefield.getField()[i][j]= new ShipPart(number);
                    //TODO gerer l'appartenance à un ship precis
                }
            }
        }
    }

    @Override
    public void setDifficulty(String difficulty) {
        DIFFICULTY = difficulty;
    }
}
