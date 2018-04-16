package com.test.shamzic.applitp1;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;


public class SelectionExercice extends Accueil{

    private static final String TAG = "RETOUR" ;
    private ListView mListView;
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
        else{
            Jour[1] = "";
        }

        if (checkBox2.isChecked()){
            Jour[2] = getString(R.string.jour2)+" ";
        }
        else{
            Jour[2] = "";
        }
        if (checkBox3.isChecked()) {
            Jour[3] = getString(R.string.jour3)+" ";
        }
        else{
            Jour[3] = "";
        }
        if (checkBox4.isChecked()){
            Jour[4] = getString(R.string.jour4)+" ";
        }
        else{
            Jour[4] = "";
        }
        if (checkBox5.isChecked()) {
            Jour[5] = getString(R.string.jour5)+" ";
        }
        else{
            Jour[5] = "";
        }
        if (checkBox6.isChecked()){
            Jour[6] = getString(R.string.jour6)+" ";
        }
        else{
            Jour[6] = "";
        }
        if (checkBox7.isChecked()){
            Jour[7] = getString(R.string.jour7);
        }
        else{
            Jour[7] = "";
        }

    }

    public void onButtonClicked (View v) {
        Intent I2 = new Intent(SelectionExercice.this, MusclesMenu.class);
        I2.putExtra("PLANNING", Jour);
        startActivity(I2);
    }

    public void onReturnButtonActivityClicked(View v) {
        this.finish();
    }
}
