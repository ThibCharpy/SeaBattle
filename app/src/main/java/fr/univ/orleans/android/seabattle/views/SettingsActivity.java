package fr.univ.orleans.android.seabattle.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;

/**
 * Created by Maxxx on 21/12/2016.
 */
public class SettingsActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        TextView music = (TextView) findViewById(R.id.music);
        music.setText("Music :");
        TextView sound = (TextView) findViewById(R.id.sound);
        sound.setText("Sound Effects :");

        SeekBar musicBar = (SeekBar)findViewById(R.id.musicBar);
        SeekBar soundBar = (SeekBar)findViewById(R.id.soundBar);


    }
}
