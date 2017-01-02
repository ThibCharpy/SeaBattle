package fr.univ.orleans.android.seabattle.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;

public class MenuActivity extends AppCompatActivity {

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        String username = intent.getStringExtra("currentProfil");
        this.controller = (Controller) intent.getSerializableExtra("controller");

        TextView textView = (TextView) findViewById(R.id.currentPlayer);
        textView.setText("Let's Play "+ username +" !");

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
}
