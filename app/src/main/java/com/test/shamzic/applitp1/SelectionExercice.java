package com.test.shamzic.applitp1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.TextUtils.indexOf;

public class SelectionExercice extends MonActivite/*ListActivity*/ {

    //final String PLANNING = "Dates Choisies" ;
    private ListView mListView;
    //protected ListView L1, L2, L3, L4, L5, L6, L7;
    //protected int[] Jour = new int[] {0,0,0,0,0,0,0};
    protected String[] Jour = new String[] {"","","","","","","",""};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_date);

        Intent intent = getIntent();
        String Titre = intent.getStringExtra(EXERCICE_SELECTION);

        TextView total = (TextView )findViewById(R.id.date1);

        if (intent != null) {
            Jour[0] = Titre;
            total.setText("Exercice choisi : "+Jour[0]);
        }
        else{
            total.setText("PROBLEME");
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.check_lundi);
        final CheckBox checkBox2 = (CheckBox) findViewById(R.id.check_mardi);
        final CheckBox checkBox3 = (CheckBox) findViewById(R.id.check_mercredi);
        final CheckBox checkBox4 = (CheckBox) findViewById(R.id.check_jeudi);
        final CheckBox checkBox5 = (CheckBox) findViewById(R.id.check_vendredi);
        final CheckBox checkBox6 = (CheckBox) findViewById(R.id.check_samedi);
        final CheckBox checkBox7 = (CheckBox) findViewById(R.id.check_dimanche);

        if (checkBox1.isChecked()) {
            Jour[1] = getString(R.string.jour1)+" ";
        }

        if (checkBox2.isChecked()){
            Jour[2] = getString(R.string.jour2)+" ";
        }
        if (checkBox3.isChecked()) {
            Jour[3] = getString(R.string.jour3)+" ";
        }
        if (checkBox4.isChecked()){
            Jour[4] = getString(R.string.jour4)+" ";
        }
        if (checkBox5.isChecked()) {
            Jour[5] = getString(R.string.jour5)+" ";
        }
        if (checkBox6.isChecked()){
            Jour[6] = getString(R.string.jour6)+" ";
        }
        if (checkBox7.isChecked()){
            Jour[7] = getString(R.string.jour7);
        }


    }

    /*SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.descriptions_exercices,
            new String[]{"nom", "description"}, new int[]{R.id.nom, R.id.description});

    mListView.setAdapter(mSchedule);

    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> a, View v, int position, long id) {;
            Intent intent = new Intent(SelectionExercice.this, MonActivite.class);
            startActivity(intent);
        }



    });*/
    public void onButtonClicked (/*AdapterView < ? > a,*/ View v/*,int position, long id*/) {
        Intent I2 = new Intent(SelectionExercice.this, MonActivite.class);
        I2.putExtra("PLANNING", Jour);
        startActivity(I2);
    }
}
