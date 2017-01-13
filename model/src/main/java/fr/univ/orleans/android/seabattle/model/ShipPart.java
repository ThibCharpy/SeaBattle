package fr.univ.orleans.android.seabattle.model;

/**
 * Created by thibault on 02/01/17.
 */

public class ShipPart extends Cell {

    private static int NATURE = 1;

    private Ship ship;

    public ShipPart(Ship ship) {
        super();
        this.ship = ship;
    }

    @Override
    public int getNature() {
        return 1*ship.getSize();
    }

    public int getPartOfShip(){
        int index = this.ship.getParts().indexOf(this);
        if (0 == index){
            return 1;
        }else{
            if (this.ship.getSize()-1 == index){
                return -1;
            }else{
                return 0;
            }
        }
    }

    public int getIndexInShip(){
        return this.ship.getParts().indexOf(this);
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}
