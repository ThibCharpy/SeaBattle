package fr.univ.orleans.android.seabattle.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;
import fr.univ.orleans.android.seabattle.model.Anyone.Player;

public class MainActivity extends AppCompatActivity {

    private Controller controller;

    EditText name;

    EditText username;

    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.controller = new Controller();
        this.name = (EditText) findViewById(R.id.name);
        this.username = (EditText) findViewById(R.id.username);
        this.display = (TextView) findViewById(R.id.display);
    }

    public void add(View view) {
        String name = this.name.getText().toString();
        String username = this.username.getText().toString();

        Player p = this.controller.addPlayer(name,username);

        display.setText("You added the player called "+p.getUsername()+" id="+p.getId());
    }
}
