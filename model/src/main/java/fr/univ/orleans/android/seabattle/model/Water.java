package fr.univ.orleans.android.seabattle.model;

/**
 * Created by thibault on 02/01/17.
 */

public class Water extends Cell {

    private int nature = 0;

    public Water() {
        super();
    }

    public Water(int nature) {
        super();
        this.nature = nature;
    }

    @Override
    public int getNature() {
        return nature;
    }

    public void setNature(int nature) {
        this.nature = nature;
    }
}
