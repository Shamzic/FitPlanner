package com.test.shamzic.applitp1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CalendarActivity extends AppCompatActivity {

    public static final String TAG = "tag";
    private CalendarView calendarView;
    private  TextView myDate;
    private TextView taskView;
    private ScrollView scrollView;
    private LinearLayout scrollLayout;
    private FirebaseAuth  firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser  user = firebaseAuth.getCurrentUser();
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("users/"+user.getEmail()/*+"/events/exercises"*/);
    private ArrayList<Object> eventList;
    public String dateText;
    public TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        t = new TextView(this);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        myDate = (TextView) findViewById(R.id.myDate);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollLayout = (LinearLayout) findViewById(R.id.scrollLayout);


        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, final int i2) {
                dateText="";
                String dateTemp = "";
                if(i2<10) {
                    dateText += "0";
                    dateTemp +="0";
                }
                dateText+= i2 + "/";
                dateTemp+= i2 + "/";
                if((i1 +1)<10) {
                    dateText += "0";
                    dateTemp += "0";
                }
                dateText +=(i1 +1) + "/" + i;
                dateTemp +=(i1 +1 ) + "/" + i;
                 final String saveDate = dateTemp;

                mDocRef.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            scrollLayout.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Exercise exoTemp = document.toObject(Exercise.class);
                                Exercise exo = new Exercise(exoTemp.getDate(),exoTemp.getVal());
                                String temp = exo.getDate();

                                if( temp.equals(saveDate))
                                {
                                    System.out.println("ici");
                                    dateText+="\n"+exo.getVal();
                                    System.out.println("dateText : "+dateText);
                                    t = new TextView(CalendarActivity.super.getApplicationContext());
                                    t.clearComposingText();
                                    t.setBackgroundResource(R.drawable.bg_btn);
                                    t.setText(exo.getVal());
                                    scrollLayout.addView(t);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
                myDate.setText(dateText);
            }
        });
    }
}
