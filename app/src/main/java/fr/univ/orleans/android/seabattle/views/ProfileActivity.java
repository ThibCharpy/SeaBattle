package fr.univ.orleans.android.seabattle.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;

/**
 * Created by Maxxx on 20/12/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    private Controller controller;

    //bonjour

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.controller = new Controller();


        TextView pseudo = (TextView)findViewById(R.id.pseudo);
        pseudo.setText("Pseudo : ");
        TextView nbwin= (TextView)findViewById(R.id.nbwin);
        nbwin.setText("Nb. Win : ");
        TextView ratio = (TextView)findViewById(R.id.ratio);
        ratio.setText("Ratio : ");

        Button buttonEdit = (Button)findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        Button buttonDelete = (Button)findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        Button buttonReset = (Button)findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        String pseudoField = "Bite";
        String nbWinField = "666";
        String ratioField = "12";

        TextView fieldPseudo = (TextView)findViewById(R.id.fieldPseudo);
        fieldPseudo.setText(pseudoField);
        TextView fieldNbwin = (TextView)findViewById(R.id.fieldNbwin);
        fieldNbwin.setText(nbWinField);
        TextView fieldRatio = (TextView)findViewById(R.id.fieldRatio);
        fieldRatio.setText(ratioField);

    }
}
