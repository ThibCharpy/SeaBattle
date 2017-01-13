package fr.univ.orleans.android.seabattle.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;
import fr.univ.orleans.android.seabattle.database.ProfilsDataSource;
import fr.univ.orleans.android.seabattle.front.exceptions.IAPlayOutOfBoundException;
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
import fr.univ.orleans.android.seabattle.model.ShipPart;
import fr.univ.orleans.android.seabattle.front.utils.Pair;

/**
 * Created by thibault on 02/01/17.
 */

public class GameActivity extends Activity {

    private Controller controller;

    private ProfilsDataSource database;

    private List<Profil> profils;

    TableLayout enemyBattlefield;

    TableLayout playerBattlefield;

    LinearLayout subBar;

    private List<Pair<Integer,Pair<TextView,ImageView>>> shipNumbers;

    private final static String INCREMENT = "inc";
    private final static String DECREMENT = "dec";

    private final static String PATHBEGINFORSHIP = "ship";

    private List<Pair<Integer,Map<String,String>>> sprites;

    private final static String LABEL_EMPTY = "empty";
    private final static String LABEL_NORMAL = "normal";
    private final static String LABEL_VALID = "valid";
    private final static String LABEL_ERROR = "error";
    private final static String LABEL_HEAD = "head";
    private final static String LABEL_TAIL = "tail";
    private final static String LABEL_PART = "part";
    private final static String LABEL_HEADHIT = "headhit";
    private final static String LABEL_TAILHIT = "tailhit";
    private final static String LABEL_PARTHIT = "parthit";
    private final static String LABEL_WATER = "water";
    private final static String LABEL_WATERSHOOT = "water_shoot";
    private final static String LABEL_SHOOT = "shoot";
    private final static String LABEL_SINK = "sink";

    private final static String[] SPRITESHIPNAMES = {LABEL_NORMAL,LABEL_EMPTY,LABEL_VALID,LABEL_ERROR};
    private final static String[] SPRITESHIPPARTNAMES = {LABEL_HEAD,LABEL_TAIL,LABEL_PART,LABEL_HEADHIT,LABEL_TAILHIT,LABEL_PARTHIT};
    private final static String[] SPRITESNONSHIP = {LABEL_WATER,LABEL_WATERSHOOT,LABEL_ERROR,LABEL_SHOOT,LABEL_SINK};

    private final static String LABEL_ROTATE = "Rotate";
    private final static String LABEL_REMOVE = "Remove";

    private boolean inGame;
    private static boolean WIN;
    private static boolean LOOSE;

    private boolean loadGame = false;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation){
            setContentView(R.layout.activity_game_landscape);
        }else if(Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation){
            setContentView(R.layout.activity_game_portrait);
        }

        this.inGame = false;
        WIN = false;
        LOOSE = false;

        Intent intent = getIntent();
        this.controller = (Controller) intent.getSerializableExtra("controller");

        /*final Profil currenProfil = this.controller.getCurrentProfil();
        if (!currenProfil.getPlayer().equals("")){
            this.loadGame = true;
        }*/

        this.database = new ProfilsDataSource(this);
        this.database.open();
        this.profils = this.database.getAllProfils();
        this.database.close();

        /*if (loadGame){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("LOAD PREVIOUSLY SAVED GAME ?");
            builder.setCancelable(false);
            builder.setPositiveButton("NEW GAME", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    controller.initializeBattlefield();
                }
            });
            builder.setNegativeButton("RESUME", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    controller.loadBattlefields(currenProfil.getPlayer(),currenProfil.getEnemy());
                    displayBoard();
                    updatePlayerBattlefield();
                    activeGame();
                    updateEnemyBattlefield();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            //ici mettre initialisation des battlefields
        }*/

        this.controller.initializeBattlefield();
        this.controller.initializeFleet();
        this.loadSprites();

        displayBoard();
        putShipsIntoSubBar();
        Button play = (Button) findViewById(R.id.buttonPlay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeGame();
            }
        });
        play.setEnabled(false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int position = v.getId();
        int row = position/this.playerBattlefield.getChildCount();
        int col = position%this.playerBattlefield.getChildCount();
        int ship_size = controller.getCellNature(row,col);
        System.out.println("CONTEXT MENU");
        menu.setHeaderTitle("Ship "+ship_size);
        menu.add(0, v.getId(), 0, LABEL_ROTATE);
        menu.add(0, v.getId(), 0, LABEL_REMOVE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getItemId();

        int row = position/this.playerBattlefield.getChildCount();
        int col = position%this.playerBattlefield.getChildCount();

        int ship_size = controller.getCellNature(row,col);

        String water = getSprite(1,LABEL_WATER);
        String ship_head = getSprite(1,LABEL_HEAD);
        String ship_part = getSprite(1,LABEL_PART);
        String ship_tail = getSprite(1,LABEL_TAIL);

        for (int i = 0; i < this.playerBattlefield.getChildCount(); i++){
            if (i == row) {
                View view = this.playerBattlefield.getChildAt(i);
                TableRow tableRow = (TableRow) view;
                for (int j = 0; j < this.playerBattlefield.getChildCount(); j++){
                    if (j == col){
                        ImageView pivot = (ImageView) tableRow.getChildAt(j);
                        if (item.getTitle().equals(LABEL_ROTATE)) { //ROTATION
                            try {
                                controller.rotateShip(row,col);
                                updatePlayerBattlefield();
                            } catch (RotateOverAnotherShipException e) {
                                Toast.makeText(getApplicationContext(), "Impossible to rotate over another ship!",Toast.LENGTH_SHORT).show();
                            } catch (RotateOutOfBattlefieldException e) {
                                Toast.makeText(getApplicationContext(), "Impossible to rotate out of battlefield!",Toast.LENGTH_SHORT).show();
                            } catch (RotateNextToAnotherShip rotateNextToAnotherShip) {
                                Toast.makeText(getApplicationContext(), "Impossible to rotate next to another ship!",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            controller.removeShip(row,col);
                            updatePlayerBattlefield();
                            changeShipNumber(INCREMENT,ship_size);
                            Button play = (Button) findViewById(R.id.buttonPlay);
                            play.setEnabled(false);
                        }
                    }
                }
            }
        }

        return true;
    }

    private int getDrawableResourceId(ImageView imageView,String name){
        Context context = imageView.getContext();
        return imageView.getContext().getResources().getIdentifier(name , "drawable", imageView.getContext().getPackageName());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void displayBoard(){
        List<Battlefield> battlefields = this.controller.getBattlefields();
        Battlefield enemyBattlefield = battlefields.get(0);
        Battlefield playerBattlefield = battlefields.get(1);

        this.enemyBattlefield = (TableLayout) findViewById(R.id.enemyGrid);
        this.playerBattlefield = (TableLayout) findViewById(R.id.myGrid);

        int eB_width = enemyBattlefield.getField().length;
        int eB_height = enemyBattlefield.getField()[0].length;

        final String water = getSprite(1,LABEL_WATER);
        final String water_shoot = getSprite(1,LABEL_WATERSHOOT);

        for (int i=0; i <eB_height; i++ ){
            TableRow row = new TableRow(this);
            for (int j=0; j<eB_width; j++){
                final int a = i, b = j;
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(getDrawableResourceId(imageView, water));

                imageView.setId(j + eB_height * i);
                row.addView(imageView);
            }
            this.enemyBattlefield.addView(row);
        }

        int pB_width = playerBattlefield.getField().length;
        int pB_height = playerBattlefield.getField()[0].length;

        for (int i=0; i <pB_height; i++ ){
            TableRow row = new TableRow(this);
            row.setId(i);
            for (int j=0; j<pB_width; j++){
                final int a=i,b=j;
                final ImageView imageView = new ImageView(this);
                imageView.setId(j+pB_height*i);
                imageView.setImageResource(getDrawableResourceId(imageView,water));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(controller.getCellNature(a,b));
                    }
                });
                imageView.setOnDragListener(new MyDragListener());
                row.addView(imageView);
            }
            this.playerBattlefield.addView(row);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void putShipsIntoSubBar(){
        TableLayout table = (TableLayout) findViewById(R.id.shipContainer);
        TableRow row = new TableRow(this);
        Map<Ship,Integer> shipsMap = this.controller.getFleet();

        this.shipNumbers = new ArrayList<>();

        LayoutInflater inflater = getLayoutInflater();
        View view = null;

        Ship currentShip = null;
        int numberOfShip = 0;

        String ship_label = null;

        for (Map.Entry<Ship, Integer> entry : shipsMap.entrySet())
        {
            currentShip = entry.getKey();
            numberOfShip = entry.getValue().intValue();

            view = inflater.inflate(R.layout.fragment_ships,null);

            LinearLayout ship = new LinearLayout(this);
            ship.addView(view);
            TextView ship_Button_Label = (TextView) view.findViewById(R.id.ship_size);
            ship_Button_Label.setText("Ship n°"+currentShip.getSize());
            ImageView ship_picture = (ImageView) view.findViewById(R.id.ship_picture);
            ship_picture.setId(currentShip.getSize());
            ship_label = getSprite(currentShip.getSize(),LABEL_EMPTY);
            ship_picture.setImageResource(getDrawableResourceId(ship_picture,ship_label));
            ship_picture.setOnTouchListener(new MyTouchListener());
            TextView ship_Number_Available = (TextView) view.findViewById(R.id.shipNumber);
            ship_Number_Available.setText(String.valueOf(numberOfShip));

            Pair<TextView,ImageView> toolBarCell = new Pair<>(ship_Number_Available,ship_picture);
            Pair<Integer,Pair<TextView,ImageView>> shipNumber = new Pair<>(Integer.valueOf(currentShip.getSize()),toolBarCell);
            this.shipNumbers.add(shipNumber);

            row.setGravity(Gravity.CENTER);
            row.addView(ship);
        }
        table.addView(row);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        List<Pair<Integer,Pair<TextView,ImageView>>> toolBar = this.shipNumbers;
        if (Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation){
            setContentView(R.layout.activity_game_landscape);
        }else if (Configuration.ORIENTATION_PORTRAIT == newConfig.orientation){
            setContentView(R.layout.activity_game_portrait);
        }
        displayBoard();
        updatePlayerBattlefield();
        if (!this.inGame) {
            putShipsIntoSubBar();
            updateShipBar(toolBar);
            Button play = (Button) findViewById(R.id.buttonPlay);
            if (readyToPlay()) {
                play.setEnabled(true);
            } else {
                play.setEnabled(false);
            }
        }else{
            letsPLay();
            updateEnemyBattlefield();
        }
    }

    private void updateEnemyBattlefield(){
        List<Battlefield> battlefields = this.controller.getBattlefields();
        Battlefield enemyBattlefield = battlefields.get(0);

        String shoot = getSprite(1,LABEL_SHOOT);
        String water = getSprite(1,LABEL_WATER);
        String water_shoot = getSprite(1,LABEL_WATERSHOOT);

        for (int i=0; i<Battlefield.getHEIGHT(); i++){
            View view = this.enemyBattlefield.getChildAt(i);
            TableRow tableRow = (TableRow) view;
            for (int j=0; j<Battlefield.getWIDTH(); j++){
                final ImageView ship_picture = (ImageView) tableRow.getChildAt(j);
                registerForContextMenu(ship_picture);
                System.out.println("ICI1");
                if (0 == enemyBattlefield.getField()[i][j].getNature()){

                    System.out.println("ICI2");
                    ship_picture.setImageResource(getDrawableResourceId(ship_picture,water));
                }else if(-1 == enemyBattlefield.getField()[i][j].getNature()){
                    System.out.println("ICI3");
                    ship_picture.setImageResource(getDrawableResourceId(ship_picture,water_shoot));
                }else if(1 < enemyBattlefield.getField()[i][j].getNature()){
                    System.out.println("ICI4");
                    ShipPart shipPart = (ShipPart) enemyBattlefield.getField()[i][j];
                    if (!shipPart.shootable()){
                        ship_picture.setImageResource(getDrawableResourceId(ship_picture, shoot));
                    }else{
                        ship_picture.setImageResource(getDrawableResourceId(ship_picture, water));
                    }
                }
            }
        }
    }

    private void updatePlayerBattlefield(){
        List<Battlefield> battlefields = this.controller.getBattlefields();
        Battlefield playerBattlefield = battlefields.get(1);

        String ship_head = getSprite(1,LABEL_HEAD);
        String ship_part = getSprite(1,LABEL_PART);
        String ship_tail = getSprite(1,LABEL_TAIL);
        String ship_head_hit = getSprite(1,LABEL_HEADHIT);
        String ship_part_hit = getSprite(1,LABEL_PARTHIT);
        String ship_tail_hit = getSprite(1,LABEL_TAILHIT);
        String water = getSprite(1,LABEL_WATER);
        String water_shoot = getSprite(1,LABEL_WATERSHOOT);

        for (int i=0; i<Battlefield.getHEIGHT(); i++){
            View view = this.playerBattlefield.getChildAt(i);
            TableRow tableRow = (TableRow) view;
            for (int j=0; j<Battlefield.getWIDTH(); j++){
                final int a=i,b=j;
                final ImageView ship_picture = (ImageView) tableRow.getChildAt(j);
                ship_picture.setRotation(0);
                ship_picture.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (1 < controller.getCellNature(a,b))
                            ship_picture.showContextMenu();
                        return true;
                    }
                });
                registerForContextMenu(ship_picture);
                if (0 == playerBattlefield.getField()[i][j].getNature()){
                    ship_picture.setImageResource(getDrawableResourceId(ship_picture,water));
                }else if(-1 == playerBattlefield.getField()[i][j].getNature()){
                    ship_picture.setImageResource(getDrawableResourceId(ship_picture,water_shoot));
                }else if(1 < playerBattlefield.getField()[i][j].getNature()){
                    ShipPart shipPart = (ShipPart) playerBattlefield.getField()[i][j];
                    int partOfShip = shipPart.getPartOfShip();
                    String orientation = shipPart.getShip().getOrientation();
                    if (!shipPart.shootable()) {
                        switch (partOfShip) {
                            case 1:
                                ship_picture.setImageResource(getDrawableResourceId(ship_picture, ship_head_hit));
                                break;
                            case 0:
                                ship_picture.setImageResource(getDrawableResourceId(ship_picture, ship_part_hit));
                                break;
                            case -1:
                                ship_picture.setImageResource(getDrawableResourceId(ship_picture, ship_tail_hit));
                                break;
                        }
                    }else{
                        switch (partOfShip) {
                            case 1:
                                ship_picture.setImageResource(getDrawableResourceId(ship_picture, ship_head));

                                break;
                            case 0:
                                ship_picture.setImageResource(getDrawableResourceId(ship_picture, ship_part));
                                break;
                            case -1:
                                ship_picture.setImageResource(getDrawableResourceId(ship_picture, ship_tail));
                                break;
                        }
                    }
                    if (orientation.equals(Ship.VERTICAL)){
                        ship_picture.setRotation(90);
                    }
                }
            }
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class MyDragListener implements View.OnDragListener {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onDrag(View v, DragEvent event) {
            View view = (View) event.getLocalState();

            ImageView ship_picture = (ImageView) v;
            int ship_size = view.getId();

            int action = event.getAction();

            int middle = ship_size/2;
            if (0 == ship_size%2)
                middle--;
            int row = ship_picture.getId()/playerBattlefield.getChildCount();
            int col = ship_picture.getId()%playerBattlefield.getChildCount();
            int cellNature = 0;
            boolean overShip = false;
            if (overGridDrag(row,col)) {
                cellNature = controller.getCellNature(row, col);
                //overShip = overShipDrag(ship_size,row,col,cellNature);
            }
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                        putShipOnBattlefield(ship_size,row,col-middle);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                        removeShipOnBattlefield(ship_size,row,col-middle);
                    break;
                case DragEvent.ACTION_DROP:
                    try {
                        controller.putShipOnBattlefield(ship_size,row,col-middle);
                        System.out.println("PUT: size:"+ship_size);
                        putShipOnBattlefield(ship_size,row,col-middle);
                        changeShipNumber(DECREMENT,ship_size);
                        Button play = (Button) findViewById(R.id.buttonPlay);
                        if (readyToPlay()){
                            play.setEnabled(true);
                        }
                        view.setVisibility(View.VISIBLE);
                    } catch (PutShipOutOfBattlefieldException e) {
                        Toast.makeText(getApplicationContext(), "Invalid ship placement !",Toast.LENGTH_SHORT).show();
                        removeShipOnBattlefield(ship_size,row,col-middle);
                        return false;
                    } catch (PutOverAnotherShipException e) {
                        Toast.makeText(getApplicationContext(), "Can't put ship over another !",Toast.LENGTH_SHORT).show();
                        removeShipOnBattlefield(ship_size,row,col-middle);
                        return false;
                    } catch (PutNextToAnotherShipExeption putNextToAnotherShipExeption) {
                        Toast.makeText(getApplicationContext(), "Can't put ship next to another !",Toast.LENGTH_SHORT).show();
                        removeShipOnBattlefield(ship_size,row,col-middle);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private boolean overGridDrag (int row, int col){
        return (row < playerBattlefield.getChildCount() && row >=0 && col < playerBattlefield.getChildCount() && col >=0);
    }

    private boolean nextToShips(int size, int row, int col){
        TableRow tableRow = (TableRow) this.playerBattlefield.getChildAt(row);
        TableRow tableRowTOP = (TableRow) this.playerBattlefield.getChildAt(row-1);
        TableRow tableRowBOT = (TableRow) this.playerBattlefield.getChildAt(row+1);
        List<ImageView> neighbors = null;
        neighbors = new ArrayList<>();
        for (int i=0; i<size; i++) {
            if (null != tableRow) {
                neighbors.add((ImageView) tableRow.getChildAt(col+i - 1));
                neighbors.add((ImageView) tableRow.getChildAt(col+i + 1));
            }
            if (null != tableRowTOP) {
                neighbors.add((ImageView) tableRowTOP.getChildAt(col+i - 1));
                neighbors.add((ImageView) tableRowTOP.getChildAt(col+i));
                neighbors.add((ImageView) tableRowTOP.getChildAt(col+i + 1));
            }
            if (null != tableRowBOT) {
                neighbors.add((ImageView) tableRowBOT.getChildAt(col+i - 1));
                neighbors.add((ImageView) tableRowBOT.getChildAt(col+i));
                neighbors.add((ImageView) tableRowBOT.getChildAt(col+i + 1));
            }

            Iterator it = neighbors.iterator();
            while (it.hasNext()) {
                ImageView picture = (ImageView) it.next();
                if (null != picture) {
                    int picture_id = picture.getId();
                    int row2 = picture_id / playerBattlefield.getChildCount();
                    int col2 = picture_id % playerBattlefield.getChildCount();
                    if (1 < controller.getCellNature(row2, col2)) {
                        return true;
                    }
                }
                it.remove();
            }
        }
        return false;
    }

    private void putShipOnBattlefield(int size, int row, int col){

        String ship_head = getSprite(1,LABEL_HEAD);
        String ship_part = getSprite(1,LABEL_PART);
        String ship_tail = getSprite(1,LABEL_TAIL);
        String error = getSprite(1,LABEL_ERROR);

        boolean outOfBoundRight = false;
        boolean outOfBoundLeft = false;
        boolean overShip = false;
        boolean nextToOtherShip = nextToShips(size,row,col);

        int cellNature = 0;

        for (int i = 0; i < this.playerBattlefield.getChildCount(); i++) {
            if (i == row) {
                View view = this.playerBattlefield.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow tableRow = (TableRow) view;
                    for (int j = 0; j < size; j++) {
                        final ImageView ship_picture = (ImageView) tableRow.getChildAt(col + j);
                        if (null != ship_picture) {
                            cellNature = controller.getCellNature(row, col+j);
                            if (0 == cellNature) {
                                registerForContextMenu(ship_picture);
                                if (!nextToOtherShip) {
                                    if (0 == j) {
                                        ship_picture.setImageResource(getDrawableResourceId(ship_picture, ship_head));
                                    } else if (size - 1 == j) {
                                        ship_picture.setImageResource(getDrawableResourceId(ship_picture, ship_tail));
                                    } else {
                                        ship_picture.setImageResource(getDrawableResourceId(ship_picture, ship_part));
                                    }
                                }else{
                                    break;
                                }
                            }else{
                                overShip = true;
                                break;
                            }
                        } else {
                            if (col < 0) {
                                outOfBoundLeft = true;
                            } else {
                                outOfBoundRight = true;
                            }
                            break;
                        }
                    }
                    if (outOfBoundRight) {
                        for (int j = col; j < tableRow.getChildCount(); j++) {
                            ImageView ship_picture = (ImageView) tableRow.getChildAt(j);
                            if (null != ship_picture) {
                                if (0 == controller.getCellNature(row,j))
                                    ship_picture.setImageResource(getDrawableResourceId(ship_picture, error));
                            }
                        }
                    } else if (outOfBoundLeft) {
                        int middle = size/2;
                        int limit = col + 2 * middle;
                        if (0 != size%2){
                            limit++;
                        }
                        for (int j = 0; j < limit; j++) {
                            ImageView ship_picture = (ImageView) tableRow.getChildAt(j);
                            if (null != ship_picture) {
                                if (0 == controller.getCellNature(row,j))
                                    ship_picture.setImageResource(getDrawableResourceId(ship_picture, error));
                            }
                        }
                    }else if(overShip){
                        for (int j = 0; j < size; j++) {
                            if (col+j < tableRow.getChildCount()) {
                                if (controller.getCellNature(row, col + j) < 1) {
                                    ImageView ship_picture = (ImageView) tableRow.getChildAt(col + j);
                                    ship_picture.setImageResource(getDrawableResourceId(ship_picture, error));
                                }
                            }
                        }
                    }else if (nextToOtherShip){
                        boolean next = false;
                        for (int j = 0; j < size; j++) {
                            if (!next) {
                                TableRow tableRowTOP = (TableRow) this.playerBattlefield.getChildAt(row - 1);
                                TableRow tableRowBOT = (TableRow) this.playerBattlefield.getChildAt(row + 1);
                                List<ImageView> neighbors = null;
                                ImageView neighbor = null;
                                neighbors = new ArrayList<>();
                                if (null != tableRow) {
                                    neighbors.add((ImageView) tableRow.getChildAt(col+j - 1));
                                    neighbors.add((ImageView) tableRow.getChildAt(col+j + 1));
                                }
                                if (null != tableRowTOP) {
                                    neighbors.add((ImageView) tableRowTOP.getChildAt(col+j - 1));
                                    neighbors.add((ImageView) tableRowTOP.getChildAt(col+j));
                                    neighbors.add((ImageView) tableRowTOP.getChildAt(col+j + 1));
                                }
                                if (null != tableRowBOT) {
                                    neighbors.add((ImageView) tableRowBOT.getChildAt(col+j - 1));
                                    neighbors.add((ImageView) tableRowBOT.getChildAt(col+j));
                                    neighbors.add((ImageView) tableRowBOT.getChildAt(col+j + 1));
                                }
                                Iterator it = neighbors.iterator();
                                while (it.hasNext()) {
                                    ImageView picture = (ImageView) it.next();
                                    if (null != picture) {
                                        int picture_id = picture.getId();
                                        int row2 = picture_id / playerBattlefield.getChildCount();
                                        int col2 = picture_id % playerBattlefield.getChildCount();
                                        if (1 < controller.getCellNature(row2, col2)) {
                                            next = true;
                                        }
                                    }
                                    it.remove();
                                }
                            }
                        }
                        if (next) {
                            for (int j = 0; j < size; j++) {
                                if (col + j < tableRow.getChildCount()) {
                                    if (controller.getCellNature(row, col + j) < 1) {
                                        ImageView ship_picture = (ImageView) tableRow.getChildAt(col + j);
                                        ship_picture.setImageResource(getDrawableResourceId(ship_picture, error));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeShipOnBattlefield(int size, int row, int col){

        String water = getSprite(1,LABEL_WATER);
        for (int i=0; i<this.playerBattlefield.getChildCount(); i++){
            if (i == row) {
                View view = this.playerBattlefield.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow tableRow = (TableRow) view;
                    for (int j=0; j<size; j++){
                        ImageView ship_picture = (ImageView) tableRow.getChildAt(col+j);
                        if (null != ship_picture){
                            if (0 == controller.getCellNature(row, col + j)) {
                                System.out.println("ID: "+ship_picture.getId());
                                System.out.println("ICI3");
                                ship_picture.setImageResource(getDrawableResourceId(ship_picture, LABEL_WATER));
                                ship_picture.setOnLongClickListener(null);
                            }
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Map<String,String> getShipPartSprites(){
        Map<String,String> sprites = new HashMap<>();

        Drawable drawable = null;
        ImageView picture = null;

        StringBuilder stringBuilder = null;

        for (final String partPath : SPRITESHIPPARTNAMES){
            stringBuilder = new StringBuilder();
            stringBuilder.append(PATHBEGINFORSHIP);
            stringBuilder.append("_");
            stringBuilder.append(partPath);
            sprites.put(partPath,stringBuilder.toString());
        }

        for (final String partPath : SPRITESNONSHIP){
            stringBuilder = new StringBuilder();
            stringBuilder.append(partPath);
            sprites.put(partPath,stringBuilder.toString());
        }

        return sprites;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Map<String,String> getShipSpritesBySize(int size){
        Map<String,String> sprites = new HashMap<>();

        Drawable drawable = null;
        ImageView picture = null;

        StringBuilder stringBuilder = null;

        for (final String partPath : SPRITESHIPNAMES){
            stringBuilder = new StringBuilder();
            stringBuilder.append(PATHBEGINFORSHIP);
            stringBuilder.append(String.valueOf(size));
            stringBuilder.append("_");
            stringBuilder.append(partPath);
            sprites.put(partPath,stringBuilder.toString());
        }

        return sprites;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void loadSprites(){
        this.sprites = new ArrayList<>();
        Map<Ship,Integer> ships = this.controller.getFleet();

        int size = 0;
        Ship currentShip = null;
        Pair<Integer,Map<String,String>> pair = null;
        Map<String,String> sprites = null;

        //ship part and other add to the general list of sprite;
        pair = new Pair<>(Integer.valueOf(1),getShipPartSprites());
        this.sprites.add(pair);

        for (Map.Entry<Ship, Integer> entry : ships.entrySet())
        {
            currentShip = entry.getKey();
            size = currentShip.getSize();

            sprites = getShipSpritesBySize(size);
            pair = new Pair<>(Integer.valueOf(size),sprites);
            this.sprites.add(pair);
        }
    }

    private String getSprite(int size, String label){

        Integer spritesSize = null;
        Map<String,String> spritesMap = null;
        Iterator iterator = null;

        for (Pair sprites : this.sprites){
            spritesSize = (Integer) sprites.getFirst();
            spritesMap = (Map<String,String>) sprites.getSecond();
            if (spritesSize.intValue() == size){
                iterator = spritesMap.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String,String> entry = (Map.Entry<String,String>) iterator.next();
                    if (entry.getKey().equals(label)){
                        return entry.getValue();
                    }
                }
            }
        }
        return null;
    }

    private void changeShipNumber(String action, int size){
        Iterator i = this.shipNumbers.iterator();
        while (i.hasNext()){
            Pair<Integer,Pair<TextView,ImageView>> shipNumber = (Pair<Integer,Pair<TextView,ImageView>>) i.next();
            if (shipNumber.getFirst().intValue() == size) {
                Pair<TextView, ImageView> displaying = shipNumber.getSecond();
                TextView number = displaying.getFirst();
                ImageView picture = displaying.getSecond();
                int intNumber = Integer.parseInt(number.getText().toString());
                int anc_intNumber = intNumber;

                if (action.equals(DECREMENT)) {
                    intNumber--;
                    number.setText(String.valueOf(intNumber));
                    if (0 == intNumber) {
                        picture.setOnTouchListener(null);
                    }
                }else{
                    intNumber++;
                    if (0 == anc_intNumber)
                        picture.setOnTouchListener(new MyTouchListener());
                }
                number.setText(String.valueOf(intNumber));
            }
        }
    }

    private boolean readyToPlay(){
        Iterator i = this.shipNumbers.iterator();
        while (i.hasNext()){
            Pair<Integer,Pair<TextView,ImageView>> shipNumber = (Pair<Integer,Pair<TextView,ImageView>>) i.next();
                Pair<TextView, ImageView> displaying = shipNumber.getSecond();
                TextView number = displaying.getFirst();
                ImageView picture = displaying.getSecond();
                int intNumber = Integer.parseInt(number.getText().toString());
                if (0 < intNumber)
                    return false;
        }
        return true;
    }

    public void updateShipBar( List<Pair<Integer,Pair<TextView,ImageView>>> toolBar){

        /*this.shipNumbers = new ArrayList<>();
        this.shipNumbers.addAll(toolBar);*/

        Map<Ship,Integer> shipsMap = this.controller.getFleet();
        Ship currentShip = null;
        int numberOfShip = 0;
        int cptCell = 0;
        int totalCell = 0;

        List<Pair<Integer,Integer>> presentShip = new ArrayList<>();


        for (Map.Entry<Ship, Integer> entry : shipsMap.entrySet()) {
            currentShip = entry.getKey();
            numberOfShip = entry.getValue().intValue();
            totalCell = currentShip.getSize()*numberOfShip;
            Pair<Integer,Integer> p = null;

            for (int i = 0; i < this.playerBattlefield.getChildCount(); i++) {
                View view = this.playerBattlefield.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow tableRow = (TableRow) view;
                    for (int j = 0; j < tableRow.getChildCount(); j++) {
                        if ( currentShip.getSize() == this.controller.getCellNature(i,j)){
                            cptCell++;
                        }
                    }
                }
            }

            int numberPresentShip = 0;
            if (0 < cptCell) {
                numberPresentShip = totalCell / cptCell;
                if (numberPresentShip == 1)
                    numberPresentShip = 0;
            }else{
                numberPresentShip = numberOfShip;
            }
            p = new Pair<>(Integer.valueOf(currentShip.getSize()),Integer.valueOf(numberPresentShip));
            presentShip.add(p);
            cptCell = 0;
        }

        for (Pair<Integer,Integer> ship : presentShip){
            int ship_size = ship.getFirst();
            int restOfShip = ship.getSecond();

            Iterator i = this.shipNumbers.iterator();
            while (i.hasNext()){
                Pair<Integer,Pair<TextView,ImageView>> shipNumber = (Pair<Integer,Pair<TextView,ImageView>>) i.next();
                int shipSize = shipNumber.getFirst();
                if (ship_size == shipSize){
                    Pair<TextView, ImageView> displaying = shipNumber.getSecond();
                    TextView number = displaying.getFirst();
                    ImageView picture = displaying.getSecond();
                    int intNumber = Integer.parseInt(number.getText().toString());
                    number.setText(String.valueOf(restOfShip));
                    if (0 == restOfShip){
                        picture.setOnTouchListener(null);
                    }
                    break;
                }

            }
        }
    }


    public void randomPlacementOnPlayerBattlefield(View v){
        this.controller.clearPlayerBattlefield();
        this.controller.randomPlacementOnPlayerBattlefield();
        updatePlayerBattlefield();
        Iterator i = this.shipNumbers.iterator();
        while (i.hasNext()){
            Pair<Integer,Pair<TextView,ImageView>> shipNumber = (Pair<Integer,Pair<TextView,ImageView>>) i.next();
            Pair<TextView, ImageView> displaying = shipNumber.getSecond();
            TextView number = displaying.getFirst();
            ImageView picture = displaying.getSecond();
            picture.setOnTouchListener(null);
            number.setText(String.valueOf("0"));
        }
        Button play = (Button) findViewById(R.id.buttonPlay);
        play.setEnabled(true);
    }

    public void activeGame(){
        letsPLay();
        this.inGame = true;
    }

    public void letsPLay() {

        LayoutInflater inflater = getLayoutInflater();
        View gameSubBar = inflater.inflate(R.layout.fragment_toolbarplay, null);

        this.subBar = (LinearLayout) findViewById(R.id.subBarPlacement);
        this.subBar.removeAllViewsInLayout();
        this.subBar.addView(gameSubBar);

        final Map<Ship, Integer> fleet = controller.getFleet();
        ImageView currentPicture = null;
        int cpt = 0;
        for (Map.Entry<Ship, Integer> entry : fleet.entrySet()) {
            cpt += entry.getValue();
        }
        final int fleetSize = cpt;

        for (int i = 0; i < this.playerBattlefield.getChildCount(); i++) {
            View v1 = this.playerBattlefield.getChildAt(i);
            if (v1 instanceof TableRow) {
                TableRow tableRow = (TableRow) v1;
                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    View v2 = tableRow.getChildAt(j);
                    if (v2 instanceof ImageView) {
                        currentPicture = (ImageView) v2;
                        currentPicture.setOnDragListener(null);
                        currentPicture.setOnClickListener(null);
                        unregisterForContextMenu(currentPicture);
                    }
                }
            }
        }

        if (!this.inGame){
            this.controller.initializeEnemyBattlefiel();
        }

        for (int i = 0; i < this.enemyBattlefield.getChildCount(); i++) {
            View v1 = this.enemyBattlefield.getChildAt(i);
            if (v1 instanceof TableRow) {
                TableRow tableRow = (TableRow) v1;
                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    View v2 = tableRow.getChildAt(j);
                    if (v2 instanceof ImageView) {
                        final int a=i,b=j,battlefield_size = this.enemyBattlefield.getChildCount();
                        currentPicture = (ImageView) v2;
                        final ImageView finalCurrentPicture = currentPicture;
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setPositiveButton("PLAY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = getIntent();
                                finish();
                                startActivityForResult(intent,2);
                            }
                        });
                        builder.setNegativeButton("MENU", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(GameActivity.this, MenuActivity.class);
                                intent.putExtra("controller",controller);
                                finish();
                                startActivityForResult(intent,2);
                            }
                        });
                        currentPicture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Pair<Integer,Boolean> attack = null;
                                try {
                                    attack = controller.attackEnemyBattlefield(a,b);
                                    int attack_type = attack.getFirst();
                                    boolean isSink = attack.getSecond();
                                    System.out.println(isSink);
                                    if (0 == attack_type){
                                        finalCurrentPicture.setImageResource(getDrawableResourceId(finalCurrentPicture,LABEL_WATERSHOOT));
                                    }else{
                                        if (!isSink){
                                            finalCurrentPicture.setImageResource(getDrawableResourceId(finalCurrentPicture, LABEL_SHOOT));
                                        }else{
                                            finalCurrentPicture.setImageResource(getDrawableResourceId(finalCurrentPicture, LABEL_SHOOT));
                                            TextView number = (TextView) findViewById(R.id.enemyShipLeftNumber);
                                            int old_number = Integer.parseInt(number.getText().toString());
                                            number.setText(String.valueOf(old_number+1));
                                            if (fleetSize == old_number+1){
                                                WIN = true;
                                                builder.setTitle("YOU WIN");
                                                controller.getCurrentProfil().addWonGames();
                                            }
                                        }
                                    }
                                    builder.setCancelable(false);
                                    AlertDialog dialog = builder.create();
                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(false);
                                    if (WIN){
                                        dialog.show();
                                    }else {
                                        Pair<Pair<Integer, Integer>, Pair<Integer, Boolean>> result = controller.iaAttackBattlefield();
                                        Pair<Integer, Integer> pos = result.getFirst();
                                        Pair<Integer, Boolean> res = result.getSecond();
                                        System.out.println("IA ATTACK: row:" + pos.getFirst() + " col:" + pos.getSecond());
                                        System.out.println("RES ATTACK: nature:" + res.getFirst() + " sink:" + res.getSecond());
                                        updatePlayerBattlefield();
                                        if (res.getSecond()) {
                                            TextView number = (TextView) findViewById(R.id.myShipLeftNumber);
                                            int old_number = Integer.parseInt(number.getText().toString());
                                            number.setText(String.valueOf(old_number + 1));
                                            if (fleetSize == old_number + 1) {
                                                WIN = true;
                                                dialog.setTitle("YOU LOSE");
                                                dialog.show();
                                            }
                                        }
                                    }
                                } catch (CellAlreadyShootException e) {
                                    Toast.makeText(getApplicationContext(), "Cell already shoot!",Toast.LENGTH_SHORT).show();
                                } catch (IAPlayOutOfBoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public void save(){
        this.database.open();
        this.database.clearTable();
        //this.controller.saveBattlefields();
        this.database.addAllProfils(getApplicationContext(),this.controller.getProfilsList());
        this.database.close();
    }

    @Override
    public void onPause() {
        save();
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        save();
        super.onBackPressed();
    }
}
