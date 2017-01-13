package fr.univ.orleans.android.seabattle.views;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;
import fr.univ.orleans.android.seabattle.database.ProfilsDataSource;

/**
 * Created by Maxxx on 20/12/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    private Controller controller;

    private ProfilsDataSource database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        this.controller = (Controller) intent.getSerializableExtra("controller");

        this.database = new ProfilsDataSource(this);

        TextView pseudo = (TextView)findViewById(R.id.pseudo);
        pseudo.setText("Pseudo : "+this.controller.getCurrentProfil().getUsername());
        TextView nbwin= (TextView)findViewById(R.id.nbwin);
        nbwin.setText("Nb. Win : "+this.controller.getCurrentProfil().getWonGames());

        Button buttonReset = (Button)findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.resetProfil();
                TextView nbwin= (TextView)findViewById(R.id.nbwin);
                nbwin.setText("Nb. Win : "+controller.getCurrentProfil().getWonGames());
            }
        });

    }

    public void save(){
        this.database.open();
        this.database.clearTable();
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