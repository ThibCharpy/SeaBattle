package fr.univ.orleans.android.seabattle.views;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;
import fr.univ.orleans.android.seabattle.database.ProfilsDataSource;
import fr.univ.orleans.android.seabattle.model.Profil;

public class MenuActivity extends Activity {

    private Controller controller;

    private ProfilsDataSource database;

    private List<Profil> profils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        this.controller = (Controller) intent.getSerializableExtra("controller");
        this.database = new ProfilsDataSource(this);

        TextView textView = (TextView) findViewById(R.id.currentPlayer);
        textView.setText("Let's Play "+ this.controller.getCurrentProfil().getUsername() +" !");

        Button buttonPlay = (Button)findViewById(R.id.buttonPlay);
        Button buttonSettings = (Button)findViewById(R.id.buttonSettings);
        Button buttonProfile = (Button)findViewById(R.id.buttonProfile);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                intent.putExtra("controller",controller);
                startActivityForResult(intent,2);
            }
        });
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                intent.putExtra("controller",controller);
                startActivity(intent);
            }
        });
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                intent.putExtra("controller",controller);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_menu_landscape);
        else {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
                setContentView(R.layout.activity_menu);
        }

        TextView textView = (TextView) findViewById(R.id.currentPlayer);
        textView.setText("Let's Play "+ this.controller.getCurrentProfil().getUsername() +" !");

        Button buttonPlay = (Button)findViewById(R.id.buttonPlay);
        Button buttonSettings = (Button)findViewById(R.id.buttonSettings);
        Button buttonProfile = (Button)findViewById(R.id.buttonProfile);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        this.database.open();
        this.database.clearTable();
        this.database.addAllProfils(getApplicationContext(),this.controller.getProfilsList());
        this.database.close();
        super.onPause();
    }
}
