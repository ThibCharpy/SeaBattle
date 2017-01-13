package fr.univ.orleans.android.seabattle.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.univ.orleans.android.seabattle.front.exceptions.PutNextToAnotherShipExeption;
import fr.univ.orleans.android.seabattle.front.exceptions.PutOverAnotherShipException;
import fr.univ.orleans.android.seabattle.front.exceptions.PutShipOutOfBattlefieldException;
import fr.univ.orleans.android.seabattle.front.exceptions.RotateNextToAnotherShip;
import fr.univ.orleans.android.seabattle.front.exceptions.RotateOutOfBattlefieldException;
import fr.univ.orleans.android.seabattle.front.exceptions.RotateOverAnotherShipException;

/**
 * Created by thibault on 02/01/17.
 */

public abstract class Battlefield extends ModelObject {

    private Cell[][] field;

    private final static int WIDTH = 10;
    private final static int HEIGHT = 10;

    public Battlefield() {
        this.field = new Cell[HEIGHT][WIDTH];
    }

    public void initialize(){
        for (int i=0; i<HEIGHT; i++){
            for (int j=0; j<WIDTH; j++){
                field[i][j] = new Water();
            }
        }
    }

    private boolean nextToShip(int ship_size, int row, int col, String orientation){
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < ship_size; i++) {
            if (orientation.equals(Ship.HORIZONTAL)) {
                try {
                    neighbors.add(this.field[row][col + i + 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row][col + i - 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + 1][col + i + 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + 1][col + i].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + 1][col + i - 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row - 1][col + i + 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row - 1][col + i].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row - 1][col + i - 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
            }else{
                try {
                    neighbors.add(this.field[row + i + 1][col].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + i - 1][col].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + i + 1][col + 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + i - 1][col + 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + i][col + 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + i + 1][col - 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + i - 1][col - 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
                try {
                    neighbors.add(this.field[row + i][col - 1].getNature());
                } catch (ArrayIndexOutOfBoundsException e) {
                    neighbors.add(-2);
                }
            }
            Iterator it = neighbors.iterator();
            while (it.hasNext()) {
                int nature = (int) it.next();
                if (1 < nature){
                    System.out.println("SUCESSS");
                    return true;
                }
                it.remove();
            }
        }
        return false;
    }

    public void putShip(int ship_size, int row, int col, String orientation) throws PutShipOutOfBattlefieldException, PutOverAnotherShipException, PutNextToAnotherShipExeption {
        Ship ship = new Ship(ship_size);
        ship.setOrientation(orientation);
        if (orientation.equals(Ship.HORIZONTAL)) {
            if (col + ship_size - 1 < WIDTH) {
                if (0 <= col) {
                    for (int i = 0; i < ship_size; i++) {
                        if (0 < this.field[row][i + col].getNature()) {
                            throw new PutOverAnotherShipException();
                        }
                    }
                    if (nextToShip(ship_size,row,col,orientation)){
                        throw new PutNextToAnotherShipExeption();
                    }
                    for (int i = 0; i < ship_size; i++) {
                        Cell shipPart = new ShipPart(ship);
                        ship.addShipPart((ShipPart) shipPart);
                        this.field[row][i + col] = shipPart;
                    }
                }else{
                    throw new PutShipOutOfBattlefieldException();
                }
            } else {
                throw new PutShipOutOfBattlefieldException();
            }
        }else{
            if (row + ship_size - 1 < HEIGHT) {
                if (0 <= row) {
                    for (int i = 0; i < ship_size; i++) {
                        if (0 < this.field[row + i][col].getNature()) {
                            throw new PutOverAnotherShipException();
                        }
                    }
                    if (nextToShip(ship_size,row,col,orientation)){
                        throw new PutNextToAnotherShipExeption();
                    }
                    for (int i = 0; i < ship_size; i++) {
                        Cell shipPart = new ShipPart(ship);
                        ship.addShipPart((ShipPart) shipPart);
                        this.field[i + row][col] = shipPart;
                    }
                }else {
                    throw new PutShipOutOfBattlefieldException();
                }
            } else {
                throw new PutShipOutOfBattlefieldException();
            }
        }
    }

    public int getNature(int row, int col){
        return  field[row][col].getNature();
    }

    public Cell[][] getField() {
        return field;
    }

    public void setField(Cell[][] field) {
        this.field = field;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public void rotateShip(int row, int col) throws RotateOverAnotherShipException, RotateOutOfBattlefieldException, RotateNextToAnotherShip {
        for (int i=0; i<HEIGHT; i++){
            if (i == row) {
                for (int j = 0; j < WIDTH; j++) {
                    if (j == col){
                        ShipPart pivot = (ShipPart) this.field[i][j];
                        Ship ship = pivot.getShip();
                        int ship_size = ship.getSize();
                        int pivot_index = pivot.getIndexInShip();

                        Cell new_cell = null;

                        if (ship.getOrientation().equals(Ship.HORIZONTAL)){
                            for (int k = 0; k < ship_size; k++ ){
                                try {
                                    Cell c = this.field[i - pivot_index + k][j];
                                    if (c instanceof ShipPart) {
                                        ShipPart s = (ShipPart) c;
                                        if (pivot != s)
                                            throw new RotateOverAnotherShipException();
                                    }
                                }catch (ArrayIndexOutOfBoundsException e){
                                    throw new RotateOutOfBattlefieldException();
                                }
                            }
                            if (nextToShip(ship_size,row - pivot_index,col,ship.getOrientation()))
                                throw new RotateNextToAnotherShip();
                            for (int k = 0; k < ship_size; k++){
                                Cell c = this.field[i][j-pivot_index+k];
                                if (c instanceof ShipPart) {
                                    ShipPart s = (ShipPart) c;
                                    if (s != pivot) {
                                        ship.getParts().remove(s);
                                        new_cell = new Water();
                                        this.field[i][j-pivot_index+k]=new_cell;
                                    }
                                }
                            }
                            for (int k = 0; k < ship_size; k++ ){
                                Cell c = this.field[i-pivot_index+k][j];
                                if (c instanceof Water) {
                                    new_cell = new ShipPart(ship);
                                    ship.getParts().add(k, (ShipPart) new_cell);
                                    this.field[i-pivot_index+k][j]=new_cell;
                                }
                            }
                            ship.setOrientation(Ship.VERTICAL);
                        }else{
                            for (int k = 0; k < ship_size; k++){
                                try {
                                    Cell c = this.field[i][j - pivot_index + k];
                                    if (c instanceof ShipPart) {
                                        ShipPart s = (ShipPart) c;
                                        if (pivot != s)
                                            throw new RotateOverAnotherShipException();
                                    }
                                }catch (ArrayIndexOutOfBoundsException e){
                                    throw new RotateOutOfBattlefieldException();
                                }
                            }
                            if (nextToShip(ship_size,row,col - pivot_index,ship.getOrientation()))
                                throw new RotateNextToAnotherShip();
                            for (int k = 0; k < ship_size; k++ ){
                                Cell c = this.field[i-pivot_index+k][j];
                                if (c instanceof ShipPart) {
                                    ShipPart s = (ShipPart) c;
                                    if (s != pivot) {
                                        ship.getParts().remove(s);
                                        new_cell = new Water();
                                        this.field[i-pivot_index+k][j]=new_cell;
                                    }
                                }
                            }
                            for (int k = 0; k < ship_size; k++){
                                Cell c = this.field[i][j-pivot_index+k];
                                if (c instanceof Water) {
                                    new_cell = new ShipPart(ship);
                                    ship.getParts().add(k, (ShipPart) new_cell);
                                    this.field[i][j-pivot_index+k]=new_cell;
                                }
                            }
                            ship.setOrientation(Ship.HORIZONTAL);
                        }
                    }
                }
            }
        }
    }

    public void removeShip(int row, int col) {
        for (int i=0; i<HEIGHT; i++) {
            if (i == row) {
                for (int j = 0; j < WIDTH; j++) {
                    if (j == col) {
                        Cell c = this.field[i][j];
                        if (c instanceof ShipPart) {
                            Cell water = null;
                            ShipPart partToRemove = null;
                            ShipPart shipPart = (ShipPart) c;
                            Ship ship = shipPart.getShip();
                            System.out.println("orient: "+ship.getOrientation());
                            int ship_size = ship.getSize();
                            int pivot_index = shipPart.getIndexInShip();
                            for (int k=0; k < ship_size; k++){
                                water = new Water();
                                if (ship.getOrientation().equals(Ship.VERTICAL)){
                                    System.out.println("VERT row:"+(i-pivot_index+k)+" col:"+j);
                                    partToRemove = (ShipPart) this.field[i-pivot_index+k][j];
                                    this.field[i-pivot_index+k][j] = water;
                                }else{
                                    System.out.println("HORIZ row:"+i+" col:"+(j-pivot_index+k));
                                    partToRemove = (ShipPart) this.field[i][j-pivot_index+k];
                                    this.field[i][j-pivot_index+k] = water;
                                }
                                ship.removeShipPart(partToRemove);
                            }
                        }
                    }
                }
            }
        }
    }

    public void clear(){
        for (int i=0; i<HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                this.field[i][j] = new Water();
            }
        }
    }

    public String save(){
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                builder.append(String.valueOf(this.field[i][j]));
            }
        }
        return builder.toString();
    }
}
