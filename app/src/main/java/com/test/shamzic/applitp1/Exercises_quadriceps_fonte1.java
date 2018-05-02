package com.test.shamzic.applitp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;

public class Exercises_quadriceps_fonte1 extends Fragment {

    private Button boutonB ;
    private String val = "";
    private final String exerciseName = "Squat barre";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.exercises_quadriceps_fonte1, container, false);
        //this.MAJ();
        boutonB = rootView.findViewById(R.id.id_button_selection);
        boutonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(getActivity(), DatePicker.class);
                I.putExtra("Exercise", (Serializable) new Exercise(exerciseName, "", val));
                startActivity(I);
            }
        });
        return rootView;
    }
}
