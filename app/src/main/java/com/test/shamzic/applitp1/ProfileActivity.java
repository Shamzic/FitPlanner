package com.test.shamzic.applitp1;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FIRSTNAME = "firstname";
    public static final String NAME = "name";
    public static final String TAG = "ProfileSaving";
    public static final String EMAIL = "email";
    public static final String AGE = "age";
    public static final String CITY = "city";
    public static final String SEXE = "sexe";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    private FirebaseAuth  firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser  user = firebaseAuth.getCurrentUser();
    private DocumentReference  mDocRef = FirebaseFirestore.getInstance().document("users/"+user.getEmail().toString());
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonSelectionActivity;
    // path : Collections/documents/Collections/documents/Collections/documents etc.

    // NAME
    private EditText nameView;
    private String nameText;
    // FIRST NAME
    private EditText firstNameView;
    private String firstNameText;
    // AGE
    private EditText ageView;
    private String ageText;
    // GENDER
    private RadioButton buttonMale;
    private RadioButton buttonFemale;
    private RadioGroup radioGoupDex;
    private String sexText;
    // CITY
    private EditText cityView;
    private String cityText;

    public void onStart() {
        super.onStart();
        mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    // NAME
                    nameText = documentSnapshot.getString(NAME);
                    nameView.setText(nameText);
                    // FIRST NAME
                    firstNameText = documentSnapshot.getString(FIRSTNAME);
                    firstNameView.setText(firstNameText);
                    // AGE
                    ageText = documentSnapshot.getString(AGE);
                    ageView.setText(ageText);
                    // GENDER
                    sexText = documentSnapshot.getString(SEXE);
                    Log.wtf(TAG, "sex text : "+sexText);
                    if(sexText.equals(MALE)) {
                        Log.wtf(TAG, "MALE CHECKED");
                        buttonMale.setChecked(true);
                    }
                    if(sexText.equals(FEMALE)) {
                        buttonFemale.setChecked(true);
                        Log.wtf(TAG, "FEMALE CHECKED");
                    }
                    // CITY
                    cityText = documentSnapshot.getString(CITY);
                    cityView.setText(cityText);
                }
                else if(e !=null) {
                    Log.wtf(TAG, "Got an exception", e);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        ageView.addTextChangedListener(mDateEntryWatcher);
        ageText = ageView.getText().toString();
        // SEXE
        buttonMale = (RadioButton) findViewById(R.id.radioButtonSexeMasculin);
        buttonFemale = (RadioButton) findViewById(R.id.radioButtonSexeFeminin);
        radioGoupDex = (RadioGroup) findViewById(R.id.radioGroupSexe);
        radioGoupDex.clearCheck();
        if(buttonFemale.isChecked())
            sexText = FEMALE;
        else
            sexText = MALE;
        // CITY
        cityView = (EditText) findViewById(R.id.editTextCity);
        cityText = cityView.getText().toString();

        textViewUserEmail.setText(getResources().getString(R.string.welcome)+" "+user.getEmail());
        buttonLogout.setOnClickListener(this);
        buttonSelectionActivity.setOnClickListener(this);
    }

    private TextWatcher mDateEntryWatcher = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + ddmmyyyy.substring(clean.length());
                }else{
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day  = Integer.parseInt(clean.substring(0,2));
                    int mon  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    clean = String.format("%02d%02d%02d",day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                ageView.setText(current);
                ageView.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    };

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

        Log.wtf(TAG, "save profil clicked");
        nameText = nameView.getText().toString();
        firstNameText = firstNameView.getText().toString();
        ageText = ageView.getText().toString();
        cityText = cityView.getText().toString();

        if(nameText.isEmpty() ||firstNameText.isEmpty() || ageText.isEmpty() || (!buttonFemale.isChecked() && !buttonMale.isChecked()) ||cityText.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.failSaveProfil, Toast.LENGTH_LONG).show();
            Log.wtf(TAG, "Every fields are not completed");
            return;
        }

        Map<String, Object>  dataToSave = new HashMap<String, Object>();

        dataToSave.put(NAME, nameText);
        dataToSave.put(FIRSTNAME, firstNameText);
        dataToSave.put(AGE, ageText);
        dataToSave.put(CITY,cityText);
        dataToSave.put(EMAIL, user.getEmail());
        if(buttonMale.isChecked())
            dataToSave.put(SEXE, MALE);
        if(buttonFemale.isChecked())
            dataToSave.put(SEXE, FEMALE);

        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.wtf(ProfileActivity.TAG, "Profile saved !");
                Toast.makeText(getApplicationContext(), R.string.achieveSaveProfil, Toast.LENGTH_LONG).show();
                finish();
                //goToAccueil();
                startActivity(new Intent(ProfileActivity.this, MusclesMenu.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.wtf(TAG, "Profile not saved !");
                Toast.makeText(getApplicationContext(), R.string.failSaveProfil, Toast.LENGTH_LONG).show();
            }
        });
    }
}
