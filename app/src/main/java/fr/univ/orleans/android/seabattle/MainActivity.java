package fr.univ.orleans.android.seabattle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.nio.CharBuffer;

import fr.univ.orleans.android.seabattle.model.Anyone;

public class MainActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Anyone a = new Anyone(1,"Thibault");
        text = (TextView) findViewById(R.id.text1);
        text.setText("Hello "+a.getName()+ " you are our "+a.getId()+" player !" );
    }
}
