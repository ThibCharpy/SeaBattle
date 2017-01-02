package fr.univ.orleans.android.seabattle.views.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import fr.univ.orleans.android.seabattle.R;
import fr.univ.orleans.android.seabattle.controller.Controller;
import fr.univ.orleans.android.seabattle.database.ProfilsDataSource;
import fr.univ.orleans.android.seabattle.model.Profil;
import fr.univ.orleans.android.seabattle.views.MainActivity;

/**
 * Created by thibault on 24/12/16.
 */

public class EditProfilFragment extends DialogFragment {

    //TODO Faire un contexte general pour chaque vue avec des maps de variable de session etc..

    EditText username;

    private String newUsername;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View v = inflater.inflate(R.layout.fragment_edit_profil, null);
        builder.setView(v);

        String old_username = getArguments().getString("username");
        this.username = (EditText) v.findViewById(R.id.new_username);
        this.username.setText(old_username);


        builder.setPositiveButton(R.string.buttonEdit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Editable new_username = username.getText();
                        Long profil_id = getArguments().getLong("id");
                        int wongames = getArguments().getInt("wongames");
                        ProfilsDataSource dataSource = new ProfilsDataSource(v.getContext());
                        dataSource.open();
                        dataSource.updateProfil(new_username.toString(),profil_id,wongames);
                        dataSource.close();
                    }
                });

        builder.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public String getNewUsername() {
        return newUsername;
    }
}
