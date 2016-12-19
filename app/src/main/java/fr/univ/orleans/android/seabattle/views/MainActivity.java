package fr.univ.orleans.android.seabattle.views;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;
import fr.univ.orleans.android.seabattle.database.PlayersDataSource;
import fr.univ.orleans.android.seabattle.model.Player;

public class MainActivity extends ListActivity {

    private Controller controller;

    private PlayersDataSource dataSource;

    EditText name;

    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.controller = new Controller();
        this.name = (EditText) findViewById(R.id.name);
        this.username = (EditText) findViewById(R.id.username);

        dataSource = new PlayersDataSource(this);
        dataSource.open();

        List<Player> players = dataSource.getAllPlayers();

        ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this,
                android.R.layout.simple_list_item_1, players);
        setListAdapter(adapter);
    }

    public void add(View view) {
        ArrayAdapter<Player> adapter = (ArrayAdapter<Player>) getListAdapter();
        Player player = null;
        Editable name = this.name.getText();
        Editable username = this.username.getText();
        if (view.getId() == R.id.buttonAdd){
            player = dataSource.createPlayer(name.toString(),username.toString());
            adapter.add(player);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
