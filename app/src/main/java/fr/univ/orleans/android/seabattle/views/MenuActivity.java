package fr.univ.orleans.android.seabattle.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.univ.orleans.android.seabattle.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
