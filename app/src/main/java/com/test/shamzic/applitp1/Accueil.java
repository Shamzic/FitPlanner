/*
package com.test.shamzic.applitp1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Accueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
    }
}
*/

package com.test.shamzic.applitp1;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Button;

import android.view.View;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Accueil extends AppCompatActivity /*implements View.OnClickListener*/{


    private ListView mListView;
    private String[][] Nom = new String[][]{
            {"Biceps", "muscle du bras",""}, {"Triceps", "muscle du bras","gggg"}, {"Trapèze", "muscle du dos","dddd"}
    };
    //final String PLANNING = "" ;
    final String EXERCICE_SELECTION = "Exercice séléctionné";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Hello, Android");
        setContentView(tv);

        setContentView(R.layout.selectionexercice);
        mListView = (ListView) findViewById(R.id.listView);

        //Création de la ArrayList qui nous permettra de remplir la listView
        final ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();


        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map = null;

        Intent intent = getIntent();

        String[] J =  intent.getStringArrayExtra("PLANNING");
        //String J =  intent.getStringExtra("PLANNING");

        for (int i = 0; i < Nom.length; i++) {
            map = new HashMap<String, String>();
            map.put("nom", Nom[i][0]);
            map.put("description", Nom[i][1]);
            //map.put("date",Nom[i][2]);

            if (J != null) {
                if (Nom[i][0].equals(J[0])) {
                    map.put("date", J[1]+J[2]+J[3]+J[4]+J[5]+J[6]+J[7]);
                    //map.put("date",Nom[i][0]+"   "+ J[0]);
                }
            }
            else{
                map.put("date","No selected");
                //map.put("date",String.valueOf(Jour.length));
            }
            listItem.add(map);
        }

        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        final SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.descriptions_exercices,
                new String[]{"nom", "description","date"}, new int[]{R.id.nom, R.id.description, R.id.date});

        //On attribue à notre listView l'adapter que l'on vient de créer
        mListView.setAdapter(mSchedule);

        //Enfin on met un écouteur d'évènement sur notre listView
        final HashMap<String, String> finalMap = map;
        //final HashMap<String, String> finalMap1 = map;
        final HashMap<String, String> finalMap1 = map;
        /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long Id) {
                String EXO = listItem.get(position).get("nom");
                Intent I1 = new Intent(Accueil.this, SelectionExercice.class);
                I1.putExtra(EXERCICE_SELECTION,EXO);
                startActivity(I1);
            }



        });*/
    }
    /*protected void onSelection (View V){
        //boolean clic = ((Button) V).callOnClick();
        //final Button B1 = (Button) findViewById(R.id.MuscleAbdo);
        Intent I2 = new Intent(Accueil.this, MusclesMenu.class);
        //I2.putExtra("PLANNING", Jour);
        startActivity(I2);
    }*/

    public void onPersoSelection (View v) {
        /*boolean clic = ((Button) v).callOnClick();
        final Button B1 = (Button) findViewById(R.id.MuscleAbdo);*/
        Intent I2 = new Intent(Accueil.this, SelectionExercice.class);
        //I2.putExtra("PLANNING", Jour);
        startActivity(I2);
    }
}
