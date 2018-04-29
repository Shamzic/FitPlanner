package com.test.shamzic.applitp1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

public class DatePicker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, Serializable {

    private Exercise m_exercise;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private DocumentReference mDocRef;
    private final String TAG = "RECEXO";
    private EditText nbRep;
    private EditText nbSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);


        // Récupère exercise du fragment
        Intent i = getIntent();
        m_exercise = (Exercise)i.getSerializableExtra("Exercise");

        nbRep = (EditText) findViewById(R.id.editTextNbRep);
        nbSeries = (EditText) findViewById(R.id.editTextNbSeries);

        // Bouton de selection de jour d'entrainement du fragment
        Button b = (Button) findViewById(R.id.id_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(currentDateString);
        m_exercise.setNbRepetitions(nbRep.getText().toString());
        m_exercise.setNbSeries(nbSeries.getText().toString());
        m_exercise.setDate(day, month, year);
        m_exercise.MAJ();
        // add sauvegarde dans firebase
        mDocRef = FirebaseFirestore.getInstance().document("users/"+user.getEmail().toString()+"/events/"+m_exercise.getName());
        mDocRef.set(m_exercise).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), R.string.achieveMAJcalendar, Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), R.string.failMAJcalendar, Toast.LENGTH_LONG).show();
            }
        });
    }
}
