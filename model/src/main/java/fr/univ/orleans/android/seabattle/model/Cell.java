package fr.univ.orleans.android.seabattle.model;

/**
 * Created by thibault on 02/01/17.
 */

public abstract class Cell extends ModelObject{

    private boolean hit;

    public Cell() {
        this.hit = false;
    }

    /**
     * Nature of the cell : water, water_shoot, shipPart or shipPartHit
     * @return
     */

    public abstract int getNature();

    public boolean shootable(){
        return !hit;
    }

    public void Shoot(){
        this.hit = true;
    }

}
