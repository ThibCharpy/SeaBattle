package fr.univ.orleans.android.seabattle.front;

import java.util.List;
import java.util.Map;

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
import fr.univ.orleans.android.seabattle.model.Ship;

/**
 * Created by thibault on 02/01/17.
 */

public interface GameManagement {

    /**
     * create the two battle fields
     * @return list[0] is enemyBattlefield and list[1] is playerBattlefield
     */
    public void initializeBattlefields();

    /**
     * create all the fleet with model parameters
     */
    public void initializeFleet();

    /**
     * return the two battlefields
     * @return player battlefield and enemy battlefield
     */
    public List<Battlefield> getBattlefields();

    /**
     * return all ships in fleet list
     * @return fleet List
     */
    public Map<Ship,Integer> getFleet();

    /**
     * put a ship on player battlefield
     * @param ship_size
     * @param row
     * @param col
     * @throws PutShipOutOfBattlefieldException
     * @throws PutOverAnotherShipException
     */
    public void putShipOnBattlefield(int ship_size, int row, int col) throws PutShipOutOfBattlefieldException, PutOverAnotherShipException, PutNextToAnotherShipExeption;

    /**
     * get the nature of the cell, water, water_shoot, or ship (size)
     * @param row
     * @param col
     * @return nature of cell
     */
    public int getCellNature(int row, int col);

    /**
     * use to rotate a ship
     * @param row
     * @param col
     */
    public void rotateShipOnBattlefield(int row, int col) throws RotateOverAnotherShipException, RotateOutOfBattlefieldException, RotateNextToAnotherShip;

    /**
     * Remove the ship with shipPart at row col in the grid
     * @param row
     * @param col
     */
    public void removeShipOnBattlefield(int row, int col);


    /**
     * Random placement on player Battlefield
     * @param battlefield
     */
    public void randomPlacementOnBattlefield(String battlefield);

    /**
     * clear the player Battlefield
     */
    public void clearPlayerBattlefield();

    /**
     * attack the ennemy battlefield
     * @param row
     * @param col
     * @return nature of the attack and if the ship is sink
     */
    public Pair<Integer,Boolean> attackBattlefield(String battlefield, int row, int col) throws CellAlreadyShootException, IAPlayOutOfBoundException;

    /**
     * random attack for weak ia
     * @return nature of the attack and if the ship is sink
     * @throws CellAlreadyShootException
     */
    public Pair<Pair<Integer,Integer>,Pair<Integer,Boolean>> randomAttackPlayerBattlefield(String difficulty) throws CellAlreadyShootException;

    /**
     * Load saved Battlefields
     * @param playerBattlefield
     * @param enemyBattlefield
     */
    public void loadBattlefields(String playerBattlefield, String enemyBattlefield);
}
