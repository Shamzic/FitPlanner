package com.test.shamzic.applitp1;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FIRSTNAME = "firstname";
    public static final String NAME = "name";
    public static final String TAG = "ProfileSaving";
    public static final String EMAIL = "email";
    public static final String AGE = "age";
    public static final String CITY = "city";
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonSelectionActivity;
    // path : Collections/documents/Collections/documents/Collections/documents etc.
    private DocumentReference mDocRef;
    private FirebaseUser user;
    // NAME
    private EditText nameView;
    private String nameText;
    // FIRST NAME
    private EditText firstNameView;
    private String firstNameText;
    // AGE
    private EditText ageView;
    private String ageText;
    // CITY
/*    private EditText cityView;
    private String cityText;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonSelectionActivity = (Button) findViewById(R.id.buttonSelectionActivity);
        // NAME
        nameView = (EditText) findViewById(R.id.editTextName);
        nameText = nameView.getText().toString();
        //FIRSTNAME
        firstNameView = (EditText) findViewById(R.id.editTextFirstName);
        firstNameText = firstNameView.getText().toString();
        // AGE
        ageView = (EditText) findViewById(R.id.editTextAge);
        ageText = ageView.getText().toString();
        // CITY
/*        cityView = (EditText) findViewById(R.id.editTextCity);
        cityText = cityView.getText().toString();*/

        textViewUserEmail.setText(getResources().getString(R.string.welcome)+" "+user.getEmail());
        buttonLogout.setOnClickListener(this);
        buttonSelectionActivity.setOnClickListener(this);

        mDocRef = FirebaseFirestore.getInstance().document("users/"+user.getEmail().toString());

        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(nameText.isEmpty() ||firstNameText.isEmpty() || ageText.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.failSaveProfil, Toast.LENGTH_LONG).show();
                        Log.wtf(TAG, "FUCK");
                        return;
                    }
                    nameView.setText(document.get(NAME).toString());
                    firstNameView.setText(document.get(FIRSTNAME).toString());
                    ageView.setText(document.get(AGE).toString());
/*                    cityView.setText(document.get(CITY).toString());*/

                    Log.wtf(TAG, document.get(NAME) + " => " + document.get(FIRSTNAME));
                } else
                 {
                Log.w(TAG, "Error getting documents.", task.getException());
                 }
             }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(view == buttonSelectionActivity) {
            //finish();
            Toast.makeText(ProfileActivity.this, "Selection des activités ... !",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Accueil.class));
        }
    }

    public void saveProfile(View view) {
        // TODO : fill this out
        Log.wtf(TAG, "save profil clicked");
        nameText = nameView.getText().toString();
        firstNameText = firstNameView.getText().toString();
        ageText = ageView.getText().toString();
/*        cityText = cityView.getText().toString();*/

        if(nameText.isEmpty() ||firstNameText.isEmpty() || ageText.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.failSaveProfil, Toast.LENGTH_LONG).show();
            Log.wtf(TAG, "y a du vide");
            return;
        }

        Map<String, Object>  dataToSave = new HashMap<String, Object>();
        dataToSave.put(NAME, nameText);
        dataToSave.put(FIRSTNAME, firstNameText);
        dataToSave.put(AGE, ageText);
/*        dataToSave.put(CITY,cityText);*/
        dataToSave.put(EMAIL, user.getEmail());
        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.wtf(ProfileActivity.TAG, "Profile saved !");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.wtf(TAG, "Profile not saved !");
            }
        });
    }
}
