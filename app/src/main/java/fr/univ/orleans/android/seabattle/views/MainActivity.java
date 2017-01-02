package fr.univ.orleans.android.seabattle.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;
import fr.univ.orleans.android.seabattle.database.ProfilsDataSource;
import fr.univ.orleans.android.seabattle.model.Profil;

public class MainActivity extends Activity {

    private static int MAX_NUMBER_OF_PROFIL = 5;
    private static int MAX_LENGTH_USERNAME = 16;
    private static String FORBIDDEN_CHARACTERS = "àé&#~';.:!%*?,µ+=°ç><()[]\\";

    private Controller controller;

    private ProfilsDataSource dataSource;

    private List<Profil> profils;

    LinearLayout input_zone;

    ListView listView;

    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.controller = new Controller();
        this.username = (EditText) findViewById(R.id.username);
        this.input_zone = (LinearLayout) findViewById(R.id.input_username);

        this.listView = (ListView) findViewById(R.id.listProfils);
        this.listView.setLongClickable(true);

        this.dataSource = new ProfilsDataSource(this);
        this.dataSource.open();
        this.profils = this.dataSource.getAllProfils();
        this.dataSource.close();

        if (profils.isEmpty()){
            this.controller.initializeProfilsList();
        }else{
            this.controller.loadProfilsList(this.profils);
        }

        if (MAX_NUMBER_OF_PROFIL <= this.profils.size()){
            this.input_zone.setVisibility(Button.GONE);
        }

        ArrayAdapter<Profil> adapter = new ArrayAdapter<Profil>(this,
                android.R.layout.simple_list_item_1, this.controller.getProfilsList());

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int position, long id) {
                Intent goToMenu = new Intent(getApplicationContext(),MenuActivity.class);
                Profil profilSelect = (Profil) list.getItemAtPosition(position);
                goToMenu.putExtra("currentProfil",profilSelect.getUsername());
                goToMenu.putExtra("controller",controller);
                startActivity(goToMenu);
            }
        });

        this.listView.setAdapter(adapter);
        registerForContextMenu(this.listView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        ArrayAdapter<Profil> adapter = (ArrayAdapter<Profil>) this.listView.getAdapter();
        menu.setHeaderTitle(adapter.getItem(info.position).getUsername());
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final ArrayAdapter<Profil> adapter = (ArrayAdapter<Profil>) this.listView.getAdapter();

        final Profil edit_profil = adapter.getItem(info.position);

        if (item.getTitle().equals("Edit")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View v = inflater.inflate(R.layout.fragment_edit_profil, null);
            builder.setView(v);

            String old_username = edit_profil.getUsername();
            EditText new_username_input = (EditText) v.findViewById(R.id.new_username);
            new_username_input.setText(old_username);

            builder.setPositiveButton(R.string.buttonEdit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    EditText new_username_edittext = (EditText) v.findViewById(R.id.new_username);
                    String new_username = new_username_edittext.getText().toString();
                    if (testUsernameValidity(new_username)) {
                        controller.setProfilUsername(edit_profil, new_username);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            builder.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setTitle("set \'"+edit_profil.getUsername()+"\' username:");
            dialog.show();

        } else if (item.getTitle() == "Delete") {
            this.controller.removeProfil(edit_profil.getUsername());
            if (this.input_zone.getVisibility() == Button.GONE)
                this.input_zone.setVisibility(Button.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            return false;
        }
        return true;
    }

    public void add(View view) {
        ArrayAdapter<Profil> adapter = (ArrayAdapter<Profil>) this.listView.getAdapter();

        String username = this.username.getText().toString();
        this.username.getText().clear();

        Profil profil = null;

        if (testUsernameValidity(username)){
            profil = new Profil(username);

            this.controller.addProfil(profil);
            adapter.notifyDataSetChanged();

            int max = this.controller.getMaxNumberOfProfil();
            if(max <= adapter.getCount()){
                this.input_zone.setVisibility(Button.GONE);
            }
        }
    }

    public boolean testUsernameValidity(String username){
        String errorStack = null;

        if (0 == username.length()){
            errorStack = "Empty username not valid";
        }

        if (MAX_LENGTH_USERNAME < username.length()){
            errorStack = "Username too long";
        }

        for (int i=0; i<username.length(); i++){
            if (FORBIDDEN_CHARACTERS.contains(String.valueOf(username.charAt(i)))){
                errorStack = "Forbidden characters found";
            }
        }

        if (this.controller.usernameAlreadyExist(username)) {
            errorStack = "Username already exist";
        }

        if (null != errorStack){
            Toast.makeText(getApplicationContext(), errorStack, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void updateList(){
        ArrayAdapter<Profil> adapter = new ArrayAdapter<Profil>(this,
                android.R.layout.simple_list_item_1, this.controller.getProfilsList());
        this.listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        Toast.makeText(this, "On pause so save", Toast.LENGTH_SHORT).show();
        this.dataSource.open();
        this.dataSource.clearTable();
        this.dataSource.addAllProfils(getApplicationContext(),this.controller.getProfilsList());
        this.dataSource.close();
        super.onPause();
    }

}
