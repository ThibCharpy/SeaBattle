package fr.univ.orleans.android.seabattle.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;

/**
 * Created by Maxxx on 21/12/2016.
 */
public class SettingsActivity extends Activity {

    private Controller controller;

    private SeekBar musicBar;

    private AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        this.controller = (Controller) intent.getSerializableExtra("controller");

        TextView music = (TextView) findViewById(R.id.music);
        music.setText("Music :");

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initControls();


        final RadioButton radioEasy = (RadioButton)findViewById(R.id.radioEasy);
        radioEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setDifficulty(radioEasy.getText().toString());
            }
        });
        final RadioButton radioMedium = (RadioButton)findViewById(R.id.radioMedium);
        radioMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setDifficulty(radioMedium.getText().toString());
            }
        });

    }

    private void initControls() {
        try
        {
            musicBar = (SeekBar)findViewById(R.id.musicBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            musicBar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            musicBar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}