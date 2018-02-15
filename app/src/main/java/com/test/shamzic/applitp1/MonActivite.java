package com.test.shamzic.applitp1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import android.view.View;

import org.w3c.dom.Text;

public class MonActivite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        //tv.setText("Hello, Android");
        //setContentView(tv);
        setContentView(R.layout.activity_mon_activite);

        final Button button = findViewById(R.id.bouton1);


        button.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
                TextView textv = findViewById(R.id.textView5);
                textv.setText("Salut Quentin tu vas bien mon choux");
                 //Code here executes on main thread after user presses button
            }
        });
    }


}
