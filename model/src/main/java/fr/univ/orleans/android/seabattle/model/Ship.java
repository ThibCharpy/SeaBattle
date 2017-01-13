package fr.univ.orleans.android.seabattle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibault on 02/01/17.
 */

public class Ship extends ModelObject {

    public final static String VERTICAL = "VERTICAL";
    public final static String HORIZONTAL = "HORIZONTAL";

    private List<ShipPart> parts;
    private int size;
    private String orientation;

    public Ship(int size) {
        this.size = size;
        this.parts = new ArrayList<>();
    }

    public Ship(int size, String orientation) {
        this.size = size;
        this.orientation = orientation;
        this.parts = new ArrayList<>();
        ShipPart s = null;
        for (int i=0; i<size; i++){
            s = new ShipPart(this);
            this.parts.add(i,s);
        }
    }

    public void addShipPart(ShipPart shipPart){
        this.getParts().add(shipPart);
    }

    public void removeShipPart(ShipPart shipPart){
        this.getParts().remove(shipPart);
    }

    public boolean isSink(){
        for (ShipPart s: this.parts) {
            if (s.shootable())
                return false;
        }
        return true;
    }

    public String getOrientation(){
        return this.orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public List<ShipPart> getParts() {
        return parts;
    }

    public void setParts(List<ShipPart> parts) {
        this.parts = parts;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
