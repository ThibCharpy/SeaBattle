package fr.univ.orleans.android.seabattle.views;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
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

//TODO faire connection vers MenuLayout

public class MainActivity extends Activity {

    private Controller controller;

    private ProfilsDataSource dataSource;

    ListView listView;

    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.controller = new Controller();
        this.username = (EditText) findViewById(R.id.username);

        this.listView = (ListView) findViewById(R.id.listProfils);
        this.listView.setLongClickable(true);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "short clicked pos: " + position, Toast.LENGTH_LONG).show();
            }
        });
        this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "long clicked pos: " + position, Toast.LENGTH_LONG).show();
                return true;
            }
        });


        this.dataSource = new ProfilsDataSource(this);
        this.dataSource.open();

        List<Profil> profils = this.dataSource.getAllProfils();

        ArrayAdapter<Profil> adapter = new ArrayAdapter<Profil>(this,
                android.R.layout.simple_list_item_1, profils);

        this.listView.setAdapter(adapter);

    }

    public void add(View view) {
        ArrayAdapter<Profil> adapter = (ArrayAdapter<Profil>) this.listView.getAdapter();
        Profil profil = null;

        Editable username = this.username.getText();

        profil = this.dataSource.createProfil(username.toString());
        adapter.add(profil);
        System.out.println(profil.toString());
        this.controller.addProfil(profil);
        adapter.notifyDataSetChanged();

        if(5 <= adapter.getCount()){
            LinearLayout input_zone = (LinearLayout) findViewById(R.id.input_username);
            input_zone.setVisibility(Button.GONE);
        }
    }
}
